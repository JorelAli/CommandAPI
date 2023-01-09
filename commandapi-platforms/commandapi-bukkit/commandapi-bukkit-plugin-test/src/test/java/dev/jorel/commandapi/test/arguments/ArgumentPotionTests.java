package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
		Mut<PotionEffectType> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
				results.set((PotionEffectType) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test speed
		server.dispatchCommand(player, "test speed");
		assertEquals(PotionEffectType.SPEED, results.get());

		// /test minecraft:speed
		server.dispatchCommand(player, "test minecraft:speed");
		assertEquals(PotionEffectType.SPEED, results.get());

		// /test bukkit:speed
		// Unknown effect, bukkit:speed is not a valid potion effect
		assertCommandFailsWith(player, "test bukkit:speed", "Unknown effect: bukkit:speed");

		assertNoMoreResults(results);
	}

	@Test
	public void executionTestWithPotionEffectArgumentAllPotionEffects() {
		Mut<PotionEffectType> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
				results.set((PotionEffectType) args.get("potion"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		for (PotionEffectType potionEffect : PotionEffectType.values()) {
			server.dispatchCommand(player, "test " + potionEffect.getKey().getKey());
			assertEquals(potionEffect, results.get());
		}

		assertNoMoreResults(results);
	}

	/********************
	 * Suggestion tests *
	 ********************/

	@Test
	public void suggestionTestWithPotionEffectArgument() {
		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();

		// /test minecraft:
		assertEquals(List.of(
			"minecraft:absorption",
			"minecraft:bad_omen",
			"minecraft:blindness",
			"minecraft:conduit_power",
			"minecraft:darkness",
			"minecraft:dolphins_grace",
			"minecraft:fire_resistance",
			"minecraft:glowing",
			"minecraft:haste",
			"minecraft:health_boost",
			"minecraft:hero_of_the_village",
			"minecraft:hunger",
			"minecraft:instant_damage",
			"minecraft:instant_health",
			"minecraft:invisibility",
			"minecraft:jump_boost",
			"minecraft:levitation",
			"minecraft:luck",
			"minecraft:mining_fatigue",
			"minecraft:nausea",
			"minecraft:night_vision",
			"minecraft:poison",
			"minecraft:regeneration",
			"minecraft:resistance",
			"minecraft:saturation",
			"minecraft:slow_falling",
			"minecraft:slowness",
			"minecraft:speed",
			"minecraft:strength",
			"minecraft:unluck",
			"minecraft:water_breathing",
			"minecraft:weakness",
			"minecraft:wither"), server.getSuggestions(player, "test minecraft:"));

		// /test minecraft:s
		assertEquals(List.of(
			"minecraft:saturation",
			"minecraft:slow_falling",
			"minecraft:slowness",
			"minecraft:speed",
			"minecraft:strength"), server.getSuggestions(player, "test minecraft:s"));

		// /test s
		assertEquals(List.of(
			"minecraft:saturation",
			"minecraft:slow_falling",
			"minecraft:slowness",
			"minecraft:speed",
			"minecraft:strength"), server.getSuggestions(player, "test s"));
	}

}
