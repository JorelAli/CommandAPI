package dev.jorel.commandapi.test;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.Brigadier;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.InternalBukkitConfig;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

	@Disabled
	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testSameCommandNameWithDefaultNamespaceAndCustomNamespace(boolean enableBeforeRegistering) {
		// TODO: This apparently fails
		Mut<String> results = Mut.of();

		Player player = null;
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand defaultNamespace = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("a"))
			.executesPlayer(info -> {
				results.set("a");
			});

		CommandAPICommand customNamespace = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("b"))
			.executesPlayer(info -> {
				results.set("b");
			});

		defaultNamespace.register();
		customNamespace.register("custom");

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		RootCommandNode<Object> root = MockPlatform.getInstance().getBrigadierDispatcher().getRoot();
		assertNotNull(root.getChild("test"));
		assertNotNull(root.getChild("custom:test"));
		assertNull(root.getChild("minecraft:test"));

		CommandMap commandMap = MockPlatform.getInstance().getSimpleCommandMap();
		assertNotNull(commandMap.getCommand("test"));
		assertNotNull(commandMap.getCommand("minecraft:test"));
		assertNotNull(commandMap.getCommand("custom:test"));
		assertNull(commandMap.getCommand("minecraft:custom:test"));

		assertStoresResult(player, "test a", results, "a");
		assertStoresResult(player, "test b", results, "b");
		assertStoresResult(player, "minecraft:test a", results, "a");
		assertStoresResult(player, "custom:test b", results, "b");

		assertCommandFailsWith(player, "custom:test a", "");
		assertCommandFailsWith(player, "minecraft:test b", "");

		assertNoMoreResults(results);
	}


	@Test
	public void testNullNamespaceWithCommandAPICommand() {
		// Registering a command using null should fail
		CommandAPICommand command = new CommandAPICommand("test").executesPlayer(P_EXEC);
		assertThrowsWithMessage(
			NullPointerException.class,
			"Parameter 'namespace' was null when registering command /test!",
			() -> command.register((String) null)
		);
	}

	@Test
	public void testNullNamespaceWithCommandTree() {
		// Registering a command using null should fail
		CommandTree commandTree = new CommandTree("test").executesPlayer(P_EXEC);
		assertThrowsWithMessage(
			NullPointerException.class,
			"Parameter 'namespace' was null when registering command /test!",
			() -> commandTree.register((String) null)
		);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testDefaultMinecraftNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Make sure the default registration with the minecraft: namespace works
		command.register();

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

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
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a custom namespace
		command.register("commandtest");

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

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
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a plugin instance
		command.register(MockPlatform.getConfiguration().getPlugin());

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

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
	public void testAliasesWithDefaultNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with the default namespace
		command.register();

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

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
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with a custom namespace
		command.register("commandtest");

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

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
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with a custom namespace
		command.register(MockPlatform.getConfiguration().getPlugin());

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

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
		if (enableBeforeRegistering) {
			tempPlayer = enableWithNamespaces();
		}

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

		if (!enableBeforeRegistering) {
			tempPlayer = enableWithNamespaces();
		}
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

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testCommandTreeRegistrationDefaultNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandTree command = new CommandTree("test")
			.then(new LiteralArgument("a")
				.then(new StringArgument("string")
					.executesPlayer(info -> {
						results.set("a");
						results.set(info.args().getUnchecked("string"));
					})
				)
			)
			.then(new LiteralArgument("b")
				.then(new IntegerArgument("integer")
					.executesPlayer(info -> {
						results.set("b");
						results.set(String.valueOf(info.args().get("integer")));
					})
				)
			);

		// Make sure the default registration with the minecraft: namespace works
		command.register();

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		server.dispatchCommand(player, "test a alpha");
		assertEquals("a", results.get());
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "minecraft:test a alpha");
		assertEquals("a", results.get());
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "test b 123");
		assertEquals("b", results.get());
		assertEquals("123", results.get());

		server.dispatchCommand(player, "minecraft:test b 123");
		assertEquals("b", results.get());
		assertEquals("123", results.get());

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false,true})
	public void testCommandTreeRegistrationCustomNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandTree command = new CommandTree("test")
			.then(new LiteralArgument("a")
				.then(new StringArgument("string")
					.executesPlayer(info -> {
						results.set("a");
						results.set(info.args().getUnchecked("string"));
					})
				)
			)
			.then(new LiteralArgument("b")
				.then(new IntegerArgument("integer")
					.executesPlayer(info -> {
						results.set("b");
						results.set(String.valueOf(info.args().get("integer")));
					})
				)
			);

		command.register("namespace");

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		server.dispatchCommand(player, "test a alpha");
		assertEquals("a", results.get());
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "namespace:test a alpha");
		assertEquals("a", results.get());
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "test b 123");
		assertEquals("b", results.get());
		assertEquals("123", results.get());

		server.dispatchCommand(player, "namespace:test b 123");
		assertEquals("b", results.get());
		assertEquals("123", results.get());

		// Running the command with the minecraft: namespace should fail
		assertCommandFailsWith(
			player,
			"minecraft:test a alpha",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);
		assertCommandFailsWith(
			player,
			"minecraft:test b 123",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false,true})
	public void testCommandTreeRegistrationPluginNamespace(boolean enableBeforeRegistering) {
		Mut<String> results = Mut.of();

		Player player = null;
		if (enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		CommandTree command = new CommandTree("test")
			.then(new LiteralArgument("a")
				.then(new StringArgument("string")
					.executesPlayer(info -> {
						results.set("a");
						results.set(info.args().getUnchecked("string"));
					})
				)
			)
			.then(new LiteralArgument("b")
				.then(new IntegerArgument("integer")
					.executesPlayer(info -> {
						results.set("b");
						results.set(String.valueOf(info.args().get("integer")));
					})
				)
			);

		command.register(MockPlatform.getConfiguration().getPlugin());

		if (!enableBeforeRegistering) {
			player = enableWithNamespaces();
		}

		server.dispatchCommand(player, "test a alpha");
		assertEquals("a", results.get());
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandapitest:test a alpha");
		assertEquals("a", results.get());
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "test b 123");
		assertEquals("b", results.get());
		assertEquals("123", results.get());

		server.dispatchCommand(player, "commandapitest:test b 123");
		assertEquals("b", results.get());
		assertEquals("123", results.get());

		// Running the command with the minecraft: namespace should fail
		assertCommandFailsWith(
			player,
			"minecraft:test a alpha",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);
		assertCommandFailsWith(
			player,
			"minecraft:test b 123",
			"Unknown or incomplete command, see below for error at position 0: <--[HERE]"
		);

		assertNoMoreResults(results);
	}

	@Disabled
	@Test
	public void testPermissions() {
		CommandAPICommand command = new CommandAPICommand("test")
			.withPermission("permission")
			.executesPlayer(P_EXEC);

		// Test with default minecraft: namespace
		command.register();

		Player player = enableWithNamespaces();

		PermissionAttachment attachment = player.addAttachment(MockPlatform.getConfiguration().getPlugin(), "permission", false);

		assertCommandFailsWith(player, "test", "Unknown or incomplete command, see below for error at position 0: <--[HERE]");
		assertCommandFailsWith(player, "minecraft:test", "Unknown or incomplete command, see below for error at position 0: <--[HERE]");

		attachment.unsetPermission("permission");

		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "minecraft:test"));

		// Unset permission und unregister command
		CommandAPI.unregister("test", true);

		// Test with custom namespace (same as with a plugins)
		command.register("commandnamespace");

		assertCommandFailsWith(player, "test", "Unknown or incomplete command, see below for error at position 0: <--[HERE]");
		assertCommandFailsWith(player, "commandnamespace:test", "Unknown or incomplete command, see below for error at position 0: <--[HERE]");

		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "commandnamespace:test"));
	}

	@Test
	public void testConfigNamespace() {
		CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin());
		InternalBukkitConfig internalConfig = new InternalBukkitConfig(config);

		// The namespace wasn't changed so it should default to minecraft
		assertEquals("minecraft", internalConfig.getNamespace());

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin());
		CommandAPIBukkitConfig finalConfig = config;

		// The namespace is set to null, this should throw a NPE
		assertThrows(NullPointerException.class, () -> finalConfig.setNamespace(null));

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin())
			.setNamespace("");
		internalConfig = new InternalBukkitConfig(config);

		// The namespace was set to an empty namespace so this should result in the default minecraft namespace
		assertEquals("minecraft", internalConfig.getNamespace());

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin())
			.setNamespace("custom");
		internalConfig = new InternalBukkitConfig(config);

		// The namespace was set to a non-empty, non-null custom namespace, this should be valid
		assertEquals("custom", internalConfig.getNamespace());

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin())
			.usePluginNamespace();
		internalConfig = new InternalBukkitConfig(config);

		// The namespace was set to use the plugin name as the namespace, this should be valid
		assertEquals("commandapitest", internalConfig.getNamespace());

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin())
			.setNamespace("custom")
			.usePluginNamespace();
		internalConfig = new InternalBukkitConfig(config);

		// The namespace was first set to a custom one, then was set to use the plugin name. This should be valid and the plugin name should be the namespace
		assertEquals("commandapitest", internalConfig.getNamespace());

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin())
			.setNamespace("custom")
			.usePluginNamespace()
			.setNamespace("custom");
		internalConfig = new InternalBukkitConfig(config);

		// The namespace was first set to a custom one, then was set to use the plugin name, then was set to use a custom one
		// usePluginNamespace() should take priority and the plugin name should be the namespace
		assertEquals("commandapitest", internalConfig.getNamespace());

		config = new CommandAPIBukkitConfig(MockPlatform.getConfiguration().getPlugin())
			.setNamespace("Custom");
		internalConfig = new InternalBukkitConfig(config);

		// The namespace uses invalid characters so the namespace should default to minecraft
		assertEquals("minecraft", internalConfig.getNamespace());

		Player player = enableWithNamespaces();

		// Here, it doesn't really matter when we're registering the command, we only care for namespaces
		CommandAPICommand command = new CommandAPICommand("test")
			.executesPlayer(P_EXEC);

		command.register("");

		// The command should be registered with the minecraft namespace because the namespace was empty
		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "minecraft:test"));

		CommandAPI.unregister("test", true);

		command.register("Command");

		// The command should be registered with the minecraft namespace because the namespace was invalid
		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "minecraft:test"));

		CommandAPI.unregister("test", true);

		command.register("test_123.-");

		// The command should be registered with the custom provided namespace
		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "test_123.-:test"));
	}

}
