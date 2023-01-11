package dev.jorel.commandapi.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.enchantments.EnchantmentsMock;
import be.seeseemelk.mockbukkit.potion.MockPotionEffectType;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.ParticleData;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument.Anchor;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.commands.arguments.TemplateMirrorArgument;
import net.minecraft.commands.arguments.TemplateRotationArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.DoubleArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.FloatArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.IntegerArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.LongArgumentInfo;
import net.minecraft.commands.synchronization.brigadier.StringArgumentSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

public class MockNMS extends ArgumentNMS {

	public MockNMS(NMS<?> baseNMS) {
		super(baseNMS);
		try {
			initializeArgumentsInArgumentTypeInfos();

			// Initialize WorldVersion (game version)
			SharedConstants.tryDetectVersion();

			// MockBukkit is very helpful and registers all of the potion
			// effects and enchantments for us. We need to not do this (because
			// we call DispenserRegistry.a() below which does the same thing)
			unregisterAllEnchantments();
			unregisterAllPotionEffects();

			// Invoke Minecraft's registry (I think that's what this does anyway)
			Bootstrap.bootStrap();
			
			// Sometimes, and I have no idea why, DispenserRegistry.a() only works
			// on the very first test in the test suite. After that, everything else
			// doesn't work. At this point, we'll use the ServerMock#createPotionEffectTypes
			// method (which unfortunately is private and pure, so instead of using reflection
			// we'll just implement it right here instead)
			@SuppressWarnings("unchecked")
			Map<NamespacedKey, PotionEffectType> byKey = (Map<NamespacedKey, PotionEffectType>) getField(PotionEffectType.class, "byKey", null);
			if(byKey.isEmpty()) {
				createPotionEffectTypes();
			}
			EnchantmentsMock.registerDefaultEnchantments();
//			System.out.println(byKey);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}
	
	private static void registerPotionEffectType(int id, @NotNull String name, boolean instant, int rgb) {
		NamespacedKey key = NamespacedKey.minecraft(name.toLowerCase(Locale.ROOT));
		PotionEffectType type = new MockPotionEffectType(key, id, name, instant, Color.fromRGB(rgb));
		PotionEffectType.registerPotionEffectType(type);
	}
	
	// ItemStackArgument requirements
	public static ItemFactory getItemFactory() {
		return CraftItemFactory.instance();
	}

	/**
	 * @return A list of all item names, sorted in alphabetical order. Each item
	 * is prefixed with {@code minecraft:}
	 */
	public static List<String> getAllItemNames() {
		// Registry.ITEM
		return StreamSupport.stream(Registry.ITEM.spliterator(), false)
			.map(Object::toString)
			.map(s -> "minecraft:" + s)
			.sorted()
			.toList();
	}
	
	/**
	 * This registers Minecraft's default {@link PotionEffectType PotionEffectTypes}. It also prevents any new effects to
	 * be created afterwards.
	 */
	public static void createPotionEffectTypes() {
		for (PotionEffectType type : PotionEffectType.values()) {
			// We probably already registered all Potion Effects
			// otherwise this would be null
			if (type != null) {
				// This is not perfect, but it works.
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
		registerPotionEffectType(33, "DARKNESS", false, 2696993);
		PotionEffectType.stopAcceptingRegistrations();
	}

	@SuppressWarnings("unchecked")
	public static void unregisterAllPotionEffects() {
		PotionEffectType[] byId = (PotionEffectType[]) getField(PotionEffectType.class, "byId", null);
		for (int i = 0; i < 34; i++) {
			byId[i] = null;
		}

		Map<String, PotionEffectType> byName = (Map<String, PotionEffectType>) getField(PotionEffectType.class, "byName", null);
		byName.clear();

		Map<NamespacedKey, PotionEffectType> byKey = (Map<NamespacedKey, PotionEffectType>) getField(PotionEffectType.class, "byKey", null);
		byKey.clear();

		try {
			Field field = PotionEffectType.class.getDeclaredField("acceptingNew");
			field.setAccessible(true);
			field.set(null, true);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void unregisterAllEnchantments() {

		Map<String, Enchantment> byName = (Map<String, Enchantment>) getField(Enchantment.class, "byName", null);
		byName.clear();

		Map<NamespacedKey, Enchantment> byKey = (Map<NamespacedKey, Enchantment>) getField(Enchantment.class, "byKey", null);
		byKey.clear();

		try {
			Field field = Enchantment.class.getDeclaredField("acceptingNew");
			field.setAccessible(true);
			field.set(null, true);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.19.2" };
	}

	CommandDispatcher<CommandSourceStack> dispatcher;

	@Override
	public CommandDispatcher<CommandSourceStack> getBrigadierDispatcher() {
		if (this.dispatcher == null) {
			this.dispatcher = new CommandDispatcher<>();
		}
		return this.dispatcher;
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((ServerMock) Bukkit.getServer()).getCommandMap();
	}

	List<ServerPlayer> players = new ArrayList<>();
	PlayerList playerListMock;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public CommandSourceStack getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> senderWrapper) {
		CommandSender sender = senderWrapper.getSource();
		CommandSourceStack css = Mockito.mock(CommandSourceStack.class);
		Mockito.when(css.getBukkitSender()).thenReturn(sender);

		if (sender instanceof Player player) {
			// Location argument
			Location loc = player.getLocation();
			Mockito.when(css.getPosition()).thenReturn(new Vec3(loc.getX(), loc.getY(), loc.getZ()));
			
			ServerLevel worldServerMock = Mockito.mock(ServerLevel.class);
			Mockito.when(css.getLevel()).thenReturn(worldServerMock);
			Mockito.when(css.getLevel().hasChunkAt(any(BlockPos.class))).thenReturn(true);
			Mockito.when(css.getLevel().isInWorldBounds(any(BlockPos.class))).thenReturn(true);
			Mockito.when(css.getAnchor()).thenReturn(Anchor.EYES);

			// Advancement argument
			MinecraftServer minecraftServerMock = Mockito.mock(MinecraftServer.class);
			Mockito.when(minecraftServerMock.getAdvancements()).thenReturn(mockAdvancementDataWorld());
			Mockito.when(css.getServer()).thenReturn(minecraftServerMock);

			// Entity selector argument
			for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				ServerPlayer entityPlayerMock = Mockito.mock(ServerPlayer.class);
				CraftPlayer craftPlayerMock = Mockito.mock(CraftPlayer.class);
				Mockito.when(craftPlayerMock.getName()).thenReturn(onlinePlayer.getName());
				Mockito.when(craftPlayerMock.getUniqueId()).thenReturn(onlinePlayer.getUniqueId());
				Mockito.when(entityPlayerMock.getBukkitEntity()).thenReturn(craftPlayerMock);
				players.add(entityPlayerMock);
			}
			
			if(playerListMock == null) {
				playerListMock = Mockito.mock(PlayerList.class);
				Mockito.when(playerListMock.getPlayerByName(anyString())).thenAnswer(invocation -> {
					String playerName = invocation.getArgument(0);
					for(ServerPlayer onlinePlayer : players) {
						if(onlinePlayer.getBukkitEntity().getName().equals(playerName)) {
							return onlinePlayer;
						}
					}
					return null;
				});
			}
			
			Mockito.when(minecraftServerMock.getPlayerList()).thenReturn(playerListMock);
			Mockito.when(minecraftServerMock.getPlayerList().getPlayers()).thenReturn(players);
			
			// Player argument
			GameProfileCache userCacheMock = Mockito.mock(GameProfileCache.class);
			Mockito.when(userCacheMock.get(anyString())).thenAnswer(invocation -> {
				String playerName = invocation.getArgument(0);
				for(ServerPlayer onlinePlayer : players) {
					if(onlinePlayer.getBukkitEntity().getName().equals(playerName)) {
						return Optional.of(new GameProfile(onlinePlayer.getBukkitEntity().getUniqueId(), playerName));
					}
				}
				return Optional.empty();
			});
			Mockito.when(minecraftServerMock.getProfileCache()).thenReturn(userCacheMock);
			
			// World (Dimension) argument
			Mockito.when(minecraftServerMock.getLevel(any(ResourceKey.class))).thenAnswer(invocation -> {
				// Get the ResourceKey<World> and extract the world name from it
				ResourceKey<Level> resourceKey = invocation.getArgument(0);
				String worldName = resourceKey.location().getPath();
				
				// Get the world via Bukkit (returns a WorldMock) and create a
				// CraftWorld clone of it for WorldServer.getWorld()
				World world = Bukkit.getServer().getWorld(worldName);
				if(world == null) {
					return null;
				} else {
					CraftWorld craftWorldMock = Mockito.mock(CraftWorld.class);
					Mockito.when(craftWorldMock.getName()).thenReturn(world.getName());
					Mockito.when(craftWorldMock.getUID()).thenReturn(world.getUID());
					
					// Create our return WorldServer object
					ServerLevel bukkitWorldServerMock = Mockito.mock(ServerLevel.class);
					Mockito.when(bukkitWorldServerMock.getWorld()).thenReturn(craftWorldMock);
					return bukkitWorldServerMock;
				}
			});
			
			Mockito.when(css.levels()).thenAnswer(invocation -> {
				Set<ResourceKey<Level>> set = new HashSet<>();
				// We only need to implement resourceKey.a()
				
				for(World world : Bukkit.getWorlds()) {
					ResourceKey<Level> key = Mockito.mock(ResourceKey.class);
					Mockito.when(key.location()).thenReturn(new ResourceLocation(world.getName()));
					set.add(key);
				}
				
				return set;
			});
			
			// Rotation argument
			Mockito.when(css.getRotation()).thenReturn(new Vec2(loc.getYaw(), loc.getPitch()));
			
			// Team argument
			ServerScoreboard scoreboardServerMock = Mockito.mock(ServerScoreboard.class);
			Mockito.when(scoreboardServerMock.getPlayerTeam(anyString())).thenAnswer(invocation -> { // Scoreboard#getPlayerTeam
				String teamName = invocation.getArgument(0);
				Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
				if (team == null) {
					return null;
				} else {
					return new PlayerTeam(scoreboardServerMock, teamName);
				}
			});
			Mockito.when(minecraftServerMock.getScoreboard()).thenReturn(scoreboardServerMock); // MinecraftServer#getScoreboard
			
			Mockito.when(css.getAllTeams()).thenAnswer(invocation -> { // CommandSourceStack#getAllTeams
				return Bukkit.getScoreboardManager().getMainScoreboard().getTeams().stream().map(Team::getName).toList();
			});
		}
		return css;
	}

	public ServerAdvancementManager mockAdvancementDataWorld() {
		ServerAdvancementManager advancementDataWorld = new ServerAdvancementManager(null);
		AdvancementList advancements = advancementDataWorld.advancements;
		// Advancement advancements = (Advancement) getField(ServerAdvancementManager.class, "c", advancementDataWorld);

		advancements.advancements.put(new ResourceLocation("my:advancement"), new Advancement(new ResourceLocation("my:advancement"), null, null, null, new HashMap<>(), null));
		advancements.advancements.put(new ResourceLocation("my:advancement2"), new Advancement(new ResourceLocation("my:advancement2"), null, null, null, new HashMap<>(), null));
		return advancementDataWorld;
	}

	public static Object getField(Class<?> className, String fieldName, Object instance) {
		try {
			Field field = className.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T getFieldAs(Class<?> className, String fieldName, Object instance, Class<T> asType) {
		try {
			Field field = className.getDeclaredField(fieldName);
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch (ReflectiveOperationException e) {
			return null;
		}
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(CommandSourceStack clw) {
		try {
			return wrapCommandSender(clw.getBukkitSender());
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<CommandSourceStack> cmdCtx, boolean forceNative) {
		return getCommandSenderFromCommandSource(cmdCtx.getSource());
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandSourceStack> dispatcher)
			throws IOException {
		Files
			.asCharSink(file, StandardCharsets.UTF_8)
			.write(new GsonBuilder()
				.setPrettyPrinting()
				.create()
				.toJson(DispatcherUtil.toJSON(dispatcher, dispatcher.getRoot())));
	}

	@SuppressWarnings("rawtypes")
	private void initializeArgumentsInArgumentTypeInfos() throws ReflectiveOperationException {
		Field f = ArgumentTypeInfos.class.getDeclaredField("a");
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<Class<?>, ArgumentTypeInfo<?, ?>> map = (Map<Class<?>, ArgumentTypeInfo<?, ?>>) f.get(null);
		map.put(BoolArgumentType.class, SingletonArgumentInfo.contextFree(BoolArgumentType::bool));
		map.put(FloatArgumentType.class, new FloatArgumentInfo());
		map.put(DoubleArgumentType.class, new DoubleArgumentInfo());
		map.put(IntegerArgumentType.class, new IntegerArgumentInfo());
		map.put(LongArgumentType.class, new LongArgumentInfo());
		map.put(StringArgumentType.class, new StringArgumentSerializer());
		map.put(EntityArgument.class, new EntityArgument.Info());
		map.put(GameProfileArgument.class, SingletonArgumentInfo.contextFree(GameProfileArgument::gameProfile));
		map.put(BlockPosArgument.class, SingletonArgumentInfo.contextFree(BlockPosArgument::blockPos));
		map.put(ColumnPosArgument.class, SingletonArgumentInfo.contextFree(ColumnPosArgument::columnPos));
		map.put(Vec3Argument.class, SingletonArgumentInfo.contextFree(Vec3Argument::vec3));
		map.put(Vec2Argument.class, SingletonArgumentInfo.contextFree(Vec2Argument::vec2));
		map.put(BlockStateArgument.class, SingletonArgumentInfo.contextAware(BlockStateArgument::block));
		map.put(BlockPredicateArgument.class, SingletonArgumentInfo.contextAware(BlockPredicateArgument::blockPredicate));
		map.put(ItemArgument.class, SingletonArgumentInfo.contextAware(ItemArgument::item));
		map.put(ItemPredicateArgument.class, SingletonArgumentInfo.contextAware(ItemPredicateArgument::itemPredicate));
		map.put(ColorArgument.class, SingletonArgumentInfo.contextFree(ColorArgument::color));
		map.put(ComponentArgument.class, SingletonArgumentInfo.contextFree(ComponentArgument::textComponent));
		map.put(MessageArgument.class, SingletonArgumentInfo.contextFree(MessageArgument::message));
		map.put(CompoundTagArgument.class, SingletonArgumentInfo.contextFree(CompoundTagArgument::compoundTag));
		map.put(NbtTagArgument.class, SingletonArgumentInfo.contextFree(NbtTagArgument::nbtTag));
		map.put(NbtPathArgument.class, SingletonArgumentInfo.contextFree(NbtPathArgument::nbtPath));
		map.put(ObjectiveArgument.class, SingletonArgumentInfo.contextFree(ObjectiveArgument::objective));
		map.put(ObjectiveCriteriaArgument.class, SingletonArgumentInfo.contextFree(ObjectiveCriteriaArgument::criteria));
		map.put(OperationArgument.class, SingletonArgumentInfo.contextFree(OperationArgument::operation));
		map.put(ParticleArgument.class, SingletonArgumentInfo.contextFree(ParticleArgument::particle));
		map.put(AngleArgument.class, SingletonArgumentInfo.contextFree(AngleArgument::angle));
		map.put(RotationArgument.class, SingletonArgumentInfo.contextFree(RotationArgument::rotation));
		map.put(ScoreboardSlotArgument.class, SingletonArgumentInfo.contextFree(ScoreboardSlotArgument::displaySlot));
		map.put(ScoreHolderArgument.class, new ScoreHolderArgument.Info());
		map.put(SwizzleArgument.class, SingletonArgumentInfo.contextFree(SwizzleArgument::swizzle));
		map.put(TeamArgument.class, SingletonArgumentInfo.contextFree(TeamArgument::team));
		map.put(SlotArgument.class, SingletonArgumentInfo.contextFree(SlotArgument::slot));
		map.put(ResourceLocationArgument.class, SingletonArgumentInfo.contextFree(ResourceLocationArgument::id));
		map.put(MobEffectArgument.class, SingletonArgumentInfo.contextFree(MobEffectArgument::effect));
		map.put(FunctionArgument.class, SingletonArgumentInfo.contextFree(FunctionArgument::functions));
		map.put(EntityAnchorArgument.class, SingletonArgumentInfo.contextFree(EntityAnchorArgument::anchor));
		map.put(RangeArgument.Ints.class, SingletonArgumentInfo.contextFree(RangeArgument::intRange));
		map.put(RangeArgument.Floats.class, SingletonArgumentInfo.contextFree(RangeArgument::floatRange));
		map.put(ItemEnchantmentArgument.class, SingletonArgumentInfo.contextFree(ItemEnchantmentArgument::enchantment));
		map.put(EntitySummonArgument.class, SingletonArgumentInfo.contextFree(EntitySummonArgument::id));
		map.put(DimensionArgument.class, SingletonArgumentInfo.contextFree(DimensionArgument::dimension));
		map.put(TimeArgument.class, SingletonArgumentInfo.contextFree(TimeArgument::time));
		map.put(ResourceOrTagLocationArgument.class, new ResourceOrTagLocationArgument.Info());
		map.put(ResourceKeyArgument.class, new ResourceKeyArgument.Info());
		map.put(TemplateMirrorArgument.class, SingletonArgumentInfo.contextFree(TemplateMirrorArgument::templateMirror));
		map.put(TemplateRotationArgument.class, SingletonArgumentInfo.contextFree(TemplateRotationArgument::templateRotation));
		map.put(UuidArgument.class, SingletonArgumentInfo.contextFree(UuidArgument::uuid));
	}

	@Override
	public World getWorldForCSS(CommandSourceStack clw) {
		return new WorldMock();
	}
	
	@Override
	public void resendPackets(Player player) {
		// There's nothing to do here, we can't "send packets to players"
		return;
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(ItemStack is) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(ParticleData<?> particle) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(PotionEffectType potion) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public String convert(Sound sound) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}

	@Override
	public void reloadDataPacks() {
		new RuntimeException("unimplemented").printStackTrace();
		throw new RuntimeException("unimplemented");
	}
	
	/**
	 * An implementation of {@link ArgumentUtils} which produces JSON from a command
	 * dispatcher and its root node. We have to avoid accessing IRegistry because it
	 * isn't mock-able and cannot be instantiated through normal means
	 */
	private static class DispatcherUtil {

		static Map<Class<?>, String> argumentParsers = new HashMap<>();

		static {
			argumentParsers.put(BoolArgumentType.class, "brigadier:bool");
			argumentParsers.put(FloatArgumentType.class, "brigadier:float");
			argumentParsers.put(DoubleArgumentType.class, "brigadier:double");
			argumentParsers.put(IntegerArgumentType.class, "brigadier:integer");
			argumentParsers.put(LongArgumentType.class, "brigadier:long");
			argumentParsers.put(StringArgumentType.class, "brigadier:string");
			argumentParsers.put(EntityArgument.class, "entity");
			argumentParsers.put(GameProfileArgument.class, "game_profile");
			argumentParsers.put(BlockPosArgument.class, "block_pos");
			argumentParsers.put(ColumnPosArgument.class, "column_pos");
			argumentParsers.put(Vec3Argument.class, "vec3");
			argumentParsers.put(Vec2Argument.class, "vec2");
			argumentParsers.put(BlockStateArgument.class, "block_state");
			argumentParsers.put(BlockPredicateArgument.class, "block_predicate");
			argumentParsers.put(ItemArgument.class, "item_stack");
			argumentParsers.put(ItemPredicateArgument.class, "item_predicate");
			argumentParsers.put(ColorArgument.class, "color");
			argumentParsers.put(ComponentArgument.class, "component");
			argumentParsers.put(MessageArgument.class, "message");
			argumentParsers.put(CompoundTagArgument.class, "nbt_compound_tag");
			argumentParsers.put(NbtTagArgument.class, "nbt_tag");
			argumentParsers.put(NbtPathArgument.class, "nbt_path");
			argumentParsers.put(ObjectiveArgument.class, "objective");
			argumentParsers.put(ObjectiveCriteriaArgument.class, "objective_criteria");
			argumentParsers.put(OperationArgument.class, "operation");
			argumentParsers.put(ParticleArgument.class, "particle");
			argumentParsers.put(AngleArgument.class, "angle");
			argumentParsers.put(RotationArgument.class, "rotation");
			argumentParsers.put(ScoreboardSlotArgument.class, "scoreboard_slot");
			argumentParsers.put(ScoreHolderArgument.class, "score_holder");
			argumentParsers.put(SwizzleArgument.class, "swizzle");
			argumentParsers.put(TeamArgument.class, "team");
			argumentParsers.put(SlotArgument.class, "item_slot");
			argumentParsers.put(ResourceLocationArgument.class, "resource_location");
			argumentParsers.put(MobEffectArgument.class, "mob_effect");
			argumentParsers.put(FunctionArgument.class, "function");
			argumentParsers.put(EntityAnchorArgument.class, "entity_anchor");
			argumentParsers.put(RangeArgument.Ints.class, "int_range");
			argumentParsers.put(RangeArgument.Floats.class, "float_range");
			argumentParsers.put(ItemEnchantmentArgument.class, "item_enchantment");
			argumentParsers.put(EntitySummonArgument.class, "entity_summon");
			argumentParsers.put(DimensionArgument.class, "dimension");
			argumentParsers.put(TimeArgument.class, "time");
			argumentParsers.put(ResourceOrTagLocationArgument.class, "resource_or_tag");
			argumentParsers.put(ResourceKeyArgument.class, "resource");
			argumentParsers.put(TemplateMirrorArgument.class, "template_mirror");
			argumentParsers.put(TemplateRotationArgument.class, "template_rotation");
			argumentParsers.put(UuidArgument.class, "uuid");
		}

		public static <S> JsonObject toJSON(CommandDispatcher<S> dispatcher, CommandNode<S> node) {
			JsonObject jsonObject = new JsonObject();

			// Unpack nodes
			if (node instanceof RootCommandNode) {
				jsonObject.addProperty("type", "root");
			} else if (node instanceof LiteralCommandNode) {
				jsonObject.addProperty("type", "literal");
			} else if (node instanceof ArgumentCommandNode) {
				ArgumentCommandNode<?, ?> argumentCommandNode = (ArgumentCommandNode<?, ?>) node;
				argToJSON(jsonObject, argumentCommandNode.getType());
			} else {
				jsonObject.addProperty("type", "unknown");
			}

			// Write children
			JsonObject children = new JsonObject();
			for (CommandNode<S> child : node.getChildren()) {
				children.add(child.getName(), (JsonElement) toJSON(dispatcher, child));
			}
			if (children.size() > 0) {
				jsonObject.add("children", (JsonElement) children);
			}

			// Write whether the command is executable
			if (node.getCommand() != null) {
				jsonObject.addProperty("executable", Boolean.valueOf(true));
			}
			if (node.getRedirect() != null) {
				Collection<String> redirectPaths = dispatcher.getPath(node.getRedirect());
				if (!redirectPaths.isEmpty()) {
					JsonArray redirects = new JsonArray();
					for (String redirectPath : redirectPaths) {
						redirects.add(redirectPath);
					}
					jsonObject.add("redirect", (JsonElement) redirects);
				}
			}
			return jsonObject;
		}

		@SuppressWarnings("unchecked")
		private static <T extends ArgumentType<?>> void argToJSON(JsonObject jsonObject, T argument) {
			ArgumentTypeInfo.Template<T> argumentInfo = ArgumentTypeInfos.unpack(argument);
			jsonObject.addProperty("type", "argument");
			jsonObject.addProperty("parser", argumentParsers.get(argument.getClass()));
			
			// Properties
			JsonObject properties = new JsonObject();
			@SuppressWarnings("rawtypes")
			ArgumentTypeInfo argumentTypeInfo = argumentInfo.type();
			argumentTypeInfo.serializeToJson(argumentInfo, properties);
			if (properties.size() > 0) {
				jsonObject.add("properties", (JsonElement) properties);
			}
		}

	}


}
