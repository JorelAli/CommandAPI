package dev.jorel.commandapi.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandTestBase;
import dev.jorel.commandapi.wrappers.IntegerRange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegerRangeArgumentTests extends CommandTestBase {
	// Setup
	private PlayerMock player;

	@BeforeEach
	public void setUp() {
		super.setUp();

		new CommandAPICommand("test")
			.withArguments(new IntegerRangeArgument("range"))
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
	void testValidInputs() {
		// Exact range
		assertCommandSucceedsWithArguments(player, "test 10", new IntegerRange(10, 10));

		// Greater than range
		assertCommandSucceedsWithArguments(player, "test 10..", IntegerRange.integerRangeGreaterThanOrEq(10));

		// Less than range
		assertCommandSucceedsWithArguments(player, "test ..10", IntegerRange.integerRangeLessThanOrEq(10));

		// Between range
		assertCommandSucceedsWithArguments(player, "test 5..20", new IntegerRange(5, 20));

		// Check integer parsing
		assertCommandSucceedsWithArguments(player, "test -10..-5", new IntegerRange(-10, -5));
		assertCommandSucceedsWithArguments(player, "test ..-10", IntegerRange.integerRangeLessThanOrEq(-10));
	}

	// Invalid inputs
	@Test
	void testEmptyInputs() {
		assertThrowsWithMessage(
			CommandSyntaxException.class,
			() -> createContextWithParser(player, "range", IntegerRangeArgumentType.INSTANCE::parse, ""),
			"Expected value or range of values at position 0: <--[HERE]"
		);
		assertCommandFails(
			player, "test  ",
			"Expected value or range of values at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test ..",
			"Expected value or range of values at position 5: test <--[HERE]"
		);
	}

	@Test
	void testFloatInput() {
		// Float, not an integer
		assertCommandFails(
			player, "test 1.0",
			"Invalid integer '1.0' at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test 1.0..",
			"Invalid integer '1.0' at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test 0..1.0",
			"Invalid integer '1.0' at position 8: test 0..<--[HERE]"
		);
		assertCommandFails(
			player, "test ..1.0",
			"Invalid integer '1.0' at position 7: test ..<--[HERE]"
		);
	}

	@Test
	void testNotNumberInput() {
		assertCommandFails(
			player, "test a",
			"Expected value or range of values at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test a..",
			"Expected value or range of values at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test 0..a",
			"Expected whitespace to end one argument, but found trailing data at position 8: test 0..<--[HERE]"
		);
		assertCommandFails(
			player, "test ..a",
			"Expected value or range of values at position 5: test <--[HERE]"
		);
	}

	@Test
	void testRangeLiteralWrong() {
		// Single . is interpreted as decimal place
		assertCommandFails(
			player, "test 10.",
			"Invalid integer '10.' at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test 10.15",
			"Invalid integer '10.15' at position 5: test <--[HERE]"
		);
		assertCommandFails(
			player, "test .10",
			"Invalid integer '.10' at position 5: test <--[HERE]"
		);

		// Comma is unknown and invalid
		assertCommandFails(
			player, "test 10,,",
			"Expected whitespace to end one argument, but found trailing data at position 7: test 10<--[HERE]"
		);
		assertCommandFails(
			player, "test 10,,15",
			"Expected whitespace to end one argument, but found trailing data at position 7: test 10<--[HERE]"
		);
		assertCommandFails(
			player, "test ,,10",
			"Expected value or range of values at position 5: test <--[HERE]"
		);
	}

	@Test
	void testRangeSwapped() {
		assertCommandFails(
			player, "test 10..5",
			"Min cannot be bigger than max at position 5: test <--[HERE]"
		);
	}
}
