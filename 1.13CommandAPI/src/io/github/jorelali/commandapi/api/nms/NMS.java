package io.github.jorelali.commandapi.api.nms;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
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
import org.bukkit.loot.LootTable;
import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import io.github.jorelali.commandapi.api.FunctionWrapper;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument.SuggestionProviders;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument.EntitySelector;
import io.github.jorelali.commandapi.api.arguments.LocationArgument.LocationType;
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
	
	public String[] compatibleVersions(); //e.g. ["1.14.1", "1.14.2"]
	
	//Argument implementations
	public ChatColor 			getChatColor(CommandContext<?> cmdCtx, String str);
	public BaseComponent[] 		getChatComponent(CommandContext<?> cmdCtx, String str);
	public Enchantment 			getEnchantment(CommandContext<?> cmdCtx, String str);
	public ItemStack 			getItemStack(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
	public Location 			getLocation(CommandContext<?> cmdCtx, String str, LocationType locationType, CommandSender sender) throws CommandSyntaxException;
	public Particle 			getParticle(CommandContext<?> cmdCtx, String str);
	public PotionEffectType 	getPotionEffect(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
	public FunctionWrapper[] 	getFunction(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
	public Player 				getPlayer(CommandContext<?> cmdCtx, String str) throws CommandSyntaxException;
	public Object 				getEntitySelector(CommandContext<?> cmdCtx, String str, EntitySelector selector) throws CommandSyntaxException;
	public EntityType 			getEntityType(CommandContext<?> cmdCtx, String str, CommandSender sender) throws CommandSyntaxException;
	public LootTable 			getLootTable(CommandContext<?> cmdCtx, String str);
	
	public void createDispatcherFile(Object server, File file, CommandDispatcher<?> dispatcher) throws IOException;
	
	public SuggestionProvider<?> getSuggestionProvider(SuggestionProviders provider);
	
	public CommandSender getSenderForCommand(CommandContext<?> cmdCtx);
	public CommandSender getCommandSenderForCLW(Object clw);

	public CommandDispatcher<?> getBrigadierDispatcher(Object server);

	public boolean isVanillaCommandWrapper(Command command);
	public SimpleCommandMap getSimpleCommandMap();
	
	//Argument types
	public ArgumentType<?> _ArgumentChatFormat();
	public ArgumentType<?> _ArgumentChatComponent();
	public ArgumentType<?> _ArgumentMinecraftKeyRegistered();
	public ArgumentType<?> _ArgumentMobEffect();
	public ArgumentType<?> _ArgumentProfile();
	public ArgumentType<?> _ArgumentParticle();
	public ArgumentType<?> _ArgumentPosition();
	public ArgumentType<?> _ArgumentVec3();
	public ArgumentType<?> _ArgumentItemStack();
	public ArgumentType<?> _ArgumentTag();
	public ArgumentType<?> _ArgumentEntitySummon();
	public ArgumentType<?> _ArgumentEntity(EntitySelector selector);
	public ArgumentType<?> _ArgumentEnchantment();
}
