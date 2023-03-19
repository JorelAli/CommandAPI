package dev.jorel.commandapi.test.arguments;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Vibration;
import org.bukkit.Vibration.Destination.BlockDestination;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Snowable;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.MCVersion;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.test.MockPlatform;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.ParticleData;

/**
 * Tests for the {@link ParticleArgument}
 */
class ArgumentParticleTests extends TestBase {

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

	// Just checks block coords, doesn't check yaw/pitch or world
	private void assertBlockLocationCoordsEquals(Location loc1, Location loc2) {
		assertEquals(loc1.getBlockX(), loc2.getBlockX(), 0.0001);
		assertEquals(loc1.getBlockY(), loc2.getBlockY(), 0.0001);
		assertEquals(loc1.getBlockZ(), loc2.getBlockZ(), 0.0001);
	}

	Set<Particle> dodgyParticles = Set.of(
		Particle.SNOW_SHOVEL, // "item_snowball" -> SNOWBALL
		Particle.SUSPENDED_DEPTH // "underwater" -> SUSPENDED
	);

	private float round(float value, int n) {
		return (float) (Math.round(value * Math.pow(10, n)) / Math.pow(10, n));
	}

	/*********
	 * Tests *
	 *********/

	@Test
	void executionTestWithParticleArgumentVoid() {
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		for (Particle particle : Particle.values()) {
			if (particle.getDataType().equals(Void.class) && !dodgyParticles.contains(particle)) {
				server.dispatchCommand(player, "test " + MockPlatform.getInstance().getNMSParticleNameFromBukkit(particle));
				assertEquals(particle, results.get().particle());
			}
		}

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithParticleArgumentDust() {
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// dust red green blue size, where red, green and blue are between 0 and 1
		server.dispatchCommand(player, "test dust 1 0.5 0 4");
		@SuppressWarnings("unchecked")
		ParticleData<DustOptions> result = (ParticleData<DustOptions>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.REDSTONE, result.particle());

		// Check the particle properties
		assertEquals(4, result.data().getSize());
		assertEquals(Color.fromRGB(255, 127, 0), result.data().getColor());

		assertNoMoreResults(results);
	}

	@RepeatedTest(10)
	void executionTestWithParticleArgumentDustRandom() {
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// dust red green blue size, where red, green and blue are between 0 and 1
		float red = round(ThreadLocalRandom.current().nextFloat(), 2);
		float green = round(ThreadLocalRandom.current().nextFloat(), 2);
		float blue = round(ThreadLocalRandom.current().nextFloat(), 2);

		// I don't know if there's a hard limit, but if this value is greater than 4, it
		// caps it off at 4.
		int size = ThreadLocalRandom.current().nextInt(1, 5);

		server.dispatchCommand(player, "test dust %.2f %.2f %.2f %d".formatted(red, green, blue, size));
		@SuppressWarnings("unchecked")
		ParticleData<DustOptions> result = (ParticleData<DustOptions>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.REDSTONE, result.particle());

		// Check the particle properties
		assertEquals(size, result.data().getSize());
		assertEquals(Color.fromRGB((int) (red * 255), (int) (green * 255), (int) (blue * 255)), result.data().getColor());

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithParticleArgumentBlock() {
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// block block_type[meta]
		server.dispatchCommand(player, "test block minecraft:grass_block[snowy=true]");
		@SuppressWarnings("unchecked")
		ParticleData<BlockData> result = (ParticleData<BlockData>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.BLOCK_CRACK, result.particle());

		// Check the particle properties
		assertEquals(Material.GRASS_BLOCK, result.data().getMaterial());
		assertTrue(((Snowable) result.data()).isSnowy());

		assertNoMoreResults(results);
	}

	@Test
	void executionTestWithParticleArgumentShriek() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_18));
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
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_18));
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
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_18));
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
	void executionTestWithParticleArgumentItem() {
		Mut<ParticleData<?>> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData<?>) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// item item_id
		server.dispatchCommand(player, "test item apple");
		@SuppressWarnings("unchecked")
		ParticleData<ItemStack> result = (ParticleData<ItemStack>) results.get();

		// Check the particle type is correct
		assertEquals(Particle.ITEM_CRACK, result.particle());

		// Check the particle properties
		assertEquals(new ItemStack(Material.APPLE), (ItemStack) result.data());

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
