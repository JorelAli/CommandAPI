package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.InvalidRangeException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.arguments.parseexceptions.InitialParseExceptionNumberArgumentTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link IntegerArgument}
 */
class ArgumentIntegerTests extends InitialParseExceptionNumberArgumentTestBase<Integer> {

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
    void executionTestWithIntegerArgument() {
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

        assertNoMoreResults(results);
    }

    @Test
    void executionTestWithBoundedIntegerArgument() {
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

        assertNoMoreResults(results);
    }

    @Test
    void executionTestWithDoubleBoundedIntegerArgument() {
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

        assertNoMoreResults(results);
    }

    /*******************************
     * InitialParseException tests *
     *******************************/

    @Test
    void initialParseExceptionTestWithIntegerArgument() {
        PlayerMock player = server.addPlayer();

        IntegerArgument argument = new IntegerArgument("int");
        argument.withInitialParseExceptionHandler(CONTEXT_VERIFIER);

        new CommandAPICommand("test")
                .withArguments(new StringArgument("buffer"), argument)
                .executesPlayer(P_EXEC)
                .register();

        // Test EXPECTED_NUMBER cases: Invalid characters given for argument (not 0-9, `-` or `.`)
        testExpectedNumberCase(
                player, "test b123 abcde",
                "Expected integer at position 10: test b123 <--[HERE]",
                10, argument
        );
        testExpectedNumberCase(
                player, "test b123 +1",
                "Expected integer at position 10: test b123 <--[HERE]",
                10, argument
        );


        // Test INVALID_NUMBER cases: Characters given for the argument do not represent a valid int
        // Nonsense number format
        testInvalidNumberCase(
                player, "test b123 .0..0",
                "Invalid integer '.0..0' at position 10: test b123 <--[HERE]",
                10, ".0..0", argument
        );
        testInvalidNumberCase(
                player, "test b123 1-",
                "Invalid integer '1-' at position 10: test b123 <--[HERE]",
                10, "1-", argument
        );

        // Floats are not integers
        testInvalidNumberCase(
                player, "test b123 1.5",
                "Invalid integer '1.5' at position 10: test b123 <--[HERE]",
                10, "1.5", argument
        );

        // Number out of bounds of int
        testInvalidNumberCase(
                player, "test b123 " + ((long) Integer.MAX_VALUE + 1),
                "Invalid integer '2147483648' at position 10: test b123 <--[HERE]",
                10, "2147483648", argument
        );
        testInvalidNumberCase(
                player, "test b123 " + ((long) Integer.MIN_VALUE - 1),
                "Invalid integer '-2147483649' at position 10: test b123 <--[HERE]",
                10, "-2147483649", argument
        );


        // Increasing characters in buffer argument increases cursor start
        testExpectedNumberCase(
                player, "test b12345 abcde",
                "Expected integer at position 12: ...st b12345 <--[HERE]",
                12, argument
        );
        testExpectedNumberCase(
                player, "test b123456789012345 abcde",
                "Expected integer at position 22: ...789012345 <--[HERE]",
                22, argument
        );

        testInvalidNumberCase(
                player, "test b12345 1.5",
                "Invalid integer '1.5' at position 12: ...st b12345 <--[HERE]",
                12, "1.5", argument
        );
        testInvalidNumberCase(
                player, "test b123456789012345 1.5",
                "Invalid integer '1.5' at position 22: ...789012345 <--[HERE]",
                22, "1.5", argument
        );
    }

    @Test
    void initialParseExceptionTestWithBoundedIntegerArgument() {
        PlayerMock player = server.addPlayer();

        IntegerArgument argument = new IntegerArgument("int", 0, 10);
        argument.withInitialParseExceptionHandler(CONTEXT_VERIFIER);

        new CommandAPICommand("test")
                .withArguments(new StringArgument("buffer"), argument)
                .executesPlayer(P_EXEC)
                .register();

        // Test NUMBER_TOO_LOW cases: Given int is below the defined bound (0)
        testNumberTooLowCase(
                player, "test b123 -1",
                "Integer must not be less than 0, found -1 at position 10: test b123 <--[HERE]",
                10, "-1", -1, argument
        );
        testNumberTooLowCase(
                player, "test b123 -10",
                "Integer must not be less than 0, found -10 at position 10: test b123 <--[HERE]",
                10, "-10", -10, argument
        );


        // Test NUMBER_TOO_HIGH cases: Given int is above the defined bound (10)
        testNumberTooHighCase(
                player, "test b123 11",
                "Integer must not be more than 10, found 11 at position 10: test b123 <--[HERE]",
                10, "11", 11, argument
        );
        testNumberTooHighCase(
                player, "test b123 100",
                "Integer must not be more than 10, found 100 at position 10: test b123 <--[HERE]",
                10, "100", 100, argument
        );


        // Increasing characters in buffer argument increases cursor start
        testNumberTooLowCase(
                player, "test b12345 -1",
                "Integer must not be less than 0, found -1 at position 12: ...st b12345 <--[HERE]",
                12, "-1", -1, argument
        );
        testNumberTooLowCase(
                player, "test b123456789012345 -1",
                "Integer must not be less than 0, found -1 at position 22: ...789012345 <--[HERE]",
                22, "-1", -1, argument
        );

        testNumberTooHighCase(
                player, "test b12345 11",
                "Integer must not be more than 10, found 11 at position 12: ...st b12345 <--[HERE]",
                12, "11", 11, argument
        );
        testNumberTooHighCase(
                player, "test b123456789012345 11",
                "Integer must not be more than 10, found 11 at position 22: ...789012345 <--[HERE]",
                22, "11", 11, argument
        );
    }

    /*********************************
     * Instantiation exception tests *
     *********************************/

    @Test
    void exceptionTestWithIntegerArgumentInvalid() {
        // Test with max value less than min value
        assertThrows(InvalidRangeException.class, () -> new IntegerArgument("value", 20, 10));
    }

    /********************
     * Suggestion tests *
     ********************/

    @Test
    void suggestionTestWithIntegerArgument() {
        new CommandAPICommand("test")
                .withArguments(new IntegerArgument("value"))
                .executesPlayer(P_EXEC)
                .register();

        PlayerMock player = server.addPlayer();

        // /test
        assertEquals(List.of(), server.getSuggestions(player, "test "));
    }
}
