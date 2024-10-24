package dev.jorel.commandapi;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

class AssertArgumentUtilitiesTests extends CommandTestBase {
	// Setup
	private PlayerMock player;

	@BeforeEach
	public void setUp() {
		super.setUp();

		new CommandAPICommand("test")
			.withArguments(
				new IntegerArgument("int"),
				new StringArgument("string"),
				new BooleanArgument("bool")
			)
			.withOptionalArguments(new BooleanArgument("shouldFail"))
			.executes(info -> {
				if (info.args().getOrDefaultUnchecked("shouldFail", false)) {
					throw CommandAPI.failWithString("Command failed");
				}
			})
			.register();

		player = server.addPlayer();
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	// Tests
	@Test
	void testSuccessArgumentCheck() {
		assertCommandSucceedsWithArguments(
			player, "test 10 hello true",
			Map.of(
				"int", 10,
				"string", "hello",
				"bool", true
			)
		);

		assertCommandFailsWithArguments(
			player, "test 10 hello true true", "Command failed",
			Map.of(
				"int", 10,
				"string", "hello",
				"bool", true,
				"shouldFail", true
			)
		);

		assertCommandSucceedsWithArguments(
			player, "test 10 hello true",
			10, "hello", true
		);

		assertCommandFailsWithArguments(
			player, "test 10 hello true true", "Command failed",
			10, "hello", true, true
		);
	}

	@Test
	void testSuccessMapOrderDoesNotMatter() {
		assertCommandSucceedsWithArguments(
			player, "test 10 hello true",
			Map.of(
				"bool", true,
				"int", 10,
				"string", "hello"
			)
		);

		assertCommandFailsWithArguments(
			player, "test 10 hello true true", "Command failed",
			Map.of(
				"bool", true,
				"int", 10,
				"shouldFail", true,
				"string", "hello"
			)
		);
	}

	@Test
	void testFailureInvalidCommand() {
		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "invalid 10 hello true",
				Map.of(
					"int", 10,
					"string", "hello",
					"bool", true
				)
			),
			"Expected command dispatch to succeed " +
				"==> Unexpected exception thrown: " +
				"com.mojang.brigadier.exceptions.CommandSyntaxException: Unknown command at position 0: <--[HERE]"
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "invalid 10 hello true true", "Command failed",
				Map.of(
					"int", 10,
					"string", "hello",
					"bool", true,
					"shouldFail", true
				)
			),
			"Expected command dispatch to fail with message <Command failed>, " +
				"but got <Unknown command at position 0: <--[HERE]>"
		);

		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "invalid 10 hello true",
				10, "hello", true
			),
			"Expected command dispatch to succeed " +
				"==> Unexpected exception thrown: " +
				"com.mojang.brigadier.exceptions.CommandSyntaxException: Unknown command at position 0: <--[HERE]"
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "invalid 10 hello true true", "Command failed",
				10, "hello", true, true
			),
			"Expected command dispatch to fail with message <Command failed>, " +
				"but got <Unknown command at position 0: <--[HERE]>"
		);
	}

	@Test
	void testFailureWrongMapArguments() {
		// Argument missing
		final Map<String, Object> missingArgument = Map.of(
			"string", "hello",
			"bool", true
		);

		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "test 10 hello true",
				missingArgument
			),
			"Argument maps are not equal " +
				// The order of keys from Map.of is not guaranteed, so we don't know its String beforehand
				"==> expected: <" + missingArgument + "> but was: <{int=10, string=hello, bool=true}>"
		);
		final Map<String, Object> missingArgumentFails = Map.of(
			"string", "hello",
			"bool", true,
			"shouldFail", true
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "test 10 hello true true", "Command failed",
				missingArgumentFails
			),
			"Argument maps are not equal " +
				// The order of keys from Map.of is not guaranteed, so we don't know its String beforehand
				"==> expected: <" + missingArgumentFails + "> but was: <{int=10, string=hello, bool=true, shouldFail=true}>"
		);

		// Argument wrong key
		final Map<String, Object> wrongKey = Map.of(
			"number", 10,
			"string", "hello",
			"bool", true
		);
		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "test 10 hello true",
				wrongKey
			),
			"Argument maps are not equal " +
				"==> expected: <" + wrongKey + "> but was: <{int=10, string=hello, bool=true}>"
		);
		final Map<String, Object> wrongKeyFails = Map.of(
			"number", 10,
			"string", "hello",
			"bool", true,
			"shouldFail", true
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "test 10 hello true true", "Command failed",
				wrongKeyFails
			),
			"Argument maps are not equal " +
				"==> expected: <" + wrongKeyFails + "> but was: <{int=10, string=hello, bool=true, shouldFail=true}>"
		);

		// Argument wrong value
		Map<String, Object> wrongValue = Map.of(
			"int", 5,
			"string", "hello",
			"bool", true
		);
		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "test 10 hello true",
				wrongValue
			),
			"Argument maps are not equal " +
				"==> expected: <" + wrongValue + "> but was: <{int=10, string=hello, bool=true}>"
		);
		Map<String, Object> wrongValueFails = Map.of(
			"int", 5,
			"string", "hello",
			"bool", true,
			"shouldFail", true
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "test 10 hello true true", "Command failed",
				wrongValueFails
			),
			"Argument maps are not equal " +
				"==> expected: <" + wrongValueFails + "> but was: <{int=10, string=hello, bool=true, shouldFail=true}>"
		);
	}

	@Test
	void testFailureWrongArrayArguments() {
		// Argument missing
		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "test 10 hello true",
				"hello", true
			),
			"Argument arrays are not equal ==> " +
				"array lengths differ, expected: <2> but was: <3>"
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "test 10 hello true true", "Command failed",
				"hello", true, true
			),
			"Argument arrays are not equal ==> " +
				"array lengths differ, expected: <3> but was: <4>"
		);

		// Arguments out of order
		assertAssertionFails(
			() -> assertCommandSucceedsWithArguments(
				player, "test 10 hello true",
				true, 10, "hello"
			),
			"Argument arrays are not equal ==> " +
				"array contents differ at index [0], expected: <true> but was: <10>"
		);
		assertAssertionFails(
			() -> assertCommandFailsWithArguments(
				player, "test 10 hello true true", "Command failed",
				true, 10, "hello", true
			),
			"Argument arrays are not equal ==> " +
				"array contents differ at index [0], expected: <true> but was: <10>"
		);
	}
}
