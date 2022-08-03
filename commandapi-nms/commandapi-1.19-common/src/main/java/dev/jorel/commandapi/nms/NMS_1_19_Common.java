/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.nms;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.DustTransition;
import org.bukkit.Sound;
import org.bukkit.Vibration;
import org.bukkit.Vibration.Destination;
import org.bukkit.Vibration.Destination.BlockDestination;
import org.bukkit.Vibration.Destination.EntityDestination;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_19_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_19_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_19_R1.CraftServer;
import org.bukkit.craftbukkit.v1_19_R1.CraftSound;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_19_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.help.CustomHelpTopic;
import org.bukkit.craftbukkit.v1_19_R1.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import io.netty.channel.Channel;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandFunction.Entry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument.Result;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryAccess.Frozen;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.MinecraftServer.ReloadableResources;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.SimpleReloadInstance;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

// Mojang-Mapped reflection
/**
 * NMS implementation for Minecraft 1.19 and 1.19.1
 */
@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
@RequireField(in = EntitySelector.class, name = "usesSelector", ofType = boolean.class)
@RequireField(in = EntityPositionSource.class, name = "entityOrUuidOrId", ofType = Either.class)
public abstract class NMS_1_19_Common extends NMS_Common {

	private static final MinecraftServer MINECRAFT_SERVER;
	private static final VarHandle SimpleHelpMap_helpTopics;
	private static final VarHandle EntityPositionSource_sourceEntity;

	// From net.minecraft.server.commands.LocateCommand
	private static final DynamicCommandExceptionType ERROR_BIOME_INVALID;

	// Derived from net.minecraft.commands.Commands;
	private static final CommandBuildContext COMMAND_BUILD_CONTEXT;

	// Compute all var handles all in one go so we don't do this during main server
	// runtime
	static {
		if (Bukkit.getServer() instanceof CraftServer server) {
			MINECRAFT_SERVER = server.getServer();
			// Construct the command build context to use for all commands - I presume this
			// lets you decide what happens when a tag isn't present by specifying the
			// MissingTagAccessPolicy. This policy has three options: RETURN_EMPTY,
			// CREATE_NEW or FAIL. We'll go with RETURN_EMPTY for now. Whether we decide to
			// add support for letting developers fine-tune their missing tag access policy
			// is something to decide at a later date.
			COMMAND_BUILD_CONTEXT = new CommandBuildContext(RegistryAccess.BUILTIN.get());
			COMMAND_BUILD_CONTEXT.missingTagAccessPolicy(CommandBuildContext.MissingTagAccessPolicy.RETURN_EMPTY);
		} else {
			MINECRAFT_SERVER = null;
			COMMAND_BUILD_CONTEXT = null;
		}

		VarHandle shm_ht = null;
		VarHandle eps_se = null;
		try {
			shm_ht = MethodHandles.privateLookupIn(SimpleHelpMap.class, MethodHandles.lookup())
				.findVarHandle(SimpleHelpMap.class, "helpTopics", Map.class);
			eps_se = MethodHandles.privateLookupIn(EntityPositionSource.class, MethodHandles.lookup())
				.findVarHandle(EntityPositionSource.class, "c", Either.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		SimpleHelpMap_helpTopics = shm_ht;
		EntityPositionSource_sourceEntity = eps_se;
		ERROR_BIOME_INVALID = new DynamicCommandExceptionType(
			arg -> net.minecraft.network.chat.Component.translatable("commands.locate.biome.invalid", arg));
	}

	private static NamespacedKey fromResourceLocation(ResourceLocation key) {
		return NamespacedKey.fromString(key.getNamespace() + ":" + key.getPath());
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockPredicate() {
		return BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT);
	}

	@Override
	public final ArgumentType<?> _ArgumentBlockState() {
		return BlockStateArgument.block(COMMAND_BUILD_CONTEXT);
	}

	@Override
	public final ArgumentType<?> _ArgumentEntity(
		dev.jorel.commandapi.arguments.EntitySelector selector) {
		return switch (selector) {
			case MANY_ENTITIES -> EntityArgument.entities();
			case MANY_PLAYERS -> EntityArgument.players();
			case ONE_ENTITY -> EntityArgument.entity();
			case ONE_PLAYER -> EntityArgument.player();
		};
	}

	@Override
	public final ArgumentType<?> _ArgumentItemPredicate() {
		return ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT);
	}

	@Override
	public final ArgumentType<?> _ArgumentItemStack() {
		return ItemArgument.item(COMMAND_BUILD_CONTEXT);
	}

	@Override
	public final ArgumentType<?> _ArgumentSyntheticBiome() {
		return ResourceOrTagLocationArgument.resourceOrTag(Registry.BIOME_REGISTRY);
	}

	@Override
	public final void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		Map<String, HelpTopic> helpTopics = (Map<String, HelpTopic>) SimpleHelpMap_helpTopics
			.get(Bukkit.getServer().getHelpMap());
		// We have to use VarHandles to use helpTopics.put (instead of .addTopic)
		// because we're updating an existing help topic, not adding a new help topic
		for (Map.Entry<String, HelpTopic> entry : helpTopicsToAdd.entrySet()) {
			helpTopics.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public final boolean canUseChatPreview() {
		return Bukkit.shouldSendChatPreviews();
	}

	@Override
	public abstract String[] compatibleVersions();

	@Override
	public final String convert(org.bukkit.inventory.ItemStack is) {
		return is.getType().getKey().toString() + CraftItemStack.asNMSCopy(is).getOrCreateTag().getAsString();
	}

	@Override
	public final String convert(ParticleData<?> particle) {
		return CraftParticle.toNMS(particle.particle(), particle.data()).writeToString();
	}

	// Converts NMS function to SimpleFunctionWrapper
	private final SimpleFunctionWrapper convertFunction(CommandFunction commandFunction) {
		ToIntFunction<CommandSourceStack> appliedObj = (CommandSourceStack css) -> MINECRAFT_SERVER.getFunctions()
			.execute(commandFunction, css);

		Entry[] cArr = commandFunction.getEntries();
		String[] result = new String[cArr.length];
		for (int i = 0, size = cArr.length; i < size; i++) {
			result[i] = cArr[i].toString();
		}
		return new SimpleFunctionWrapper(fromResourceLocation(commandFunction.getId()), appliedObj, result);
	}

	@Override
	public final void createDispatcherFile(File file, com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher)
		throws IOException {
		Files.asCharSink(file, StandardCharsets.UTF_8).write(new GsonBuilder().setPrettyPrinting().create()
			.toJson(ArgumentUtils.serializeNodeToJson(dispatcher, dispatcher.getRoot())));
	}

	@Override
	public final HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription,
		String permission) {
		return new CustomHelpTopic(commandName, shortDescription, fullDescription, permission);
	}

	@SuppressWarnings("removal")
	@Override
	public final Component getAdventureChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return PaperComponents.gsonSerializer()
			.deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Differs(from = "1.18.2", by = "Biomes now go via the registry. Also have to manually implement ERROR_BIOME_INVALID")
	@Override
	public final Biome getBiome(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		Result<net.minecraft.world.level.biome.Biome> biomeResult = ResourceOrTagLocationArgument
			.getRegistryType(cmdCtx, key, Registry.BIOME_REGISTRY, ERROR_BIOME_INVALID);
		if (biomeResult.unwrap().left().isPresent()) {
			// It's a resource key. Unwrap the result, get the resource key (left)
			// and get its location and return its path. Important information if
			// anyone ever has to maintain this very complicated code, because this
			// took about an hour to figure out:

			// For reference, unwrapping the object like this returns a ResourceKey:
			// biomeResult.unwrap().left().get()

			// This has two important functions:
			// location() and registry().

			// The location() returns a ResourceLocation with a namespace and
			// path of the biome, for example:
			// minecraft:badlands.

			// The registry() returns a ResourceLocation with a namespace and
			// path of the registry where the biome is declared in, for example:
			// minecraft:worldgen/biome.
			// This is the same registry that you'll find in registries.json and
			// in the command_registration.json

			return Biome.valueOf(biomeResult.unwrap().left().get().location().getPath().toUpperCase());
		} else {
			// This isn't a biome, tell the user this.

			// For reference, unwrapping this gives you the tag's namespace in location()
			// and a (recursive structure of the) path of the registry, for example
			// [minecraft:root / minecraft:worldgen/biome]

			// ResourceOrTagLocationArgument.ERROR_INVALID_BIOME

			throw ERROR_BIOME_INVALID.create(biomeResult.asPrintable());
		}
	}

	@Override
	public final Predicate<Block> getBlockPredicate(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		Predicate<BlockInWorld> predicate = BlockPredicateArgument.getBlockPredicate(cmdCtx, key);
		return (Block block) -> {
			return predicate.test(new BlockInWorld(cmdCtx.getSource().getLevel(),
				new BlockPos(block.getX(), block.getY(), block.getZ()), true));
		};
	}

	@Override
	public final BlockData getBlockState(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return CraftBlockData.fromData(BlockStateArgument.getBlock(cmdCtx, key).getState());
	}

	@Override
	public final com.mojang.brigadier.CommandDispatcher<CommandSourceStack> getBrigadierDispatcher() {
		return MINECRAFT_SERVER.vanillaCommandDispatcher.getDispatcher();
	}

	@Override
	public final CommandSourceStack getCLWFromCommandSender(CommandSender sender) {
		return VanillaCommandWrapper.getListener(sender);
	}

	@Override
	public final Object getEntitySelector(CommandContext<CommandSourceStack> cmdCtx, String str,
		dev.jorel.commandapi.arguments.EntitySelector selector)
		throws CommandSyntaxException {

		// We override the rule whereby players need "minecraft.command.selector" and
		// have to have
		// level 2 permissions in order to use entity selectors. We're trying to allow
		// entity selectors
		// to be used by anyone that registers a command via the CommandAPI.
		EntitySelector argument = cmdCtx.getArgument(str, EntitySelector.class);
		try {
			CommandAPIHandler.getInstance().getField(EntitySelector.class, "o").set(argument, false);
		} catch (IllegalArgumentException | IllegalAccessException e1) {
			e1.printStackTrace();
		}

		return switch (selector) {
			case MANY_ENTITIES:
				try {
					List<org.bukkit.entity.Entity> result = new ArrayList<>();
					for (Entity entity : argument.findEntities(cmdCtx.getSource())) {
						result.add(entity.getBukkitEntity());
					}
					yield result;
				} catch (CommandSyntaxException e) {
					yield new ArrayList<org.bukkit.entity.Entity>();
				}
			case MANY_PLAYERS:
				try {
					List<Player> result = new ArrayList<>();
					for (ServerPlayer player : argument.findPlayers(cmdCtx.getSource())) {
						result.add(player.getBukkitEntity());
					}
					yield result;
				} catch (CommandSyntaxException e) {
					yield new ArrayList<Player>();
				}
			case ONE_ENTITY:
				yield argument.findSingleEntity(cmdCtx.getSource()).getBukkitEntity();
			case ONE_PLAYER:
				yield argument.findSinglePlayer(cmdCtx.getSource()).getBukkitEntity();
		};
	}

	@SuppressWarnings("deprecation")
	@Override
	public final EntityType getEntityType(CommandContext<CommandSourceStack> cmdCtx, String str)
		throws CommandSyntaxException {
		return EntityType.fromName(net.minecraft.world.entity.EntityType
			.getKey(Registry.ENTITY_TYPE.get(EntitySummonArgument.getSummonableEntity(cmdCtx, str))).getPath());
	}

	@Override
	public final FunctionWrapper[] getFunction(CommandContext<CommandSourceStack> cmdCtx, String str)
		throws CommandSyntaxException {
		List<FunctionWrapper> result = new ArrayList<>();
		CommandSourceStack css = cmdCtx.getSource().withSuppressedOutput().withMaximumPermission(2);

		for (CommandFunction commandFunction : FunctionArgument.getFunctions(cmdCtx, str)) {
			result.add(FunctionWrapper.fromSimpleFunctionWrapper(convertFunction(commandFunction), css,
				entity -> cmdCtx.getSource().withEntity(((CraftEntity) entity).getHandle())));
		}
		return result.toArray(new FunctionWrapper[0]);
	}

	@Override
	public final org.bukkit.inventory.ItemStack getItemStack(CommandContext<CommandSourceStack> cmdCtx, String str)
		throws CommandSyntaxException {
		return CraftItemStack.asBukkitCopy(ItemArgument.getItem(cmdCtx, str).createItemStack(1, false));
	}

	@Override
	public final Predicate<org.bukkit.inventory.ItemStack> getItemStackPredicate(CommandContext<CommandSourceStack> cmdCtx,
		String key) throws CommandSyntaxException {
		// Not inside the lambda because getItemPredicate throws CommandSyntaxException
		Predicate<ItemStack> predicate = ItemPredicateArgument.getItemPredicate(cmdCtx, key);
		return item -> predicate.test(CraftItemStack.asNMSCopy(item));
	}

	@Override
	public final Location2D getLocation2DBlock(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		ColumnPos blockPos = ColumnPosArgument.getColumnPos(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), blockPos.x(), blockPos.z());
	}

	@Override
	public final Location2D getLocation2DPrecise(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		Vec2 vecPos = Vec2Argument.getVec2(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), vecPos.x, vecPos.y);
	}

	@Override
	public final Location getLocationBlock(CommandContext<CommandSourceStack> cmdCtx, String str)
		throws CommandSyntaxException {
		BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(cmdCtx, str);
		return new Location(getWorldForCSS(cmdCtx.getSource()), blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public final Location getLocationPrecise(CommandContext<CommandSourceStack> cmdCtx, String str)
		throws CommandSyntaxException {
		Vec3 vecPos = Vec3Argument.getCoordinates(cmdCtx, str).getPosition(cmdCtx.getSource());
		return new Location(getWorldForCSS(cmdCtx.getSource()), vecPos.x(), vecPos.y(), vecPos.z());
	}

	@Override
	public final org.bukkit.loot.LootTable getLootTable(CommandContext<CommandSourceStack> cmdCtx, String str) {
		ResourceLocation resourceLocation = ResourceLocationArgument.getId(cmdCtx, str);
		return new CraftLootTable(fromResourceLocation(resourceLocation),
			MINECRAFT_SERVER.getLootTables().get(resourceLocation));
	}

	@Override
	public final ParticleData<?> getParticle(CommandContext<CommandSourceStack> cmdCtx, String str) {
		final ParticleOptions particleOptions = ParticleArgument.getParticle(cmdCtx, str);
		final Level level = cmdCtx.getSource().getLevel();

		final Particle particle = CraftParticle.toBukkit(particleOptions);
		if (particleOptions instanceof SimpleParticleType options) {
			return new ParticleData<Void>(particle, null);
		}
		if (particleOptions instanceof BlockParticleOption options) {
			return new ParticleData<BlockData>(particle, CraftBlockData.fromData(options.getState()));
		}
		if (particleOptions instanceof DustColorTransitionOptions options) {
			final Color color = Color.fromRGB((int) (options.getColor().x() * 255.0F),
				(int) (options.getColor().y() * 255.0F), (int) (options.getColor().z() * 255.0F));
			final Color toColor = Color.fromRGB((int) (options.getToColor().x() * 255.0F),
				(int) (options.getToColor().y() * 255.0F), (int) (options.getToColor().z() * 255.0F));
			return new ParticleData<DustTransition>(particle, new DustTransition(color, toColor, options.getScale()));
		}
		if (particleOptions instanceof DustParticleOptions options) {
			final Color color = Color.fromRGB((int) (options.getColor().x() * 255.0F),
				(int) (options.getColor().y() * 255.0F), (int) (options.getColor().z() * 255.0F));
			return new ParticleData<DustOptions>(particle, new DustOptions(color, options.getScale()));
		}
		if (particleOptions instanceof ItemParticleOption options) {
			return new ParticleData<org.bukkit.inventory.ItemStack>(particle,
				CraftItemStack.asBukkitCopy(options.getItem()));
		}
		if (particleOptions instanceof VibrationParticleOption options) {
			// The "from" part of the Vibration object in Bukkit is completely ignored now,
			// so we just populate it with some "feasible" information
			final Vec3 origin = cmdCtx.getSource().getPosition();
			Location from = new Location(level.getWorld(), origin.x, origin.y, origin.z);
			final Destination destination;
			if (options.getDestination() instanceof BlockPositionSource positionSource) {
				Vec3 to = positionSource.getPosition(level).get();
				destination = new BlockDestination(new Location(level.getWorld(), to.x(), to.y(), to.z()));
			} else if (options.getDestination() instanceof EntityPositionSource positionSource) {
				positionSource.getPosition(level); // Populate Optional sourceEntity
				Either<Entity, Either<UUID, Integer>> entity = (Either<Entity, Either<UUID, Integer>>) EntityPositionSource_sourceEntity
					.get(positionSource);
				if (entity.left().isPresent()) {
					destination = new EntityDestination(entity.left().get().getBukkitEntity());
				} else {
					Either<UUID, Integer> id = entity.right().get();
					if (id.left().isPresent()) {
						destination = new EntityDestination(Bukkit.getEntity(id.left().get()));
					} else {
						// Do an entity lookup by numerical ID. There's no "helper function"
						// like Bukkit.getEntity() to do this for us, so we just have to do it
						// manually
						Entity foundEntity = null;
						for (ServerLevel world : MINECRAFT_SERVER.getAllLevels()) {
							Entity entityById = world.getEntity(id.right().get());
							if (entityById != null) {
								foundEntity = entityById;
								break;
							}
						}
						destination = new EntityDestination(foundEntity.getBukkitEntity());
					}
				}
			} else {
				CommandAPI.getLogger().warning("Unknown vibration destination " + options.getDestination());
				return new ParticleData<Void>(particle, null);
			}
			return new ParticleData<Vibration>(particle, new Vibration(from, destination, options.getArrivalInTicks()));
		}
		if (particleOptions instanceof ShriekParticleOption options) {
			// CraftBukkit implements shriek particles as a (boxed) Integer object
			return new ParticleData<Integer>(particle, Integer.valueOf(options.getDelay()));
		}
		if (particleOptions instanceof SculkChargeParticleOptions options) {
			// CraftBukkit implements sculk charge particles as a (boxed) Float object
			return new ParticleData<Float>(particle, Float.valueOf(options.roll()));
		}
		CommandAPI.getLogger().warning("Invalid particle data type for " + particle.getDataType().toString());
		return new ParticleData<Void>(particle, null);
	}

	@Override
	public final SimpleCommandMap getSimpleCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public final Sound getSound(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return CraftSound.getBukkit(Registry.SOUND_EVENT.get(ResourceLocationArgument.getId(cmdCtx, key)));
	}

	@Override
	public final SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		Collection<CommandFunction> customFunctions = MINECRAFT_SERVER.getFunctions()
			.getTag(new ResourceLocation(key.getNamespace(), key.getKey()));
		return customFunctions.toArray(new SimpleFunctionWrapper[0]);
	}

	@Differs(from = "1.19", by = "Use of 1.19.1 chat preview handler")
	@Override
	public abstract void hookChatPreview(Plugin plugin, Player player);

	@Override
	public final boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public final void reloadDataPacks() {
		CommandAPI.logNormal("Reloading datapacks...");

		// Get previously declared recipes to be re-registered later
		Iterator<Recipe> recipes = Bukkit.recipeIterator();

		// Update the commandDispatcher with the current server's commandDispatcher
		ReloadableResources serverResources = MINECRAFT_SERVER.resources;
		serverResources.managers().commands = MINECRAFT_SERVER.getCommands();

		// Update the ServerFunctionLibrary's command dispatcher with the new one
		try {
			CommandAPIHandler.getInstance().getField(ServerFunctionLibrary.class, "i")
				.set(serverResources.managers().getFunctionLibrary(), getBrigadierDispatcher());
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}

		// From MINECRAFT_SERVER.reloadResources //
		// Discover new packs
		Collection<String> collection;
		{
			List<String> packIDs = new ArrayList<>(MINECRAFT_SERVER.getPackRepository().getSelectedIds());
			List<String> disabledPacks = MINECRAFT_SERVER.getWorldData().getDataPackConfig().getDisabled();

			for (String availablePack : MINECRAFT_SERVER.getPackRepository().getAvailableIds()) {
				// Add every other available pack that is not disabled
				// and is not already in the list of existing packs
				if (!disabledPacks.contains(availablePack) && !packIDs.contains(availablePack)) {
					packIDs.add(availablePack);
				}
			}
			collection = packIDs;
		}

		Frozen registryAccess = MINECRAFT_SERVER.registryAccess();

		// Step 1: Construct an async supplier of a list of all resource packs to
		// be loaded in the reload phase
		CompletableFuture<List<PackResources>> first = CompletableFuture.supplyAsync(() -> {
			PackRepository serverPackRepository = MINECRAFT_SERVER.getPackRepository();

			List<PackResources> packResources = new ArrayList<>();
			for (String packID : collection) {
				Pack pack = serverPackRepository.getPack(packID);
				if (pack != null) {
					packResources.add(pack.open());
				}
			}
			return packResources;
		});

		// Step 2: Convert all of the resource packs into ReloadableResources which
		// are replaced by our custom server resources with defined commands
		CompletableFuture<ReloadableResources> second = first.thenCompose(packResources -> {
			MultiPackResourceManager resourceManager = new MultiPackResourceManager(PackType.SERVER_DATA,
				packResources);

			// Not using packResources, because we really really want this to work
			CompletableFuture<?> simpleReloadInstance = SimpleReloadInstance.create(
				resourceManager, serverResources.managers().listeners(), MINECRAFT_SERVER.executor,
				MINECRAFT_SERVER, CompletableFuture
					.completedFuture(Unit.INSTANCE) /* ReloadableServerResources.DATA_RELOAD_INITIAL_TASK */,
				LogUtils.getLogger().isDebugEnabled()).done();

			return simpleReloadInstance.thenApply(x -> serverResources);
		});

		// Step 3: Actually load all of the resources
		CompletableFuture<Void> third = second.thenAcceptAsync(resources -> {
			MINECRAFT_SERVER.resources.close();
			MINECRAFT_SERVER.resources = serverResources;
			MINECRAFT_SERVER.server.syncCommands();
			MINECRAFT_SERVER.getPackRepository().setSelected(collection);

			// MINECRAFT_SERVER.getSelectedPacks
			Collection<String> selectedIDs = MINECRAFT_SERVER.getPackRepository().getSelectedIds();
			List<String> enabledIDs = ImmutableList.copyOf(selectedIDs);
			List<String> disabledIDs = new ArrayList<>(MINECRAFT_SERVER.getPackRepository().getAvailableIds());

			ListIterator<String> disabledIDsIterator = disabledIDs.listIterator();
			while (disabledIDsIterator.hasNext()) {
				if (enabledIDs.contains(disabledIDsIterator.next())) {
					disabledIDsIterator.remove();
				}
			}

			MINECRAFT_SERVER.getWorldData().setDataPackConfig(new DataPackConfig(enabledIDs, disabledIDs));
			MINECRAFT_SERVER.resources.managers().updateRegistryTags(registryAccess);
			// May need to be commented out, may not. Comment it out just in case.
			// For some reason, calling getPlayerList().saveAll() may just hang
			// the server indefinitely. Not sure why!
			// MINECRAFT_SERVER.getPlayerList().saveAll();
			// MINECRAFT_SERVER.getPlayerList().reloadResources();
			// MINECRAFT_SERVER.getFunctions().replaceLibrary(MINECRAFT_SERVER.resources.managers().getFunctionLibrary());
			MINECRAFT_SERVER.getStructureManager()
				.onResourceManagerReload(MINECRAFT_SERVER.resources.resourceManager());
		});

		// Step 4: Block the thread until everything's done
		if (MINECRAFT_SERVER.isSameThread()) {
			MINECRAFT_SERVER.managedBlock(third::isDone);
		}

		// Run the completableFuture (and bind tags?)
		try {
			third.get();

			// Register recipes again because reloading datapacks removes all non-vanilla
			// recipes
			Recipe recipe;
			while (recipes.hasNext()) {
				recipe = recipes.next();
				try {
					Bukkit.addRecipe(recipe);
					if (recipe instanceof Keyed keyedRecipe) {
						CommandAPI.logInfo("Re-registering recipe: " + keyedRecipe.getKey());
					}
				} catch (Exception e) {
					continue; // Can't re-register registered recipes. Not an error.
				}
			}

			CommandAPI.logNormal("Finished reloading datapacks");
		} catch (Exception e) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			CommandAPI.logError(
				"Failed to load datapacks, can't proceed with normal server load procedure. Try fixing your datapacks?\n"
					+ stringWriter.toString());
		}
	}

	@Override
	public final void resendPackets(Player player) {
		MINECRAFT_SERVER.getCommands().sendCommands(((CraftPlayer) player).getHandle());
	}

	@Override
	public final void unhookChatPreview(Player player) {
		final Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
		if (channel.pipeline().get("CommandAPI_" + player.getName()) != null) {
			channel.eventLoop().submit(() -> channel.pipeline().remove("CommandAPI_" + player.getName()));
		}
	}

	@Override
	public final MinecraftServer getMinecraftServer() {
		return MINECRAFT_SERVER;
	}
}