package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;

import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.arguments.PotionEffectArgument;
import dev.jorel.commandapi.test.MockNMS;
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

		assumeTrue(version.lessThanOrEqualTo(MCVersion.V1_16_5));
		for (PotionEffectType potionEffect : MockNMS.getPotionEffects()) {
			server.dispatchCommand(player, "test " + MockNMS.getNMSPotionEffectName_1_16_5(potionEffect));
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
		
		assumeTrue(version.lessThanOrEqualTo(MCVersion.V1_16_5));

		// /test minecraft:
		assertEquals(
			Arrays.stream(MockNMS.getPotionEffects())
				.map(MockNMS::getNMSPotionEffectName_1_16_5)
				.sorted()
				.toList(),
			server.getSuggestions(player, "test minecraft:")
		);

		// /test minecraft:s
		assertEquals(
			Arrays.stream(MockNMS.getPotionEffects())
				.map(MockNMS::getNMSPotionEffectName_1_16_5)
				.filter(s -> s.startsWith("minecraft:s"))
				.sorted()
				.toList(),
			server.getSuggestions(player, "test minecraft:s"));

		// /test s
		assertEquals(
			Arrays.stream(MockNMS.getPotionEffects())
				.map(MockNMS::getNMSPotionEffectName_1_16_5)
				.filter(s -> s.startsWith("minecraft:s"))
				.sorted()
				.toList(),
			server.getSuggestions(player, "test s"));
	}

}
