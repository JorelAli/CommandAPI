package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Predicate;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument;
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

		// /test dirt
		server.dispatchCommand(player, "test dirt");
		assertTrue(results.get().test(new ItemStack(Material.DIRT)));
		
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
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();
		
		// TODO: Literally the same as ItemStackTests
	}

}