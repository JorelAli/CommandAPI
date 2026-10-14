package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the semantics of registering commands
 */
class CommandRegistrationTests extends TestBase {

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
	void testCommandAPICommandGreedyArgumentException() {
		// Shouldn't throw, greedy argument is at the end
		CommandAPICommand validGreedyCommand = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.withArguments(new GreedyStringArgument("arg2"))
			.executesPlayer(P_EXEC);

		assertDoesNotThrow(() -> validGreedyCommand.register());

		// Should throw, greedy argument is not at the end
		CommandAPICommand invalidGreedyCommand = new CommandAPICommand("test")
			.withArguments(new GreedyStringArgument("arg1"))
			.withArguments(new StringArgument("arg2"))
			.executesPlayer(P_EXEC);

		assertThrowsWithMessage(
			GreedyArgumentException.class,
			"Only one GreedyStringArgument or ChatArgument can be declared, at the end of a List. Found arguments: arg1<GreedyStringArgument> arg2<StringArgument> ",
			invalidGreedyCommand::register
		);
	}

	@Test
	void testCommandTreeGreedyArgumentException() {
		// Shouldn't throw, greedy argument is at the end
		CommandTree validGreedyCommand = new CommandTree("test")
			.then(
				new StringArgument("arg1")
					.then(
						new GreedyStringArgument("arg2")
							.executesPlayer(P_EXEC)
					)
			);

		assertDoesNotThrow(() -> validGreedyCommand.register());

		// Should throw, greedy argument is not at the end
		CommandTree invalidGreedyCommand = new CommandTree("test")
			.then(
				new GreedyStringArgument("arg1")
					.then(
						new StringArgument("arg2")
							.executesPlayer(P_EXEC)
					)
			);

		assertThrowsWithMessage(
			GreedyArgumentException.class,
			"Only one GreedyStringArgument or ChatArgument can be declared, at the end of a List. Found arguments: arg1<GreedyStringArgument> arg2<StringArgument> ",
			invalidGreedyCommand::register
		);
	}

	@Test
	void testCommandAPICommandInvalidCommandNameException() {
		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'null' cannot be registered!",
			() -> new CommandAPICommand((String) null)
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name '' cannot be registered!",
			() -> new CommandAPICommand("")
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'my command' cannot be registered!",
			() -> new CommandAPICommand("my command")
		);
	}

	@Test
	void testCommandTreeInvalidCommandNameException() {
		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'null' cannot be registered!",
			() -> new CommandTree(null)
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name '' cannot be registered!",
			() -> new CommandTree("")
		);

		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'my command' cannot be registered!",
			() -> new CommandTree("my command")
		);
	}

	@Test
	void testCommandAPICommandMissingCommandExecutorException() {
		// This command has no executor, should complain because this isn't runnable
		CommandAPICommand commandWithNoExecutors = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"/test does not declare any executors or executable subcommands!",
			commandWithNoExecutors::register
		);

		// This command has no executable subcommands, should complain because this isn't runnable
		CommandAPICommand commandWithNoRunnableSubcommands = new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("sub"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"/test does not declare any executors or executable subcommands!",
			commandWithNoRunnableSubcommands::register
		);

		// This command is okay because it is eventually executable through a subcommand
		CommandAPICommand commandWithEventuallyRunnableSubcommand = new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("sub")
				.withSubcommand(new CommandAPICommand("sub")
					.withSubcommand(new CommandAPICommand("sub")
						.withSubcommand(new CommandAPICommand("sub")
							.executesPlayer(P_EXEC)
						)
					)
				)
			);

		assertDoesNotThrow(() -> commandWithEventuallyRunnableSubcommand.register());
	}

	// TODO: This test does not succeed
	//  Calling `register` on CommandTree without any `executes` defined simply does not create any commands
	//  These cases should throw MissingCommandExecutorExceptions
	@Disabled
	@Test
	void testCommandTreeMissingCommandExecutorException() {
		// This command has no executor, should complain because this isn't runnable
		CommandTree commandWithNoExecutors = new CommandTree("test");

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"",
			commandWithNoExecutors::register
		);

		// This command has no executable arguments, should complain because this isn't runnable
		CommandTree commandWithNoRunnableSubcommands = new CommandTree("test")
			.then(new LiteralArgument("sub"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"",
			commandWithNoRunnableSubcommands::register
		);

		// This command is okay because it eventually has an executable argument
		CommandTree commandWithEventuallyRunnableSubcommand = new CommandTree("test")
			.then(new LiteralArgument("sub")
				.then(new LiteralArgument("sub")
					.then(new LiteralArgument("sub")
						.then(new LiteralArgument("sub")
							.executesPlayer(P_EXEC)
						)
					)
				)
			);

		assertDoesNotThrow(() -> commandWithEventuallyRunnableSubcommand.register());

		// This command is not okay because some paths are not executable
		CommandTree commandTreeWithSomeNotExecutablePaths = new CommandTree("test")
			.then(new LiteralArgument("executable1").then(new LiteralArgument("sub").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("notExecutable1").then(new LiteralArgument("sub")))
			.then(new LiteralArgument("notExecutable2").then(new LiteralArgument("sub")))
			.then(new LiteralArgument("executable2").then(new LiteralArgument("sub").executesPlayer(P_EXEC)));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"",
			commandTreeWithSomeNotExecutablePaths::register
		);
	}

}