package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.bukkit.NamespacedKey;
import org.bukkit.loot.LootTable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.LootTableArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link LootTableArgument}
 */
class ArgumentLootTableTests extends TestBase {

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
	void executionTestWithLootTableArgument() {
		Mut<LootTable> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new LootTableArgument("loottable"))
			.executesPlayer((player, args) -> {
				results.set((LootTable) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test chests/simple_dungeon
		server.dispatchCommand(player, "test chests/simple_dungeon");
		assertEquals(NamespacedKey.minecraft("chests/simple_dungeon"), results.get().getKey());

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	void suggestionTestWithLootTableArgument() {
		new CommandAPICommand("test")
			.withArguments(new LootTableArgument("loottable"))
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();

		assertEquals(Arrays.stream(MockPlatform.getInstance().getLootTables()).map(lt -> lt.getKey().toString())
			.sorted().toList(), server.getSuggestions(player, "test "));
	}

}
