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
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
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

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import de.tr7zw.nbtapi.NBTContainer;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;

public interface NMS<CommandListenerWrapper> {

	/* Argument types */
	ArgumentType<?> _ArgumentAngle();

	ArgumentType<?> _ArgumentAxis();

	ArgumentType<?> _ArgumentBlockPredicate();

	ArgumentType<?> _ArgumentBlockState();

	ArgumentType<?> _ArgumentChat();

	ArgumentType<?> _ArgumentChatComponent();

	ArgumentType<?> _ArgumentChatFormat();

	ArgumentType<?> _ArgumentDimension();

	ArgumentType<?> _ArgumentEnchantment();

	ArgumentType<?> _ArgumentEntity(EntitySelector selector);

	ArgumentType<?> _ArgumentEntitySummon();

	ArgumentType<?> _ArgumentFloatRange();

	ArgumentType<?> _ArgumentIntRange();

	ArgumentType<?> _ArgumentItemPredicate();

	ArgumentType<?> _ArgumentItemStack();

	ArgumentType<?> _ArgumentMathOperation();

	ArgumentType<?> _ArgumentMinecraftKeyRegistered();

	ArgumentType<?> _ArgumentMobEffect();

	ArgumentType<?> _ArgumentNBTCompound();

	ArgumentType<?> _ArgumentParticle();

	ArgumentType<?> _ArgumentPosition();

	ArgumentType<?> _ArgumentPosition2D();

	ArgumentType<?> _ArgumentProfile();

	ArgumentType<?> _ArgumentRotation();

	ArgumentType<?> _ArgumentScoreboardCriteria();

	ArgumentType<?> _ArgumentScoreboardObjective();

	ArgumentType<?> _ArgumentScoreboardSlot();

	ArgumentType<?> _ArgumentScoreboardTeam();

	ArgumentType<?> _ArgumentScoreholder(boolean single);

	ArgumentType<?> _ArgumentTag();

	ArgumentType<?> _ArgumentTime();

	ArgumentType<?> _ArgumentUUID();

	ArgumentType<?> _ArgumentVec2();

	ArgumentType<?> _ArgumentVec3();

	/**
	 * A String array of Minecraft versions that this NMS implementation is
	 * compatible with. For example, ["1.14", "1.14.1", "1.14.2", "1.14.3"]. This
	 * can be found by opening a Minecraft jar file, viewing the version.json file
	 * and reading the object "name".
	 * 
	 * @return A String array of compatible Minecraft versions
	 */
	String[] compatibleVersions();

	String convert(ItemStack is);

	String convert(Particle particle);

	String convert(PotionEffectType potion);

	String convert(Sound sound);

	/**
	 * Creates a JSON file that describes the hierarchical structure of the commands
	 * that have been registered by the server.
	 * 
	 * @param file       The JSON file to write to
	 * @param dispatcher The Brigadier CommandDispatcher
	 * @throws IOException When the file fails to be written to
	 */
	void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher) throws IOException;

	Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Component getAdventureChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Component getAdventureChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Biome getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	/**
	 * Returns the Brigadier CommandDispatcher from the NMS CommandDispatcher
	 * 
	 * @return A Brigadier CommandDispatcher
	 */
	CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher();

	BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	/**
	 * Converts a CommandSender into a CLW
	 * 
	 * @param sender the command sender to convert
	 * @return a CLW.
	 */
	CommandListenerWrapper getCLWFromCommandSender(CommandSender sender);

	/**
	 * Returns a CommandSender of a given CommandListenerWrapper object
	 * 
	 * @param clw The CommandListenerWrapper object
	 * @return A CommandSender (not proxied) from the command listener wrapper
	 */
	CommandSender getCommandSenderFromCSS(CommandListenerWrapper clw);

	Environment getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String key, EntitySelector selector)
			throws CommandSyntaxException;

	EntityType getEntityType(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	SimpleFunctionWrapper getFunction(NamespacedKey key);

	Set<NamespacedKey> getFunctions();

	IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	ItemStack getItemStack(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Predicate<ItemStack> getItemStackPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	String getKeyedAsString(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Location2D getLocation2DBlock(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;
	
	Location2D getLocation2DPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	Location getLocationBlock(CommandContext<CommandListenerWrapper> cmdCtx, String str) throws CommandSyntaxException;

	Location getLocationPrecise(CommandContext<CommandListenerWrapper> cmdCtx, String str)
			throws CommandSyntaxException;

	LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	NBTContainer getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	String getObjective(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException;

	String getObjectiveCriteria(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Particle getParticle(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;
	OfflinePlayer getOfflinePlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	Recipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	/**
	 * Retrieves a CommandSender, given some CommandContext. This method should
	 * handle Proxied CommandSenders for entities if a Proxy is being used.
	 * 
	 * @param cmdCtx      The
	 *                    <code>CommandContext&lt;CommandListenerWrapper&gt;</code>
	 *                    for a given command
	 * @param forceNative whether or not the CommandSender should be a
	 *                    NativeProxyCommandSender or not
	 * @return A CommandSender instance (such as a ProxiedNativeCommandSender or
	 *         Player)
	 */
	CommandSender getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean forceNative);

	/**
	 * Returns the Server's internal (OBC) CommandMap
	 * 
	 * @return A SimpleCommandMap from the OBC server
	 */
	SimpleCommandMap getSimpleCommandMap();

	Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	/**
	 * Retrieve a specific NMS implemented SuggestionProvider
	 * 
	 * @param provider The SuggestionProvider type to retrieve
	 * @return A SuggestionProvider that matches the SuggestionProviders input
	 */
	SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider);

	SimpleFunctionWrapper[] getTag(NamespacedKey key);

	Set<NamespacedKey> getTags();

	String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	World getWorldForCSS(CommandListenerWrapper clw);

	/**
	 * Checks if a Command is an instance of the OBC VanillaCommandWrapper
	 * 
	 * @param command The Command to check
	 * @return true if Command is an instance of VanillaCommandWrapper
	 */
	boolean isVanillaCommandWrapper(Command command);

	/**
	 * Reloads the datapacks by using the updated the commandDispatcher tree
	 * 
	 * @throws SecurityException        reflection exception
	 * @throws NoSuchFieldException     reflection exception
	 * @throws IllegalAccessException   reflection exception
	 * @throws IllegalArgumentException reflection exception
	 */
	default void reloadDataPacks()
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	}

	/**
	 * Resends the command dispatcher's set of commands to a player.
	 * 
	 * @param player the player to send the command graph packet to
	 */
	void resendPackets(Player player);

	HelpTopic generateHelpTopic(String commandName, String shortDescription, String fullDescription, String permission);

	void addToHelpMap(Map<String, HelpTopic> helpTopicsToAdd);

}
