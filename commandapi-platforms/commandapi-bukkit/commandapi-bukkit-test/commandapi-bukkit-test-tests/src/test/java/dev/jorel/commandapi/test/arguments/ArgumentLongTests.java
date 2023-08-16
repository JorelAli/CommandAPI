package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LongArgument;
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
 * Test for the {@link LongArgument}
 */
class ArgumentLongTests extends InitialParseExceptionNumberArgumentTestBase<Long> {

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
    void executionTestWithLongArgument() {
        Mut<Long> results = Mut.of();

        new CommandAPICommand("test")
                .withArguments(new LongArgument("value"))
                .executesPlayer((player, args) -> {
                    results.set((long) args.get(0));
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

        // /test 9223372036854775807
        server.dispatchCommand(player, "test 9223372036854775807");
        assertEquals(Long.MAX_VALUE, results.get());

        // /test -9223372036854775808
        server.dispatchCommand(player, "test -9223372036854775808");
        assertEquals(Long.MIN_VALUE, results.get());

        // /test 123hello
        assertCommandFailsWith(player, "test 123hello", "Expected whitespace to end one argument, but found trailing data at position 8: test 123<--[HERE]");

        // /test hello123
        assertCommandFailsWith(player, "test hello123", "Expected long at position 5: test <--[HERE]");

        // /test 9223372036854775808
        assertCommandFailsWith(player, "test 9223372036854775808", "Invalid long '9223372036854775808' at position 5: test <--[HERE]");

        assertNoMoreResults(results);
    }

    @Test
    void executionTestWithBoundedLongArgument() {
        Mut<Long> results = Mut.of();

        new CommandAPICommand("test")
                .withArguments(new LongArgument("value", 10))
                .executesPlayer((player, args) -> {
                    results.set((long) args.get(0));
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
        assertCommandFailsWith(player, "test 0", "Long must not be less than 10, found 0 at position 5: test <--[HERE]");

        assertNoMoreResults(results);
    }

    @Test
    void executionTestWithDoubleBoundedLongArgument() {
        Mut<Long> results = Mut.of();

        new CommandAPICommand("test")
                .withArguments(new LongArgument("value", 10, 20))
                .executesPlayer((player, args) -> {
                    results.set((long) args.get(0));
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
        assertCommandFailsWith(player, "test 0", "Long must not be less than 10, found 0 at position 5: test <--[HERE]");

        // /test 30
        assertCommandFailsWith(player, "test 30", "Long must not be more than 20, found 30 at position 5: test <--[HERE]");

        assertNoMoreResults(results);
    }

    /*******************************
     * InitialParseException tests *
     *******************************/

    @Test
    void initialParseExceptionTestWithLongArgument() {
        PlayerMock player = server.addPlayer();

        LongArgument argument = new LongArgument("long");
        argument.withInitialParseExceptionHandler(CONTEXT_VERIFIER);

        new CommandAPICommand("test")
                .withArguments(new StringArgument("buffer"), argument)
                .executesPlayer(P_EXEC)
                .register();

        // Test EXPECTED_NUMBER cases: Invalid characters given for argument (not 0-9, `-` or `.`)
        testExpectedNumberCase(
                player, "test b123 abcde",
                "Expected long at position 10: test b123 <--[HERE]",
                10, argument
        );
        testExpectedNumberCase(
                player, "test b123 +1",
                "Expected long at position 10: test b123 <--[HERE]",
                10, argument
        );


        // Test INVALID_NUMBER cases: Characters given for the argument do not represent a valid long
        // Nonsense number format
        testInvalidNumberCase(
                player, "test b123 .0..0",
                "Invalid long '.0..0' at position 10: test b123 <--[HERE]",
                10, ".0..0", argument
        );
        testInvalidNumberCase(
                player, "test b123 1-",
                "Invalid long '1-' at position 10: test b123 <--[HERE]",
                10, "1-", argument
        );

        // Floats are not longs
        testInvalidNumberCase(
                player, "test b123 1.5",
                "Invalid long '1.5' at position 10: test b123 <--[HERE]",
                10, "1.5", argument
        );

        // Number out of bounds of long
        // Long.MAX_VALUE + 1 = 9,223,372,036,854,775,807
        testInvalidNumberCase(
                player, "test b123 9223372036854775808",
                "Invalid long '9223372036854775808' at position 10: test b123 <--[HERE]",
                10, "9223372036854775808", argument
        );
        // Long.MIN_VALUE - 1 = -9,223,372,036,854,775,809
        testInvalidNumberCase(
                player, "test b123 -9223372036854775809",
                "Invalid long '-9223372036854775809' at position 10: test b123 <--[HERE]",
                10, "-9223372036854775809", argument
        );


        // Increasing characters in buffer argument increases cursor start
        testExpectedNumberCase(
                player, "test b12345 abcde",
                "Expected long at position 12: ...st b12345 <--[HERE]",
                12, argument
        );
        testExpectedNumberCase(
                player, "test b123456789012345 abcde",
                "Expected long at position 22: ...789012345 <--[HERE]",
                22, argument
        );

        testInvalidNumberCase(
                player, "test b12345 1.5",
                "Invalid long '1.5' at position 12: ...st b12345 <--[HERE]",
                12, "1.5", argument
        );
        testInvalidNumberCase(
                player, "test b123456789012345 1.5",
                "Invalid long '1.5' at position 22: ...789012345 <--[HERE]",
                22, "1.5", argument
        );
    }

    @Test
    void initialParseExceptionTestWithBoundedLongArgument() {
        PlayerMock player = server.addPlayer();

        LongArgument argument = new LongArgument("long", 0, 10);
        argument.withInitialParseExceptionHandler(CONTEXT_VERIFIER);

        new CommandAPICommand("test")
                .withArguments(new StringArgument("buffer"), argument)
                .executesPlayer(P_EXEC)
                .register();

        // Test NUMBER_TOO_LOW cases: Given long is below the defined bound (0)
        testNumberTooLowCase(
                player, "test b123 -1",
                "Long must not be less than 0, found -1 at position 10: test b123 <--[HERE]",
                10, "-1", -1L, argument
        );
        testNumberTooLowCase(
                player, "test b123 -10",
                "Long must not be less than 0, found -10 at position 10: test b123 <--[HERE]",
                10, "-10", -10L, argument
        );


        // Test NUMBER_TOO_HIGH cases: Given long is above the defined bound (10)
        testNumberTooHighCase(
                player, "test b123 11",
                "Long must not be more than 10, found 11 at position 10: test b123 <--[HERE]",
                10, "11", 11L, argument
        );
        testNumberTooHighCase(
                player, "test b123 100",
                "Long must not be more than 10, found 100 at position 10: test b123 <--[HERE]",
                10, "100", 100L, argument
        );


        // Increasing characters in buffer argument increases cursor start
        testNumberTooLowCase(
                player, "test b12345 -1",
                "Long must not be less than 0, found -1 at position 12: ...st b12345 <--[HERE]",
                12, "-1", -1L, argument
        );
        testNumberTooLowCase(
                player, "test b123456789012345 -1",
                "Long must not be less than 0, found -1 at position 22: ...789012345 <--[HERE]",
                22, "-1", -1L, argument
        );

        testNumberTooHighCase(
                player, "test b12345 11",
                "Long must not be more than 10, found 11 at position 12: ...st b12345 <--[HERE]",
                12, "11", 11L, argument
        );
        testNumberTooHighCase(
                player, "test b123456789012345 11",
                "Long must not be more than 10, found 11 at position 22: ...789012345 <--[HERE]",
                22, "11", 11L, argument
        );
    }

    /*********************************
     * Instantiation exception tests *
     *********************************/

    @Test
    void exceptionTestWithLongArgumentInvalid() {
        // Test with max value less than min value
        assertThrows(InvalidRangeException.class, () -> new LongArgument("value", 20L, 10L));
    }

    /********************
     * Suggestion tests *
     ********************/

    @Test
    void suggestionTestWithLongArgument() {
        new CommandAPICommand("test")
                .withArguments(new LongArgument("value"))
                .executesPlayer(P_EXEC)
                .register();

        PlayerMock player = server.addPlayer();

        // /test
        assertEquals(List.of(), server.getSuggestions(player, "test "));
    }
}
