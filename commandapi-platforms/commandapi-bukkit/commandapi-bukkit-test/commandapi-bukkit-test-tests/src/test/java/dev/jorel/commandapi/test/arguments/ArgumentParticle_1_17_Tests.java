package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Vibration.Destination.BlockDestination;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.ParticleData;

/**
 * Tests for the {@link ParticleArgument}, specifically for sculk-related
 * particles (vibrations, sculk charge and shriek) and the dust transition
 * particle (doesn't exist in 1.16.5)
 */
class ArgumentParticle_1_17_Tests extends TestBase {

	/*********
	 * Setup *
	 *********/

	@BeforeEach
	public void setUp() {
		super.setUp();

		assumeTrue(version.lessThan(MCVersion.V1_20_3));
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	// Just checks block coords, doesn't check yaw/pitch or world
	private void assertBlockLocationCoordsEquals(Location loc1, Location loc2) {
		assertEquals(loc1.getBlockX(), loc2.getBlockX(), 0.0001);
		assertEquals(loc1.getBlockY(), loc2.getBlockY(), 0.0001);
		assertEquals(loc1.getBlockZ(), loc2.getBlockZ(), 0.0001);
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void executionTestWithParticleArgumentShriek() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_19));
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// shriek delay
		server.dispatchCommand(player, "test shriek 10");
		@SuppressWarnings("unchecked")
		ParticleData<Integer> result = (ParticleData<Integer>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.valueOf("SHRIEK"), result.particle());

		// Check the particle properties
		assertEquals(10, result.data());

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithParticleArgumentSculkCharge() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_19));
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// sculk_charge angle
		server.dispatchCommand(player, "test sculk_charge 2");
		@SuppressWarnings("unchecked")
		ParticleData<Float> result = (ParticleData<Float>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.valueOf("SCULK_CHARGE"), result.particle());

		// Check the particle properties
		assertEquals(2, result.data());

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithParticleArgumentVibration() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_19));
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// vibration x y z ticks
		server.dispatchCommand(player, "test vibration 5.0 64.0 0.0 200");
		@SuppressWarnings("unchecked")
		ParticleData<Vibration> result = (ParticleData<Vibration>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.VIBRATION, result.particle());

		// Check the particle properties. We only support BlockDestination for commands.
		assertBlockLocationCoordsEquals(player.getLocation(), result.data().getOrigin());
		assertBlockLocationCoordsEquals(new Location(player.getWorld(), 5.0, 64.0, 0.0), ((BlockDestination) result.data().getDestination()).getLocation());
		assertEquals(200, result.data().getArrivalTime());

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithParticleArgumentDustTransition() {
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// dust_color_transition red1 green1 blue1 size red2 green2 blue2
		server.dispatchCommand(player, "test dust_color_transition 1.0 0.0 0.0 3.0 0.0 0.0 1.0");
		@SuppressWarnings("unchecked")
		ParticleData<DustTransition> result = (ParticleData<DustTransition>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.DUST_COLOR_TRANSITION, result.particle());

		// Check the particle properties
		assertEquals(Color.RED, ((DustTransition) result.data()).getColor());
		assertEquals(Color.BLUE, ((DustTransition) result.data()).getToColor());
		assertEquals(3.0f, ((DustTransition) result.data()).getSize());

		assertNoMoreResults(results);
	}

}
