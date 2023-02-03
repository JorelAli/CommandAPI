package dev.jorel.commandapi.test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;

/**
 * Tests for command semantics
 */
@SuppressWarnings("unused")
class CommandTests extends TestBase {
	
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
	void testGreedyStringArgumentNotAtEnd() {
		
		// Shouldn't throw, greedy argument is at the end
		CommandAPICommand validGreedyCommand = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.withArguments(new GreedyStringArgument("arg2"))
			.executes((sender, args) -> {
				String arg1 = (String) args.get(0);
				String arg2 = (String) args.get(0);
			});
		
		assertDoesNotThrow(() -> {
			validGreedyCommand.register();
		});

		// Should throw, greedy argument is not at the end
		CommandAPICommand invalidGreedyCommand = new CommandAPICommand("test")
			.withArguments(new GreedyStringArgument("arg1"))
			.withArguments(new StringArgument("arg2"))
			.executes((sender, args) -> {
				String arg1 = (String) args.get(0);
				String arg2 = (String) args.get(0);
			});

		assertThrows(GreedyArgumentException.class, () -> {
			invalidGreedyCommand.register();
		});
	}

	@Test
	void testInvalidCommandName() {
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand((String) null);
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("");
		});
		
		assertThrows(InvalidCommandNameException.class, () -> {
			new CommandAPICommand("my command");
		});
	}
	
	@Test
	void testNoExecutor() {
		// This command has no executor, should complain because this isn't runnable
		CommandAPICommand commandWithNoExecutors = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"));
		
		assertThrows(MissingCommandExecutorException.class, () -> {
			commandWithNoExecutors.register();
		});
		
		// This command has no subcommands, should complain because this isn't runnable
		CommandAPICommand commandWithNoRunnableSubcommands = new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("sub"));
		
		assertThrows(MissingCommandExecutorException.class, () -> {
			commandWithNoRunnableSubcommands.register();
		});
	}

}
