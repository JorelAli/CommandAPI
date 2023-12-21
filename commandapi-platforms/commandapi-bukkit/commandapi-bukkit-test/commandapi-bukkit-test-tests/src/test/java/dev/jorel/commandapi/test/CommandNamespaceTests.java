package dev.jorel.commandapi.test;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.Brigadier;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerLoadEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Tests for testing if namespaces work correctly
 */
public class CommandNamespaceTests extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	Player enableWithNamespaces() {
		// Register minecraft: namespace. MockBukkit doesn't do this on their own
		// Simulate `CraftServer#setVanillaCommands`
		MockPlatform<Object> mockPlatform = MockPlatform.getInstance();
		SimpleCommandMap commandMap = mockPlatform.getSimpleCommandMap();
		for (CommandNode<Object> node : mockPlatform.getBrigadierDispatcher().getRoot().getChildren()) {
			commandMap.register("minecraft", mockPlatform.wrapToVanillaCommandWrapper(node));
		}

		// Run the CommandAPI's enable tasks, especially `fixNamespaces`
		assertTrue(CommandAPI.canRegister()); // Make sure we weren't already enabled
		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());
		assertFalse(CommandAPI.canRegister());

		// Get a CraftPlayer for running VanillaCommandWrapper commands
		Player runCommandsPlayer = Mockito.mock(MockPlatform.getInstance().getCraftPlayerClass());
		// Give player permission to run command
		Mockito.when(runCommandsPlayer.hasPermission(any(String.class))).thenReturn(true);
		// Get location is used when creating the BrigadierSource in MockNMS
		Mockito.when(runCommandsPlayer.getLocation()).thenReturn(new Location(null, 0, 0, 0));

		// Provide proper handle as VanillaCommandWrapper expects
		Method getHandle = assertDoesNotThrow(() -> runCommandsPlayer.getClass().getDeclaredMethod("getHandle"));
		Object brigadierSource = Brigadier.getBrigadierSourceFromCommandSender(runCommandsPlayer);
		Object handle = Mockito.mock(getHandle.getReturnType(), invocation -> brigadierSource);
		// This is a funny quirk of Mockito, but you don't need to put the method call you want to mock inside `when`
		//  As long as the method is called, `when` knows what you are talking about
		//  That means we can mock `CraftPlayer#getHandle` indirectly using reflection
		//  See the additional response in https://stackoverflow.com/a/10131885
		assertDoesNotThrow(() -> getHandle.invoke(runCommandsPlayer));
		Mockito.when(null).thenReturn(handle);

		return runCommandsPlayer;
	}

	/*********
	 * Tests *
	 *********/

	// TODO: based on code coverage, we still need to test:
	//  - Cases when CommandAPIBukkit#minecraftCommandNamespaces is used
	//  - Registering CommandTrees with namespaces

	@Test
	public void testNullNamespace() {
		// Registering a command using null should fail
		CommandAPICommand command = new CommandAPICommand("test").executesPlayer(P_EXEC);
		assertThrowsWithMessage(
			NullPointerException.class,
			"Parameter 'namespace' was null when registering a CommandAPICommand!",
			() -> command.register((String) null)
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testDefaultMinecraftNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Make sure the default registration with the minecraft: namespace works
		command.register();

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		// Unlike custom namespaces, the minecraft namespace should NOT appear in the Brigadier dispatcher
		//  `minecraft:test` is only created when moved to Bukkit's CommandMap
		assertNull(rootNode.getChild("minecraft:test"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("minecraft:test"));

		// Commands should run
		assertStoresResult(player, "test alpha", results, "alpha");
		assertStoresResult(player, "minecraft:test alpha", results, "alpha");

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testStringNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a custom namespace
		command.register("commandtest");

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		assertNotNull(rootNode.getChild("commandtest:test"));
		assertNull(rootNode.getChild("minecraft:test"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("commandtest:test"));
		assertNull(commandMap.getCommand("minecraft:test"));

		// Commands should run
		assertStoresResult(player, "test alpha", results, "alpha");
		assertStoresResult(player, "commandtest:test alpha", results, "alpha");

		// Running the command with the minecraft: namespace should fail
		assertCommandFailsWith(
			player,
			"minecraft:test alpha",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testPluginNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a plugin instance
		command.register(MockPlatform.getConfiguration().getPlugin());

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		assertNotNull(rootNode.getChild("commandapitest:test"));
		assertNull(rootNode.getChild("minecraft:test"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("commandapitest:test"));
		assertNull(commandMap.getCommand("minecraft:test"));

		// Commands should run
		assertStoresResult(player, "test alpha", results, "alpha");
		assertStoresResult(player, "commandapitest:test alpha", results, "alpha");

		// Running the command with the minecraft: namespace should fail
		assertCommandFailsWith(
			player,
			"minecraft:test alpha",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testNoNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		// Special case: Registering a command without a namespace
		CommandAPICommand command = new CommandAPICommand("test")
			.executesPlayer(info -> {
				results.set("success");
			});

		command.register("");

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		assertNull(rootNode.getChild("minecraft:test"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNull(commandMap.getCommand("minecraft:test"));

		assertStoresResult(player, "test", results, "success");

		assertCommandFailsWith(
			player,
			"minecraft:test",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testAliasesWithDefaultNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with the default namespace
		command.register();

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		// `minecraft` namespace is only created in the CommandMap
		assertNull(rootNode.getChild("minecraft:test"));
		assertNotNull(rootNode.getChild("alpha"));
		assertNull(rootNode.getChild("minecraft:alpha"));
		assertNotNull(rootNode.getChild("beta"));
		assertNull(rootNode.getChild("minecraft:beta"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("minecraft:test"));
		assertNotNull(commandMap.getCommand("alpha"));
		assertNotNull(commandMap.getCommand("minecraft:alpha"));
		assertNotNull(commandMap.getCommand("beta"));
		assertNotNull(commandMap.getCommand("minecraft:beta"));

		// Commands should run
		assertStoresResult(player, "test discord", results, "discord");
		assertStoresResult(player, "alpha discord", results, "discord");
		assertStoresResult(player, "beta discord", results, "discord");
		assertStoresResult(player, "minecraft:test discord", results, "discord");
		assertStoresResult(player, "minecraft:alpha discord", results, "discord");
		assertStoresResult(player, "minecraft:beta discord", results, "discord");

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testAliasesWithCustomNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with a custom namespace
		command.register("commandtest");

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		assertNotNull(rootNode.getChild("commandtest:test"));
		assertNull(rootNode.getChild("minecraft:test"));

		assertNotNull(rootNode.getChild("alpha"));
		assertNotNull(rootNode.getChild("commandtest:alpha"));
		assertNull(rootNode.getChild("minecraft:test"));

		assertNotNull(rootNode.getChild("beta"));
		assertNotNull(rootNode.getChild("commandtest:beta"));
		assertNull(rootNode.getChild("minecraft:test"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("commandtest:test"));
		assertNull(commandMap.getCommand("minecraft:test"));

		assertNotNull(commandMap.getCommand("alpha"));
		assertNotNull(commandMap.getCommand("commandtest:alpha"));
		assertNull(commandMap.getCommand("minecraft:alpha"));

		assertNotNull(commandMap.getCommand("beta"));
		assertNotNull(commandMap.getCommand("commandtest:beta"));
		assertNull(commandMap.getCommand("minecraft:beta"));

		// Commands should run
		assertStoresResult(player, "test discord", results, "discord");
		assertStoresResult(player, "alpha discord", results, "discord");
		assertStoresResult(player, "beta discord", results, "discord");
		assertStoresResult(player, "commandtest:test discord", results, "discord");
		assertStoresResult(player, "commandtest:alpha discord", results, "discord");
		assertStoresResult(player, "commandtest:beta discord", results, "discord");

		// Running the command with the minecraft: namespace should fail
		assertCommandFailsWith(
			player,
			"minecraft:test discord",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);
		assertCommandFailsWith(
			player,
			"minecraft:alpha discord",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);
		assertCommandFailsWith(
			player,
			"minecraft:beta discord",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testAliasesWithPluginNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if(enableBeforeRegistering) player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with a custom namespace
		command.register(MockPlatform.getConfiguration().getPlugin());

		if(!enableBeforeRegistering) player = enableWithNamespaces();

		// Check contents of Brigadier CommandDispatcher
		RootCommandNode<Object> rootNode = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(rootNode.getChild("test"));
		assertNotNull(rootNode.getChild("commandapitest:test"));
		assertNull(rootNode.getChild("minecraft:test"));

		assertNotNull(rootNode.getChild("alpha"));
		assertNotNull(rootNode.getChild("commandapitest:alpha"));
		assertNull(rootNode.getChild("minecraft:test"));

		assertNotNull(rootNode.getChild("beta"));
		assertNotNull(rootNode.getChild("commandapitest:beta"));
		assertNull(rootNode.getChild("minecraft:test"));

		// Check contents of Bukkit CommandMap
		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("commandapitest:test"));
		assertNull(commandMap.getCommand("minecraft:test"));

		assertNotNull(commandMap.getCommand("alpha"));
		assertNotNull(commandMap.getCommand("commandapitest:alpha"));
		assertNull(commandMap.getCommand("minecraft:test"));

		assertNotNull(commandMap.getCommand("beta"));
		assertNotNull(commandMap.getCommand("commandapitest:beta"));
		assertNull(commandMap.getCommand("minecraft:test"));

		// Commands should run
		assertStoresResult(player, "test discord", results, "discord");
		assertStoresResult(player, "alpha discord", results, "discord");
		assertStoresResult(player, "beta discord", results, "discord");
		assertStoresResult(player, "commandapitest:test discord", results, "discord");
		assertStoresResult(player, "commandapitest:alpha discord", results, "discord");
		assertStoresResult(player, "commandapitest:beta discord", results, "discord");

		// Running the command with the minecraft: namespace should fail
		assertCommandFailsWith(
			player,
			"minecraft:test discord",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);
		assertCommandFailsWith(
			player,
			"minecraft:alpha discord",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);
		assertCommandFailsWith(
			player,
			"minecraft:beta discord",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testCommandNameConflictButDifferentNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player tempPlayer = null;
		if(enableBeforeRegistering) tempPlayer = enableWithNamespaces();

		CommandAPICommand a = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("a"))
			.executesPlayer(info -> {
				results.set("a");
			});

		CommandAPICommand b = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("b"))
			.executesPlayer(info -> {
				results.set("b");
			});

		a.register("a");
		b.register("b");

		if(!enableBeforeRegistering) tempPlayer = enableWithNamespaces();
		final Player player = tempPlayer; // Since we use `player` in a lambda it needs to be final

		// The two branches should have merged in the un-namespaced version
		assertStoresResult(player, "test a", results, "a");
		assertStoresResult(player, "test b", results, "b");

		// The branches should be separated in the namespaced versions
		assertStoresResult(player, "a:test a", results, "a");
		// For some reason, running this test using `server.dispatchThrowableCommand`
		//  (which is used by assertCommandFailsWith) doesn't work. Stepping through the code,
		//  I can verify that the command fails, but instead of throwing a `CommandSyntaxException`,
		//  it seems to just send the failure message to the executor. However, if we execute the
		//  command directly with Brigadier rather than via `VanillaCommandWrapper`, we can inspect
		//  the failure ourselves.
		assertThrowsWithMessage(
			CommandSyntaxException.class,
			"Incorrect argument for command at position 7: a:test <--[HERE]",
			() -> server.dispatchThrowableBrigadierCommand(player, "a:test b")
		);

		assertThrowsWithMessage(
			CommandSyntaxException.class,
			"Incorrect argument for command at position 7: b:test <--[HERE]",
			() -> server.dispatchThrowableBrigadierCommand(player, "b:test a")
		);
		assertStoresResult(player, "b:test b", results, "b");

		assertNoMoreResults(results);
	}
}
