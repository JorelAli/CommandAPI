package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
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
	

	
	// List of really stupid particles that may not have proper mappings:
//	"block" -> BLOCK_CRACK
//	"block" -> BLOCK_DUST
//	"block" -> LEGACY_BLOCK_CRACK
//	"block" -> LEGACY_BLOCK_DUST
//	"falling_dust" -> FALLING_DUST
//	"falling_dust" -> LEGACY_FALLING_DUST
//	"item_snowball" -> SNOW_SHOVEL
//	"item_snowball" -> SNOWBALL
//	"underwater" -> SUSPENDED
//	"underwater" -> SUSPENDED_DEPTH
	
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
	void executionTestWithParticleArgumentDust2() {
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

}
