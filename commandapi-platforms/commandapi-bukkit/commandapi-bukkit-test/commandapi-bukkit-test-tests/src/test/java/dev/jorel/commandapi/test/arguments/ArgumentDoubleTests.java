package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.InvalidRangeException;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.arguments.parseexceptions.InitialParseExceptionNumberArgumentTestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for the {@link DoubleArgument}
 */
public class ArgumentDoubleTests extends InitialParseExceptionNumberArgumentTestBase<Double> {

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
    void executionTestWithDoubleArgument() {
        Mut<Double> results = Mut.of();

        new CommandAPICommand("test")
                .withArguments(new DoubleArgument("value"))
                .executesPlayer((player, args) -> {
                    results.set((double) args.get(0));
                })
                .register();

        PlayerMock player = server.addPlayer();

        // /test 10
        server.dispatchCommand(player, "test 10.0");
        assertEquals(10.0, results.get());

        // /test -10
        server.dispatchCommand(player, "test -10.0");
        assertEquals(-10.0, results.get());

        // /test 0
        server.dispatchCommand(player, "test 0.0");
        assertEquals(0.0, results.get());

        // /test Double.MAX_VALUE
        String doubleMaxValue = String.format(Locale.ENGLISH, "%f", Double.MAX_VALUE);
        server.dispatchCommand(player, "test " + doubleMaxValue);
        assertEquals(Double.MAX_VALUE, results.get());

        // /test -Double.MAX_VALUE
        String doubleMinValue = String.format(Locale.ENGLISH, "%f", -Double.MAX_VALUE);
        server.dispatchCommand(player, "test " + doubleMinValue);
        assertEquals(-Double.MAX_VALUE, results.get());

        // /test 123hello
        assertCommandFailsWith(player, "test 123hello", "Expected whitespace to end one argument, but found trailing data at position 8: test 123<--[HERE]");

        // /test hello123
        assertCommandFailsWith(player, "test hello123", "Expected double at position 5: test <--[HERE]");

        assertNoMoreResults(results);
    }

    @Test
    void executionTestWithDoubleBoundedArgument() {
        Mut<Double> results = Mut.of();

        new CommandAPICommand("test")
                .withArguments(new DoubleArgument("value", 10.0, 20.0))
                .executesPlayer((player, args) -> {
                    results.set((double) args.get(0));
                })
                .register();

        PlayerMock player = server.addPlayer();

        // /test 10
        server.dispatchCommand(player, "test 10.0");
        assertEquals(10.0, results.get());

        // /test 15
        server.dispatchCommand(player, "test 15.0");
        assertEquals(15.0, results.get());

        // /test 20
        server.dispatchCommand(player, "test 20.0");
        assertEquals(20.0, results.get());

        // /test 0
        assertCommandFailsWith(player, "test 0", "Double must not be less than 10.0, found 0.0 at position 5: test <--[HERE]");

        // /test 30
        assertCommandFailsWith(player, "test 30", "Double must not be more than 20.0, found 30.0 at position 5: test <--[HERE]");

        assertNoMoreResults(results);
    }

    /*******************************
     * InitialParseException tests *
     *******************************/

    @Test
    void initialParseExceptionTestWithDoubleArgument() {
        PlayerMock player = server.addPlayer();

        DoubleArgument argument = new DoubleArgument("double");
        argument.withInitialParseExceptionHandler(CONTEXT_VERIFIER);

        new CommandAPICommand("test")
                .withArguments(new StringArgument("buffer"), argument)
                .executesPlayer(P_EXEC)
                .register();

        // Test EXPECTED_NUMBER cases: Invalid characters given for argument (not 0-9, `-` or `.`)
        testExpectedNumberCase(
                player, "test b123 abcde",
                "Expected double at position 10: test b123 <--[HERE]",
                10, argument
        );
        testExpectedNumberCase(
                player, "test b123 +1",
                "Expected double at position 10: test b123 <--[HERE]",
                10, argument
        );


        // Test INVALID_NUMBER cases: Characters given for the argument do not represent a valid double
        // Nonsense number format
        testInvalidNumberCase(
                player, "test b123 .0..0",
                "Invalid double '.0..0' at position 10: test b123 <--[HERE]",
                10, ".0..0", argument
        );
        testInvalidNumberCase(
                player, "test b123 1-",
                "Invalid double '1-' at position 10: test b123 <--[HERE]",
                10, "1-", argument
        );

        // Increasing characters in buffer argument increases cursor start
        testExpectedNumberCase(
                player, "test b12345 abcde",
                "Expected double at position 12: ...st b12345 <--[HERE]",
                12, argument
        );
        testExpectedNumberCase(
                player, "test b123456789012345 abcde",
                "Expected double at position 22: ...789012345 <--[HERE]",
                22, argument
        );

        testInvalidNumberCase(
                player, "test b12345 1-",
                "Invalid double '1-' at position 12: ...st b12345 <--[HERE]",
                12, "1-", argument
        );
        testInvalidNumberCase(
                player, "test b123456789012345 1-",
                "Invalid double '1-' at position 22: ...789012345 <--[HERE]",
                22, "1-", argument
        );
    }

    @Test
    void initialParseExceptionTestWithBoundedDoubleArgument() {
        PlayerMock player = server.addPlayer();

        DoubleArgument argument = new DoubleArgument("double", 0, 10);
        argument.withInitialParseExceptionHandler(CONTEXT_VERIFIER);

        new CommandAPICommand("test")
                .withArguments(new StringArgument("buffer"), argument)
                .executesPlayer(P_EXEC)
                .register();

        // Test NUMBER_TOO_LOW cases: Given double is below the defined bound (0)
        testNumberTooLowCase(
                player, "test b123 -1",
                "Double must not be less than 0.0, found -1.0 at position 10: test b123 <--[HERE]",
                10, "-1", -1.0, argument
        );
        testNumberTooLowCase(
                player, "test b123 -10",
                "Double must not be less than 0.0, found -10.0 at position 10: test b123 <--[HERE]",
                10, "-10", -10.0, argument
        );


        // Test NUMBER_TOO_HIGH cases: Given double is above the defined bound (10)
        testNumberTooHighCase(
                player, "test b123 11",
                "Double must not be more than 10.0, found 11.0 at position 10: test b123 <--[HERE]",
                10, "11", 11.0, argument
        );
        testNumberTooHighCase(
                player, "test b123 100",
                "Double must not be more than 10.0, found 100.0 at position 10: test b123 <--[HERE]",
                10, "100", 100.0, argument
        );


        // Increasing characters in buffer argument increases cursor start
        testNumberTooLowCase(
                player, "test b12345 -1",
                "Double must not be less than 0.0, found -1.0 at position 12: ...st b12345 <--[HERE]",
                12, "-1", -1.0, argument
        );
        testNumberTooLowCase(
                player, "test b123456789012345 -1",
                "Double must not be less than 0.0, found -1.0 at position 22: ...789012345 <--[HERE]",
                22, "-1", -1.0, argument
        );

        testNumberTooHighCase(
                player, "test b12345 11",
                "Double must not be more than 10.0, found 11.0 at position 12: ...st b12345 <--[HERE]",
                12, "11", 11.0, argument
        );
        testNumberTooHighCase(
                player, "test b123456789012345 11",
                "Double must not be more than 10.0, found 11.0 at position 22: ...789012345 <--[HERE]",
                22, "11", 11.0, argument
        );
    }

    /*********************************
     * Instantiation exception tests *
     *********************************/

    @Test
    void exceptionTestWithDoubleArgumentInvalid() {
        // Test with max value less than min value
        assertThrows(InvalidRangeException.class, () -> new DoubleArgument("value", 20.0, 10.0));
    }

    /********************
     * Suggestion tests *
     ********************/

    @Test
    void suggestionTestWithDoubleArgument() {
        new CommandAPICommand("test")
                .withArguments(new DoubleArgument("value"))
                .executesPlayer(P_EXEC)
                .register();

        PlayerMock player = server.addPlayer();

        // /test
        assertEquals(List.of(), server.getSuggestions(player, "test "));
    }
}
