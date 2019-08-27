package io.github.jorelali.commandapi.api.nms;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

import org.bukkit.Axis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.advancement.Advancement;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument.SuggestionProviders;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument.EntitySelector;
import io.github.jorelali.commandapi.api.arguments.LocationType;
import io.github.jorelali.commandapi.api.wrappers.FloatRange;
import io.github.jorelali.commandapi.api.wrappers.FunctionWrapper;
import io.github.jorelali.commandapi.api.wrappers.IntegerRange;
import io.github.jorelali.commandapi.api.wrappers.Location2D;
import io.github.jorelali.commandapi.api.wrappers.Rotation;
import net.md_5.bungee.api.chat.BaseComponent;

public interface NMS {

	//Returns the world in which a command sender is from
	default World getCommandSenderWorld(CommandSender sender) {
		if(sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getWorld();
		} else if(sender instanceof ProxiedCommandSender) {
			return getCommandSenderWorld(((ProxiedCommandSender) sender).getCallee());
		} else if(sender instanceof Entity) {
			return ((Entity) sender).getWorld();
		} else {
			return null;
		}
	}
	
	/**
	 * Resends the command dispatcher's set of commands to a player.
	 * @param player
	 */
	void resendPackets(Player player);
	
	/**
	 * A String array of Minecraft versions that this NMS implementation
	 * is compatible with. For example, ["1.14", "1.14.1", "1.14.2", "1.14.3"]
	 * @return A String array of compatible Minecraft versions
	 */
	String[] compatibleVersions();
	
	/**
	 * Creates a JSON file that describes the hierarchical structure of
	 * the commands that have been registered by the server.
	 * @param server The NMS MinecraftServer instance
	 * @param file The JSON file to write to
	 * @param dispatcher The Brigadier CommandDispatcher
	 * @throws IOException When the file fails to be written to
	 */
	void createDispatcherFile(Object server, File file, CommandDispatcher<?> dispatcher) throws IOException;
	
	/**
	 * Retrieve a specific NMS implemented SuggestionProvider
	 * @param provider The SuggestionProvider type to retrieve
	 * @return A SuggestionProvider that matches the SuggestionProviders input
	 */
	SuggestionProvider<?> getSuggestionProvider(SuggestionProviders provider);
	
	/**
	 * Retrieves a CommandSender, given some CommandContext. This
	 * method should handle Proxied CommandSenders for entities
	 * if a Proxy is being used.
	 * @param cmdCtx The CommandContext for a given command
	 * @return A CommandSender instance (such as a ProxiedNativeCommandSender or Player)
	 */
	CommandSender getSenderForCommand(CommandContext<?> cmdCtx);
	
	/**
	 * Returns a CommandSender of a given CommandListenerWrapper object
	 * @param clw The CommandListenerWrapper object
	 * @return A CommandSender (not proxied) from the command listener wrapper
	 */
	CommandSender getCommandSenderForCLW(Object clw);

	/**
	 * Given the MinecraftServer instance, returns the Brigadier
	 * CommandDispatcher from the NMS CommandDispatcher
	 * @param server The NMS MinecraftServer instance
	 * @return A Brigadier CommandDispatcher
	 */
	CommandDispatcher<?> getBrigadierDispatcher(Object server);

	/**
	 * Checks if a Command is an instance of the OBC VanillaCommandWrapper
	 * @param command The Command to check
	 * @return true if Command is an instance of VanillaCommandWrapper
	 */
	boolean isVanillaCommandWrapper(Command command);
	
	/**
	 * Returns the Server's internal (OBC) CommandMap
	 * @return A SimpleCommandMap from the OBC server
	 */
	SimpleCommandMap getSimpleCommandMap();
	
	//Argument implementations
    ItemStack         getItemStack(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
    Location          getLocation(CommandContext<?> cmdCtx, String str, LocationType locationType, CommandSender sender) throws CommandSyntaxException;
    PotionEffectType  getPotionEffect(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
    FunctionWrapper[] getFunction(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
    Player            getPlayer(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
    Object            getEntitySelector(CommandContext<?> cmdCtx, String str, EntitySelector selector) throws CommandSyntaxException;
    EntityType        getEntityType(CommandContext<?> cmdCtx, String str, CommandSender sender) throws CommandSyntaxException;
    Advancement       getAdvancement(CommandContext<?> cmdCtx, String key) throws CommandSyntaxException;
    Recipe            getRecipe(CommandContext<?> cmdCtx, String key) throws CommandSyntaxException; 
	Location2D        getLocation2D(CommandContext<?> cmdCtx, String key, LocationType locationType2d, CommandSender sender) throws CommandSyntaxException;
    
    ChatColor         getChatColor(CommandContext<?> cmdCtx, String str);
    BaseComponent[]   getChatComponent(CommandContext<?> cmdCtx, String str);
    Enchantment       getEnchantment(CommandContext<?> cmdCtx, String str);
    Particle          getParticle(CommandContext<?> cmdCtx, String str);
    LootTable         getLootTable(CommandContext<?> cmdCtx, String str);
    Sound             getSound(CommandContext<?> cmdCtx, String key);
	int               getTime(CommandContext<?> cmdCtx, String key);
	IntegerRange      getIntRange(CommandContext<?> cmdCtx, String key);
	FloatRange        getFloatRange(CommandContext<?> cmdCtx, String key);
	Environment       getDimension(CommandContext<?> cmdCtx, String key);
	Rotation          getRotation(CommandContext<?> cmdCtx, String key);
	EnumSet<Axis>     getAxis(CommandContext<?> cmdCtx, String key);
	
	//Argument types
	ArgumentType<?> _ArgumentChatFormat();
	ArgumentType<?> _ArgumentChatComponent();
	ArgumentType<?> _ArgumentDimension();
	ArgumentType<?> _ArgumentEntitySummon();
	ArgumentType<?> _ArgumentEntity(EntitySelector selector);
	ArgumentType<?> _ArgumentEnchantment();
	ArgumentType<?> _ArgumentFloatRange();
	ArgumentType<?> _ArgumentIntRange();
	ArgumentType<?> _ArgumentMinecraftKeyRegistered();
	ArgumentType<?> _ArgumentMobEffect();
	ArgumentType<?> _ArgumentProfile();
	ArgumentType<?> _ArgumentParticle();
	ArgumentType<?> _ArgumentPosition();
	ArgumentType<?> _ArgumentPosition2D();
	ArgumentType<?> _ArgumentRotation();
	ArgumentType<?> _ArgumentItemStack();
	ArgumentType<?> _ArgumentTag();
	ArgumentType<?> _ArgumentTime();
	ArgumentType<?> _ArgumentVec2();
	ArgumentType<?> _ArgumentVec3();

	ArgumentType<?> _ArgumentAxis();

	ArgumentType<?> _ArgumentItemSlot();

	int getItemSlot(CommandContext<?> cmdCtx, String key);

	ArgumentType<?> _ArgumentScoreboardSlot();

	DisplaySlot getScoreboardSlot(CommandContext<?> cmdCtx, String key);

	

}
