package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
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

		assumeTrue(version.lessThan(MCVersion.V1_20_3));
	}

	@AfterEach
	public void tearDown() {
		super.tearDown();
	}

	private Set<Particle> dodgyParticles = Set.of(
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

		server.dispatchCommand(player, "test dust %s %s %s %d".formatted(red, green, blue, size));
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

}
