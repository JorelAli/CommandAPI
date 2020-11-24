package dev.jorel.commandapi.nms;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.EntitySelector;
import dev.jorel.commandapi.arguments.ICustomProvidedArgument.SuggestionProviders;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.FunctionWrapper;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.Location2D;
import dev.jorel.commandapi.wrappers.MathOperation;
import dev.jorel.commandapi.wrappers.Rotation;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.md_5.bungee.api.chat.BaseComponent;

public interface NMS<CommandListenerWrapper> {

	/**
	 * Resends the command dispatcher's set of commands to a player.
	 * 
	 * @param player the player to send the command graph packet to
	 */
	void resendPackets(Player player);

	/**
	 * A String array of Minecraft versions that this NMS implementation is
	 * compatible with. For example, ["1.14", "1.14.1", "1.14.2", "1.14.3"]. This
	 * can be found by opening a Minecraft jar file, viewing the version.json file
	 * and reading the object "name".
	 * 
	 * @return A String array of compatible Minecraft versions
	 */
	String[] compatibleVersions();

	/**
	 * Creates a JSON file that describes the hierarchical structure of the commands
	 * that have been registered by the server.
	 * 
	 * @param file       The JSON file to write to
	 * @param dispatcher The Brigadier CommandDispatcher
	 * @throws IOException When the file fails to be written to
	 */
	void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher) throws IOException;

	/**
	 * Retrieve a specific NMS implemented SuggestionProvider
	 * 
	 * @param provider The SuggestionProvider type to retrieve
	 * @return A SuggestionProvider that matches the SuggestionProviders input
	 */
	SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider);

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
	 * Returns a CommandSender of a given CommandListenerWrapper object
	 * 
	 * @param clw The CommandListenerWrapper object
	 * @return A CommandSender (not proxied) from the command listener wrapper
	 */
	CommandSender getCommandSenderForCLW(CommandListenerWrapper clw);

	/**
	 * Converts a CommandSender into a CLW
	 * 
	 * @param sender the command sender to convert
	 * @return a CLW.
	 */
	CommandListenerWrapper getCLWFromCommandSender(CommandSender sender);

	/**
	 * Returns the Brigadier CommandDispatcher from the NMS CommandDispatcher
	 * 
	 * @return A Brigadier CommandDispatcher
	 */
	CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher();

	/**
	 * Checks if a Command is an instance of the OBC VanillaCommandWrapper
	 * 
	 * @param command The Command to check
	 * @return true if Command is an instance of VanillaCommandWrapper
	 */
	boolean isVanillaCommandWrapper(Command command);

	/**
	 * Returns the Server's internal (OBC) CommandMap
	 * 
	 * @return A SimpleCommandMap from the OBC server
	 */
	SimpleCommandMap getSimpleCommandMap();

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
	};

	/* Argument implementations with CommandSyntaxExceptions */
	Advancement getAdvancement(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Predicate<Block> getBlockPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Environment getDimension(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	ItemStack getItemStack(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Object getEntitySelector(CommandContext<CommandListenerWrapper> cmdCtx, String key, EntitySelector selector)
			throws CommandSyntaxException;

	EntityType getEntityType(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	FunctionWrapper[] getFunction(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	Predicate<ItemStack> getItemStackPredicate(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	String getKeyedAsString(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Location getLocation(CommandContext<CommandListenerWrapper> cmdCtx, String key, LocationType locationType)
			throws CommandSyntaxException;

	Location2D getLocation2D(CommandContext<CommandListenerWrapper> cmdCtx, String key, LocationType locationType2d)
			throws CommandSyntaxException;

	String getObjective(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws IllegalArgumentException, CommandSyntaxException;

	Player getPlayer(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	PotionEffectType getPotionEffect(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	Recipe getRecipe(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	Collection<String> getScoreHolderMultiple(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	String getScoreHolderSingle(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	String getTeam(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	MathOperation getMathOperation(CommandContext<CommandListenerWrapper> cmdCtx, String key)
			throws CommandSyntaxException;

	/* Argument implementations without CommandSyntaxExceptions */
	float getAngle(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	EnumSet<Axis> getAxis(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Biome getBiome(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	BlockData getBlockState(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Enchantment getEnchantment(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	LootTable getLootTable(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	NBTContainer getNBTCompound(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	String getObjectiveCriteria(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Particle getParticle(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Rotation getRotation(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	ScoreboardSlot getScoreboardSlot(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Sound getSound(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	int getTime(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	UUID getUUID(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	/* Argument types */
	ArgumentType<?> _ArgumentAngle();

	ArgumentType<?> _ArgumentAxis();

	ArgumentType<?> _ArgumentBlockPredicate();

	ArgumentType<?> _ArgumentBlockState();

	ArgumentType<?> _ArgumentChat();

	ArgumentType<?> _ArgumentChatFormat();

	ArgumentType<?> _ArgumentChatComponent();

	ArgumentType<?> _ArgumentDimension();

	ArgumentType<?> _ArgumentEntity(EntitySelector selector);

	ArgumentType<?> _ArgumentEntitySummon();

	ArgumentType<?> _ArgumentEnchantment();

	ArgumentType<?> _ArgumentFloatRange();

	ArgumentType<?> _ArgumentIntRange();

	ArgumentType<?> _ArgumentItemPredicate();

	ArgumentType<?> _ArgumentItemStack();

	ArgumentType<?> _ArgumentMinecraftKeyRegistered();

	ArgumentType<?> _ArgumentMathOperation();

	ArgumentType<?> _ArgumentMobEffect();

	ArgumentType<?> _ArgumentNBTCompound();

	ArgumentType<?> _ArgumentProfile();

	ArgumentType<?> _ArgumentParticle();

	ArgumentType<?> _ArgumentPosition();

	ArgumentType<?> _ArgumentPosition2D();

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

	String convert(ItemStack is);

	String convert(Particle particle);

	String convert(PotionEffectType potion);

	String convert(Sound sound);

	SimpleFunctionWrapper[] getTag(NamespacedKey key);

	SimpleFunctionWrapper getFunction(NamespacedKey key);

	Set<NamespacedKey> getFunctions();

	Set<NamespacedKey> getTags();

	default void inject(CommandContext<CommandListenerWrapper> cmdCtx) {};
	default Class<?> g() { return null; };

}
