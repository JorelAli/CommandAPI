package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
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
		
		// Dev note: We're no longer using assertEquals on the ItemStack
		// object here because these itemstacks will now have ItemMeta
		// which has a reference to the NBT Compound Tag which we can't
		// perform equals against.
		
		// Dev note: To make these tests work, we have to overwrite MockBukkit's
		// ItemFactory with our own, see CommandAPIServerMock#getItemFactory()
		
		// /test minecraft:stone{Count:3b}
		{
			server.dispatchCommand(player, "test minecraft:stone{Count:3b}");
			ItemStack actual = results.get();
			assertEquals(Material.STONE, actual.getType());
			assertEquals(3, actual.getAmount());
		}
		
		// /test minecraft:diamond_sword{Enchantments:[{id:"minecraft:sharpness",lvl:1s}]}
		{
			server.dispatchCommand(player, "test minecraft:diamond_sword{Enchantments:[{id:\"minecraft:sharpness\",lvl:1s}]}");
			ItemStack actual = results.get();
			assertEquals(Material.DIAMOND_SWORD, actual.getType());
			assertEquals(1, actual.getAmount());
			assertEquals(1, actual.getEnchantmentLevel(Enchantment.DAMAGE_ALL));
		}
		
		//{Enchantments:[{id:"minecraft:sharpness",lvl:1s}]}
		
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
