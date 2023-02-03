package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.RotationArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.Rotation;

/**
 * Tests for the {@link RotationArgument}
 */
class ArgumentRotationTests extends TestBase {

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
	void executionTestWithRotationArgumentWithNamespaces() {
		Mut<Rotation> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new RotationArgument("rotation"))
			.executesPlayer((player, args) -> {
				results.set((Rotation) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		{
			// /test 90 180
			server.dispatchCommand(player, "test 90 180"); // yaw = 90, pitch = 180
			Rotation rotation = results.get();
			assertEquals(90.0f, rotation.getYaw());
			assertEquals(180.0f, rotation.getPitch());
		}

		{
			// /test 360 360
			server.dispatchCommand(player, "test 360 360");
			Rotation rotation = results.get();
			assertEquals(360.0f, rotation.getYaw());
			assertEquals(360.0f, rotation.getPitch());
		}

		{
			// /test ~ ~
			server.dispatchCommand(player, "test ~ ~");
			Rotation rotation = results.get();
			assertEquals(0.0f, rotation.getYaw());
			assertEquals(0.0f, rotation.getPitch());
		}

		{
			// TODO: Come back to this, it's broken af
//			player.setLocation(new Location(new WorldMock(), 2, 2, 2, 75, 135));
//			
//			// /test ~ ~
//			server.dispatchCommand(player, "test ~ ~");
//			Rotation rotation = results.get();
//			assertEquals(75.0f, rotation.getYaw());
//			assertEquals(135.0f, rotation.getPitch());
		}

		assertNoMoreResults(results);
	}

//	@Test
//	public void executionTestWithRotationArgumentAllRotations() {
//		Mut<Rotation> results = Mut.of();
//
//		new CommandAPICommand("test")
//			.withArguments(new RotationArgument("potion"))
//			.executesPlayer((player, args) -> {
//				results.set((Rotation) args.get("potion"));
//			})
//			.register();
//
//		PlayerMock player = server.addPlayer();
//
//		for (Rotation Rotation : Rotation.values()) {
//			server.dispatchCommand(player, "test " + Rotation.getKey().getKey());
//			assertEquals(Rotation, results.get());
//		}
//
//		assertNoMoreResults(results);
//	}

	/********************
	 * Suggestion tests *
	 ********************/

//	@Test
//	public void suggestionTestWithRotationArgument() {
//		new CommandAPICommand("test")
//			.withArguments(new RotationArgument("potion"))
//			.executesPlayer((player, args) -> {
//			})
//			.register();
//
//		PlayerMock player = server.addPlayer();
//
//		// /test minecraft:
//		assertEquals(List.of(
//			"minecraft:absorption",
//			"minecraft:bad_omen",
//			"minecraft:blindness",
//			"minecraft:conduit_power",
//			"minecraft:darkness",
//			"minecraft:dolphins_grace",
//			"minecraft:fire_resistance",
//			"minecraft:glowing",
//			"minecraft:haste",
//			"minecraft:health_boost",
//			"minecraft:hero_of_the_village",
//			"minecraft:hunger",
//			"minecraft:instant_damage",
//			"minecraft:instant_health",
//			"minecraft:invisibility",
//			"minecraft:jump_boost",
//			"minecraft:levitation",
//			"minecraft:luck",
//			"minecraft:mining_fatigue",
//			"minecraft:nausea",
//			"minecraft:night_vision",
//			"minecraft:poison",
//			"minecraft:regeneration",
//			"minecraft:resistance",
//			"minecraft:saturation",
//			"minecraft:slow_falling",
//			"minecraft:slowness",
//			"minecraft:speed",
//			"minecraft:strength",
//			"minecraft:unluck",
//			"minecraft:water_breathing",
//			"minecraft:weakness",
//			"minecraft:wither"), server.getSuggestions(player, "test minecraft:"));
//
//		// /test minecraft:s
//		assertEquals(List.of(
//			"minecraft:saturation",
//			"minecraft:slow_falling",
//			"minecraft:slowness",
//			"minecraft:speed",
//			"minecraft:strength"), server.getSuggestions(player, "test minecraft:s"));
//
//		// /test s
//		assertEquals(List.of(
//			"minecraft:saturation",
//			"minecraft:slow_falling",
//			"minecraft:slowness",
//			"minecraft:speed",
//			"minecraft:strength"), server.getSuggestions(player, "test s"));
//	}

}
