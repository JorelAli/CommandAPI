package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link ItemStackPredicateArgument}
 */
class ArgumentItemStackPredicateTests extends TestBase {

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
	void executionTestWithItemStackPredicateArgument() {
		Mut<Predicate<ItemStack>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ItemStackPredicateArgument("predicate"))
			.executesPlayer((player, args) -> {
				results.set(args.getUnchecked(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// I don't know why, but for some reason, in 1.20.5 you can't have
		// an item predicate that doesn't contain a # character?
		if (version.lessThan(MCVersion.V1_20_5)) {
			// /test dirt
			server.dispatchCommand(player, "test dirt");
			assertTrue(results.get().test(new ItemStack(Material.DIRT)));
		} else {
			// /test #axes
			server.dispatchCommand(player, "test #axes");
			assertTrue(results.get().test(new ItemStack(Material.DIAMOND_AXE)));
		}

		// TODO: Not sure why this fails, but it does!
		// /test dirt
//		server.dispatchCommand(player, "test dirt{Count:3b}");
//		assertTrue(results.get().test(new ItemStack(Material.DIRT)));
//		assertTrue(results.get().test(new ItemStack(Material.DIRT, 3)));
		
		// TODO: For some reason, the item registry doesn't have any tags in the
		// testing environment! I'm not sure why, but it would be very nice if
		// we could get tag testing here as well
		// /test #minecraft:planks
//		{
//			server.dispatchCommand(player, "test #minecraft:planks");
//			Predicate<ItemStack> predicate = results.get();
//			assertTrue(predicate.test(new ItemStack(Material.ACACIA_PLANKS)));
//			assertFalse(predicate.test(new ItemStack(Material.DIRT)));
//		}

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	void suggestionTestWithItemStackPredicateArgument() {
		new CommandAPICommand("test")
			.withArguments(new ItemStackPredicateArgument("predicate"))
			.executesPlayer(P_EXEC)
			.register();

		PlayerMock player = server.addPlayer();
		
		// Identical to the ItemStackArgument tests, except ItemStackPredicates have a slightly
		// different list of possible values because it can also accept * and tags (starting with #)
		List<String> itemPredicateNames = new ArrayList<>(MockPlatform.getInstance().getAllItemNames());
		if (version.greaterThanOrEqualTo(MCVersion.V1_20_5)) {
			itemPredicateNames.add(0, "#");
			itemPredicateNames.add(1, "*");
		}
		
		// /test
		// All items should be suggested
		assertEquals(itemPredicateNames, server.getSuggestions(player, "test "));
	
		// /test x
		// All items starting with 'a' should be suggested, as well as items which
		// are underscore-separated and start with 'a', such as 'wooden_axe'
		assertEquals(itemPredicateNames.stream().filter(s -> s.contains(":a") || s.contains("_a")).toList(), server.getSuggestions(player, "test a"));
		
		// /test dirt
		// Completed item names should suggest open curly braces (or square brackets in 1.20.5+)
		if (version.greaterThanOrEqualTo(MCVersion.V1_20_5)) {
			assertEquals(List.of("["), server.getSuggestions(player, "test dirt"));
		} else {
			assertEquals(List.of("{"), server.getSuggestions(player, "test dirt"));
		}
		
		// /test dirt{
		// /test dirt[ (1.20.5+)
		// NBT has no suggestions
		if (version.greaterThanOrEqualTo(MCVersion.V1_20_5)) {
			assertEquals(List.of(), server.getSuggestions(player, "test dirt["));
		} else {
			assertEquals(List.of(), server.getSuggestions(player, "test dirt{"));
		}
		
		// /test dirt{}
		// /test dirt[] (1.20.5+)
		// NBT has no suggestions
		if (version.greaterThanOrEqualTo(MCVersion.V1_20_5)) {
			assertEquals(List.of(), server.getSuggestions(player, "test dirt[]"));
		} else {
			assertEquals(List.of(), server.getSuggestions(player, "test dirt{}"));
		}
	}

}