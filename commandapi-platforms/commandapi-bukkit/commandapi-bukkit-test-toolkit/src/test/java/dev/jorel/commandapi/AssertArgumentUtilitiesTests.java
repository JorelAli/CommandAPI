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
			.executes(DEFAULT_EXECUTOR)
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
		assertCommandRunsWithArguments(
			player, "test 10 hello true",
			Map.of(
				"int", 10,
				"string", "hello",
				"bool", true
			)
		);

		assertCommandRunsWithArguments(
			player, "test 10 hello true",
			10, "hello", true
		);
	}

	@Test
	void testSuccessMapOrderDoesNotMatter() {
		assertCommandRunsWithArguments(
			player, "test 10 hello true",
			Map.of(
				"bool", true,
				"int", 10,
				"string", "hello"
			)
		);
	}

	@Test
	void testFailureInvalidCommand() {
		assertAssertionFails(
			() -> assertCommandRunsWithArguments(
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
			() -> assertCommandRunsWithArguments(
				player, "invalid 10 hello true",
				10, "hello", true
			),
			"Expected command dispatch to succeed " +
				"==> Unexpected exception thrown: " +
				"com.mojang.brigadier.exceptions.CommandSyntaxException: Unknown command at position 0: <--[HERE]"
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
			() -> assertCommandRunsWithArguments(
				player, "test 10 hello true",
				missingArgument
			),
			"Argument maps are not equal " +
				// The order of keys from Map.of is not guaranteed, so we don't know its String beforehand
				"==> expected: <" + missingArgument + "> but was: <{int=10, string=hello, bool=true}>"
		);

		// Argument wrong key
		final Map<String, Object> wrongKey = Map.of(
			"number", 10,
			"string", "hello",
			"bool", true
		);
		assertAssertionFails(
			() -> assertCommandRunsWithArguments(
				player, "test 10 hello true",
				wrongKey
			),
			"Argument maps are not equal " +
				"==> expected: <" + wrongKey + "> but was: <{int=10, string=hello, bool=true}>"
		);

		// Argument wrong value
		Map<String, Object> wrongValue = Map.of(
			"int", 5,
			"string", "hello",
			"bool", true
		);
		assertAssertionFails(
			() -> assertCommandRunsWithArguments(
				player, "test 10 hello true",
				wrongValue
			),
			"Argument maps are not equal " +
				"==> expected: <" + wrongValue + "> but was: <{int=10, string=hello, bool=true}>"
		);
	}

	@Test
	void testFailureWrongArrayArguments() {
		// Argument missing
		assertAssertionFails(
			() -> assertCommandRunsWithArguments(
				player, "test 10 hello true",
				"hello", true
			),
			"Argument arrays are not equal ==> " +
				"array lengths differ, expected: <2> but was: <3>"
		);

		// Arguments out of order
		assertAssertionFails(
			() -> assertCommandRunsWithArguments(
				player, "test 10 hello true",
				true, 10, "hello"
			),
			"Argument arrays are not equal ==> " +
				"array contents differ at index [0], expected: <true> but was: <10>"
		);
	}
}
