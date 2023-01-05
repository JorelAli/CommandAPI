package dev.jorel.commandapi.test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PotionEffectArgument;

/**
 * Tests for suggestions
 */
public class SuggestionsTests extends TestBase {
	
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
	public void testPotionEffectArgumentSuggestions() {
		new CommandAPICommand("test")
			.withArguments(new PotionEffectArgument("potion"))
			.executesPlayer((player, args) -> {
			})
			.register();

		PlayerMock player = server.addPlayer();
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

		assertEquals(List.of(
			"minecraft:saturation",
			"minecraft:slow_falling",
			"minecraft:slowness",
			"minecraft:speed",
			"minecraft:strength"), server.getSuggestions(player, "test minecraft:s"));
		
		assertEquals(List.of(
			"minecraft:saturation",
			"minecraft:slow_falling",
			"minecraft:slowness",
			"minecraft:speed",
			"minecraft:strength"), server.getSuggestions(player, "test s"));
	}
	
//	@Test
//	public void testAxisArgumentSuggestions() {
//		new CommandAPICommand("test")
//			.withArguments(new AxisArgument("axis"))
//			.executesPlayer((player, args) -> {
//			})
//			.register();
//
//		PlayerMock player = server.addPlayer();
//		assertEquals(List.of(
//			"x",
//			"xy",
//			"xyz",
//			"xz",
//			"y",
//			"yz",
//			"z"), server.getSuggestions(player, "test "));
//		
//		assertEquals(List.of(
//			"x",
//			"xy",
//			"xyz",
//			"xz"), server.getSuggestions(player, "test x"));
//	}

}
