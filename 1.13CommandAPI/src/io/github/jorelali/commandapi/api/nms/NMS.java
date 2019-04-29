package io.github.jorelali.commandapi.api.nms;

import java.io.File;

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
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import io.github.jorelali.commandapi.api.FunctionWrapper;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument.SuggestionProviders;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument.EntitySelector;
import io.github.jorelali.commandapi.api.arguments.LocationArgument.LocationType;
import net.md_5.bungee.api.chat.BaseComponent;

@SuppressWarnings("rawtypes")
public interface NMS {

	public ChatColor getChatColor(CommandContext cmdCtx, String str);
	
	public BaseComponent[] getChatComponent(CommandContext cmdCtx, String str);
	
	public Enchantment getEnchantment(CommandContext cmdCtx, String str);
	
	public ItemStack getItemStack(CommandContext cmdCtx, String str) throws CommandSyntaxException;
	
	public Location getLocation(CommandContext cmdCtx, String str, LocationType locationType, CommandSender sender) throws CommandSyntaxException;
	
	public Particle getParticle(CommandContext cmdCtx, String str);
	
	public PotionEffectType getPotionEffect(CommandContext cmdCtx, String str) throws CommandSyntaxException;
	
	public void createDispatcherFile(File file);
	
	public SuggestionProvider getSuggestionProvider(SuggestionProviders provider);
	
	public FunctionWrapper[] getFunction(CommandContext cmdCtx, String str) throws CommandSyntaxException;
	
	//Returns the world in which a command sender is from
	default World getCommandSenderWorld(CommandSender sender) {
		if(sender instanceof BlockCommandSender) {
			return ((BlockCommandSender) sender).getBlock().getWorld();
		} else if(sender instanceof ProxiedCommandSender) {
			CommandSender callee = ((ProxiedCommandSender) sender).getCallee();
			if(callee instanceof Entity) {
				return ((Entity) callee).getWorld();
			} else {
				return null;
			}
		} else if(sender instanceof Entity) {
			return ((Entity) sender).getWorld();
		} else {
			return null;
		}
	}

	public CommandSender getSenderForCommand(CommandContext cmdCtx);

	public Object getNMSCommandDispatcher(Object server);

	public CommandDispatcher getDispatcher(Object server);

	public CommandSender getCommandSenderForCLW(Object clw);
	
	public Player getPlayer(CommandContext cmdCtx, String str) throws CommandSyntaxException;
	
	public Object getEntitySelector(CommandContext cmdCtx, String str, EntitySelector selector) throws CommandSyntaxException;

	public EntityType getEntityType(CommandContext cmdCtx, String str, CommandSender sender) throws CommandSyntaxException;
	
	public LootTable getLootTable(CommandContext cmdCtx, String str);
	
	public SimpleCommandMap getSimpleCommandMap();
	
	public boolean isVanillaCommandWrapper(Command command);
}
