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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
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
import org.bukkit.craftbukkit.v1_17_R1.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.help.CustomHelpTopic;
import org.bukkit.craftbukkit.v1_17_R1.help.SimpleHelpMap;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ComplexRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.potion.PotionEffectType;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.ComplexRecipeImpl;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandFunction.Entry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.EntitySummonArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.ItemEnchantmentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.MobEffectArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.commands.arguments.TeamArgument;
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
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.server.ServerResources;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

// Mojang-Mapped reflection
@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
@RequireField(in = EntitySelector.class, name = "usesSelector", ofType = boolean.class)
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
public class NMS_1_17_R1 implements NMS<CommandSourceStack> {
	
	private static final MinecraftServer MINECRAFT_SERVER = ((CraftServer) Bukkit.getServer()).getServer();
	private static final VarHandle SimpleHelpMap_helpTopics;
	
	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		VarHandle shm_ht = null;
		try {
			 shm_ht = MethodHandles.privateLookupIn(SimpleHelpMap.class, MethodHandles.lookup()).findVarHandle(SimpleHelpMap.class, "helpTopics", Map.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		 SimpleHelpMap_helpTopics = shm_ht;
	}

	private static NamespacedKey fromResourceLocation(ResourceLocation key) {
		return NamespacedKey.fromString(key.getNamespace() + ":" + key.getPath());
	}
	
	@Override
	public ArgumentType<?> _ArgumentAngle() {
		return AngleArgument.angle();
	}
	
	@Override
	public ArgumentType<?> _ArgumentAxis() {
		return SwizzleArgument.swizzle();
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
	public ArgumentType<?> _ArgumentChat() {
		return MessageArgument.message();
	}

	@Override
	public ArgumentType<?> _ArgumentChatComponent() {
		return ComponentArgument.textComponent();
	}

	@Override
	public ArgumentType<?> _ArgumentChatFormat() {
		return ColorArgument.color();
	}

	@Override
	public ArgumentType<?> _ArgumentDimension() {
		return DimensionArgument.dimension();
	}

	@Override
	public ArgumentType<?> _ArgumentEnchantment() {
		return ItemEnchantmentArgument.enchantment();
	}

	@Override
	public ArgumentType<?> _ArgumentEntity(dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector selector) {
		return switch (selector) {
		case MANY_ENTITIES -> EntityArgument.entities();
		case MANY_PLAYERS  -> EntityArgument.players();
		case ONE_ENTITY    -> EntityArgument.entity();
		case ONE_PLAYER    -> EntityArgument.player();
		};
	}

	@Override
	public ArgumentType<?> _ArgumentEntitySummon() {
		return EntitySummonArgument.id();
	}

	@Override
	public ArgumentType<?> _ArgumentFloatRange() {
		return RangeArgument.floatRange();
	}

	@Override
	public ArgumentType<?> _ArgumentIntRange() {
		return RangeArgument.intRange();
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
	public ArgumentType<?> _ArgumentMathOperation() {
		return OperationArgument.operation();
	}

	@Override
	public ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return ResourceLocationArgument.id();
	}

	@Override
	public ArgumentType<?> _ArgumentMobEffect() {
		return MobEffectArgument.effect();
	}

	@Override
	public ArgumentType<?> _ArgumentNBTCompound() {
		return CompoundTagArgument.compoundTag();
	}

	@Override
	public ArgumentType<?> _ArgumentParticle() {
		return ParticleArgument.particle();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition() {
		return BlockPosArgument.blockPos();
	}

	@Override
	public ArgumentType<?> _ArgumentPosition2D() {
		return ColumnPosArgument.columnPos();
	}

	@Override
	public ArgumentType<?> _ArgumentProfile() {
		return GameProfileArgument.gameProfile();
	}

	@Override
	public ArgumentType<?> _ArgumentRotation() {
		return RotationArgument.rotation();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardCriteria() {
		return ObjectiveCriteriaArgument.criteria();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardObjective() {
		return ObjectiveArgument.objective();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardSlot() {
		return ScoreboardSlotArgument.displaySlot();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreboardTeam() {
		return TeamArgument.team();
	}

	@Override
	public ArgumentType<?> _ArgumentScoreholder(boolean single) {
		return single ? ScoreHolderArgument.scoreHolder() : ScoreHolderArgument.scoreHolders();
	}

	@Override
	public ArgumentType<?> _ArgumentTag() {
		return FunctionArgument.functions();
	}

	@Override
	public ArgumentType<?> _ArgumentTime() {
		return TimeArgument.time();
	}

	@Override
	public ArgumentType<?> _ArgumentUUID() {
		return UuidArgument.uuid();
	}

	@Override
	public ArgumentType<?> _ArgumentVec2() {
		return Vec2Argument.vec2();
	}

	@Override
	public ArgumentType<?> _ArgumentVec3() {
		return Vec3Argument.vec3();
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.17", "1.17.1" };
	}

	@Override
	public String convert(org.bukkit.inventory.ItemStack is) {
		return is.getType().getKey().toString() + CraftItemStack.asNMSCopy(is).getOrCreateTag().getAsString();
	}

	@Override
	public String convert(Particle particle) {
		return CraftParticle.toNMS(particle).writeToString();
	}

	@Override
	public String convert(PotionEffectType potion) {
		return potion.getName().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public String convert(Sound sound) {
		return sound.getKey().toString();
	}

	//Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CommandFunction commandFunction) {
		ToIntFunction<CommandSourceStack> appliedObj = (CommandSourceStack css) -> MINECRAFT_SERVER.getFunctions().execute(commandFunction, css);

		Entry[] cArr = commandFunction.getEntries();
		String[] result = new String[cArr.length];
		for(int i = 0, size = cArr.length; i < size; i++) {
			result[i] = cArr[i].toString();
		}
		return new SimpleFunctionWrapper(fromResourceLocation(commandFunction.getId()), appliedObj, result);
	}

	@Override
	public void createDispatcherFile(File file, com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher) throws IOException {
		Files.write(new GsonBuilder().setPrettyPrinting().create()
				.toJson(ArgumentTypes.serializeNodeToJson(dispatcher, dispatcher.getRoot())), file, StandardCharsets.UTF_8);
	}

	@Override
	public org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		return ResourceLocationArgument.getAdvancement(cmdCtx, key).bukkit;
	}

	@Override
	public Component getAdventureChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException  {
		return PaperComponents.gsonSerializer().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	public Component getAdventureChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return PaperComponents.gsonSerializer().deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Override
	public float getAngle(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return AngleArgument.getAngle(cmdCtx, key);
	}

	@Override
	public EnumSet<Axis> getAxis(CommandContext<CommandSourceStack> cmdCtx, String key) {
		EnumSet<Axis> set = EnumSet.noneOf(Axis.class);
		EnumSet<net.minecraft.core.Direction.Axis> parsedEnumSet = SwizzleArgument.getSwizzle(cmdCtx, key);
		for (net.minecraft.core.Direction.Axis element : parsedEnumSet) {
			switch (element) {
			case X -> set.add(Axis.X);
			case Y -> set.add(Axis.Y);
			case Z -> set.add(Axis.Z);
			default -> throw new IllegalArgumentException("Unexpected value: " + element);
			}
		}
		return set;
	}

	@Override
	public Biome getBiome(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return Biome.valueOf(cmdCtx.getArgument(key, ResourceLocation.class).getPath().toUpperCase());
	}

	@Override
	public Predicate<Block> getBlockPredicate(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
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
	public BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	public ChatColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String str) {
		return CraftChatMessage.getColor(ColorArgument.getColor(cmdCtx, str));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String str) {
		return ComponentSerializer.parse(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, str)));
	}

	@Override
	public CommandSourceStack getCLWFromCommandSender(CommandSender sender) {
		return VanillaCommandWrapper.getListener(sender);
	}

	@Override
	public CommandSender getCommandSenderFromCSS(CommandSourceStack css) {
		try {
			return css.getBukkitSender();
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public Environment getDimension(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return DimensionArgument.getDimension(cmdCtx, key).getWorld().getEnvironment();
	}

	@Override
	public Enchantment getEnchantment(CommandContext<CommandSourceStack> cmdCtx, String str) {
		return new CraftEnchantment(ItemEnchantmentArgument.getEnchantment(cmdCtx, str));
	}
	
	@Override
	public Object getEntitySelector(CommandContext<CommandSourceStack> cmdCtx, String str, dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector selector)
			throws CommandSyntaxException {
		
		// We override the rule whereby players need "minecraft.command.selector" and have to have
		// level 2 permissions in order to use entity selectors. We're trying to allow entity selectors
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
				for(Entity entity : argument.findEntities(cmdCtx.getSource())) {
					result.add(entity.getBukkitEntity());
				}
				yield result;
			} catch (CommandSyntaxException e) {
				yield new ArrayList<org.bukkit.entity.Entity>();
			}
		case MANY_PLAYERS:
			try {
				List<Player> result = new ArrayList<>();
				for(ServerPlayer player : argument.findPlayers(cmdCtx.getSource())) {
					result.add(player.getBukkitEntity());
				}
				yield result;
			} catch (CommandSyntaxException e) {
				yield new ArrayList<Player>();
			}
		case ONE_ENTITY:
			yield (org.bukkit.entity.Entity) argument.findSingleEntity(cmdCtx.getSource()).getBukkitEntity();
		case ONE_PLAYER:
			yield (Player) argument.findSinglePlayer(cmdCtx.getSource()).getBukkitEntity();
		};
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public EntityType getEntityType(CommandContext<CommandSourceStack> cmdCtx, String str) throws CommandSyntaxException {
		return EntityType.fromName(net.minecraft.world.entity.EntityType.getKey(Registry.ENTITY_TYPE.get(EntitySummonArgument.getSummonableEntity(cmdCtx, str))).getPath());
	}
	
	@Override
	public FloatRange getFloatRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		MinMaxBounds.Doubles range = RangeArgument.Floats.getRange(cmdCtx, key);
		double low = range.getMin() == null ? -Float.MAX_VALUE : range.getMin();
		double high = range.getMax() == null ? Float.MAX_VALUE : range.getMax();
		return new FloatRange((float) low, (float) high);
	}
	
	@Override
	public FunctionWrapper[] getFunction(CommandContext<CommandSourceStack> cmdCtx, String str) throws CommandSyntaxException {
		List<FunctionWrapper> result = new ArrayList<>();
		CommandSourceStack css = cmdCtx.getSource().withSuppressedOutput().withMaximumPermission(2);
		
		for(CommandFunction commandFunction : FunctionArgument.getFunctions(cmdCtx, str)) {
			result.add(
				FunctionWrapper.fromSimpleFunctionWrapper(
					convertFunction(commandFunction), 
					css, 
					entity -> cmdCtx.getSource().withEntity(((CraftEntity) entity).getHandle())
				)
			);
		}
		return result.toArray(new FunctionWrapper[0]);
	}
	
	@Override
	public SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return convertFunction(MINECRAFT_SERVER.getFunctions().get(new ResourceLocation(key.getNamespace(), key.getKey())).get());
	}
	
	@Override
	public Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> result = new HashSet<>();
		for(ResourceLocation resourceLocation : MINECRAFT_SERVER.getFunctions().getFunctionNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}

	@Override
	public IntegerRange getIntRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		MinMaxBounds.Ints range = RangeArgument.Ints.getRange(cmdCtx, key);
		int low = range.getMin() == null ? Integer.MIN_VALUE : range.getMin();
		int high = range.getMax() == null ? Integer.MAX_VALUE : range.getMax();
		return new IntegerRange(low, high);
	}

	@Override
	public org.bukkit.inventory.ItemStack getItemStack(CommandContext<CommandSourceStack> cmdCtx, String str)
			throws CommandSyntaxException {
		return CraftItemStack.asBukkitCopy(ItemArgument.getItem(cmdCtx, str).createItemStack(1, false));
	}

	@Override
	public Predicate<org.bukkit.inventory.ItemStack> getItemStackPredicate(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		// Not inside the lambda because getItemPredicate throws CommandSyntaxException
		Predicate<ItemStack> predicate = ItemPredicateArgument.getItemPredicate(cmdCtx, key);
		return item -> predicate.test(CraftItemStack.asNMSCopy(item));
	}

	@Override
	public String getKeyedAsString(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ResourceLocationArgument.getId(cmdCtx, key).toString();
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
	public Location2D getLocation2DPrecise(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		Vec2 vecPos = Vec2Argument.getVec2(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), vecPos.x, vecPos.y);
	}

	@Override
	public Location2D getLocation2DBlock(CommandContext<CommandSourceStack> cmdCtx, String key)
			throws CommandSyntaxException {
		ColumnPos blockPos = ColumnPosArgument.getColumnPos(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), blockPos.x, blockPos.z);
	}

	@Override
	public org.bukkit.loot.LootTable getLootTable(CommandContext<CommandSourceStack> cmdCtx, String str) {
		ResourceLocation resourceLocation = ResourceLocationArgument.getId(cmdCtx, str);
		return new CraftLootTable(fromResourceLocation(resourceLocation), MINECRAFT_SERVER.getLootTables().get(resourceLocation));
	}

	@Override
	public MathOperation getMathOperation(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		// We run this to ensure the argument exists/parses properly 
		OperationArgument.getOperation(cmdCtx, key);
		return MathOperation.fromString(CommandAPIHandler.getRawArgumentInput(cmdCtx, key));
	}

	@Override
	public NBTContainer getNBTCompound(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return new NBTContainer(CompoundTagArgument.getCompoundTag(cmdCtx, key));
	}

	@Override
	public String getObjective(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ObjectiveArgument.getObjective(cmdCtx, key).getName();
	}

	@Override
	public String getObjectiveCriteria(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ObjectiveCriteriaArgument.getCriteria(cmdCtx, key).getName();
	}

	@Override
	public Particle getParticle(CommandContext<CommandSourceStack> cmdCtx, String str) {
		return CraftParticle.toBukkit(ParticleArgument.getParticle(cmdCtx, str));
	}

	@Override
	public Player getPlayer(CommandContext<CommandSourceStack> cmdCtx, String str) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(GameProfileArgument.getGameProfiles(cmdCtx, str).iterator().next().getId());
		if (target == null) {
			throw GameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
		} else {
			return target;
		}
	}
	
	@Override
	public OfflinePlayer getOfflinePlayer(CommandContext<CommandSourceStack> cmdCtx, String str) throws CommandSyntaxException {
		OfflinePlayer target = Bukkit.getOfflinePlayer(GameProfileArgument.getGameProfiles(cmdCtx, str).iterator().next().getId());
		if (target == null) {
			throw GameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
		} else {
			return target;
		}
	}

	@Override
	public PotionEffectType getPotionEffect(CommandContext<CommandSourceStack> cmdCtx, String str) throws CommandSyntaxException {
		return new CraftPotionEffectType(MobEffectArgument.getEffect(cmdCtx, str));
	}

	@Override
	public ComplexRecipe getRecipe(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		net.minecraft.world.item.crafting.Recipe<?> recipe = ResourceLocationArgument.getRecipe(cmdCtx, key);
		return new ComplexRecipeImpl(fromResourceLocation(recipe.getId()), recipe.toBukkitRecipe());
	}

	@Override
	public Rotation getRotation(CommandContext<CommandSourceStack> cmdCtx, String key) {
		Vec2 rotation = RotationArgument.getRotation(cmdCtx, key).getRotation(cmdCtx.getSource());
		return new Rotation(rotation.x, rotation.y);
	}

	@Override
	public ScoreboardSlot getScoreboardSlot(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return new ScoreboardSlot(ScoreboardSlotArgument.getDisplaySlot(cmdCtx, key));
	}

	@Override
	public Collection<String> getScoreHolderMultiple(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ScoreHolderArgument.getNames(cmdCtx, key);
	}

	@Override
	public String getScoreHolderSingle(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ScoreHolderArgument.getName(cmdCtx, key);
	}

	@Override
	public CommandSender getSenderForCommand(CommandContext<CommandSourceStack> cmdCtx, boolean isNative) {
		CommandSourceStack css = cmdCtx.getSource();

		CommandSender sender = css.getBukkitSender();
		Vec3 pos = css.getPosition();
		Vec2 rot = css.getRotation();
		World world = getWorldForCSS(css);
		Location location = new Location(world, pos.x(), pos.y(), pos.z(), rot.x, rot.y);

		Entity proxyEntity = css.getEntity();
		CommandSender proxy = proxyEntity == null ? null : proxyEntity.getBukkitEntity();
		if (isNative || (proxy != null && !sender.equals(proxy))) {
			return new NativeProxyCommandSender(sender, proxy, location, world);
		} else {
			return sender;
		}
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
	public SuggestionProvider<CommandSourceStack> getSuggestionProvider(SuggestionProviders provider) {
		return switch (provider) {
		case FUNCTION -> 
			(context, builder) -> {
				ServerFunctionManager functionData = MINECRAFT_SERVER.getFunctions();
				SharedSuggestionProvider.suggestResource(functionData.getTagNames(), builder, "#");
				return SharedSuggestionProvider.suggestResource(functionData.getFunctionNames(), builder);
			};
		case RECIPES -> net.minecraft.commands.synchronization.SuggestionProviders.ALL_RECIPES;
		case SOUNDS -> net.minecraft.commands.synchronization.SuggestionProviders.AVAILABLE_SOUNDS;
		case ADVANCEMENTS ->
			(cmdCtx, builder) -> {
				return SharedSuggestionProvider.suggestResource(MINECRAFT_SERVER.getAdvancements().getAllAdvancements().stream().map(net.minecraft.advancements.Advancement::getId), builder);
			};
		case LOOT_TABLES ->
			(cmdCtx, builder) -> {
				return SharedSuggestionProvider.suggestResource(MINECRAFT_SERVER.getLootTables().getIds(), builder);
			};
		case BIOMES -> net.minecraft.commands.synchronization.SuggestionProviders.AVAILABLE_BIOMES;
		case ENTITIES -> net.minecraft.commands.synchronization.SuggestionProviders.SUMMONABLE_ENTITIES;
		default -> (context, builder) -> Suggestions.empty();
		};
	}

	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		List<CommandFunction> customFunctions = MINECRAFT_SERVER.getFunctions().getTag(new ResourceLocation(key.getNamespace(), key.getKey())).getValues();
		SimpleFunctionWrapper[] result = new SimpleFunctionWrapper[customFunctions.size()];
		for(int i = 0, size = customFunctions.size(); i < size; i++) {
			result[i] = convertFunction(customFunctions.get(i));
		}
		return result;
	}

	@Override
	public Set<NamespacedKey> getTags() {
		Set<NamespacedKey> result = new HashSet<>();
		for(ResourceLocation resourceLocation : MINECRAFT_SERVER.getFunctions().getFunctionNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}

	@Override
	public String getTeam(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return TeamArgument.getTeam(cmdCtx, key).getName();
	}

	@Override
	public int getTime(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return (Integer) cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public UUID getUUID(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return UuidArgument.getUuid(cmdCtx, key);
	}

	@Override
	public World getWorldForCSS(CommandSourceStack css) {
		return (css.getLevel() == null) ? null : css.getLevel().getWorld();
	}

	@Override
	public boolean isVanillaCommandWrapper(Command command) {
		return command instanceof VanillaCommandWrapper;
	}

	@Override
	public void reloadDataPacks() {
		CommandAPI.logNormal("Reloading datapacks...");

		// Get previously declared recipes to be re-registered later
		Iterator<Recipe> recipes = Bukkit.recipeIterator();

		// Update the commandDispatcher with the current server's commandDispatcher
		ServerResources serverResources = MINECRAFT_SERVER.resources;
		serverResources.commands = MINECRAFT_SERVER.getCommands();
		
		// Update the ServerFunctionLibrary's command dispatcher with the new one
		try {
			CommandAPIHandler.getInstance().getField(ServerFunctionLibrary.class, "i")
					.set(serverResources.getFunctionLibrary(), getBrigadierDispatcher());
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		
		// Construct the new CompletableFuture that now uses our updated serverResources
		CompletableFuture<?> unitCompletableFuture = ((ReloadableResourceManager) serverResources.getResourceManager()).reload(
			Util.backgroundExecutor(),
			Runnable::run, 
			MINECRAFT_SERVER.getPackRepository().openAllSelected(), 
			CompletableFuture.completedFuture(null)
		);		
		CompletableFuture<ServerResources> completablefuture = unitCompletableFuture
			.whenComplete((Object u, Throwable t) -> {
				if (t != null) {
					serverResources.close();
				}
			})
			.thenApply((Object u) -> serverResources);

		// Run the completableFuture and bind tags
		try {
			completablefuture.get().updateGlobals();

			// Register recipes again because reloading datapacks removes all non-vanilla recipes
			Recipe recipe;
			while(recipes.hasNext()) {
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
			
			CommandAPI.logError("Failed to load datapacks, can't proceed with normal server load procedure. Try fixing your datapacks?\n" + stringWriter.toString());
		}
	}

	@Override
	public void resendPackets(Player player) {
		MINECRAFT_SERVER.getCommands().sendCommands(((CraftPlayer) player).getHandle());
	}

	@Override
	public HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription,
			String permission) {
		return new CustomHelpTopic(commandName, shortDescription, fullDescription, permission);
	}

	@Override
	public void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd) {
		Map<String, HelpTopic> helpTopics = (Map<String, HelpTopic>) SimpleHelpMap_helpTopics.get(Bukkit.getServer().getHelpMap());
		for(Map.Entry<String, HelpTopic> entry : helpTopicsToAdd.entrySet()) {
			helpTopics.put(entry.getKey(), entry.getValue());
		}
	}
}
