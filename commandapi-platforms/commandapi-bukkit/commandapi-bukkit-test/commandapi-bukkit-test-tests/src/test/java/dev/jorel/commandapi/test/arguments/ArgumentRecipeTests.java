package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.arguments.RecipeArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link RecipeArgument}
 */
class ArgumentRecipeTests extends TestBase {

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
	void executionTestWithRecipeArgument() {
		Mut<Recipe> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer((player, args) -> {
				results.set((Recipe) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test diamond_pickaxe
		server.dispatchCommand(player, "test diamond_pickaxe");
		assertEquals(new ItemStack(Material.DIAMOND_PICKAXE), results.get().getResult());

		// /test unknownRecipe
		assertCommandFailsWith(player, "test unknownrecipe", "Unknown recipe: minecraft:unknownrecipe");

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithRecipeArgumentKeyed() {
		Mut<Keyed> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer((player, args) -> {
				results.set((Keyed) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		for (NamespacedKey str : MockPlatform.getInstance().getAllRecipes()) {
			if (version.greaterThanOrEqualTo(MCVersion.V1_20_5)) {
				// Note that this will fail in 1.20.5 for certain items such as wolf armour
				// because we're currently (as of 24th April 2024) running against 1.20.1.
				// This isn't really important or anything, but it's better to test SOME
				// things rather than skip this entire test altogether.
				try {
					server.dispatchCommand(player, "test " + str.toString());
					assertEquals(str, results.get().getKey());
				} catch(NullPointerException e) {
					assertEquals("Cannot invoke \"org.bukkit.Material.isLegacy()\" because \"this.type\" is null", e.getMessage());
					System.err.println("Error in testing " + str + " recipe: (Null recipe)");
				} catch(IllegalArgumentException e) {
					assertEquals("Cannot have null choice", e.getMessage());
					System.err.println("Error in testing " + str + " recipe: (Null choice)");
				}
			} else {
				server.dispatchCommand(player, "test " + str.toString());
				assertEquals(str, results.get().getKey());
			}
		}

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	void suggestionTestWithRecipeArgument() {
		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();

		// /test
		assertEquals(
			MockPlatform.getInstance().getAllRecipes().stream()
				.map(k -> k.toString())
				.sorted()
				.toList(),
			server.getSuggestions(player, "test "));

		// /test minecraft:s
		assertEquals(
			MockPlatform.getInstance().getAllRecipes().stream()
				.map(k -> k.toString())
				.filter(s -> s.startsWith("minecraft:s"))
				.sorted()
				.toList(),
			server.getSuggestions(player, "test minecraft:s"));
	}

}
