package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerRangeArgument;
import dev.jorel.commandapi.arguments.FloatRangeArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.IntegerRange;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the range arguments {@link IntegerRangeArgument} and {@link FloatRangeArgument}
 */
public class ArgumentRangeTests extends TestBase {

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

	// TODO: FloatRange
	@Test
	public void executionTestWithIntegerRangeArgument() {
		Mut<IntegerRange> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new IntegerRangeArgument("value"))
			.executesPlayer((player, args) -> {
				results.set((IntegerRange) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 0..10
		IntegerRange testZeroToTen = new IntegerRange(0, 10);
		server.dispatchCommand(player, "test 0..10");
		assertEquals(testZeroToTen, results.get());

		// /test -10..0
		IntegerRange testMinusTenToZero = new IntegerRange(-10, 0);
		server.dispatchCommand(player, "test -10..0");
		assertEquals(testMinusTenToZero, results.get());

		// /test 10
		IntegerRange testTenToTen = new IntegerRange(10, 10);
		server.dispatchCommand(player, "test 10");
		assertEquals(testTenToTen, results.get());

		// /test -2147483648..2147483647
		IntegerRange testMinIntToMaxInt = new IntegerRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
		server.dispatchCommand(player, "test -2147483648..2147483647");
		assertEquals(testMinIntToMaxInt, results.get());

		// /test ..2147483647
		IntegerRange testMinIntToMaxIntWithoutLower = IntegerRange.integerRangeLessThanOrEq(Integer.MAX_VALUE);
		server.dispatchCommand(player, "test ..2147483647");
		assertEquals(testMinIntToMaxIntWithoutLower, results.get());

		// /test -2147483648..
		IntegerRange testMinIntToMaxIntWithoutUpper = IntegerRange.integerRangeGreaterThanOrEq(Integer.MIN_VALUE);
		server.dispatchCommand(player, "test -2147483648..");
		assertEquals(testMinIntToMaxIntWithoutUpper, results.get());

		// /test -2147483648..2147483648
		assertCommandFailsWith(player, "test -2147483648..2147483648", "Invalid integer '2147483648' at position 5: test <--[HERE]");

		// /test -2147483649..2147483647
		assertCommandFailsWith(player, "test -2147483649..2147483647", "Invalid integer '-2147483649' at position 5: test <--[HERE]");

		// test hello123..10
		assertCommandFailsWith(player, "test hello123..10", "Expected value or range of values at position 5: test <--[HERE]");

		// test 123hello..10
		assertCommandFailsWith(player, "test 123hello..10", "Expected whitespace to end one argument, but found trailing data at position 8: test 123<--[HERE]");

		assertNoMoreResults(results);
	}

}
