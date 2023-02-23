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

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.NAME_CHANGED;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.REQUIRES_CRAFTBUKKIT;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.REQUIRES_CSS;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.VERSION_SPECIFIC_IMPLEMENTATION;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
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
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.ArgumentSubType;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.Overridden;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.ParticleData;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import io.papermc.paper.text.PaperComponents;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandFunction;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.FunctionArgument;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;

/**
 * Common NMS code To ensure that this code actually works across all versions
 * of Minecraft that this is supposed to support (1.17+), you should be
 * compiling this code against all of the declared Maven profiles specified in
 * this submodule's pom.xml file, by running the following commands:
 * <ul>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_19_3_R2</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_19_R1</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_18_2_R2</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_18_R1</code></li>
 * <li><code>mvn clean package -pl :commandapi-bukkit-nms-common -P Platform.Bukkit,Spigot_1_17_R1</code></li>
 * </ul>
 * Any of these that do not work should be removed or implemented otherwise
 * (introducing another NMS_Common module perhaps?
 */
public abstract class NMS_Common extends CommandAPIBukkit<CommandSourceStack> {

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
	public ArgumentType<?> _ArgumentDimension() {
		return DimensionArgument.dimension();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19.3")
	public abstract ArgumentType<?> _ArgumentEnchantment();

	@Override
	// I mean... really? Why?
	@Unimplemented(because = NAME_CHANGED, info = "a (1.17)                -> entity   (1.18) -> a (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "multipleEntities (1.17) -> entities (1.18) -> b (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "c (1.17)                -> player   (1.18) -> c (1.19)")
	@Unimplemented(because = NAME_CHANGED, info = "d (1.17)                -> players  (1.18) -> d (1.19)")
	public abstract ArgumentType<?> _ArgumentEntity(ArgumentSubType subType);

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19.3")
	public abstract ArgumentType<?> _ArgumentEntitySummon();

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
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19.3")
	public abstract ArgumentType<?> _ArgumentMobEffect();

	@Override
	public final ArgumentType<?> _ArgumentNBTCompound() {
		return CompoundTagArgument.compoundTag();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19.3")
	public abstract ArgumentType<?> _ArgumentParticle();

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
	public final ArgumentType<?> _ArgumentScoreholder(ArgumentSubType subType) {
		return switch(subType) {
			case SCOREHOLDER_SINGLE -> ScoreHolderArgument.scoreHolder();
			case SCOREHOLDER_MULTIPLE -> ScoreHolderArgument.scoreHolders();
			default -> throw new IllegalArgumentException("Unexpected value: " + subType);
		};
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
		ToIntFunction<CommandSourceStack> appliedObj = (CommandSourceStack css) -> this.<MinecraftServer>getMinecraftServer().getFunctions()
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
	@Unimplemented(because = NAME_CHANGED, from = "getWorld()", to = "f()", in = "1.19")
	public abstract Predicate<Block> getBlockPredicate(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftBlockData")
	public abstract BlockData getBlockState(CommandContext<CommandSourceStack> cmdCtx, String key);

	@SuppressWarnings("resource")
	@Override
	public CommandDispatcher<CommandSourceStack> getBrigadierDispatcher() {
		return this.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher.getDispatcher();
	}

	@Override
	public final BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	public final ChatColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ChatColor.getByChar(ColorArgument.getColor(cmdCtx, key).getChar());
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
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftWorld", info = "CraftWorld is implicitly referenced by ServerLevel#getWorld, due to package renaming, it can't resolve at runtime")
	public abstract World getDimension(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19.3")
	public abstract Enchantment getEnchantment(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = NAME_CHANGED, from = "getKey()", to = "a()", in = "1.19")
	public abstract EntityType getEntityType(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final FloatRange getFloatRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		MinMaxBounds.Doubles range = RangeArgument.Floats.getRange(cmdCtx, key);
		final Double lowBoxed = range.getMin();
		final Double highBoxed = range.getMax();
		final double low = lowBoxed == null ? -Float.MAX_VALUE : lowBoxed;
		final double high = highBoxed == null ? Float.MAX_VALUE : highBoxed;
		return new FloatRange((float) low, (float) high);
	}

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftEntity")
	public abstract FunctionWrapper[] getFunction(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final SimpleFunctionWrapper getFunction(NamespacedKey key) {
		final ResourceLocation resourceLocation = new ResourceLocation(key.getNamespace(), key.getKey());
		Optional<CommandFunction> commandFunctionOptional = this.<MinecraftServer>getMinecraftServer().getFunctions().get(resourceLocation);
		if(commandFunctionOptional.isPresent()) {
			return convertFunction(commandFunctionOptional.get());
		} else {
			throw new IllegalStateException("Failed to get defined function " + key
				+ "! This should never happen - please report this to the CommandAPI"
				+ "developers, we'd love to know how you got this error message!");
		}
	}

	@Override
	public final Set<NamespacedKey> getFunctions() {
		Set<NamespacedKey> result = new HashSet<>();
		for (ResourceLocation resourceLocation : this.<MinecraftServer>getMinecraftServer().getFunctions().getFunctionNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}

	@Override
	public final IntegerRange getIntRange(CommandContext<CommandSourceStack> cmdCtx, String key) {
		MinMaxBounds.Ints range = RangeArgument.Ints.getRange(cmdCtx, key);
		final Integer lowBoxed = range.getMin();
		final Integer highBoxed = range.getMax();
		final int low = lowBoxed == null ? Integer.MIN_VALUE : lowBoxed;
		final int high = highBoxed == null ? Integer.MAX_VALUE : highBoxed;
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

	@Override
	public final <NBTContainer> Object getNBTCompound(CommandContext<CommandSourceStack> cmdCtx, String key,
		Function<Object, NBTContainer> nbtContainerConstructor) {
		return nbtContainerConstructor.apply(CompoundTagArgument.getCompoundTag(cmdCtx, key));
	}

	@Override
	public final Objective getObjective(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException {
		String objectiveName = ObjectiveArgument.getObjective(cmdCtx, key).getName();
		return Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objectiveName);
	}

	@Override
	public final String getObjectiveCriteria(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ObjectiveCriteriaArgument.getCriteria(cmdCtx, key).getName();
	}

	@Override
	public final OfflinePlayer getOfflinePlayer(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return Bukkit.getOfflinePlayer(GameProfileArgument.getGameProfiles(cmdCtx, key).iterator().next().getId());
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
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.18")
	public abstract PotionEffectType getPotionEffect(CommandContext<CommandSourceStack> cmdCtx, String key)
		throws CommandSyntaxException;

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, info = """
		1.17 has what appears to be a different obfuscation for recipe.getId().
		I can't be bothered to figure out what it is, but all I know is it doesn't work,
		and we need to move it outta here!
		""")
	public abstract Recipe getRecipe(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final Rotation getRotation(CommandContext<CommandSourceStack> cmdCtx, String key) {
		Vec2 rotation = RotationArgument.getRotation(cmdCtx, key).getRotation(cmdCtx.getSource());
		return new Rotation(rotation.y, rotation.x);
	}

	@Override
	public final ScoreboardSlot getScoreboardSlot(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ScoreboardSlot.ofMinecraft(ScoreboardSlotArgument.getDisplaySlot(cmdCtx, key));
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
	public abstract Object getSound(CommandContext<CommandSourceStack> cmdCtx, String key, ArgumentSubType subType);

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, info = """
		The various methods that this uses is obfuscated to different method
		names for different versions. For example, getMinecraftServer().getLootTables.getIds()
		is mapped to a different method in 1.18 compared to 1.19.2. This also has various other
		implications across all sorts of versions, so it's much more reliable to just implement
		them in every version.
		""")
	public abstract SuggestionProvider<CommandSourceStack> getSuggestionProvider(SuggestionProviders provider);

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, from = "1.18.2", to = "1.19")
	@Differs(from = "1.18.2", by = "getTag() now returns a Collection<> instead of a Tag<>, so don't have to call .getValues()")
	public abstract SimpleFunctionWrapper[] getTag(NamespacedKey key);

	@Override
	public final Set<NamespacedKey> getTags() {
		Set<NamespacedKey> result = new HashSet<>();
		for (ResourceLocation resourceLocation : this.<MinecraftServer>getMinecraftServer().getFunctions().getTagNames()) {
			result.add(fromResourceLocation(resourceLocation));
		}
		return result;
	}

	@Override
	public Team getTeam(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		String teamName = TeamArgument.getTeam(cmdCtx, key).getName();
		return Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName);
	}

	@Override
	public final int getTime(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public final UUID getUUID(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return UuidArgument.getUuid(cmdCtx, key);
	}

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
