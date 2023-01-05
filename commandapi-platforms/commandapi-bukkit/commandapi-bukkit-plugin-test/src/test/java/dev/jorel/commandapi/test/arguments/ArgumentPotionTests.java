package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;

/**
 * Tests for the {@link PotionEffectArgument}
 */
public class ArgumentPotionTests extends TestBase {

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
	public void executionTestWithPotionEffectArgumentWithNamespaces() {
		Mut<PotionEffectType> type = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
				type.set((PotionEffectType) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();
		server.dispatchCommand(player, "test speed");
		server.dispatchCommand(player, "test minecraft:speed");
		server.dispatchCommand(player, "test bukkit:speed");

		assertEquals(PotionEffectType.SPEED, type.get());
		assertEquals(PotionEffectType.SPEED, type.get());
		assertEquals(null, type.get());
	}

	@Test
	public void executionTestWithPotionEffectArgumentAllPotionEffects() {
		Mut<PotionEffectType> type = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
				type.set((PotionEffectType) args.get("potion"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		for(PotionEffectType potionEffect : PotionEffectType.values()) {
			server.dispatchCommand(player, "test " + potionEffect.getKey().getKey());
			assertEquals(potionEffect, type.get());
		}
	}

}
