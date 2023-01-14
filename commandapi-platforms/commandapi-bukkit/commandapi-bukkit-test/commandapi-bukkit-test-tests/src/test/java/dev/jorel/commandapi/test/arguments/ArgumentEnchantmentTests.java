package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.enchantments.Enchantment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.EnchantmentArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link EnchantmentArgument}
 */
public class ArgumentEnchantmentTests extends TestBase {

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
	public void executionTestWithEnchantmentArgument() {
		Mut<Enchantment> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new EnchantmentArgument("enchantment"))
			.executesPlayer((player, args) -> {
				results.set((Enchantment) args.get("enchantment"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test sharpness
		server.dispatchCommand(player, "test sharpness");
		assertEquals(Enchantment.DAMAGE_ALL, results.get());

		// /test minecraft:sharpness
		server.dispatchCommand(player, "test minecraft:sharpness");
		assertEquals(Enchantment.DAMAGE_ALL, results.get());

		// /test blah
		// Unknown enchantment, blah is not a valid enchantment
		assertCommandFailsWith(player, "test blah", "Unknown enchantment: minecraft:blah");

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithEnchantmentArgumentAllEnchantments() {
		Mut<Enchantment> results = Mut.of();

		new CommandAPICommand("test")
		.withArguments(new EnchantmentArgument("enchantment"))
		.executesPlayer((player, args) -> {
			results.set((Enchantment) args.get("enchantment"));
		})
		.register();

		PlayerMock player = server.addPlayer();

		for (Enchantment enchantment : MockPlatform.getInstance().getEnchantments()) {
			server.dispatchCommand(player, "test " + enchantment.getKey().getKey());
			assertEquals(enchantment, results.get());
		}

		assertNoMoreResults(results);
	}

}
