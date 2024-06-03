package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.FloatArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.LongArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.CommandConflictException;
import dev.jorel.commandapi.exceptions.DuplicateNodeNameException;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;
import dev.jorel.commandapi.executors.NormalExecutorInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.bukkit.entity.Player;

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
			"A greedy argument can only be declared at the end of a command. Going down the [test<MultiLiteralArgument>] branch, found the greedy argument arg1<GreedyStringArgument> followed by arg2<StringArgument>",
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
			"A greedy argument can only be declared at the end of a command. Going down the [test<MultiLiteralArgument>] branch, found the greedy argument arg1<GreedyStringArgument> followed by arg2<StringArgument>",
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

		// This command is not okay, because the presence of arguments on
		//  the root indicate the root path was meant to be executed
		CommandAPICommand commandWithNotExecutableRootArgument = new CommandAPICommand("test")
			.withArguments(new StringArgument("arg1"))
			.withSubcommand(
				new CommandAPICommand("sub")
					.executesPlayer(P_EXEC)
			);

		assertThrowsWithMessage(
			MissingCommandExecutorException.class,
			"The command path test<LiteralArgument> is not executable!",
			commandWithNotExecutableRootArgument::register
		);
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
			"Duplicate node names for listed arguments are not allowed! Going down the [test<MultiLiteralArgument> alice<StringArgument> bob<StringArgument>] branch, found alice<StringArgument>, which had a duplicated node name",
			commandWithDuplicateArgumentNames::register
		);

		// This command is okay because unlisted arguments do not cause conflict
		CommandAPICommand commandWithDuplicateUnlistedArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new StringArgument("alice").setListed(false),
				new StringArgument("bob"),
				new StringArgument("alice"),
				new StringArgument("bob").setListed(false)
			)
			.executesPlayer(P_EXEC);

		assertDoesNotThrow(() -> commandWithDuplicateUnlistedArgumentNames.register());

		// This command is okay because LiteralArguments are unlisted by default
		CommandAPICommand commandWithDuplicateLiteralArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new LiteralArgument("alice"),
				new LiteralArgument("bob"),
				new LiteralArgument("alice")
			)
			.executesPlayer(P_EXEC);

		assertDoesNotThrow(() -> commandWithDuplicateLiteralArgumentNames.register());

		// However, listed LiteralArguments do conflict
		CommandAPICommand commandWithDuplicateListedLiteralArgumentNames = new CommandAPICommand("test")
			.withArguments(
				new LiteralArgument("alice").setListed(true),
				new LiteralArgument("bob").setListed(true),
				new LiteralArgument("alice").setListed(true)
			)
			.executesPlayer(P_EXEC);

		assertThrowsWithMessage(
			DuplicateNodeNameException.class,
			"Duplicate node names for listed arguments are not allowed! Going down the [test<MultiLiteralArgument> alice<LiteralArgument> bob<LiteralArgument>] branch, found alice<LiteralArgument>, which had a duplicated node name",
			commandWithDuplicateListedLiteralArgumentNames::register
		);
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
			"Duplicate node names for listed arguments are not allowed! Going down the [test<MultiLiteralArgument> alice<StringArgument> bob<StringArgument>] branch, found alice<StringArgument>, which had a duplicated node name",
			commandWithDuplicateArgumentNames::register
		);

		// This command is okay because unlisted arguments do not cause conflict
		CommandTree commandWithDuplicateUnlistedArgumentNames = new CommandTree("test")
			.then(
				new StringArgument("alice").setListed(false).then(
					new StringArgument("bob").then(
						new StringArgument("alice").then(
							new StringArgument("bob").setListed(false)
								.executesPlayer(P_EXEC)
						)
					)
				)
			);

		assertDoesNotThrow(() -> commandWithDuplicateUnlistedArgumentNames.register());

		// This command is okay because LiteralArguments are unlisted by default
		CommandTree commandWithDuplicateLiteralArgumentNames = new CommandTree("test")
			.then(
				new LiteralArgument("alice").then(
					new LiteralArgument("bob").then(
						new LiteralArgument("alice")
							.executesPlayer(P_EXEC)
					)
				)
			);

		assertDoesNotThrow(() -> commandWithDuplicateLiteralArgumentNames.register());

		// However, listed LiteralArguments do conflict
		CommandTree commandWithDuplicateListedLiteralArgumentNames = new CommandTree("test")
			.then(
				new LiteralArgument("alice").setListed(true).then(
					new LiteralArgument("bob").setListed(true).then(
						new LiteralArgument("alice").setListed(true)
							.executesPlayer(P_EXEC)
					)
				)
			);

		assertThrowsWithMessage(
			DuplicateNodeNameException.class,
			"Duplicate node names for listed arguments are not allowed! Going down the [test<MultiLiteralArgument> alice<LiteralArgument> bob<LiteralArgument>] branch, found alice<LiteralArgument>, which had a duplicated node name",
			commandWithDuplicateListedLiteralArgumentNames::register
		);

		// This command is okay because the duplicate names are on different paths
		CommandTree commandWithDuplicateNamesSeparated = new CommandTree("test")
			.then(new LiteralArgument("path1").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path2").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path3").then(new StringArgument("alice").executesPlayer(P_EXEC)))
			.then(new LiteralArgument("path4").then(new StringArgument("alice").executesPlayer(P_EXEC)));

		assertDoesNotThrow(() -> commandWithDuplicateNamesSeparated.register());
	}

	@Test
	void testCommandConflictException() {
		Mut<String> results = Mut.of();
		Player player = server.addPlayer();

		// The executor we register first should not be overwritten and should always run
		NormalExecutorInfo<Player, ?> firstExecutor = info -> {results.set("first");};
		NormalExecutorInfo<Player, ?> secondExecutor = info -> {results.set("second");};

		// No arguments
		new CommandAPICommand("noArguments")
			.executesPlayer(firstExecutor)
			.register();

		CommandAPICommand noArguments = new CommandAPICommand("noArguments")
			.executesPlayer(secondExecutor);

		assertThrowsWithMessage(
			CommandConflictException.class, 
			"The command path \"/noArguments\" could not be registered because it conflicts with a previously registered command.", 
			noArguments::register
		);
		assertStoresResult(player, "noArguments", results, "first");

		// Arguments
		new CommandAPICommand("arguments")
			.withArguments(
				new LiteralArgument("literal"),
				new StringArgument("string")
			)
			.executesPlayer(firstExecutor)
			.register();

		CommandAPICommand arguments = new CommandAPICommand("arguments")
			.withArguments(
				new LiteralArgument("literal"),
				new StringArgument("string")
			)
			.executesPlayer(secondExecutor);

		assertThrowsWithMessage(
			CommandConflictException.class,
			"The command path \"/arguments literal string\" could not be registered because it conflicts with a previously registered command.",
			arguments::register
		);
		assertStoresResult(player, "arguments literal string", results, "first");

		// Different argument types
		new CommandAPICommand("argumentTypes")
			.withArguments(
				new IntegerArgument("alice"),
				new FloatArgument("bob")
			)
			.executesPlayer(firstExecutor)
			.register();

		CommandAPICommand argumentTypes = new CommandAPICommand("argumentTypes")
			.withArguments(
				new LongArgument("alice"),
				new DoubleArgument("bob")
			)
			.executesPlayer(secondExecutor);

		assertThrowsWithMessage(
			CommandConflictException.class, 
			"The command path \"/argumentTypes alice bob\" could not be registered because it conflicts with a previously registered command.", 
			argumentTypes::register
		);
		assertStoresResult(player, "argumentTypes 0 0", results, "first");

		// Different branches
		new CommandTree("branches")
			.then(
				new LiteralArgument("alice")
					.executesPlayer(firstExecutor)
			)
			.then(
				new LiteralArgument("bob")
					.executesPlayer(firstExecutor)
			)
			.register();

		CommandTree branches = new CommandTree("branches")
			.then(
				new LiteralArgument("bob")
					.executesPlayer(secondExecutor)
			)
			.then(
				new LiteralArgument("alice")
					.executesPlayer(secondExecutor)
			);

		assertThrowsWithMessage(
			CommandConflictException.class, 
			"The command path \"/branches bob\" could not be registered because it conflicts with a previously registered command.", 
			branches::register
		);
		assertStoresResult(player, "branches alice", results, "first");
		assertStoresResult(player, "branches bob", results, "first");

		assertNoMoreResults(results);
	}
}
