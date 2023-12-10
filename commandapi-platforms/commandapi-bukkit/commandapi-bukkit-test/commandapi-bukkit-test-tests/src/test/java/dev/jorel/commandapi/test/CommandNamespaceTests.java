package dev.jorel.commandapi.test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerLoadEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

	/*********
	 * Tests *
	 *********/

	@Test
	public void minecraftNamespaceTestsWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Make sure the default registration with the minecraft: namespace works
		command.register();

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "minecraft:alpha");
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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		//server.dispatchCommand(player, "minecraft:test alpha");
		// assertEquals("alpha", results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void stringNamespaceTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		CommandAPICommand command = new CommandAPICommand("test")
			.withArguments(new StringArgument("string"))
			.executesPlayer(info -> {
				results.set(info.args().getUnchecked("string"));
			});

		// Test registering the command with a custom namespace
		command.register("commandtest");

		server.dispatchCommand(player, "test alpha");
		assertEquals("alpha", results.get());

		server.dispatchCommand(player, "commandtest:alpha");
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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "alpha discord");
		assertEquals("discord", results.get());

		server.dispatchCommand(player, "beta discord");
		assertEquals("discord", results.get());

		//server.dispatchCommand(player, "minecraft:alpha discord");
		//assertEquals("discord", results.get());

		//server.dispatchCommand(player, "minecraft:beta discord");
		//assertEquals("discord", results.get());

		assertNoMoreResults(results);
	}

	@Test
	public void aliasesWithCustomNamespacesTestWhileServerIsEnabledWhenRegistering() {
		Mut<String> results = Mut.of();

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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
		command.register(Main.getPlugin(Main.class));

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

		assertNoMoreResults(results);
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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		server.dispatchCommand(player ,"test notforced");
		assertEquals("notforced", results.get());

		server.dispatchCommand(player, "b:test notforced");
		assertEquals("notforced", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "a:test notforced"));
		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "b:test forced"));

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player, "a:test forced");
		assertEquals("forced", results.get());

		server.dispatchCommand(player ,"test notforced");
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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

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

		disablePaperImplementations();
		Bukkit.getPluginManager().callEvent(new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP));
		assertDoesNotThrow(() -> server.getScheduler().performOneTick());

		assertFalse(CommandAPI.canRegister());

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test");
		assertEquals("success", results.get());

		assertThrows(CommandSyntaxException.class, () -> server.dispatchThrowableCommand(player, "minecraft:test"));
	}

	@Test
	public void nullNamespaceTest() {
		// Registering a command using null should fail
		String namespace = null;
		assertThrows(NullPointerException.class, () -> new CommandAPICommand("test").executesPlayer(info -> {}).register(namespace));
	}

}
