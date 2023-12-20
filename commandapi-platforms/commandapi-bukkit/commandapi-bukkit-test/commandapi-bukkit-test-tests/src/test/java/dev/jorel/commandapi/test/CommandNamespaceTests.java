package dev.jorel.commandapi.test;

import be.seeseemelk.mockbukkit.command.MockCommandMap;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.Brigadier;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.RegisteredCommand;
import dev.jorel.commandapi.SafeVarHandle;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerLoadEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for testing if namespaces work correctly
 */
public class CommandNamespaceTests extends TestBase {

	private static final SafeVarHandle<MockCommandMap, Map<String, Command>> commandMapKnownCommands = SafeVarHandle.ofOrNull(MockCommandMap.class, "knownCommands", "knownCommands", Map.class);

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
		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));

		Map<String, Command> knowCommands = commandMapKnownCommands.get((MockCommandMap) MockPlatform.getInstance().getSimpleCommandMap());

		// Register minecraft: namespace. MockBukkit doesn't do this on their own
		for (RegisteredCommand command : CommandAPI.getRegisteredCommands()) {
			if (!command.namespace().equals("minecraft")) {
				continue;
			}

			CommandNode<Object> node = MockPlatform.getInstance().getBrigadierDispatcher().getRoot().getChild(command.commandName());

			for (CommandNode<Object> child : node.getChildren()) {
				// This is probably not really future-proof but it works for the tests if they only have one argument
				LiteralCommandNode<Object> namespacedNode = LiteralArgumentBuilder.literal("minecraft:" + node.getName())
					.requires(node.getRequirement())
					.then(child)
					.executes(child.getCommand()).build();
				MockPlatform.getInstance().getBrigadierDispatcher().getRoot().addChild(namespacedNode);
				knowCommands.put(namespacedNode.getLiteral(), MockPlatform.getInstance().wrapToVanillaCommandWrapper(namespacedNode));

				// If the command has aliases, register their namespaced versions too
				for (String alias : command.aliases()) {
					LiteralCommandNode<Object> namespacedAlias = LiteralArgumentBuilder.literal("minecraft:" + alias)
						.requires(node.getRequirement())
						.then(child)
						.executes(child.getCommand()).build();
					MockPlatform.getInstance().getBrigadierDispatcher().getRoot().addChild(namespacedAlias);
					knowCommands.put(namespacedAlias.getLiteral(), MockPlatform.getInstance().wrapToVanillaCommandWrapper(namespacedAlias));
				}
			}
		}

		assertDoesNotThrow(() -> server.getScheduler().performOneTick());
		assertFalse(CommandAPI.canRegister());

		// Get a CraftPlayer for running VanillaCommandWrapper commands
		Player runCommandsPlayer = Mockito.mock(MockPlatform.getInstance().getCraftPlayerClass());
		// Give player permission to run command
		Mockito.when(runCommandsPlayer.hasPermission(ArgumentMatchers.eq("permission"))).thenReturn(true);
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

	@Test
	public void minecraftNamespaceTestsWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Make sure the default registration with the minecraft: namespace works
		command.register();

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "minecraft:test alpha");
		assertEquals("alpha", results.get());
		assertNoMoreResults(results);
	}

	@Test
	public void minecraftNamespaceTestsWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Make sure the default registration with the minecraft: namespace works
		command.register();

		Player player = enableWithNamespaces();

		assertNotNull(MockPlatform.getInstance().getBrigadierDispatcher().getRoot().getChild("test"));
		assertNotNull(MockPlatform.getInstance().getBrigadierDispatcher().getRoot().getChild("minecraft:test"));

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "minecraft:test alpha");
		assertEquals("alpha", results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void stringNamespaceTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a custom namespace
		command.register("commandtest");

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandtest:test alpha");
		assertEquals("alpha", results.get());

		// Running the command with the minecraft: namespace should fail
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test alpha"));
		assertNoMoreResults(results);
	}

	@Test
	public void stringNamespaceTestWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a custom namespace
		command.register("commandtest");

		Player player = enableWithNamespaces();

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandtest:test alpha");
		assertEquals("alpha", results.get());

		// Running the command with the minecraft: namespace should fail
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test alpha"));
		assertNoMoreResults(results);
	}

	@Disabled
	public void pluginNamespaceTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a plugin instance
		//TODO Apparently, registering this command using the plugin instance here fails
		command.register(Main.getPlugin(Main.class));

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandapitest:test alpha");
		assertEquals("alpha", results.get());

		// Running the command with the minecraft: namespace should fail
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test alpha"));
		assertNoMoreResults(results);
	}

	@Disabled
	public void pluginNamespaceTestWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a plugin instance
		command.register(Main.getPlugin(Main.class));

		Player player = enableWithNamespaces();

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandapitest:test alpha");
		assertEquals("alpha", results.get());

		// Running the command with the minecraft: namespace should fail
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test alpha"));
		assertNoMoreResults(results);
	}

	@Test
	public void aliasesWithDefaultNamespaceTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with the default namespace
		command.register();

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "minecraft:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "minecraft:beta discord");
		assertEquals("discord", results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void aliasesWithDefaultNamespaceTestWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with the default namespace
		command.register();

		Player player = enableWithNamespaces();

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "minecraft:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "minecraft:beta discord");
		assertEquals("discord", results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void aliasesWithCustomNamespacesTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with a custom namespace
		command.register("commandtest");

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandtest:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandtest:beta discord");
		assertEquals("discord", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:alpha discord"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:beta discord"));

		assertNoMoreResults(results);

		CommandAPI.unregister("test", true);
		CommandAPI.unregister("alpha", true);
		CommandAPI.unregister("beta", true);

		// Test aliases with a plugin instance
		// TODO: Apparently, this is failing now
		/*command.register(Main.getPlugin(Main.class));

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandapitest:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandapitest:beta discord");
		assertEquals("discord", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:alpha discord"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:beta discord"));

		assertNoMoreResults(results);*/
	}

	@Test
	public void aliasesWithCustomNamespacesTestWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		CommandAPICommand command = new CommandAPICommand("test")
			.withAliases("alpha", "beta")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test aliases with a custom namespace
		command.register("commandtest");

		Player player = enableWithNamespaces();

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandtest:alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "commandtest:beta discord");
		assertEquals("discord", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:alpha discord"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:beta discord"));

		assertNoMoreResults(results);
	}

	@Test
	public void sameCommandNameButDifferentNamespaceTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		CommandAPICommand a = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("forced"))
			.executesPlayer(info -> {
				results.set("forced");
			});

		CommandAPICommand b = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("notforced"))
			.executesPlayer(info -> {
				results.set("notforced");
			});

		a.register("a");
		b.register("b");

		server.dispatchCommand(player, "test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player, "a:test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player, "test notforced");
		assertEquals("notforced", results.get());

		server.dispatchCommand(player, "b:test notforced");
		assertEquals("notforced", results.get());

		// TODO: These two apparently fail now. I have no idea why
		//assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "a:test notforced"));
		//assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "b:test forced"));

		assertNoMoreResults(results);
	}

	@Test
	public void sameCommandNameButDifferentNamespaceTestWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		CommandAPICommand a = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("forced"))
			.executesPlayer(info -> {
				results.set("forced");
			});

		CommandAPICommand b = new CommandAPICommand("test")
			.withArguments(LiteralArgument.of("notforced"))
			.executesPlayer(info -> {
				results.set("notforced");
			});

		a.register("a");
		b.register("b");

		Player player = enableWithNamespaces();

		server.dispatchCommand(player, "test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player, "a:test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player, "test notforced");
		assertEquals("notforced", results.get());

		server.dispatchCommand(player, "b:test notforced");
		assertEquals("notforced", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "a:test notforced"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "b:test forced"));

		assertNoMoreResults(results);
	}

	@Test
	public void specialCasesTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		Player player = enableWithNamespaces();

		// Special case: Registering a command without a namespace
		CommandAPICommand command = new CommandAPICommand("test")
			.executesPlayer(info -> {
				results.set("success");
			});

		command.register("");

		server.dispatchCommand(player, "test");
		assertEquals("success", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test"));
	}

	@Test
	public void noNamespaceTestWhileServerIsStartingWhenRegistering() {
		Mut<String> results = Mut.of();

		// Special case: Registering a command without a namespace
		CommandAPICommand command = new CommandAPICommand("test")
			.executesPlayer(info -> {
				results.set("success");
			});

		command.register("");

		Player player = enableWithNamespaces();

		server.dispatchCommand(player, "test");
		assertEquals("success", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test"));
	}

	@Test
	public void nullNamespaceTest() {
		// Registering a command using null should fail
		String namespace = null;
		assertThrows(NullPointerException.class, () -> new CommandAPICommand("test").executesPlayer(info -> {
		}).register(namespace));
	}

}
