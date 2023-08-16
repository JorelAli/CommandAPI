package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link BooleanArgument}
 */
public class ArgumentBooleanTests extends TestBase {

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
    void executionTestWithBooleanArgument() {
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

        assertNoMoreResults(results);
    }

    /********************
     * Suggestion tests *
     ********************/

    @Test
    void suggestionTestWithBooleanArgument() {
        new CommandAPICommand("test")
                .withArguments(new BooleanArgument("value"))
                .executesPlayer(P_EXEC)
                .register();

        PlayerMock player = server.addPlayer();

        // /test
        // Both values should be suggested
        assertEquals(List.of("false", "true"), server.getSuggestions(player, "test "));

        // /test f
        // Only "false" should be suggested
        assertEquals(List.of("false"), server.getSuggestions(player, "test f"));

        // /test t
        // Only "true" should be suggested
        assertEquals(List.of("true"), server.getSuggestions(player, "test t"));

        // /test x
        // Nothing should be suggested
        assertEquals(List.of(), server.getSuggestions(player, "test x"));
    }
}
