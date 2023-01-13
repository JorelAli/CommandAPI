package dev.jorel.commandapi.test.arguments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
/* ${ifdef.1.17}
import org.bukkit.Particle.DustTransition;
/* */
import org.bukkit.Vibration;
import org.bukkit.Vibration.Destination.BlockDestination;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.entity.PlayerMock;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPIVersionHandler.MCVersion;
import dev.jorel.commandapi.arguments.ParticleArgument;
import dev.jorel.commandapi.test.Mut;
import dev.jorel.commandapi.test.TestBase;
import dev.jorel.commandapi.wrappers.ParticleData;

/**
 * Tests for the {@link ParticleArgument}
 */
@SuppressWarnings("rawtypes")
public class ArgumentParticleTests extends TestBase {

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

	// From CraftParticle + https://minecraft.fandom.com/wiki/Particles#Particle_IDs
	private Map<String, Particle> getParticles() {

		// DO NOT dynamically populate this list using CraftParticle or otherwise
		// because the map is not bijective (when it should be). "underwater"
		// maps to SUSPENDED, but Bukkit has SUSPENDED and SUSPENDED_DEPTH which
		// both map to "underwater"

		Map<String, Particle> particleNames = new HashMap<>();
		particleNames.put("ambient_entity_effect", Particle.SPELL_MOB_AMBIENT); // Non-standard Bukkit name
		particleNames.put("angry_villager", Particle.VILLAGER_ANGRY); // Non-standard Bukkit name
		particleNames.put("ash", Particle.ASH);
		particleNames.put("block", Particle.BLOCK_CRACK); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("block_marker", Particle.BLOCK_MARKER);
		/* */
		particleNames.put("bubble", Particle.WATER_BUBBLE); // Non-standard Bukkit name
		particleNames.put("bubble_pop", Particle.BUBBLE_POP);
		particleNames.put("bubble_column_up", Particle.BUBBLE_COLUMN_UP);
		particleNames.put("campfire_cosy_smoke", Particle.CAMPFIRE_COSY_SMOKE);
		particleNames.put("campfire_signal_smoke", Particle.CAMPFIRE_SIGNAL_SMOKE);
		particleNames.put("cloud", Particle.CLOUD);
		particleNames.put("composter", Particle.COMPOSTER);
		particleNames.put("crimson_spore", Particle.CRIMSON_SPORE);
		particleNames.put("crit", Particle.CRIT);
		particleNames.put("current_down", Particle.CURRENT_DOWN);
		particleNames.put("damage_indicator", Particle.DAMAGE_INDICATOR);
		particleNames.put("dolphin", Particle.DOLPHIN);
		particleNames.put("dragon_breath", Particle.DRAGON_BREATH);
		/* ${ifdef.1.17}
		particleNames.put("dripping_dripstone_lava", Particle.DRIPPING_DRIPSTONE_LAVA);
		particleNames.put("dripping_dripstone_water", Particle.DRIPPING_DRIPSTONE_WATER);
		/* */
		particleNames.put("dripping_lava", Particle.DRIP_LAVA); // Non-standard Bukkit name
		particleNames.put("dripping_obsidian_tear", Particle.DRIPPING_OBSIDIAN_TEAR);
		particleNames.put("dripping_water", Particle.DRIP_WATER); // Non-standard Bukkit name
		particleNames.put("dust", Particle.REDSTONE); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("dust_color_transition", Particle.DUST_COLOR_TRANSITION);
		/* */
		particleNames.put("effect", Particle.SPELL); // Non-standard Bukkit name
		particleNames.put("elder_guardian", Particle.MOB_APPEARANCE); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("electric_spark", Particle.ELECTRIC_SPARK);
		/* */
		particleNames.put("enchant", Particle.ENCHANTMENT_TABLE); // Non-standard Bukkit name
		particleNames.put("enchanted_hit", Particle.CRIT_MAGIC); // Non-standard Bukkit name
		particleNames.put("end_rod", Particle.END_ROD);
		particleNames.put("entity_effect", Particle.SPELL_MOB); // Non-standard Bukkit name
		particleNames.put("explosion_emitter", Particle.EXPLOSION_HUGE); // Non-standard Bukkit name
		particleNames.put("explosion", Particle.EXPLOSION_LARGE); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("falling_dripstone_lava", Particle.FALLING_DRIPSTONE_LAVA);
		particleNames.put("falling_dripstone_water", Particle.FALLING_DRIPSTONE_WATER);
		/* */
		particleNames.put("falling_dust", Particle.FALLING_DUST);
		particleNames.put("falling_lava", Particle.FALLING_LAVA);
		particleNames.put("falling_obsidian_tear", Particle.FALLING_OBSIDIAN_TEAR);
		/* ${ifdef.1.17}
		particleNames.put("falling_spore_blossom", Particle.FALLING_SPORE_BLOSSOM);
		/* */
		particleNames.put("falling_water", Particle.FALLING_WATER);
		particleNames.put("firework", Particle.FIREWORKS_SPARK); // Non-standard Bukkit name
		particleNames.put("fishing", Particle.WATER_WAKE); // Non-standard Bukkit name
		particleNames.put("flame", Particle.FLAME);
		particleNames.put("flash", Particle.FLASH);
		/* ${ifdef.1.17}
		particleNames.put("glow", Particle.GLOW);
		particleNames.put("glow_squid_ink", Particle.GLOW_SQUID_INK);
		/* */
		particleNames.put("happy_villager", Particle.VILLAGER_HAPPY); // Non-standard Bukkit name
		particleNames.put("heart", Particle.HEART);
		particleNames.put("instant_effect", Particle.SPELL_INSTANT); // Non-standard Bukkit name
		particleNames.put("item", Particle.ITEM_CRACK); // Non-standard Bukkit name
		particleNames.put("item_slime", Particle.SLIME); // Non-standard Bukkit name
		particleNames.put("item_snowball", Particle.SNOWBALL); // Non-standard Bukkit name
		particleNames.put("landing_lava", Particle.LANDING_LAVA);
		particleNames.put("landing_obsidian_tear", Particle.LANDING_OBSIDIAN_TEAR);
		particleNames.put("large_smoke", Particle.SMOKE_LARGE); // Non-standard Bukkit name
		particleNames.put("lava", Particle.LAVA);
		particleNames.put("mycelium", Particle.TOWN_AURA); // Non-standard Bukkit name
		particleNames.put("nautilus", Particle.NAUTILUS);
		particleNames.put("note", Particle.NOTE);
		particleNames.put("poof", Particle.EXPLOSION_NORMAL); // Non-standard Bukkit name
		particleNames.put("portal", Particle.PORTAL);
		particleNames.put("rain", Particle.WATER_DROP); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("scrape", Particle.SCRAPE);
		/* */
		particleNames.put("smoke", Particle.SMOKE_NORMAL); // Non-standard Bukkit name
		particleNames.put("sneeze", Particle.SNEEZE);
		/* ${ifdef.1.17}
		particleNames.put("snowflake", Particle.SNOWFLAKE);
		/* */
		particleNames.put("soul", Particle.SOUL);
		particleNames.put("soul_fire_flame", Particle.SOUL_FIRE_FLAME);
		particleNames.put("spit", Particle.SPIT);
		particleNames.put("splash", Particle.WATER_SPLASH); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("spore_blossom_air", Particle.SPORE_BLOSSOM_AIR);
		/* */
		particleNames.put("squid_ink", Particle.SQUID_INK);
		particleNames.put("sweep_attack", Particle.SWEEP_ATTACK);
		particleNames.put("totem_of_undying", Particle.TOTEM); // Non-standard Bukkit name
		particleNames.put("underwater", Particle.SUSPENDED); // Non-standard Bukkit name
		/* ${ifdef.1.17}
		particleNames.put("vibration", Particle.VIBRATION);
		/* */
		particleNames.put("warped_spore", Particle.WARPED_SPORE);
		/* ${ifdef.1.17}
		particleNames.put("wax_off", Particle.WAX_OFF);
		particleNames.put("wax_on", Particle.WAX_ON);
		/* */
		particleNames.put("white_ash", Particle.WHITE_ASH);
		particleNames.put("witch", Particle.SPELL_WITCH); // Non-standard Bukkit name

		return particleNames;
	}

	/*********
	 * Tests *
	 *********/

	@Test
	public void executionTestWithParticleArgumentBasicParticles() {
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get("particle"));
			})
			.register();

		PlayerMock player = server.addPlayer();

		// Particles that don't have any data
		for (Entry<String, Particle> particles : getParticles().entrySet()) {
			if (particles.getValue().getDataType().equals(Void.class)) {
				server.dispatchCommand(player, "test " + particles.getKey());
				assertEquals(particles.getValue(), results.get().particle());
			}
		}
	}

	@Test
	public void executionTestWithParticleArgumentBlocks() {
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test block minecraft:grass_block[snowy=true]");
		ParticleData result = results.get();
		assertEquals(Particle.BLOCK_CRACK, result.particle());
		assertInstanceOf(BlockData.class, result.data());

		BlockData blockData = (BlockData) result.data();
		assertEquals(Material.GRASS_BLOCK, blockData.getMaterial());
	}

	@Test
	public void executionTestWithParticleArgumentDust() {
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test dust 1.0 0.5 0.5 1.0"); // R G B size
		ParticleData result = results.get();
		assertEquals(Particle.REDSTONE, result.particle());
		assertInstanceOf(DustOptions.class, result.data());

		DustOptions dustOptions = (DustOptions) result.data();
		assertEquals(Color.fromRGB(255, 255 / 2, 255 / 2), dustOptions.getColor());
		assertEquals(1.0f, dustOptions.getSize());
	}

	/* ${ifdef.1.17}
	@Test
	public void executionTestWithParticleArgumentDustTransition() {
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test dust_color_transition 1.0 0.0 0.0 1.0 0.0 0.0 1.0"); // R G B size R G B
		ParticleData result = results.get();
		assertEquals(Particle.DUST_COLOR_TRANSITION, result.particle());
		assertInstanceOf(DustTransition.class, result.data());

		DustTransition dustTransition = (DustTransition) result.data();
		assertEquals(Color.fromRGB(255, 0, 0), dustTransition.getColor());
		assertEquals(Color.fromRGB(0, 0, 255), dustTransition.getToColor());
		assertEquals(1.0f, dustTransition.getSize());
	}

	@Test
	public void executionTestWithParticleArgumentItem() {
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test item minecraft:apple");
		ParticleData result = results.get();
		assertEquals(Particle.ITEM_CRACK, result.particle());
		assertInstanceOf(ItemStack.class, result.data());

		ItemStack itemStack = (ItemStack) result.data();
		assertEquals(Material.APPLE, itemStack.getType());
		assertEquals(1, itemStack.getAmount());
	}

	@Test
	public void executionTestWithParticleArgumentSculkCharge() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_19), "Sculk charge particle only exists in 1.19+");
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test sculk_charge 0.2");
		ParticleData result = results.get();
		assertEquals(Particle.valueOf("SCULK_CHARGE"), result.particle());
		assertInstanceOf(Float.class, result.data());

		float angle = (float) result.data();
		assertEquals(0.2, angle, 0.0001);
	}

	@Test
	public void executionTestWithParticleArgumentShriek() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_19), "Shriek particle only exists in 1.19+");
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test shriek 100");
		ParticleData result = results.get();
		assertEquals(Particle.valueOf("SHRIEK"), result.particle());
		assertInstanceOf(Integer.class, result.data());

		int delay = (int) result.data();
		assertEquals(100, delay);
	}

	@Test
	public void executionTestWithParticleArgumentVibration() {
		assumeTrue(version.greaterThanOrEqualTo(MCVersion.V1_19), "Vibration particle only exists in 1.19+");
		Mut<ParticleData> results = Mut.of();

		new CommandAPICommand("test")
			.withArguments(new ParticleArgument("particle"))
			.executesPlayer((player, args) -> {
				results.set((ParticleData) args.get(0));
			})
			.register();

		PlayerMock player = server.addPlayer();

		server.dispatchCommand(player, "test vibration 5.0 64.0 0.0 200"); // x y z ticks
		ParticleData result = results.get();
		assertEquals(Particle.VIBRATION, result.particle());
		assertInstanceOf(Vibration.class, result.data());

		Vibration vibration = (Vibration) result.data();
		assertInstanceOf(BlockDestination.class, vibration.getDestination());
		BlockDestination destination = (BlockDestination) vibration.getDestination();
		{
			Location location = destination.getLocation();
			assertEquals(5, location.getBlockX());
			assertEquals(64, location.getBlockY());
			assertEquals(0, location.getBlockZ());
		}
		assertEquals(200, vibration.getArrivalTime());
	}
	/* */

}
