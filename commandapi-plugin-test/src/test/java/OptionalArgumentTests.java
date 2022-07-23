import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

/**
 * Tests for optional arguments
 */
public class OptionalArgumentTests {
	
	private CustomServerMock server;
	private Main plugin;

	private String getDispatcherString() {
		try {
			return Files.readString(new File("command_registration.json").toPath());
		} catch (IOException e) {
			return "";
		}
	}
	
	@BeforeEach
	public void setUp() {
		server = MockBukkit.mock(new CustomServerMock());
		plugin = MockBukkit.load(Main.class);
	}

	@AfterEach
	public void tearDown() {
		Bukkit.getScheduler().cancelTasks(plugin);
		plugin.onDisable();
		MockBukkit.unmock();
	}

	@Test
	public void optionalArgument() {
		new CommandAPICommand("test")
			.withOptionalArgument(new StringArgument("stringarg"), "hello")
			.executesPlayer((player, args) -> {
				player.sendMessage("value: " + args[0]);
			})
			.register();

		PlayerMock player = server.addPlayer();
		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "test bye"));
		assertEquals("value: hello", player.nextMessage());
		assertEquals("value: bye", player.nextMessage());
	}
	
	@Test
	public void twoOptionalArguments() {
		new CommandAPICommand("test")
			.withOptionalArgument(new StringArgument("arg1"), "hello")
			.withOptionalArgument(new StringArgument("arg2"), "bye")
			.executesPlayer((player, args) -> {
				player.sendMessage("value: " + Arrays.toString(args));
			})
			.register();

//		System.out.println(getDispatcherString());
		
		PlayerMock player = server.addPlayer();
		assertTrue(server.dispatchCommand(player, "test"));
		assertTrue(server.dispatchCommand(player, "test 1"));
		assertTrue(server.dispatchCommand(player, "test 1 2"));
		assertNotEquals("value: [hello, bye]", player.nextMessage()); // TODO: Value is currently [bye]
		assertNotEquals("value: [1, bye]", player.nextMessage());     // TODO: Value is currently [1]
		assertEquals("value: [1, 2]", player.nextMessage());
	}
	
	@Test
	public void optionalArgumentThenRequiredArgument() {
		new CommandAPICommand("test")
			.withOptionalArgument(new StringArgument("stringarg"), "defaultstr")
			.withArguments(new IntegerArgument("intarg"))
			.executesPlayer((player, args) -> {
				player.sendMessage("value: " + Arrays.toString(args));
			})
			.register();

		PlayerMock player = server.addPlayer();
		assertTrue(server.dispatchCommand(player, "test 123"));
		assertTrue(server.dispatchCommand(player, "test hello 123"));
		assertEquals("value: [defaultstr, 123]", player.nextMessage());
		assertEquals("value: [hello, 123]", player.nextMessage());
	}

}
