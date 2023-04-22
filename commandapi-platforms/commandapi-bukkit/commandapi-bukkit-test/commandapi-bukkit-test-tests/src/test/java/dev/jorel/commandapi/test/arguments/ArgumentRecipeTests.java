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

		// In 1.16 onwards, recipes are implemented using ComplexRecipe, which
		// is simply a recipe that extends Keyed. Because we don't have access
		// to ComplexRecipe in pre-1.16 versions, it's simpler to just check
		// against Keyed instead - we're already doing normal ItemStack result
		// checks in the other test method.
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_16));

		Mut<Keyed> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer((player, args) -> {
				results.set((Keyed) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		for (NamespacedKey str : MockPlatform.getInstance().getAllRecipes()) {
			server.dispatchCommand(player, "test " + str.toString());
			assertEquals(str, results.get().getKey());
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
