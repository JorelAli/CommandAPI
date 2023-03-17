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

public class ArgumentParseExceptionTest extends TestBase {

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
					if(context.exception().getMessage().startsWith("Item is not allowed in list")) {
						throw CommandAPI.failWithString("No such player!");
					} else {
						throw context.exception();
					}
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
		assertCommandFailsWith(a, "test PlayerA PlayerD PlayerC", "No such player!");


		assertNoMoreResults(players);
	}
}
