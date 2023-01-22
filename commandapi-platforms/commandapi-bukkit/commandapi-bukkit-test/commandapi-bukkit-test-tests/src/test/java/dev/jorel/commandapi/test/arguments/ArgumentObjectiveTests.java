package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.scoreboard.ScoreboardMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ObjectiveArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link ObjectiveArgument}
 */
public class ArgumentObjectiveTests extends TestBase {

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
	public void executionTestWithObjectiveArgument() {
		Mut<Objective> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ObjectiveArgument("objective"))
			.executesPlayer((player, args) -> {
				results.set((Objective) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();
		// TODO: This test is skipped and I have no idea why it is skipped. This needs to be looked into

		Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective("test-objective", "dummy");
		Objective testObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective("test-objective");

		// /test test-objective
		server.dispatchCommand(player, "test test-objective");
		assertEquals(testObjective, results.get());

		// /test my-objective
		assertCommandFailsWith(player, "test my-objective", "Unknown scoreboard objective 'test-objective'");

		assertNoMoreResults(results);
	}

}
