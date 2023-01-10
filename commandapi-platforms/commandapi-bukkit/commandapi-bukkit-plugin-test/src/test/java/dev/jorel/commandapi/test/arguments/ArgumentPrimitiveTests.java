package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.jorel.commandapi.arguments.FloatArgument;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the primitive arguments {@link BooleanArgument},
 * {@link IntegerArgument} etc.
 */
@SuppressWarnings("null")
public class ArgumentPrimitiveTests extends TestBase {

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
	public void executionTestWithBooleanArgument() {
		Mut<Boolean> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new BooleanArgument("value"))
			.executesPlayer((player, args) -> {
				results.set((boolean) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test true
		server.dispatchCommand(player, "test true");
		assertEquals(true, results.get());

		// /test false
		server.dispatchCommand(player, "test false");
		assertEquals(false, results.get());

		// /test aaaaa
		assertCommandFailsWith(player, "test aaaaa", "Invalid boolean, expected 'true' or 'false' but found 'aaaaa' at position 5: test <--[HERE]");
	}

	@Test
	public void executionTestWithIntegerArgument() {
		Mut<Integer> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("value"))
			.executesPlayer((player, args) -> {
				results.set((int) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 10
		server.dispatchCommand(player, "test 10");
		assertEquals(10, results.get());

		// /test -10
		server.dispatchCommand(player, "test -10");
		assertEquals(-10, results.get());

		// /test 0
		server.dispatchCommand(player, "test 0");
		assertEquals(0, results.get());

		// /test 2147483647
		server.dispatchCommand(player, "test 2147483647");
		assertEquals(Integer.MAX_VALUE, results.get());

		// /test -2147483648
		server.dispatchCommand(player, "test -2147483648");
		assertEquals(Integer.MIN_VALUE, results.get());

		// /test 123hello
		assertCommandFailsWith(player, "test 123hello", "Expected whitespace to end one argument, but found trailing data at position 8: test 123<--[HERE]");
		
		// /test hello123
		assertCommandFailsWith(player, "test hello123", "Expected integer at position 5: test <--[HERE]");

		// /test 2147483648
		assertCommandFailsWith(player, "test 2147483648", "Invalid integer '2147483648' at position 5: test <--[HERE]");
	}

	@Test
	public void executionTestWithBoundedIntegerArgument() {
		Mut<Integer> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("value", 10))
			.executesPlayer((player, args) -> {
				results.set((int) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 10
		server.dispatchCommand(player, "test 10");
		assertEquals(10, results.get());

		// /test 20
		server.dispatchCommand(player, "test 20");
		assertEquals(20, results.get());

		// /test 0
		assertCommandFailsWith(player, "test 0", "Integer must not be less than 10, found 0 at position 5: test <--[HERE]");
	}

	@Test
	public void executionTestWithDoubleBoundedIntegerArgument() {
		Mut<Integer> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new IntegerArgument("value", 10, 20))
			.executesPlayer((player, args) -> {
				results.set((int) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 10
		server.dispatchCommand(player, "test 10");
		assertEquals(10, results.get());
		
		// /test 15
		server.dispatchCommand(player, "test 15");
		assertEquals(15, results.get());

		// /test 20
		server.dispatchCommand(player, "test 20");
		assertEquals(20, results.get());

		// /test 0
		assertCommandFailsWith(player, "test 0", "Integer must not be less than 10, found 0 at position 5: test <--[HERE]");
		
		// /test 30
		assertCommandFailsWith(player, "test 30", "Integer must not be more than 20, found 30 at position 5: test <--[HERE]");
	}

	// TODO: Double, Long
	@Test
	public void executionTestWithFloatArgument() {
		Mut<Float> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new FloatArgument("value"))
			.executesPlayer((player, args) -> {
				results.set((float) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 10
		server.dispatchCommand(player, "test 10.0");
		assertEquals(10.0F, results.get());

		// /test -10
		server.dispatchCommand(player, "test -10.0");
		assertEquals(-10.0F, results.get());

		// /test 0
		server.dispatchCommand(player, "test 0.0");
		assertEquals(0.0F, results.get());

		// /test Float.MAX_VALUE
		// String floatMaxValue = String.valueOf(Float.MAX_VALUE - 1.0F);
		// server.dispatchCommand(player, "test " + floatMaxValue);
		// assertEquals(Float.MAX_VALUE - 1.0F, results.get());

		// /test -Float.MAX_VALUE
		// String floatMinValue = String.valueOf(-Float.MAX_VALUE);
		// server.dispatchCommand(player, "test " + floatMinValue);
		// assertEquals(-Float.MAX_VALUE, results.get());

		// /test 123hello
		assertCommandFailsWith(player, "test 123hello", "Expected whitespace to end one argument, but found trailing data at position 8: test 123<--[HERE]");

		// /test hello123
		assertCommandFailsWith(player, "test hello123", "Expected float at position 5: test <--[HERE]");
	}

	@Test
	public void executionTestWithFloatBoundedArgument() {
		Mut<Float> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new FloatArgument("value", 10.0F, 20.0F))
			.executesPlayer((player, args) -> {
				results.set((Float) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test 10
		server.dispatchCommand(player, "test 10.0");
		assertEquals(10.0F, results.get());

		// /test 15
		server.dispatchCommand(player, "test 15.0");
		assertEquals(15.0F, results.get());

		// /test 20
		server.dispatchCommand(player, "test 20.0");
		assertEquals(20.0F, results.get());

		// /test 0
		assertCommandFailsWith(player, "test 0", "Float must not be less than 10.0, found 0.0 at position 5: test <--[HERE]");

		// /test 30
		assertCommandFailsWith(player, "test 30", "Float must not be more than 20.0, found 30.0 at position 5: test <--[HERE]");

	}
	
}
