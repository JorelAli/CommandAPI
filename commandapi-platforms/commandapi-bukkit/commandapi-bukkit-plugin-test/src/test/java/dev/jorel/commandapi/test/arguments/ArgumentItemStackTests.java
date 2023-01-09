package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link ItemStackArgument}
 */
public class ArgumentItemStackTests extends TestBase {

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
	public void executionTestWithItemStackArgument() {
		Mut<ItemStack> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ItemStackArgument("item"))
			.executesPlayer((player, args) -> {
				results.set((ItemStack) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test dirt
		server.dispatchCommand(player, "test dirt");
		assertEquals(new ItemStack(Material.DIRT), results.get());
		
		// TODO: Implement the rest of this

//		// /test xy
//		server.dispatchCommand(player, "test xy");
//		assertEquals(EnumSet.of(Axis.X, Axis.Y), results.get());
//
//		// /test xyz
//		server.dispatchCommand(player, "test xyz");
//		assertEquals(EnumSet.of(Axis.X, Axis.Y, Axis.Z), results.get());
//
//		// /test xyz
//		server.dispatchCommand(player, "test zyx");
//		assertEquals(EnumSet.of(Axis.X, Axis.Y, Axis.Z), results.get());
//
//		// /test w
//		assertCommandFailsWith(player, "test w", "Invalid swizzle, expected combination of 'x', 'y' and 'z'");
//
//		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

//	@Test
//	public void suggestionTestWithItemStackArgument() {
//		new CommandAPICommand("test")
//			.withArguments(new AxisArgument("axis"))
//			.executesPlayer((player, args) -> {
//			})
//			.register();
//
//		PlayerMock player = server.addPlayer();
//
//		// /test
//		// The axis argument doesn't have any suggestions
//		assertEquals(List.of(), server.getSuggestions(player, "test "));
//
//		// /test x
//		// The axis argument doesn't have any suggestions
//		assertEquals(List.of(), server.getSuggestions(player, "test x"));
//	}

}
