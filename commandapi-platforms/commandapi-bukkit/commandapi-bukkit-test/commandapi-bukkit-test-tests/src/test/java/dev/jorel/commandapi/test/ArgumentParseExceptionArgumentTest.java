package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ListArgumentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

class ArgumentParseExceptionArgumentTest extends TestBase {

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
    void test() {
        Mut<List<Player>> players = Mut.of();

        new CommandAPICommand("test")
                .withArguments(
                        new ListArgumentBuilder<Player>("players")
                                .withList(() -> (Collection<Player>) Bukkit.getOnlinePlayers())
                                .withMapper(Player::getName)
                                .buildGreedy()
                                .withArgumentParseExceptionHandler(context -> {
									// IntelliJ complains this is too few cases for switch, but I think this looks great
									// Dunno how good this is for execution, but the if statement version looks terrible
                                    // because you have to specify the whole path to INVALID_ITEM :P
									throw switch (context.exceptionInformation().type()) {
										case INVALID_ITEM -> CommandAPI.failWithString(
                                                "Could not find player: " +  context.exceptionInformation().currentItem()
                                        );
										default -> context.exception();
									};
                                })
                )
                .executes(info -> {
                    players.set(info.args().getUnchecked("players"));
                })
                .register();

        Player a = server.addPlayer("PlayerA");
        Player b = server.addPlayer("PlayerB");
        Player c = server.addPlayer("PlayerC");

        // Make sure the list argument still works normally
        // /test PlayerA PlayerB PlayerC
        assertStoresResult(a, "test PlayerA PlayerB PlayerC", players, List.of(a, b, c));

        // /test PlayerA PlayerB PlayerB
        assertCommandFailsWith(a, "test PlayerA PlayerB PlayerB", "Duplicate arguments are not allowed at position 16: ...A PlayerB <--[HERE]");

        // Make sure our custom exception was applied
        // /test PlayerA PlayerD PlayerC
        assertCommandFailsWith(a, "test PlayerA PlayerD PlayerC", "Could not find player: PlayerD");


        assertNoMoreResults(players);
    }
}