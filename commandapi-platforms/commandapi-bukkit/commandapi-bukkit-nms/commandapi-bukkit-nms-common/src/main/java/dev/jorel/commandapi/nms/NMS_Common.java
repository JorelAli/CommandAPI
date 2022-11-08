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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.BukkitPlatform;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.Overridden;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.*;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;
import net.minecraft.world.phys.Vec2;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.*;

/**
 * Common NMS code To ensure that this code actually works across all versions
 * of Minecraft that this is supposed to support (1.17+), you should be
 * compiling this code against all of the declared Maven profiles specified in
 * this submodule's pom.xml file, by running the following commands:
 * <ul>
 * <li><code>mvn clean package -pl :commandapi-nms-common -P Spigot_1_19_R1</code></li>
 * <li><code>mvn clean package -pl :commandapi-nms-common -P Spigot_1_18_2_R2</code></li>
 * <li><code>mvn clean package -pl :commandapi-nms-common -P Spigot_1_18_R1</code></li>
 * <li><code>mvn clean package -pl :commandapi-nms-common -P Spigot_1_17_R1</code></li>
 * </ul>
 * Any of these that do not work should be removed or implemented otherwise
 * (introducing another NMS_Common module perhaps?
 */
public abstract class NMS_Common extends BukkitPlatform<CommandSourceStack> {

	private static NamespacedKey fromResourceLocation(ResourceLocation key) {
		return NamespacedKey.fromString(key.getNamespace() + ":" + key.getPath());
	}

	@Override
	public final ArgumentType<?> _ArgumentAngle() {
		return AngleArgument.angle();
	}

	@Override
	public final ArgumentType<?> _ArgumentAxis() {
		return SwizzleArgument.swizzle();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentBlockPredicate();

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentBlockState();

	@Override
	public final ArgumentType<?> _ArgumentChat() {
		return MessageArgument.message();
	}

	@Override
	public final ArgumentType<?> _ArgumentChatComponent() {
		return ComponentArgument.textComponent();
	}

	@Override
	public final ArgumentType<?> _ArgumentChatFormat() {
		return ColorArgument.color();
	}

	@Override
	public final ArgumentType<?> _ArgumentDimension() {
		return DimensionArgument.dimension();
	}

	@Override
	public final ArgumentType<?> _ArgumentEnchantment() {
		return ItemEnchantmentArgument.enchantment();
	}

	@Override
	// I mean... really? Why?
	@Unimplemented(because = NAME_CHANGED, info = "a (1.17)                -> entity   (1.18) -> a (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "multipleEntities (1.17) -> entities (1.18) -> b (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "c (1.17)                -> player   (1.18) -> c (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "d (1.17)                -> players  (1.18) -> d (1.19)")
	public abstract ArgumentType<?> _ArgumentEntity(EntitySelector selector);

	@Override
	public final ArgumentType<?> _ArgumentEntitySummon() {
		return EntitySummonArgument.id();
	}

	@Override
	public final ArgumentType<?> _ArgumentFloatRange() {
		return RangeArgument.floatRange();
	}

	@Override
	public final ArgumentType<?> _ArgumentIntRange() {
		return RangeArgument.intRange();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentItemPredicate();

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentItemStack();

	@Override
	public final ArgumentType<?> _ArgumentMathOperation() {
		return OperationArgument.operation();
	}

	@Override
	public final ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return ResourceLocationArgument.id();
	}

	@Override
	public final ArgumentType<?> _ArgumentMobEffect() {
		return MobEffectArgument.effect();
	}

	@Override
	public final ArgumentType<?> _ArgumentNBTCompound() {
		return CompoundTagArgument.compoundTag();
	}

	@Override
	public final ArgumentType<?> _ArgumentParticle() {
		return ParticleArgument.particle();
	}

	@Override
	public final ArgumentType<?> _ArgumentPosition() {
		return BlockPosArgument.blockPos();
	}

	@Override
	public final ArgumentType<?> _ArgumentPosition2D() {
		return ColumnPosArgument.columnPos();
	}

	@Override
	public final ArgumentType<?> _ArgumentProfile() {
		return GameProfileArgument.gameProfile();
	}

	@Override
	public final ArgumentType<?> _ArgumentRotation() {
		return RotationArgument.rotation();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardCriteria() {
		return ObjectiveCriteriaArgument.criteria();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardObjective() {
		return ObjectiveArgument.objective();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardSlot() {
		return ScoreboardSlotArgument.displaySlot();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardTeam() {
		return TeamArgument.team();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreholder(boolean single) {
		return single ? ScoreHolderArgument.scoreHolder() : ScoreHolderArgument.scoreHolders();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.18.2")
	public abstract ArgumentType<?> _ArgumentSyntheticBiome();

	@Override
	public final ArgumentType<?> _ArgumentTag() {
		return FunctionArgument.functions();
	}

	@Override
	public final ArgumentType<?> _ArgumentTime() {
		return TimeArgument.time();
	}

	@Override
	public final ArgumentType<?> _ArgumentUUID() {
		return UuidArgument.uuid();
	}

	@Override
	public final ArgumentType<?> _ArgumentVec2() {
		return Vec2Argument.vec2();
	}

	@Override
	public final ArgumentType<?> _ArgumentVec3() {
		return Vec3Argument.vec3();
	}

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "SimpleHelpMap")
	public abstract void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd);

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION)
	public abstract String[] compatibleVersions();

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftItemStack")
	public abstract String convert(ItemStack is);

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION)
	public abstract String convert(ParticleData<?> particle);

	@Override
	public final String convert(PotionEffectType potion) {
		return potion.getName().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public final String convert(Sound sound) {
		return sound.getKey().toString();
	}

	// Converts NMS function to SimpleFunctionWrapper
	private SimpleFunctionWrapper convertFunction(CommandFunction commandFunction) {
		ToIntFunction<CommandSourceStack> appliedObj = (CommandSourceStack css) -> getMinecraftServer().getFunctions()
			.execute(commandFunction, css);

		CommandFunction.Entry[] cArr = commandFunction.getEntries();
		String[] result = new String[cArr.length];
		for (int i = 0, size = cArr.length; i < size; i++) {
			result[i] = cArr[i].toString();
		}
		return new SimpleFunctionWrapper(fromResourceLocation(commandFunction.getId()), appliedObj, result);
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract void createDispatcherFile(File file, CommandDispatcher<CommandSourceStack> dispatcher) throws IOException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CustomHelpTopic")
	public abstract HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription,
		String permission);

	@Override
	public final org.bukkit.advancement.Advancement getAdvancement(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		return ResourceLocationArgument.getAdvancement(cmdCtx, key).bukkit;
	}

	@SuppressWarnings("removal")
	@Override
	public final Component getAdventureChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return PaperComponents.gsonSerializer().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	public abstract Component getAdventureChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key);

	@Override
	public final float getAngle(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return AngleArgument.getAngle(cmdCtx, key);
	}

	@Override
	public final EnumSet<Axis> getAxis(CommandContext<CommandSourceStack> cmdCtx, String key) {
		EnumSet<Axis> set = EnumSet.noneOf(Axis.class);
		EnumSet<net.minecraft.core.Direction.Axis> parsedEnumSet = SwizzleArgument.getSwizzle(cmdCtx, key);
		for (net.minecraft.core.Direction.Axis element : parsedEnumSet) {
			set.add(switch (element) {
				case X -> Axis.X;
				case Y -> Axis.Y;
				case Z -> Axis.Z;
			});
		}
		return set;
	}

	@Override
	public abstract Biome getBiome(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = NAME_CHANGED, from = "getWorld()", to = "f()", in = "1.19")
	public abstract Predicate<Block> getBlockPredicate(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftBlockData")
	public abstract BlockData getBlockState(CommandContext<CommandSourceStack> cmdCtx, String key);

	@Override
	public CommandDispatcher<CommandSourceStack> getBrigadierDispatcher() {
		return getMinecraftServer().vanillaCommandDispatcher.getDispatcher();
	}

	@Override
	public final BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	public final ChatColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ChatColor.getByChar(ColorArgument.getColor(cmdCtx, key).code);
	}

	@Override
	public final BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ComponentSerializer.parse(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Override
	public abstract CommandSourceStack getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> sender);

	@Override
	public final BukkitCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(CommandSourceStack css) {
		try {
			return wrapCommandSender(css.getBukkitSender());
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public final Environment getDimension(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return DimensionArgument.getDimension(cmdCtx, key).getWorld().getEnvironment();
	}

	@Override
	public final Enchantment getEnchantment(CommandContext<CommandSourceStack> cmdCtx, String key) {
		/* TODO: Requires testing */
		return Enchantment.getByKey(fromResourceLocation(Registry.ENCHANTMENT.getKey(ItemEnchantmentArgument.getEnchantment(cmdCtx, key))));
	}

	@Override
	public abstract Object getEntitySelector(CommandContext<CommandSourceStack> cmdCtx, String key, EntitySelector selector)
		throws CommandSyntaxException;

	@Override
	@Unimplemented(because = NAME_CHANGED, from = "getKey()", to = "a()", in = "1.19")
	public abstract EntityType getEntityType(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final FloatRange getFloatRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		MinMaxBounds.Doubles range = RangeArgument.Floats.getRange(cmdCtx, key);
		double low = range.getMin() == null ? -Float.MAX_VALUE : range.getMin();
		double high = range.getMax() == null ? Float.MAX_VALUE : range.getMax();
		return new FloatRange((float) low, (float) high);
	}

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftEntity")
	public abstract FunctionWrapper[] getFunction(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final SimpleFunctionWrapper getFunction(NamespacedKey key) {
		return convertFunction(
			getMinecraftServer().getFunctions().get(new ResourceLocation(key.getNamespace(), key.getKey())).get());
	}

	@Override
	public final Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> result = new HashSet<>();
		for (ResourceLocation resourceLocation : getMinecraftServer().getFunctions().getFunctionNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}

	@Override
	public final IntegerRange getIntRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		MinMaxBounds.Ints range = RangeArgument.Ints.getRange(cmdCtx, key);
		int low = range.getMin() == null ? Integer.MIN_VALUE : range.getMin();
		int high = range.getMax() == null ? Integer.MAX_VALUE : range.getMax();
		return new IntegerRange(low, high);
	}

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftItemStack")
	public abstract ItemStack getItemStack(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftItemStack")
	public abstract Predicate<ItemStack> getItemStackPredicate(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException;

	@Override
	@Unimplemented(because = { NAME_CHANGED, REQUIRES_CSS }, from = "a, b", to = "c(), d()")
	public abstract Location2D getLocation2DBlock(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public Location2D getLocation2DPrecise(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		Vec2 vecPos = Vec2Argument.getVec2(cmdCtx, key);
		return new Location2D(getWorldForCSS(cmdCtx.getSource()), vecPos.x, vecPos.y);
	}

	@Override
	@Unimplemented(because = { NAME_CHANGED, REQUIRES_CSS }, from = "getX(), getY(), getZ()", to = "u(), v(), w()")
	public abstract Location getLocationBlock(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = { NAME_CHANGED, REQUIRES_CSS }, from = "getX(), getY(), getZ()", to = "a(), b(), c()")
	public abstract Location getLocationPrecise(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftLootTable")
	public abstract LootTable getLootTable(CommandContext<CommandSourceStack> cmdCtx, String key);

	@Override
	public final MathOperation getMathOperation(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		// We run this to ensure the argument exists/parses properly
		OperationArgument.getOperation(cmdCtx, key);
		return MathOperation.fromString(CommandAPIHandler.getRawArgumentInput(cmdCtx, key));
	}

	@Override
	@Overridden(in = "1.17 common", because = "1.17 uses ArgumentMinecraftKeyRegistered.f instead of ArgumentMinecraftKeyRegistered.e")
	public NamespacedKey getMinecraftKey(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return fromResourceLocation(ResourceLocationArgument.getId(cmdCtx, key));
	}

	/*
	 * This should return MINECRAFT_SERVER
	 */
	public abstract MinecraftServer getMinecraftServer();

	@Override
	public final <NBTContainer> Object getNBTCompound(CommandContext<CommandSourceStack> cmdCtx, String key,
		Function<Object, NBTContainer> nbtContainerConstructor) {
		return nbtContainerConstructor.apply(CompoundTagArgument.getCompoundTag(cmdCtx, key));
	}

	@Override
	public final String getObjective(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		return ObjectiveArgument.getObjective(cmdCtx, key).getName();
	}

	@Override
	public final String getObjectiveCriteria(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ObjectiveCriteriaArgument.getCriteria(cmdCtx, key).getName();
	}

	@Override
	public final OfflinePlayer getOfflinePlayer(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		OfflinePlayer target = Bukkit
			.getOfflinePlayer(GameProfileArgument.getGameProfiles(cmdCtx, key).iterator().next().getId());
		if (target == null) {
			throw GameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
		} else {
			return target;
		}
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.18, 1.19")
	public abstract ParticleData<?> getParticle(CommandContext<CommandSourceStack> cmdCtx, String key);

	@Override
	public final Player getPlayer(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(GameProfileArgument.getGameProfiles(cmdCtx, key).iterator().next().getId());
		if (target == null) {
			throw GameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
		} else {
			return target;
		}
	}

	@Override
	public final PotionEffectType getPotionEffect(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		return PotionEffectType.getByKey(fromResourceLocation(Registry.MOB_EFFECT.getKey(MobEffectArgument.getEffect(cmdCtx, key))));
	}

	@Override
	public final Recipe getRecipe(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		net.minecraft.world.item.crafting.Recipe<?> recipe = ResourceLocationArgument.getRecipe(cmdCtx, key);
		return new ComplexRecipeImpl(fromResourceLocation(recipe.getId()), recipe.toBukkitRecipe());
	}

	@Override
	public final Rotation getRotation(CommandContext<CommandSourceStack> cmdCtx, String key) {
		Vec2 rotation = RotationArgument.getRotation(cmdCtx, key).getRotation(cmdCtx.getSource());
		return new Rotation(rotation.x, rotation.y);
	}

	@Override
	public final ScoreboardSlot getScoreboardSlot(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return new ScoreboardSlot(ScoreboardSlotArgument.getDisplaySlot(cmdCtx, key));
	}

	@Override
	public final Collection<String> getScoreHolderMultiple(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		return ScoreHolderArgument.getNames(cmdCtx, key);
	}

	@Override
	public final String getScoreHolderSingle(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ScoreHolderArgument.getName(cmdCtx, key);
	}

	@Override
	// Whyyyyyyyyyyyyyyyyyyy
	@Unimplemented(because = NAME_CHANGED, info = "CommandListenerWrapper changes:")
	@Unimplemented(because = NAME_CHANGED, info = "getPosition (1.17)  -> getPosition (1.18) -> e (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "i (1.17)            -> getRotation (1.18) -> l (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "getEntity (1.17)    -> getEntity (1.18)   -> g (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "getWorld (1.17)     -> getLevel (1.18)    -> f (1.19)")
	public abstract BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<CommandSourceStack> cmdCtx, boolean isNative);

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftServer")
	public abstract SimpleCommandMap getSimpleCommandMap();

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftSound")
	public abstract Sound getSound(CommandContext<CommandSourceStack> cmdCtx, String key);

	// TODO: This differs from 1.18 -> 1.18.2 due to biome suggestions. Need to ensure
	//  this doesn't blow up, but it should be covered by the default case (empty)
	@Override
	@Differs(from = "1.18", by = "Use of argument synthetic biome's listSuggestions method")
	public final SuggestionProvider<CommandSourceStack> getSuggestionProvider(SuggestionProviders provider) {
		return switch (provider) {
			case FUNCTION -> (context, builder) -> {
				ServerFunctionManager functionData = getMinecraftServer().getFunctions();
				SharedSuggestionProvider.suggestResource(functionData.getTagNames(), builder, "#");
				return SharedSuggestionProvider.suggestResource(functionData.getFunctionNames(), builder);
			};
			case RECIPES -> net.minecraft.commands.synchronization.SuggestionProviders.ALL_RECIPES;
			case SOUNDS -> net.minecraft.commands.synchronization.SuggestionProviders.AVAILABLE_SOUNDS;
			case ADVANCEMENTS -> (cmdCtx, builder) -> {
				return SharedSuggestionProvider.suggestResource(getMinecraftServer().getAdvancements().getAllAdvancements()
					.stream().map(net.minecraft.advancements.Advancement::getId), builder);
			};
			case LOOT_TABLES -> (cmdCtx, builder) -> {
				return SharedSuggestionProvider.suggestResource(getMinecraftServer().getLootTables().getIds(), builder);
			};
			case BIOMES -> _ArgumentSyntheticBiome()::listSuggestions;
			case ENTITIES -> net.minecraft.commands.synchronization.SuggestionProviders.SUMMONABLE_ENTITIES;
			default -> (context, builder) -> Suggestions.empty();
		};
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, from = "1.18.2", to = "1.19")
	@Differs(from = "1.18.2", by = "getTag() now returns a Collection<> instead of a Tag<>, so don't have to call .getValues()")
	public abstract SimpleFunctionWrapper[] getTag(NamespacedKey key);

	@Override
	public final Set<NamespacedKey> getTags() {
		Set<NamespacedKey> result = new HashSet<>();
		for (ResourceLocation resourceLocation : getMinecraftServer().getFunctions().getFunctionNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}

	@Override
	public final String getTeam(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return TeamArgument.getTeam(cmdCtx, key).getName();
	}

	@Override
	public final int getTime(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return (int) cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public final UUID getUUID(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return UuidArgument.getUuid(cmdCtx, key);
	}

	@Override
	public abstract World getWorldForCSS(CommandSourceStack css);

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "VanillaCommandWrapper")
	public abstract boolean isVanillaCommandWrapper(Command command);

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION)
	public abstract void reloadDataPacks();

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftPlayer")
	public abstract void resendPackets(Player player);
}
