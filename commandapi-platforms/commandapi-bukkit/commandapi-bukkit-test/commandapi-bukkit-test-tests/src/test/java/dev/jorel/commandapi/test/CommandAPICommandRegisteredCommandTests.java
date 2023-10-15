package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.RegisteredCommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for making sure the {@link RegisteredCommand} information is correct when registering {@link CommandAPICommand}s
 */
class CommandAPICommandRegisteredCommandTests extends TestBase {

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

	private RegisteredCommand registeredCommandNoHelpOrPermission(String name, List<String> argsAsStr, String... aliases) {
		return new RegisteredCommand(name, argsAsStr,
			Optional.empty(), Optional.empty(), Optional.empty(),
			aliases,
			CommandPermission.NONE, "minecraft"
		);
	}

	private void assertCreatedRegisteredCommands(RegisteredCommand... commands) {
		List<RegisteredCommand> expectedCommands = Arrays.asList(commands);
		List<RegisteredCommand> actualCommands = CommandAPI.getRegisteredCommands();

		if (expectedCommands.size() != actualCommands.size()) {
			StringBuilder builder = new StringBuilder();
			builder.append("Expected ").append(expectedCommands.size()).append(" command(s), found ").append(actualCommands.size()).append(" command(s).");

			builder.append("\nExpected: ");
			addRegisteredCommandList(builder, expectedCommands);
			builder.append("\nActual: ");
			addRegisteredCommandList(builder, actualCommands);

			fail(builder.toString());
		}

		for (int i = 0; i < expectedCommands.size(); i++) {
			RegisteredCommand expectedCommand = expectedCommands.get(i);
			RegisteredCommand actualCommand = actualCommands.get(i);

			if (!Objects.equals(expectedCommand, actualCommand)) {
				StringBuilder builder = new StringBuilder();
				builder.append("Command #").append(i + 1).append(" differed. Expected:\n");
				builder.append(expectedCommand);
				builder.append("\nActual:\n");
				builder.append(actualCommand);

				builder.append("\nExpected list: ");
				addRegisteredCommandList(builder, expectedCommands);
				builder.append("\nActual list: ");
				addRegisteredCommandList(builder, actualCommands);

				fail(builder.toString());
			}
		}
	}

	private void addRegisteredCommandList(StringBuilder builder, List<RegisteredCommand> commands) {
		if (commands.isEmpty()) {
			builder.append("[]");
			return;
		}

		builder.append("[\n");
		for (RegisteredCommand command : commands) {
			builder.append("\t");
			builder.append(command);
			builder.append("\n");
		}
		builder.append("]");
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void testRegister() {
		new CommandAPICommand("command")
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(registeredCommandNoHelpOrPermission("command", List.of()));
	}

	@Test
	void testRegisterHelpInformation() {
		new CommandAPICommand("command")
			.withHelp("short description", "full description")
			.withUsage(
				"usage 1",
				"usage 2",
				"usage 3"
			)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			new RegisteredCommand(
				"command", List.of(),
				Optional.of("short description"), Optional.of("full description"), Optional.of(new String[]{"usage 1", "usage 2", "usage 3"}),
				new String[0], CommandPermission.NONE, "minecraft"
			)
		);
	}

	@Test
	void testRegisterOpPermission() {
		new CommandAPICommand("command")
			.withPermission(CommandPermission.OP)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			new RegisteredCommand(
				"command", List.of(),
				Optional.empty(), Optional.empty(), Optional.empty(),
				new String[0], CommandPermission.OP, "minecraft"
			)
		);
	}

	@Test
	void testRegisterStringPermission() {
		new CommandAPICommand("command")
			.withPermission("permission")
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			new RegisteredCommand(
				"command", List.of(),
				Optional.empty(), Optional.empty(), Optional.empty(),
				new String[0], CommandPermission.fromString("permission"), "minecraft"
			)
		);
	}

	@Test
	void testRegisterOneAlias() {
		new CommandAPICommand("command")
			.withAliases("alias1")
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(registeredCommandNoHelpOrPermission("command", List.of(), "alias1"));
	}

	@Test
	void testRegisterTwoAliases() {
		new CommandAPICommand("command")
			.withAliases("alias1", "alias2")
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(registeredCommandNoHelpOrPermission("command", List.of(), "alias1", "alias2"));
	}

	@Test
	void testRegisterOneArgument() {
		new CommandAPICommand("command")
			.withArguments(new StringArgument("string"))
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(registeredCommandNoHelpOrPermission("command", List.of("string:StringArgument")));
	}

	@Test
	void testRegisterTwoArguments() {
		new CommandAPICommand("command")
			.withArguments(
				new StringArgument("string"),
				new IntegerArgument("integer")
			)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(registeredCommandNoHelpOrPermission("command", List.of("string:StringArgument", "integer:IntegerArgument")));
	}

	@Test
	void testRegisterMultiLiteralArguments() {
		new CommandAPICommand("command")
			.withArguments(
				new MultiLiteralArgument("literal1", "a", "b", "c"),
				new MultiLiteralArgument("literal2", "d", "e", "f")
			)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("a:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("b:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("c:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("a:LiteralArgument", "e:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("b:LiteralArgument", "e:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("c:LiteralArgument", "e:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("a:LiteralArgument", "f:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("b:LiteralArgument", "f:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("c:LiteralArgument", "f:LiteralArgument"))
		);
	}

	@Test
	void testRegisterOneOptionalArgument() {
		new CommandAPICommand("command")
			.withOptionalArguments(new StringArgument("string"))
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of()),
			registeredCommandNoHelpOrPermission("command", List.of("string:StringArgument"))
		);
	}

	@Test
	void testRegisterTwoOptionalArguments() {
		new CommandAPICommand("command")
			.withOptionalArguments(
				new StringArgument("string"),
				new IntegerArgument("integer")
			)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of()),
			registeredCommandNoHelpOrPermission("command", List.of("string:StringArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("string:StringArgument", "integer:IntegerArgument"))
		);
	}

	@Test
	void testRegisterCombinedOptionalArguments() {
		new CommandAPICommand("command")
			.withOptionalArguments(
				new LiteralArgument("1").combineWith(new LiteralArgument("2")),
				new LiteralArgument("3").combineWith(new LiteralArgument("4"))
			)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of()),
			registeredCommandNoHelpOrPermission("command", List.of("1:LiteralArgument", "2:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("1:LiteralArgument", "2:LiteralArgument", "3:LiteralArgument", "4:LiteralArgument"))
		);
	}

	@Test
	void testRegisterCombinedRequiredAndOptionalArguments() {
		new CommandAPICommand("command")
			.withArguments(
				new LiteralArgument("1").combineWith(new LiteralArgument("2")),
				new LiteralArgument("3").combineWith(new LiteralArgument("4"))
			)
			.withOptionalArguments(
				new LiteralArgument("5").combineWith(new LiteralArgument("6")),
				new LiteralArgument("7").combineWith(new LiteralArgument("8"))
			)
			.executesPlayer(P_EXEC)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of(
				"1:LiteralArgument", "2:LiteralArgument", "3:LiteralArgument", "4:LiteralArgument"
			)),
			registeredCommandNoHelpOrPermission("command", List.of(
				"1:LiteralArgument", "2:LiteralArgument", "3:LiteralArgument", "4:LiteralArgument",
				"5:LiteralArgument", "6:LiteralArgument"
			)),
			registeredCommandNoHelpOrPermission("command", List.of(
				"1:LiteralArgument", "2:LiteralArgument", "3:LiteralArgument", "4:LiteralArgument",
				"5:LiteralArgument", "6:LiteralArgument", "7:LiteralArgument", "8:LiteralArgument"
			))
		);
	}

	//////////////////////////////////////
	// SUBCOMMANDS                      //
	// The same as commands, but deeper //
	//////////////////////////////////////

	@Test
	void testRegisterOneSubcommand() {
		new CommandAPICommand("command")
			.withSubcommand(
				new CommandAPICommand("subcommand")
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument")));
	}

	@Test
	void testRegisterTwoSubcommands() {
		new CommandAPICommand("command")
			.withSubcommands(
				new CommandAPICommand("subcommand1")
					.executesPlayer(P_EXEC),
				new CommandAPICommand("subcommand2")
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument"))
		);
	}

	@Test
	void testRegisterOneSubcommandAndBaseExecutable() {
		new CommandAPICommand("command")
			.executesPlayer(P_EXEC)
			.withSubcommand(
				new CommandAPICommand("subcommand")
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of()),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument"))
		);
	}

	@Test
	void testRegisterSubcommandWithAliases() {
		new CommandAPICommand("command")
			.withSubcommand(
				new CommandAPICommand("subcommand")
					.withAliases("alias1", "alias2")
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias1:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias2:LiteralArgument"))
		);
	}

	@Test
	void testRegisterSubcommandsWithArguments() {
		new CommandAPICommand("command")
			.withSubcommands(
				new CommandAPICommand("subcommand1")
					.withArguments(
						new StringArgument("string1"),
						new IntegerArgument("integer1")
					)
					.executesPlayer(P_EXEC),
				new CommandAPICommand("subcommand2")
					.withArguments(
						new StringArgument("string2"),
						new IntegerArgument("integer2")
					)
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument", "string1:StringArgument", "integer1:IntegerArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument", "string2:StringArgument", "integer2:IntegerArgument"))
		);
	}

	@Test
	void testRegisterSubcommandWithAliasesAndMultiLiteralArgument() {
		new CommandAPICommand("command")
			.withSubcommand(
				new CommandAPICommand("subcommand")
					.withAliases("alias1", "alias2")
					.withArguments(
						new MultiLiteralArgument("literal1", "a", "b"),
						new MultiLiteralArgument("literal2", "c", "d")
					)
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument", "a:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias1:LiteralArgument", "a:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias2:LiteralArgument", "a:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument", "b:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias1:LiteralArgument", "b:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias2:LiteralArgument", "b:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument", "a:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias1:LiteralArgument", "a:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias2:LiteralArgument", "a:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand:LiteralArgument", "b:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias1:LiteralArgument", "b:LiteralArgument", "d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("alias2:LiteralArgument", "b:LiteralArgument", "d:LiteralArgument"))
		);
	}

	@Test
	void testRegisterSubcommandsWithOptionalArguments() {
		new CommandAPICommand("command")
			.withSubcommand(
				new CommandAPICommand("subcommand1")
					.withOptionalArguments(
						new LiteralArgument("a"),
						new LiteralArgument("b")
					)
					.executesPlayer(P_EXEC)
			)
			.withSubcommand(
				new CommandAPICommand("subcommand2")
					.withOptionalArguments(
						new LiteralArgument("c"),
						new LiteralArgument("d")
					)
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument", "a:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument", "a:LiteralArgument", "b:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument", "c:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument", "c:LiteralArgument", "d:LiteralArgument"))
		);
	}

	@Test
	void testRegisterSubcommandsWithCombinedRequiredAndOptionalArguments() {
		new CommandAPICommand("command")
			.withSubcommand(
				new CommandAPICommand("subcommand1")
					.withArguments(
						new LiteralArgument("1a").combineWith(new LiteralArgument("1b"))
					)
					.withOptionalArguments(
						new LiteralArgument("1c").combineWith(new LiteralArgument("1d"))
					)
					.executesPlayer(P_EXEC)
			)
			.withSubcommand(
				new CommandAPICommand("subcommand2")
					.withArguments(
						new LiteralArgument("2a").combineWith(new LiteralArgument("2b"))
					)
					.withOptionalArguments(
						new LiteralArgument("2c").combineWith(new LiteralArgument("2d"))
					)
					.executesPlayer(P_EXEC)
			)
			.register();

		assertCreatedRegisteredCommands(
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument", "1a:LiteralArgument", "1b:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand1:LiteralArgument", "1a:LiteralArgument", "1b:LiteralArgument", "1c:LiteralArgument", "1d:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument", "2a:LiteralArgument", "2b:LiteralArgument")),
			registeredCommandNoHelpOrPermission("command", List.of("subcommand2:LiteralArgument", "2a:LiteralArgument", "2b:LiteralArgument", "2c:LiteralArgument", "2d:LiteralArgument"))
		);
	}
}
