package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
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
public class ArgumentRecipeTests extends TestBase {

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
	public void executionTestWithRecipeArgument() {
		Mut<Recipe> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer((player, args) -> {
				results.set((Recipe) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test entity.enderman.death
//		server.dispatchCommand(player, "test entity.enderman.death");
//		assertEquals(Recipe.ENTITY_ENDERMAN_DEATH, results.get());
//		
//		// /test minecraft:entity.enderman.death
//		server.dispatchCommand(player, "test minecraft:entity.enderman.death");
//		assertEquals(Recipe.ENTITY_ENDERMAN_DEATH, results.get());
//
//		// TODO: This test returns null, instead of throwing an exception of a Recipe not existing.
//		// This HAS to be documented in both the JavaDocs and the main documentation - the CommandAPI
//		// assumes everything is non-null UNLESS explicitly stated
//		
//		// /test unknownRecipe
//		server.dispatchCommand(player, "test unknownRecipe");
//		assertEquals(null, results.get());

		assertNoMoreResults(results);
	}
	
	@Test
	public void executionTestWithRecipeArgumentKeyed() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_16));
		
		// In 1.16 onwards, recipes are implemented using ComplexRecipe, which
		// is simply a recipe that extends Keyed. Because we don't have access
		// to ComplexRecipe in pre-1.16 versions, it's simpler to just check
		// against Keyed instead - we're already doing normal ItemStack result
		// checks in the other test method.
		Mut<Keyed> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer((player, args) -> {
				results.set((Keyed) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();
		
		for(NamespacedKey str : MockPlatform.getInstance().getAllRecipes()) {
			server.dispatchCommand(player, "test " + str.toString());
			assertEquals(str, results.get().getKey());
		}

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	public void suggestionTestWithRecipeArgument() {
		new CommandAPICommand("test")
			.withArguments(new RecipeArgument("recipe"))
			.executesPlayer((player, args) -> {
			})
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
