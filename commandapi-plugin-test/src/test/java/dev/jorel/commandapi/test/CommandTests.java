package dev.jorel.commandapi.test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.test.Main;

/**
 * Tests for command semantics
 */
@SuppressWarnings("unused")
public class CommandTests {

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
	public void testGreedyStringArgumentNotAtEnd() {
		assertDoesNotThrow(() -> {
			new CommandAPICommand("test")
				.withArguments(new StringArgument("arg1"))
				.withArguments(new GreedyStringArgument("arg2"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
					String arg2 = (String) args.get(0);
				})
				.register();
		});

		assertThrows(GreedyArgumentException.class, () -> {
			new CommandAPICommand("test")
				.withArguments(new GreedyStringArgument("arg1"))
				.withArguments(new StringArgument("arg2"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
					String arg2 = (String) args.get(0);
				})
				.register();
		});
	}

	@Test
	public void testInvalidCommandName() {
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand((String) null)
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
				})
				.register();
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("")
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
				})
				.register();
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("my command")
				.withArguments(new StringArgument("arg1"))
				.executes((sender, args) -> {
					String arg1 = (String) args.get(0);
				})
				.register();
		});
	}
	
	@Test
	public void testNoExecutor() {
		// TODO: Catch this case. Need to check if has no executor AND has no
		// subcommand or otherwise when .register() called
		new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.register();
	}

}
