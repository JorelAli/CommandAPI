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
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.REQUIRES_MINECRAFT_SERVER;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.VERSION_SPECIFIC_IMPLEMENTATION;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
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

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.SuggestionProviders;
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
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.advancements.critereon.CriterionConditionValue;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.arguments.ArgumentAngle;
import net.minecraft.commands.arguments.ArgumentChat;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentChatFormat;
import net.minecraft.commands.arguments.ArgumentCriterionValue;
import net.minecraft.commands.arguments.ArgumentDimension;
import net.minecraft.commands.arguments.ArgumentEnchantment;
import net.minecraft.commands.arguments.ArgumentEntitySummon;
import net.minecraft.commands.arguments.ArgumentMathOperation;
import net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered;
import net.minecraft.commands.arguments.ArgumentMobEffect;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.commands.arguments.ArgumentParticle;
import net.minecraft.commands.arguments.ArgumentProfile;
import net.minecraft.commands.arguments.ArgumentScoreboardCriteria;
import net.minecraft.commands.arguments.ArgumentScoreboardObjective;
import net.minecraft.commands.arguments.ArgumentScoreboardSlot;
import net.minecraft.commands.arguments.ArgumentScoreboardTeam;
import net.minecraft.commands.arguments.ArgumentScoreholder;
import net.minecraft.commands.arguments.ArgumentUUID;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentRotation;
import net.minecraft.commands.arguments.coordinates.ArgumentRotationAxis;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2I;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.commands.arguments.item.ArgumentTag;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.phys.Vec2F;

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
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class NMS_Common<T> implements NMS<T> {

	@Override
	public final ArgumentType<?> _ArgumentAngle() {
		return ArgumentAngle.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentAxis() {
		return ArgumentRotationAxis.a();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentBlockPredicate();

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentBlockState();

	@Override
	public final ArgumentType<?> _ArgumentChat() {
		return ArgumentChat.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentChatComponent() {
		return ArgumentChatComponent.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentChatFormat() {
		return ArgumentChatFormat.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentDimension() {
		return ArgumentDimension.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentEnchantment() {
		return ArgumentEnchantment.a();
	}

	@Override
	// TODO:
	@Unimplemented(because = NAME_CHANGED, from = "??", to = "b")
	public abstract ArgumentType<?> _ArgumentEntity(EntitySelector selector);

	@Override
	public final ArgumentType<?> _ArgumentEntitySummon() {
		return ArgumentEntitySummon.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentFloatRange() {
		return ArgumentCriterionValue.b();
	}

	@Override
	public final ArgumentType<?> _ArgumentIntRange() {
		return ArgumentCriterionValue.a();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentItemPredicate();

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract ArgumentType<?> _ArgumentItemStack();

	@Override
	public final ArgumentType<?> _ArgumentMathOperation() {
		return ArgumentMathOperation.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentMinecraftKeyRegistered() {
		return ArgumentMinecraftKeyRegistered.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentMobEffect() {
		return ArgumentMobEffect.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentNBTCompound() {
		return ArgumentNBTTag.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentParticle() {
		return ArgumentParticle.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentPosition() {
		return ArgumentPosition.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentPosition2D() {
		return ArgumentVec2I.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentProfile() {
		return ArgumentProfile.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentRotation() {
		return ArgumentRotation.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardCriteria() {
		return ArgumentScoreboardCriteria.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardObjective() {
		return ArgumentScoreboardObjective.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardSlot() {
		return ArgumentScoreboardSlot.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreboardTeam() {
		return ArgumentScoreboardTeam.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentScoreholder(boolean single) {
		return single ? ArgumentScoreholder.a() : ArgumentScoreholder.b();
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.18.2")
	public abstract ArgumentType<?> _ArgumentSyntheticBiome();

	@Override
	public final ArgumentType<?> _ArgumentTag() {
		return ArgumentTag.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentTime() {
		return ArgumentTag.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentUUID() {
		return ArgumentUUID.a();
	}

//	@SuppressWarnings("removal")
//	@Override
//	public final Component getAdventureChat(CommandContext cmdCtx, String key)
//			throws CommandSyntaxException {
//		return PaperComponents.gsonSerializer().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
//	}
//
//	@SuppressWarnings("removal")
//	@Override
//	public final Component getAdventureChatComponent(CommandContext cmdCtx, String key) {
//		return PaperComponents.gsonSerializer()
//				.deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
//	}

	@Override
	public final ArgumentType<?> _ArgumentVec2() {
		return ArgumentVec2.a();
	}

	@Override
	public final ArgumentType<?> _ArgumentVec3() {
		return ArgumentVec3.a();
	}

	@Override
	public final BaseComponent[] getChat(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public final BaseComponent[] getChatComponent(CommandContext cmdCtx, String str) {
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, str)));
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

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.19")
	public abstract void createDispatcherFile(File file, CommandDispatcher<T> dispatcher) throws IOException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CustomHelpTopic")
	public abstract HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription,
			String permission);

	@Override
	public final org.bukkit.advancement.Advancement getAdvancement(CommandContext cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentMinecraftKeyRegistered.a(cmdCtx, key).bukkit;
	}

	@Override
	public final float getAngle(CommandContext cmdCtx, String key) {
		return ArgumentAngle.a(cmdCtx, key);
	}

	@Override
	public final EnumSet<Axis> getAxis(CommandContext cmdCtx, String key) {
		EnumSet<Axis> set = EnumSet.noneOf(Axis.class);
		EnumSet<EnumDirection.EnumAxis> parsedEnumSet = ArgumentRotationAxis.a(cmdCtx, key);
		for (EnumDirection.EnumAxis element : parsedEnumSet) {
			set.add(switch (element) {
				case a -> Axis.X;
				case b -> Axis.Y;
				case c -> Axis.Z;
			});
		}
		return set;
	}

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract T getCLWFromCommandSender(CommandSender sender);

	@Override
	public final Environment getDimension(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentDimension.a(cmdCtx, key).getWorld().getEnvironment();
	}

	@Override
	public final FloatRange getFloatRange(CommandContext cmdCtx, String key) {
		CriterionConditionValue.DoubleRange range = ArgumentCriterionValue.a.a(cmdCtx, key);
		double low = range.a() == null ? -Float.MAX_VALUE : range.a();
		double high = range.b() == null ? Float.MAX_VALUE : range.b();
		return new FloatRange((float) low, (float) high);
	}

	@Override
	public final IntegerRange getIntRange(CommandContext cmdCtx, String key) {
		CriterionConditionValue.IntegerRange range = ArgumentCriterionValue.b.a(cmdCtx, key);
		int low = (range.a() == null) ? Integer.MIN_VALUE : ((Integer) range.a()).intValue();
		int high = (range.b() == null) ? Integer.MAX_VALUE : ((Integer) range.b()).intValue();
		return new IntegerRange(low, high);
	}

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftItemStack")
	public abstract ItemStack getItemStack(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final String getKeyedAsString(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// XXX: In 1.17, Spigot/Minecraft/whoever introduced a bug where getting the
		// value of ResourceLocation.toString() returns namespace:namespace instead of
		// namespace:key. We fix this by constructing it manually instead of using the
		// ResourceLocation.toString() method!
		MinecraftKey resourceLocation = ArgumentMinecraftKeyRegistered.e(cmdCtx, key);
		return resourceLocation.b() + ":" + resourceLocation.a();
	}

	@Override
	public final MathOperation getMathOperation(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		// We run this to ensure the argument exists/parses properly
		ArgumentMathOperation.a(cmdCtx, key);
		return MathOperation.fromString(CommandAPIHandler.getRawArgumentInput(cmdCtx, key));
	}

	@SuppressWarnings("deprecation")
	@Override
	public NamespacedKey getMinecraftKey(CommandContext cmdCtx, String key) {
		MinecraftKey resourceLocation = ArgumentMinecraftKeyRegistered.e(cmdCtx, key);
		return new NamespacedKey(resourceLocation.b(), resourceLocation.a());
	}

	@Override
	public <NBTContainer> Object getNBTCompound(CommandContext<T> cmdCtx, String key,
			Function<Object, NBTContainer> nbtContainerConstructor) {
		return nbtContainerConstructor.apply(ArgumentNBTTag.a(cmdCtx, key));
	}

	@Override
	public final Rotation getRotation(CommandContext cmdCtx, String key) {
		Vec2F rotation = ArgumentRotation.a(cmdCtx, key).b((CommandListenerWrapper) cmdCtx.getSource());
		return new Rotation(rotation.i, rotation.j);
	}

	@Override
	public final ScoreboardSlot getScoreboardSlot(CommandContext cmdCtx, String key) {
		return new ScoreboardSlot(ArgumentScoreboardSlot.a(cmdCtx, key));
	}

	@Override
	public final Collection<String> getScoreHolderMultiple(CommandContext cmdCtx, String key)
			throws CommandSyntaxException {
		return ArgumentScoreholder.b(cmdCtx, key);
	}

	@Override
	public final String getScoreHolderSingle(CommandContext cmdCtx, String key) throws CommandSyntaxException {
		return ArgumentScoreholder.a(cmdCtx, key);
	}

	@Override
	public final int getTime(CommandContext cmdCtx, String key) {
		return (Integer) cmdCtx.getArgument(key, Integer.class);
	}

	@Override
	public final UUID getUUID(CommandContext cmdCtx, String key) {
		return ArgumentUUID.a(cmdCtx, key);
	}

	@Override
	public abstract Component getAdventureChat(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public abstract Component getAdventureChatComponent(CommandContext<T> cmdCtx, String key);

	@Override
	public abstract Biome getBiome(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = NAME_CHANGED, from = "getWorld()", to = "f()", in = "1.19")
	public abstract Predicate<Block> getBlockPredicate(CommandContext<T> cmdCtx, String key)
			throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftBlockData")
	public abstract BlockData getBlockState(CommandContext<T> cmdCtx, String key);

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract CommandDispatcher<T> getBrigadierDispatcher();

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftChatMessage")
	public abstract ChatColor getChatColor(CommandContext<T> cmdCtx, String key);

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract CommandSender getCommandSenderFromCSS(T clw);

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftEnchantment")
	public abstract Enchantment getEnchantment(CommandContext<T> cmdCtx, String key);

	@Override
	public abstract Object getEntitySelector(CommandContext<T> cmdCtx, String key, EntitySelector selector)
			throws CommandSyntaxException;

	@Override
	@Unimplemented(because = NAME_CHANGED, from = "getKey()", to = "a()", in = "1.19")
	public abstract EntityType getEntityType(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract FunctionWrapper[] getFunction(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract SimpleFunctionWrapper getFunction(NamespacedKey key);

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract Set<NamespacedKey> getFunctions();

	@Override
	public abstract Predicate<ItemStack> getItemStackPredicate(CommandContext<T> cmdCtx, String key)
			throws CommandSyntaxException;

	@Override
	@Unimplemented(because = { NAME_CHANGED, REQUIRES_CSS }, from = "a, b", to = "c(), d()")
	public abstract Location2D getLocation2DBlock(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract Location2D getLocation2DPrecise(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = { NAME_CHANGED, REQUIRES_CSS }, from = "getX(), getY(), getZ()", to = "u(), v(), w()")
	public abstract Location getLocationBlock(CommandContext<T> cmdCtx, String str) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = { NAME_CHANGED, REQUIRES_CSS }, from = "getX(), getY(), getZ()", to = "a(), b(), c()")
	public abstract Location getLocationPrecise(CommandContext<T> cmdCtx, String str) throws CommandSyntaxException;

	@Override
	public abstract LootTable getLootTable(CommandContext<T> cmdCtx, String key);

	@Override
	public abstract String getObjective(CommandContext<T> cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException;

	@Override
	public abstract String getObjectiveCriteria(CommandContext<T> cmdCtx, String key);

	@Override
	public final OfflinePlayer getOfflinePlayer(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		OfflinePlayer target = Bukkit
				.getOfflinePlayer(((GameProfile) ArgumentProfile.a(cmdCtx, str).iterator().next()).getId());
		if (target == null)
			throw ArgumentProfile.a.create();
		return target;
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION, introducedIn = "1.18, 1.19")
	public abstract ParticleData<?> getParticle(CommandContext<T> cmdCtx, String key);

	@Override
	public final Player getPlayer(CommandContext cmdCtx, String str) throws CommandSyntaxException {
		Player target = Bukkit.getPlayer(((GameProfile) ArgumentProfile.a(cmdCtx, str).iterator().next()).getId());
		if (target == null)
			throw ArgumentProfile.a.create();
		return target;
	}

	@Override
	public abstract PotionEffectType getPotionEffect(CommandContext<T> cmdCtx, String key)
			throws CommandSyntaxException;

	@Override
	public abstract Recipe getRecipe(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract CommandSender getSenderForCommand(CommandContext<T> cmdCtx, boolean forceNative);

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftServer")
	public abstract SimpleCommandMap getSimpleCommandMap();

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "CraftSound")
	public abstract Sound getSound(CommandContext<T> cmdCtx, String key);

	@Override
	public abstract SuggestionProvider<T> getSuggestionProvider(SuggestionProviders provider);

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract SimpleFunctionWrapper[] getTag(NamespacedKey key);

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract Set<NamespacedKey> getTags();

	@Override
	public abstract String getTeam(CommandContext<T> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract World getWorldForCSS(T clw);

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT, classNamed = "VanillaCommandWrapper")
	public abstract boolean isVanillaCommandWrapper(Command command);

	@Override
	public abstract void reloadDataPacks();

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract void resendPackets(Player player);
}
