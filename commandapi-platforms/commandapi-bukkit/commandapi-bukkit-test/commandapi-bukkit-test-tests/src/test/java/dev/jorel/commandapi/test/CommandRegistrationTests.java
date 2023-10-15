package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.DuplicateNodeNameException;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
			"A greedy argument can only be declared at the end of a command. Going down the [test<MultiLiteralArgument>] branch, found arg1<GreedyStringArgument> followed by [arg2<StringArgument>]",
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
			"A greedy argument can only be declared at the end of a command. Going down the [test<MultiLiteralArgument>] branch, found arg1<GreedyStringArgument> followed by [arg2<StringArgument>]",
			invalidGreedyCommand::register
		);
	}

	@Test
	void testCommandAPICommandInvalidCommandNameException() {
		assertThrowsWithMessage(
			InvalidCommandNameException.class,
			"Invalid command with name 'null' cannot be registered!",
			() -> new CommandAPICommand(null)
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
			"The command path test<LiteralArgument> is not executable!",
			commandWithNoExecutors::register
		);

		// This command has no executable subcommands, should complain because this isn't runnable
		CommandAPICommand commandWithNoRunnableSubcommands = new CommandAPICommand("test")
			.withSubcommand(new CommandAPICommand("sub"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"The command path sub<LiteralArgument> is not executable!",
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

	@Test
	void testCommandTreeMissingCommandExecutorException() {
		// This command has no executor, should complain because this isn't runnable
		CommandTree commandWithNoExecutors = new CommandTree("test");

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"The command path test<LiteralArgument> is not executable!",
			commandWithNoExecutors::register
		);

		// This command has no executable arguments, should complain because this isn't runnable
		CommandTree commandWithNoRunnableSubcommands = new CommandTree("test")
			.then(new LiteralArgument("sub"));

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"The command path [test<MultiLiteralArgument>] ending with sub<LiteralArgument> is not executable!",
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
			"The command path [test<MultiLiteralArgument> notExecutable1<LiteralArgument>] ending with sub<LiteralArgument> is not executable!",
			commandTreeWithSomeNotExecutablePaths::register
		);
	}

	@Test
	void testCommandAPICommandDuplicateNodeNameException() {
		// This command is not okay because it has duplicate names for Arguments 1 and 3
		CommandAPICommand commandWithDuplicateArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new StringArgument("alice"),
				new StringArgument("bob"),
				new StringArgument("alice")
			)
			.executesPlayer(P_EXEC);

		assertThrowsWithMessage(
			DuplicateNodeNameException.class,
			"Duplicate node names for non-literal arguments are not allowed! Going down the [test<MultiLiteralArgument> alice<StringArgument> bob<StringArgument>] branch, found alice<StringArgument>, which had a duplicate node name",
			commandWithDuplicateArgumentNames::register
		);

		// This command is okay because LiteralArguments are exempt from the duplicate name rule
		CommandAPICommand commandWithDuplicateLiteralArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new LiteralArgument("alice"),
				new LiteralArgument("bob"),
				new LiteralArgument("alice")
			)
			.executesPlayer(P_EXEC);

		assertDoesNotThrow(commandWithDuplicateLiteralArgumentNames::register);

		// This command is okay because MultiLiteralArguments are exempt from the duplicate name rule
		CommandAPICommand commandWithDuplicateMultiLiteralArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new MultiLiteralArgument("alice", "option1", "option2"),
				new MultiLiteralArgument("bob", "option1", "option2"),
				new MultiLiteralArgument("alice", "option1", "option2")
			)
			.executesPlayer(P_EXEC);

		assertDoesNotThrow(commandWithDuplicateMultiLiteralArgumentNames::register);
	}

	@Test
	void testCommandTreeDuplicateNodeNameException() {
		// This command is not okay because it has duplicate names for Arguments 1 and 3
		CommandTree commandWithDuplicateArgumentNames = new CommandTree("test")
			.then(
				new StringArgument("alice").then(
					new StringArgument("bob").then(
						new StringArgument("alice")
							.executesPlayer(P_EXEC)
					)
				)
			);

		assertThrowsWithMessage(
			DuplicateNodeNameException.class,
			"Duplicate node names for non-literal arguments are not allowed! Going down the [test<MultiLiteralArgument> alice<StringArgument> bob<StringArgument>] branch, found alice<StringArgument>, which had a duplicate node name",
			commandWithDuplicateArgumentNames::register
		);

		// This command is okay because LiteralArguments are exempt from the duplicate name rule
		CommandTree commandWithDuplicateLiteralArgumentNames = new CommandTree("test")
			.then(
				new LiteralArgument("alice").then(
					new LiteralArgument("bob").then(
						new LiteralArgument("alice")
							.executesPlayer(P_EXEC)
					)
				)
			);

		assertDoesNotThrow(commandWithDuplicateLiteralArgumentNames::register);

		// This command is okay because MultiLiteralArguments are exempt from the duplicate name rule
		CommandTree commandWithDuplicateMultiLiteralArgumentNames = new CommandTree("test")
			.then(
				new MultiLiteralArgument("alice", "option1", "option2").then(
					new MultiLiteralArgument("bob", "option1", "option2").then(
						new MultiLiteralArgument("alice", "option1", "option2")
							.executesPlayer(P_EXEC)
					)
				)
			);

		assertDoesNotThrow(commandWithDuplicateMultiLiteralArgumentNames::register);

		// This command is okay because the duplicate names are on different paths
		CommandTree commandWithDuplicateNamesSeparated = new CommandTree("test")
			.then(new LiteralArgument("path1").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path2").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path3").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path4").then(new StringArgument("alice").executesPlayer(P_EXEC)));

		assertDoesNotThrow(commandWithDuplicateNamesSeparated::register);
	}
}
