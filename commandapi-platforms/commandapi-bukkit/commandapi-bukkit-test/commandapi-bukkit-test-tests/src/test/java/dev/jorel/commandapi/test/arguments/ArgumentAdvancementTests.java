package dev.jorel.commandapi.test.arguments;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.AdvancementArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link dev.jorel.commandapi.arguments.AdvancementArgument}
 */
public class ArgumentAdvancementTests extends TestBase {

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
	public void executionTestWithAdvancementArgument() {
		Mut<Advancement> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new AdvancementArgument("advancement"))
			.executesPlayer((player, args) -> {
				results.set((Advancement) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test adventure/adventuring_time
		server.dispatchCommand(player, "test adventure/adventuring_time");
		assertEquals(NamespacedKey.minecraft("adventure/adventuring_time"), results.get().getKey());

		assertNoMoreResults(results);
	}

	// TODO: Suggestion tests

}
