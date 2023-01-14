package dev.jorel.commandapi.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.mockito.Mockito;

import com.google.common.collect.Streams;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.enchantments.EnchantmentMock;
import be.seeseemelk.mockbukkit.potion.MockPotionEffectType;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.ParticleData;
import net.minecraft.server.v1_16_R3.Advancement;
import net.minecraft.server.v1_16_R3.AdvancementDataWorld;
import net.minecraft.server.v1_16_R3.ArgumentAnchor.Anchor;
import net.minecraft.server.v1_16_R3.ArgumentRegistry;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.DispenserRegistry;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.LootTableRegistry;
import net.minecraft.server.v1_16_R3.LootTables;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.MobEffectList;
import net.minecraft.server.v1_16_R3.PlayerList;
import net.minecraft.server.v1_16_R3.ResourceKey;
import net.minecraft.server.v1_16_R3.ScoreboardServer;
import net.minecraft.server.v1_16_R3.ScoreboardTeam;
import net.minecraft.server.v1_16_R3.SharedConstants;
import net.minecraft.server.v1_16_R3.UserCache;
import net.minecraft.server.v1_16_R3.Vec2F;
import net.minecraft.server.v1_16_R3.Vec3D;
import net.minecraft.server.v1_16_R3.WorldServer;

public class MockNMS extends ArgumentNMS {

	static {
		CodeSource src = PotionEffectType.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			System.err.println("Loading PotionEffectType sources from " + src.getLocation());
		}
	}

	public MockNMS(NMS<?> baseNMS) {
		super(baseNMS);

		// Stub in our getMinecraftServer implementation
		CommandAPIBukkit nms = Mockito.spy((CommandAPIBukkit) BASE_NMS);
		Mockito.when(nms.getMinecraftServer()).thenAnswer(i -> getMinecraftServer());
		BASE_NMS = nms;

		// Initialize WorldVersion (game version)
		SharedConstants.b();

		// MockBukkit is very helpful and registers all of the potion
		// effects and enchantments for us. We need to not do this (because
		// we call Bootstrap.bootStrap() below which does the same thing)
		unregisterAllEnchantments();
		unregisterAllPotionEffects();

		// Invoke Minecraft's registry. This also initializes all argument types.
		// How convenient!
		DispenserRegistry.init();

		// Don't use EnchantmentMock.registerDefaultEnchantments because we want
		// to specify what enchantments to mock (i.e. only 1.18 ones, and not any
		// 1.19 ones!)
		registerDefaultPotionEffects();
		registerDefaultEnchantments();
	}

	/*************************
	 * Registry manipulation *
	 *************************/

	private void unregisterAllPotionEffects() {
		PotionEffectType[] byId = getFieldAs(PotionEffectType.class, "byId", null, PotionEffectType[].class);
		for (int i = 0; i < byId.length; i++) {
			byId[i] = null;
		}

		getFieldAs(PotionEffectType.class, "byName", null, Map.class).clear();
		setField(PotionEffectType.class, "acceptingNew", null, true);
	}

	private void registerDefaultPotionEffects() {
		for (PotionEffectType type : PotionEffectType.values()) {
			if (type != null) {
				return;
			}
		}

		registerPotionEffectType(1, "SPEED", false, 8171462);
		registerPotionEffectType(2, "SLOWNESS", false, 5926017);
		registerPotionEffectType(3, "HASTE", false, 14270531);
		registerPotionEffectType(4, "MINING_FATIGUE", false, 4866583);
		registerPotionEffectType(5, "STRENGTH", false, 9643043);
		registerPotionEffectType(6, "INSTANT_HEALTH", true, 16262179);
		registerPotionEffectType(7, "INSTANT_DAMAGE", true, 4393481);
		registerPotionEffectType(8, "JUMP_BOOST", false, 2293580);
		registerPotionEffectType(9, "NAUSEA", false, 5578058);
		registerPotionEffectType(10, "REGENERATION", false, 13458603);
		registerPotionEffectType(11, "RESISTANCE", false, 10044730);
		registerPotionEffectType(12, "FIRE_RESISTANCE", false, 14981690);
		registerPotionEffectType(13, "WATER_BREATHING", false, 3035801);
		registerPotionEffectType(14, "INVISIBILITY", false, 8356754);
		registerPotionEffectType(15, "BLINDNESS", false, 2039587);
		registerPotionEffectType(16, "NIGHT_VISION", false, 2039713);
		registerPotionEffectType(17, "HUNGER", false, 5797459);
		registerPotionEffectType(18, "WEAKNESS", false, 4738376);
		registerPotionEffectType(19, "POISON", false, 5149489);
		registerPotionEffectType(20, "WITHER", false, 3484199);
		registerPotionEffectType(21, "HEALTH_BOOST", false, 16284963);
		registerPotionEffectType(22, "ABSORPTION", false, 2445989);
		registerPotionEffectType(23, "SATURATION", true, 16262179);
		registerPotionEffectType(24, "GLOWING", false, 9740385);
		registerPotionEffectType(25, "LEVITATION", false, 13565951);
		registerPotionEffectType(26, "LUCK", false, 3381504);
		registerPotionEffectType(27, "UNLUCK", false, 12624973);
		registerPotionEffectType(28, "SLOW_FALLING", false, 16773073);
		registerPotionEffectType(29, "CONDUIT_POWER", false, 1950417);
		registerPotionEffectType(30, "DOLPHINS_GRACE", false, 8954814);
		registerPotionEffectType(31, "BAD_OMEN", false, 745784);
		registerPotionEffectType(32, "HERO_OF_THE_VILLAGE", false, 4521796);
		PotionEffectType.stopAcceptingRegistrations();
	}

	private void registerPotionEffectType(int id, String name, boolean instant, int rgb) {
		PotionEffectType.registerPotionEffectType(new MockPotionEffectType(id, name, instant, Color.fromRGB(rgb)));
	}

	private void unregisterAllEnchantments() {
		getFieldAs(Enchantment.class, "byName", null, Map.class).clear();
		getFieldAs(Enchantment.class, "byKey", null, Map.class).clear();
		setField(Enchantment.class, "acceptingNew", null, true);
	}

	private void registerDefaultEnchantments() {
		for (Enchantment enchantment : getInstance().getEnchantments()) {
			if (Enchantment.getByKey(enchantment.getKey()) == null) {
				Enchantment.registerEnchantment(new EnchantmentMock(enchantment.getKey(), enchantment.getKey().getKey()));
			}
		}
	}
	
	/**************************
	 * MockPlatform overrides *
	 **************************/

	// ItemStackArgument requirements
	@Override
	public ItemFactory getItemFactory() {
		return CraftItemFactory.instance();
	}

	/**
	 * @return A list of all item names, sorted in alphabetical order. Each item is
	 *         prefixed with {@code minecraft:}
	 */
	@Override
	public List<String> getAllItemNames() {
		// Registry.ITEM
		return StreamSupport.stream(IRegistry.ITEM.spliterator(), false)
			.map(Object::toString)
			.map(s -> "minecraft:" + s)
			.sorted()
			.toList();
	}

	@Override
	public String[] compatibleVersions() {
		return BASE_NMS.compatibleVersions();
	}

	CommandDispatcher<CommandListenerWrapper> dispatcher;

	@Override
	public CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		if (this.dispatcher == null) {
			this.dispatcher = new CommandDispatcher<>();
		}
		return this.dispatcher;
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((ServerMock) Bukkit.getServer()).getCommandMap();
	}

	List<EntityPlayer> players = new ArrayList<>();
	PlayerList playerListMock;

	@SuppressWarnings("unchecked")
	@Override
	public CommandListenerWrapper getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> senderWrapper) {
		CommandSender sender = senderWrapper.getSource();
		CommandListenerWrapper clw = Mockito.mock(CommandListenerWrapper.class);
		Mockito.when(clw.getBukkitSender()).thenReturn(sender);

		if (sender instanceof Player player) {
			// Location argument
			Location loc = player.getLocation();
			Mockito.when(clw.getPosition()).thenReturn(new Vec3D(loc.getX(), loc.getY(), loc.getZ()));

			WorldServer worldServerMock = Mockito.mock(WorldServer.class);
			Mockito.when(clw.getWorld()).thenReturn(worldServerMock);
//			Mockito.when(clw.getWorld().hasChunkAt(any(BlockPosition.class))).thenReturn(true);
//			Mockito.when(clw.getWorld().isInWorldBounds(any(BlockPosition.class))).thenReturn(true);
			Mockito.when(clw.k()).thenReturn(Anchor.EYES);

			Mockito.when(clw.getServer()).thenAnswer(s -> getMinecraftServer());

			// Entity selector argument
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				EntityPlayer entityPlayerMock = Mockito.mock(EntityPlayer.class);
				CraftPlayer craftPlayerMock = Mockito.mock(CraftPlayer.class);
				Mockito.when(craftPlayerMock.getName()).thenReturn(onlinePlayer.getName());
				Mockito.when(craftPlayerMock.getUniqueId()).thenReturn(onlinePlayer.getUniqueId());
				Mockito.when(entityPlayerMock.getBukkitEntity()).thenReturn(craftPlayerMock);
				players.add(entityPlayerMock);
			}

			if (playerListMock == null) {
				playerListMock = Mockito.mock(PlayerList.class);
				Mockito.when(playerListMock.getPlayer(anyString())).thenAnswer(invocation -> {
					String playerName = invocation.getArgument(0);
					for (EntityPlayer onlinePlayer : players) {
						if (onlinePlayer.getBukkitEntity().getName().equals(playerName)) {
							return onlinePlayer;
						}
					}
					return null;
				});
			}

			Mockito.when(clw.p()).thenAnswer(invocation -> {
				Set<ResourceKey<net.minecraft.server.v1_16_R3.World>> set = new HashSet<>();
				// We only need to implement resourceKey.a()

				for (World world : Bukkit.getWorlds()) {
					ResourceKey<net.minecraft.server.v1_16_R3.World> key = Mockito.mock(ResourceKey.class);
					Mockito.when(key.a()).thenReturn(new MinecraftKey(world.getName()));
					set.add(key);
				}

				return set;
			});

			// Rotation argument
			Mockito.when(clw.i()).thenReturn(new Vec2F(loc.getYaw(), loc.getPitch()));

			Mockito.when(clw.m()).thenAnswer(invocation -> { // CommandListenerWrapper#getAllTeams
				return Bukkit.getScoreboardManager().getMainScoreboard().getTeams().stream().map(Team::getName).toList();
			});
		}
		return clw;
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(CommandListenerWrapper clw) {
		try {
			return wrapCommandSender(clw.getBukkitSender());
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean forceNative) {
		return getCommandSenderFromCommandSource(cmdCtx.getSource());
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher)
		throws IOException {
		Files
			.asCharSink(file, StandardCharsets.UTF_8)
			.write(new GsonBuilder()
				.setPrettyPrinting()
				.create()
				.toJson(ArgumentRegistry.a(dispatcher, dispatcher.getRoot())));
	}

	@Override
	public World getWorldForCSS(CommandListenerWrapper clw) {
		return new WorldMock();
	}

	@Override
	public void resendPackets(Player player) {
		// There's nothing to do here, we can't "send packets to players"
		return;
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		throw new Error("unimplemented");
	}

	@Override
	public String convert(ItemStack is) {
		throw new Error("unimplemented");
	}

	@Override
	public String convert(ParticleData<?> particle) {
		throw new Error("unimplemented");
	}

	@Override
	public String convert(PotionEffectType potion) {
		throw new Error("unimplemented");
	}

	@Override
	public String convert(Sound sound) {
		throw new Error("unimplemented");
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		throw new Error("unimplemented");
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		throw new Error("unimplemented");
	}

	@Override
	public void reloadDataPacks() {
		throw new Error("unimplemented");
	}

	@Override
	public PotionEffectType[] getPotionEffects() {
		return new PotionEffectType[] {
			PotionEffectType.SPEED,
			PotionEffectType.SLOW,
			PotionEffectType.FAST_DIGGING,
			PotionEffectType.SLOW_DIGGING,
			PotionEffectType.INCREASE_DAMAGE,
			PotionEffectType.HEAL,
			PotionEffectType.HARM,
			PotionEffectType.JUMP,
			PotionEffectType.CONFUSION,
			PotionEffectType.REGENERATION,
			PotionEffectType.DAMAGE_RESISTANCE,
			PotionEffectType.FIRE_RESISTANCE,
			PotionEffectType.WATER_BREATHING,
			PotionEffectType.INVISIBILITY,
			PotionEffectType.BLINDNESS,
			PotionEffectType.NIGHT_VISION,
			PotionEffectType.HUNGER,
			PotionEffectType.WEAKNESS,
			PotionEffectType.POISON,
			PotionEffectType.WITHER,
			PotionEffectType.HEALTH_BOOST,
			PotionEffectType.ABSORPTION,
			PotionEffectType.SATURATION,
			PotionEffectType.GLOWING,
			PotionEffectType.LEVITATION,
			PotionEffectType.LUCK,
			PotionEffectType.UNLUCK,
			PotionEffectType.SLOW_FALLING,
			PotionEffectType.CONDUIT_POWER,
			PotionEffectType.DOLPHINS_GRACE,
			PotionEffectType.BAD_OMEN,
			PotionEffectType.HERO_OF_THE_VILLAGE
		};
	}

	@Override
	public EntityType[] getEntityTypes() {
		return new EntityType[] {
			EntityType.DROPPED_ITEM,
			EntityType.EXPERIENCE_ORB,
			EntityType.AREA_EFFECT_CLOUD,
			EntityType.ELDER_GUARDIAN,
			EntityType.WITHER_SKELETON,
			EntityType.STRAY,
			EntityType.EGG,
			EntityType.LEASH_HITCH,
			EntityType.PAINTING,
			EntityType.ARROW,
			EntityType.SNOWBALL,
			EntityType.FIREBALL,
			EntityType.SMALL_FIREBALL,
			EntityType.ENDER_PEARL,
			EntityType.ENDER_SIGNAL,
			EntityType.SPLASH_POTION,
			EntityType.THROWN_EXP_BOTTLE,
			EntityType.ITEM_FRAME,
			EntityType.WITHER_SKULL,
			EntityType.PRIMED_TNT,
			EntityType.FALLING_BLOCK,
			EntityType.FIREWORK,
			EntityType.HUSK,
			EntityType.SPECTRAL_ARROW,
			EntityType.SHULKER_BULLET,
			EntityType.DRAGON_FIREBALL,
			EntityType.ZOMBIE_VILLAGER,
			EntityType.SKELETON_HORSE,
			EntityType.ZOMBIE_HORSE,
			EntityType.ARMOR_STAND,
			EntityType.DONKEY,
			EntityType.MULE,
			EntityType.EVOKER_FANGS,
			EntityType.EVOKER,
			EntityType.VEX,
			EntityType.VINDICATOR,
			EntityType.ILLUSIONER,
			EntityType.MINECART_COMMAND,
			EntityType.BOAT,
			EntityType.MINECART,
			EntityType.MINECART_CHEST,
			EntityType.MINECART_FURNACE,
			EntityType.MINECART_TNT,
			EntityType.MINECART_HOPPER,
			EntityType.MINECART_MOB_SPAWNER,
			EntityType.CREEPER,
			EntityType.SKELETON,
			EntityType.SPIDER,
			EntityType.GIANT,
			EntityType.ZOMBIE,
			EntityType.SLIME,
			EntityType.GHAST,
			EntityType.ZOMBIFIED_PIGLIN,
			EntityType.ENDERMAN,
			EntityType.CAVE_SPIDER,
			EntityType.SILVERFISH,
			EntityType.BLAZE,
			EntityType.MAGMA_CUBE,
			EntityType.ENDER_DRAGON,
			EntityType.WITHER,
			EntityType.BAT,
			EntityType.WITCH,
			EntityType.ENDERMITE,
			EntityType.GUARDIAN,
			EntityType.SHULKER,
			EntityType.PIG,
			EntityType.SHEEP,
			EntityType.COW,
			EntityType.CHICKEN,
			EntityType.SQUID,
			EntityType.WOLF,
			EntityType.MUSHROOM_COW,
			EntityType.SNOWMAN,
			EntityType.OCELOT,
			EntityType.IRON_GOLEM,
			EntityType.HORSE,
			EntityType.RABBIT,
			EntityType.POLAR_BEAR,
			EntityType.LLAMA,
			EntityType.LLAMA_SPIT,
			EntityType.PARROT,
			EntityType.VILLAGER,
			EntityType.ENDER_CRYSTAL,
			EntityType.TURTLE,
			EntityType.PHANTOM,
			EntityType.TRIDENT,
			EntityType.COD,
			EntityType.SALMON,
			EntityType.PUFFERFISH,
			EntityType.TROPICAL_FISH,
			EntityType.DROWNED,
			EntityType.DOLPHIN,
			EntityType.CAT,
			EntityType.PANDA,
			EntityType.PILLAGER,
			EntityType.RAVAGER,
			EntityType.TRADER_LLAMA,
			EntityType.WANDERING_TRADER,
			EntityType.FOX,
			EntityType.BEE,
			EntityType.HOGLIN,
			EntityType.PIGLIN,
			EntityType.STRIDER,
			EntityType.ZOGLIN,
			EntityType.PIGLIN_BRUTE,
			EntityType.FISHING_HOOK,
			EntityType.LIGHTNING,
			EntityType.PLAYER,
			EntityType.UNKNOWN
		};
	}

	@Override
	public Enchantment[] getEnchantments() {
		return new Enchantment[] {
			Enchantment.PROTECTION_ENVIRONMENTAL,
			Enchantment.PROTECTION_FIRE,
			Enchantment.PROTECTION_FALL,
			Enchantment.PROTECTION_EXPLOSIONS,
			Enchantment.PROTECTION_PROJECTILE,
			Enchantment.OXYGEN,
			Enchantment.WATER_WORKER,
			Enchantment.THORNS,
			Enchantment.DEPTH_STRIDER,
			Enchantment.FROST_WALKER,
			Enchantment.BINDING_CURSE,
			Enchantment.DAMAGE_ALL,
			Enchantment.DAMAGE_UNDEAD,
			Enchantment.DAMAGE_ARTHROPODS,
			Enchantment.KNOCKBACK,
			Enchantment.FIRE_ASPECT,
			Enchantment.LOOT_BONUS_MOBS,
			Enchantment.SWEEPING_EDGE,
			Enchantment.DIG_SPEED,
			Enchantment.SILK_TOUCH,
			Enchantment.DURABILITY,
			Enchantment.LOOT_BONUS_BLOCKS,
			Enchantment.ARROW_DAMAGE,
			Enchantment.ARROW_KNOCKBACK,
			Enchantment.ARROW_FIRE,
			Enchantment.ARROW_INFINITE,
			Enchantment.LUCK,
			Enchantment.LURE,
			Enchantment.LOYALTY,
			Enchantment.IMPALING,
			Enchantment.RIPTIDE,
			Enchantment.CHANNELING,
			Enchantment.MULTISHOT,
			Enchantment.QUICK_CHARGE,
			Enchantment.PIERCING,
			Enchantment.MENDING,
			Enchantment.VANISHING_CURSE,
			Enchantment.SOUL_SPEED
		};
	}

	@Override
	public org.bukkit.loot.LootTables[] getLootTables() {
		return new org.bukkit.loot.LootTables[] {
			// org.bukkit.loot.LootTables.EMPTY,
			org.bukkit.loot.LootTables.ABANDONED_MINESHAFT,
			org.bukkit.loot.LootTables.BURIED_TREASURE,
			org.bukkit.loot.LootTables.DESERT_PYRAMID,
			org.bukkit.loot.LootTables.END_CITY_TREASURE,
			org.bukkit.loot.LootTables.IGLOO_CHEST,
			org.bukkit.loot.LootTables.JUNGLE_TEMPLE,
			org.bukkit.loot.LootTables.JUNGLE_TEMPLE_DISPENSER,
			org.bukkit.loot.LootTables.NETHER_BRIDGE,
			org.bukkit.loot.LootTables.PILLAGER_OUTPOST,
			org.bukkit.loot.LootTables.BASTION_TREASURE,
			org.bukkit.loot.LootTables.BASTION_OTHER,
			org.bukkit.loot.LootTables.BASTION_BRIDGE,
			org.bukkit.loot.LootTables.BASTION_HOGLIN_STABLE,
			org.bukkit.loot.LootTables.RUINED_PORTAL,
			org.bukkit.loot.LootTables.SHIPWRECK_MAP,
			org.bukkit.loot.LootTables.SHIPWRECK_SUPPLY,
			org.bukkit.loot.LootTables.SHIPWRECK_TREASURE,
			org.bukkit.loot.LootTables.SIMPLE_DUNGEON,
			org.bukkit.loot.LootTables.SPAWN_BONUS_CHEST,
			org.bukkit.loot.LootTables.STRONGHOLD_CORRIDOR,
			org.bukkit.loot.LootTables.STRONGHOLD_CROSSING,
			org.bukkit.loot.LootTables.STRONGHOLD_LIBRARY,
			org.bukkit.loot.LootTables.UNDERWATER_RUIN_BIG,
			org.bukkit.loot.LootTables.UNDERWATER_RUIN_SMALL,
			org.bukkit.loot.LootTables.VILLAGE_ARMORER,
			org.bukkit.loot.LootTables.VILLAGE_BUTCHER,
			org.bukkit.loot.LootTables.VILLAGE_CARTOGRAPHER,
			org.bukkit.loot.LootTables.VILLAGE_DESERT_HOUSE,
			org.bukkit.loot.LootTables.VILLAGE_FISHER,
			org.bukkit.loot.LootTables.VILLAGE_FLETCHER,
			org.bukkit.loot.LootTables.VILLAGE_MASON,
			org.bukkit.loot.LootTables.VILLAGE_PLAINS_HOUSE,
			org.bukkit.loot.LootTables.VILLAGE_SAVANNA_HOUSE,
			org.bukkit.loot.LootTables.VILLAGE_SHEPHERD,
			org.bukkit.loot.LootTables.VILLAGE_SNOWY_HOUSE,
			org.bukkit.loot.LootTables.VILLAGE_TAIGA_HOUSE,
			org.bukkit.loot.LootTables.VILLAGE_TANNERY,
			org.bukkit.loot.LootTables.VILLAGE_TEMPLE,
			org.bukkit.loot.LootTables.VILLAGE_TOOLSMITH,
			org.bukkit.loot.LootTables.VILLAGE_WEAPONSMITH,
			org.bukkit.loot.LootTables.WOODLAND_MANSION,
			org.bukkit.loot.LootTables.ARMOR_STAND,
			org.bukkit.loot.LootTables.BAT,
			org.bukkit.loot.LootTables.BEE,
			org.bukkit.loot.LootTables.BLAZE,
			org.bukkit.loot.LootTables.CAT,
			org.bukkit.loot.LootTables.CAVE_SPIDER,
			org.bukkit.loot.LootTables.CHICKEN,
			org.bukkit.loot.LootTables.COD,
			org.bukkit.loot.LootTables.COW,
			org.bukkit.loot.LootTables.CREEPER,
			org.bukkit.loot.LootTables.DOLPHIN,
			org.bukkit.loot.LootTables.DONKEY,
			org.bukkit.loot.LootTables.DROWNED,
			org.bukkit.loot.LootTables.ELDER_GUARDIAN,
			org.bukkit.loot.LootTables.ENDERMAN,
			org.bukkit.loot.LootTables.ENDERMITE,
			org.bukkit.loot.LootTables.ENDER_DRAGON,
			org.bukkit.loot.LootTables.EVOKER,
			org.bukkit.loot.LootTables.FOX,
			org.bukkit.loot.LootTables.GHAST,
			org.bukkit.loot.LootTables.GIANT,
			org.bukkit.loot.LootTables.GUARDIAN,
			org.bukkit.loot.LootTables.HOGLIN,
			org.bukkit.loot.LootTables.HORSE,
			org.bukkit.loot.LootTables.HUSK,
			org.bukkit.loot.LootTables.ILLUSIONER,
			org.bukkit.loot.LootTables.IRON_GOLEM,
			org.bukkit.loot.LootTables.LLAMA,
			org.bukkit.loot.LootTables.MAGMA_CUBE,
			org.bukkit.loot.LootTables.MOOSHROOM,
			org.bukkit.loot.LootTables.MULE,
			org.bukkit.loot.LootTables.OCELOT,
			org.bukkit.loot.LootTables.PANDA,
			org.bukkit.loot.LootTables.PARROT,
			org.bukkit.loot.LootTables.PHANTOM,
			org.bukkit.loot.LootTables.PIG,
			org.bukkit.loot.LootTables.PIGLIN,
			org.bukkit.loot.LootTables.PIGLIN_BRUTE,
			org.bukkit.loot.LootTables.PILLAGER,
			org.bukkit.loot.LootTables.PLAYER,
			org.bukkit.loot.LootTables.POLAR_BEAR,
			org.bukkit.loot.LootTables.PUFFERFISH,
			org.bukkit.loot.LootTables.RABBIT,
			org.bukkit.loot.LootTables.RAVAGER,
			org.bukkit.loot.LootTables.SALMON,
			org.bukkit.loot.LootTables.SHULKER,
			org.bukkit.loot.LootTables.SILVERFISH,
			org.bukkit.loot.LootTables.SKELETON,
			org.bukkit.loot.LootTables.SKELETON_HORSE,
			org.bukkit.loot.LootTables.SLIME,
			org.bukkit.loot.LootTables.SNOW_GOLEM,
			org.bukkit.loot.LootTables.SPIDER,
			org.bukkit.loot.LootTables.SQUID,
			org.bukkit.loot.LootTables.STRAY,
			org.bukkit.loot.LootTables.STRIDER,
			org.bukkit.loot.LootTables.TRADER_LLAMA,
			org.bukkit.loot.LootTables.TROPICAL_FISH,
			org.bukkit.loot.LootTables.TURTLE,
			org.bukkit.loot.LootTables.VEX,
			org.bukkit.loot.LootTables.VILLAGER,
			org.bukkit.loot.LootTables.VINDICATOR,
			org.bukkit.loot.LootTables.WANDERING_TRADER,
			org.bukkit.loot.LootTables.WITCH,
			org.bukkit.loot.LootTables.WITHER,
			org.bukkit.loot.LootTables.WITHER_SKELETON,
			org.bukkit.loot.LootTables.WOLF,
			org.bukkit.loot.LootTables.ZOGLIN,
			org.bukkit.loot.LootTables.ZOMBIE,
			org.bukkit.loot.LootTables.ZOMBIE_HORSE,
			org.bukkit.loot.LootTables.ZOMBIE_VILLAGER,
			org.bukkit.loot.LootTables.ZOMBIFIED_PIGLIN,
			org.bukkit.loot.LootTables.ARMORER_GIFT,
			org.bukkit.loot.LootTables.BUTCHER_GIFT,
			org.bukkit.loot.LootTables.CARTOGRAPHER_GIFT,
			org.bukkit.loot.LootTables.CAT_MORNING_GIFT,
			org.bukkit.loot.LootTables.CLERIC_GIFT,
			org.bukkit.loot.LootTables.FARMER_GIFT,
			org.bukkit.loot.LootTables.FISHERMAN_GIFT,
			org.bukkit.loot.LootTables.FISHING,
			org.bukkit.loot.LootTables.FISHING_FISH,
			org.bukkit.loot.LootTables.FISHING_JUNK,
			org.bukkit.loot.LootTables.FISHING_TREASURE,
			org.bukkit.loot.LootTables.FLETCHER_GIFT,
			org.bukkit.loot.LootTables.LEATHERWORKER_GIFT,
			org.bukkit.loot.LootTables.LIBRARIAN_GIFT,
			org.bukkit.loot.LootTables.MASON_GIFT,
			org.bukkit.loot.LootTables.SHEPHERD_GIFT,
			org.bukkit.loot.LootTables.TOOLSMITH_GIFT,
			org.bukkit.loot.LootTables.WEAPONSMITH_GIFT,
			org.bukkit.loot.LootTables.PIGLIN_BARTERING,
			org.bukkit.loot.LootTables.SHEEP,
			org.bukkit.loot.LootTables.SHEEP_BLACK,
			org.bukkit.loot.LootTables.SHEEP_BLUE,
			org.bukkit.loot.LootTables.SHEEP_BROWN,
			org.bukkit.loot.LootTables.SHEEP_CYAN,
			org.bukkit.loot.LootTables.SHEEP_GRAY,
			org.bukkit.loot.LootTables.SHEEP_GREEN,
			org.bukkit.loot.LootTables.SHEEP_LIGHT_BLUE,
			org.bukkit.loot.LootTables.SHEEP_LIGHT_GRAY,
			org.bukkit.loot.LootTables.SHEEP_LIME,
			org.bukkit.loot.LootTables.SHEEP_MAGENTA,
			org.bukkit.loot.LootTables.SHEEP_ORANGE,
			org.bukkit.loot.LootTables.SHEEP_PINK,
			org.bukkit.loot.LootTables.SHEEP_PURPLE,
			org.bukkit.loot.LootTables.SHEEP_RED,
			org.bukkit.loot.LootTables.SHEEP_WHITE,
			org.bukkit.loot.LootTables.SHEEP_YELLOW
		};
	}

	public static String getNMSPotionEffectName_1_16_5(PotionEffectType potionEffectType) {
		return MobEffectList.fromId(potionEffectType.getId()).c().replace("effect.minecraft.", "minecraft:");
	}

	MinecraftServer minecraftServerMock = null;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getMinecraftServer() {
		if (minecraftServerMock != null) {
			return (T) minecraftServerMock;
		}
		minecraftServerMock = Mockito.mock(MinecraftServer.class);

		// LootTableArgument
		Mockito.when(minecraftServerMock.getLootTableRegistry()).thenAnswer(invocation -> {
			LootTableRegistry lootTables = Mockito.mock(LootTableRegistry.class);
			Mockito.when(lootTables.getLootTable(any(MinecraftKey.class))).thenAnswer(i -> {
				if (LootTables.a().contains(i.getArgument(0))) {
					return net.minecraft.server.v1_16_R3.LootTable.EMPTY;
				} else {
					return null;
				}
			});
			Mockito.when(lootTables.a()).thenAnswer(i -> {
				return Streams
					.concat(
						Arrays.stream(getEntityTypes())
							.filter(e -> !e.equals(EntityType.UNKNOWN))
							.filter(e -> e.isAlive())
							.map(EntityType::getKey)
							.map(k -> new MinecraftKey("minecraft", "entities/" + k.getKey())),
						LootTables.a().stream())
					.collect(Collectors.toSet());
			});
			return lootTables;
		});

		// AdvancementArgument
		Mockito.when(minecraftServerMock.getAdvancementData()).thenReturn(advancementDataWorld);

		// TeamArgument
		ScoreboardServer scoreboardServerMock = Mockito.mock(ScoreboardServer.class);
		Mockito.when(scoreboardServerMock.getTeam(anyString())).thenAnswer(invocation -> { // Scoreboard#getTeam is used for 1.16.5 instead of Scoreboard#getPlayerTeam
			String teamName = invocation.getArgument(0);
			Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
			if (team == null) {
				return null;
			} else {
				return new ScoreboardTeam(scoreboardServerMock, teamName);
			}
		});
		Mockito.when(minecraftServerMock.getScoreboard()).thenReturn(scoreboardServerMock); // MinecraftServer#getScoreboard

		// WorldArgument (Dimension)
		Mockito.when(minecraftServerMock.getWorldServer(any(ResourceKey.class))).thenAnswer(invocation -> {
			// Get the ResourceKey<World> and extract the world name from it
			ResourceKey<net.minecraft.server.v1_16_R3.World> resourceKey = invocation.getArgument(0);
			String worldName = resourceKey.a().getKey();

			// Get the world via Bukkit (returns a WorldMock) and create a
			// CraftWorld clone of it for WorldServer.getWorld()
			World world = Bukkit.getServer().getWorld(worldName);
			if (world == null) {
				return null;
			} else {
				CraftWorld craftWorldMock = Mockito.mock(CraftWorld.class);
				Mockito.when(craftWorldMock.getName()).thenReturn(world.getName());
				Mockito.when(craftWorldMock.getUID()).thenReturn(world.getUID());

				// Create our return WorldServer object
				WorldServer bukkitWorldServerMock = Mockito.mock(WorldServer.class);
				Mockito.when(bukkitWorldServerMock.getWorld()).thenReturn(craftWorldMock);
				return bukkitWorldServerMock;
			}
		});

		Mockito.when(minecraftServerMock.getPlayerList()).thenAnswer(i -> playerListMock);
		Mockito.when(minecraftServerMock.getPlayerList().getPlayers()).thenAnswer(i -> players);

		// Player argument
		UserCache userCacheMock = Mockito.mock(UserCache.class);
		Mockito.when(userCacheMock.getProfile(anyString())).thenAnswer(invocation -> {
			String playerName = invocation.getArgument(0);
			for (EntityPlayer onlinePlayer : players) {
				if (onlinePlayer.getBukkitEntity().getName().equals(playerName)) {
					return new GameProfile(onlinePlayer.getBukkitEntity().getUniqueId(), playerName);
				}
			}
			return null;
		});
		Mockito.when(minecraftServerMock.getUserCache()).thenReturn(userCacheMock);

		return (T) minecraftServerMock;
	}

	static AdvancementDataWorld advancementDataWorld = new AdvancementDataWorld(null);

	public org.bukkit.advancement.Advancement addAdvancement(NamespacedKey key) {
		advancementDataWorld.REGISTRY.advancements.put(new MinecraftKey(key.toString()),
			new Advancement(new MinecraftKey(key.toString()), null, null, null, new HashMap<>(), null));
		return new org.bukkit.advancement.Advancement() {

			@Override
			public NamespacedKey getKey() {
				return key;
			}

			@Override
			public Collection<String> getCriteria() {
				return List.of();
			}
		};
	}

}
