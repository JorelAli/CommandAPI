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
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.bukkit.Bukkit;
import org.bukkit.Color;
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
import org.bukkit.craftbukkit.v1_17_R1.CraftLootTable;
import org.bukkit.craftbukkit.v1_17_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftSound;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_17_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.help.CustomHelpTopic;
import org.bukkit.craftbukkit.v1_17_R1.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandFunction.Entry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
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
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

// Mojang-Mapped reflection
/**
 * NMS implementation for Minecraft 1.17 and 1.17.1
 * SpecialSource compiles MinecraftServer.resources (used in reloadDataPacks)
 * to two different fields (aC or aB) for 1.17 and 1.17.1 respectively. This
 * common implementation is built those two ways by the corresponding modules.
 */
@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
@RequireField(in = EntitySelector.class, name = "usesSelector", ofType = boolean.class)
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
@RequireField(in = EntityPositionSource.class, name = "sourceEntity", ofType = Optional.class)
public abstract class NMS_1_17_Common extends NMS_Common {

	protected static final MinecraftServer MINECRAFT_SERVER = ((CraftServer) Bukkit.getServer()).getServer();
	private static final VarHandle SimpleHelpMap_helpTopics;
	private static final VarHandle EntityPositionSource_sourceEntity;

	// Compute all var handles all in one go so we don't do this during main server
	// runtime
	static {
		VarHandle shm_ht = null;
		VarHandle eps_se = null;
		try {
			shm_ht = MethodHandles.privateLookupIn(SimpleHelpMap.class, MethodHandles.lookup())
					.findVarHandle(SimpleHelpMap.class, "helpTopics", Map.class);
			eps_se = MethodHandles.privateLookupIn(EntityPositionSource.class, MethodHandles.lookup())
					.findVarHandle(EntityPositionSource.class, "d", Optional.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		SimpleHelpMap_helpTopics = shm_ht;
		EntityPositionSource_sourceEntity = eps_se;
	}

	private static NamespacedKey fromResourceLocation(ResourceLocation key) {
		return NamespacedKey.fromString(key.getNamespace() + ":" + key.getPath());
	}

	@Override
	public ArgumentType<?> _ArgumentBlockPredicate() {
		return BlockPredicateArgument.blockPredicate();
	}

	@Override
	public ArgumentType<?> _ArgumentBlockState() {
		return BlockStateArgument.block();
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(
			dev.jorel.commandapi.arguments.EntitySelector selector) {
		return switch (selector) {
			case MANY_ENTITIES -> EntityArgument.entities();
			case MANY_PLAYERS -> EntityArgument.players();
			case ONE_ENTITY -> EntityArgument.entity();
			case ONE_PLAYER -> EntityArgument.player();
		};
	}

	@Override
	public ArgumentType<?> _ArgumentItemPredicate() {
		return ItemPredicateArgument.itemPredicate();
	}

	@Override
	public ArgumentType<?> _ArgumentItemStack() {
		return ItemArgument.item();
	}

	@Override
	public ArgumentType<?> _ArgumentSyntheticBiome() {
		return _ArgumentMinecraftKeyRegistered();
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		Map<String, HelpTopic> helpTopics = (Map<String, HelpTopic>) SimpleHelpMap_helpTopics
				.get(Bukkit.getServer().getHelpMap());
		for (Map.Entry<String, HelpTopic> entry : helpTopicsToAdd.entrySet()) {
			helpTopics.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public abstract String[] compatibleVersions();

	@Override
	public String convert(org.bukkit.inventory.ItemStack is) {
		return is.getType().getKey().toString() + CraftItemStack.asNMSCopy(is).getOrCreateTag().getAsString();
	}

	@Override
	public String convert(ParticleData<?> particle) {
		return CraftParticle.toNMS(particle.particle(), particle.data()).writeToString();
	}

	// Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CommandFunction commandFunction) {
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
	public void createDispatcherFile(File file, com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher)
			throws IOException {
		Files.write(
				new GsonBuilder().setPrettyPrinting().create()
						.toJson(ArgumentTypes.serializeNodeToJson(dispatcher, dispatcher.getRoot())),
				file, StandardCharsets.UTF_8);
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription,
			String permission) {
		return new CustomHelpTopic(commandName, shortDescription, fullDescription, permission);
	}

	@SuppressWarnings("removal")
	@Override
	public Component getAdventureChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return PaperComponents.gsonSerializer()
				.deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Override
	public Biome getBiome(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return Biome.valueOf(cmdCtx.getArgument(key, ResourceLocation.class).getPath().toUpperCase());
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		Predicate<BlockInWorld> predicate = BlockPredicateArgument.getBlockPredicate(cmdCtx, key);
		return (Block block) -> {
			return predicate.test(new BlockInWorld(cmdCtx.getSource().getLevel(),
					new BlockPos(block.getX(), block.getY(), block.getZ()), true));
		};
	}

	@Override
	public BlockData getBlockState(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return CraftBlockData.fromData(BlockStateArgument.getBlock(cmdCtx, key).getState());
	}

	@Override
	public com.mojang.brigadier.CommandDispatcher<CommandSourceStack> getBrigadierDispatcher() {
		return MINECRAFT_SERVER.vanillaCommandDispatcher.getDispatcher();
	}

	@Override
	public CommandSourceStack getCLWFromCommandSender(CommandSender sender) {
		return VanillaCommandWrapper.getListener(sender);
	}

	@Override
	public Object getEntitySelector(CommandContext<CommandSourceStack> cmdCtx, String str,
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
	public EntityType getEntityType(CommandContext<CommandSourceStack> cmdCtx, String str)
			throws CommandSyntaxException {
		return EntityType.fromName(net.minecraft.world.entity.EntityType
				.getKey(Registry.ENTITY_TYPE.get(EntitySummonArgument.getSummonableEntity(cmdCtx, str))).getPath());
	}

	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandSourceStack> cmdCtx, String str)
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
	public org.bukkit.inventory.ItemStack getItemStack(CommandContext<CommandSourceStack> cmdCtx, String str)
			throws CommandSyntaxException {
		return CraftItemStack.asBukkitCopy(ItemArgument.getItem(cmdCtx, str).createItemStack(1, false));
	}

	@Override
	public Predicate<org.bukkit.inventory.ItemStack> getItemStackPredicate(CommandContext<CommandSourceStack> cmdCtx,
			String key) throws CommandSyntaxException {
		// Not inside the lambda because getItemPredicate throws CommandSyntaxException
		Predicate<ItemStack> predicate = ItemPredicateArgument.getItemPredicate(cmdCtx, key);
		return item -> predicate.test(CraftItemStack.asNMSCopy(item));
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		ColumnPos blockPos = ColumnPosArgument.getColumnPos(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), blockPos.x, blockPos.z);
	}

	@Override
	public Location2D getLocation2DPrecise(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		Vec2 vecPos = Vec2Argument.getVec2(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), vecPos.x, vecPos.y);
	}

	@Override
	public Location getLocationBlock(CommandContext<CommandSourceStack> cmdCtx, String str)
			throws CommandSyntaxException {
		BlockPos blockPos = BlockPosArgument.getLoadedBlockPos(cmdCtx, str);
		return new Location(getWorldForCSS(cmdCtx.getSource()), blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	@Override
	public Location getLocationPrecise(CommandContext<CommandSourceStack> cmdCtx, String str)
			throws CommandSyntaxException {
		Vec3 vecPos = Vec3Argument.getCoordinates(cmdCtx, str).getPosition(cmdCtx.getSource());
		return new Location(getWorldForCSS(cmdCtx.getSource()), vecPos.x(), vecPos.y(), vecPos.z());
	}

	@Override
	public org.bukkit.loot.LootTable getLootTable(CommandContext<CommandSourceStack> cmdCtx, String str) {
		ResourceLocation resourceLocation = ResourceLocationArgument.getId(cmdCtx, str);
		return new CraftLootTable(fromResourceLocation(resourceLocation),
				MINECRAFT_SERVER.getLootTables().get(resourceLocation));
	}

	@Override
	public ParticleData<?> getParticle(CommandContext<CommandSourceStack> cmdCtx, String str) {
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
			final BlockPos origin = options.getVibrationPath().getOrigin();
			final Location from = new Location(level.getWorld(), origin.getX(), origin.getY(), origin.getZ());
			final Destination destination;
			if (options.getVibrationPath().getDestination() instanceof BlockPositionSource positionSource) {
				BlockPos to = positionSource.getPosition(level).get();
				destination = new BlockDestination(new Location(level.getWorld(), to.getX(), to.getY(), to.getZ()));
			} else if (options.getVibrationPath().getDestination() instanceof EntityPositionSource positionSource) {
				positionSource.getPosition(level); // Populate Optional sourceEntity
				Optional<Entity> entity = (Optional<Entity>) EntityPositionSource_sourceEntity.get(positionSource);
				destination = new EntityDestination(entity.get().getBukkitEntity());
			} else {
				CommandAPI.getLogger()
						.warning("Unknown vibration destination " + options.getVibrationPath().getDestination());
				return new ParticleData<Void>(particle, null);
			}
			return new ParticleData<Vibration>(particle,
					new Vibration(from, destination, options.getVibrationPath().getArrivalInTicks()));
		}
		CommandAPI.getLogger().warning("Invalid particle data type for " + particle.getDataType().toString());
		return new ParticleData<Void>(particle, null);
	}

	@Override
	public SimpleCommandMap getSimpleCommandMap() {
		return ((CraftServer) Bukkit.getServer()).getCommandMap();
	}

	@Override
	public Sound getSound(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return CraftSound.getBukkit(Registry.SOUND_EVENT.get(ResourceLocationArgument.getId(cmdCtx, key)));
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		List<CommandFunction> customFunctions = MINECRAFT_SERVER.getFunctions()
				.getTag(new ResourceLocation(key.getNamespace(), key.getKey())).getValues();
		SimpleFunctionWrapper[] result = new SimpleFunctionWrapper[customFunctions.size()];
		for (int i = 0, size = customFunctions.size(); i < size; i++) {
			result[i] = convertFunction(customFunctions.get(i));
		}
		return result;
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public abstract void reloadDataPacks();

	@Override
	public void resendPackets(Player player) {
		MINECRAFT_SERVER.getCommands().sendCommands(((CraftPlayer) player).getHandle());
	}

	@Override
	public MinecraftServer getMinecraftServer() {
		return MINECRAFT_SERVER;
	}
}
