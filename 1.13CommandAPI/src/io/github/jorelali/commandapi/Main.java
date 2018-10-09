package io.github.jorelali.commandapi;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument.EntitySelector;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;

public class Main extends JavaPlugin {

	final static private boolean TEST = true;
	
	@Override
	public void onEnable() {
		//Nothing required here, just need
		//to state that this is a plugin so
		//other plugins can depend on it
		
		if(TEST) {
		
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("player", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
			CommandAPI.getInstance().register("apioneplayer", arguments, (sender, args) -> {
				((Player) args[0]).sendMessage("hello");
			});
			
			arguments = new LinkedHashMap<>();
			arguments.put("players", new EntitySelectorArgument(EntitySelector.MANY_PLAYERS));
			CommandAPI.getInstance().register("apimanyplayers", arguments, (sender, args) -> {
				((Collection<Player>) args[0]).forEach(p -> p.sendMessage("hi"));
			});
			
			arguments = new LinkedHashMap<>();
			arguments.put("entity", new EntitySelectorArgument(EntitySelector.ONE_ENTITY));
			CommandAPI.getInstance().register("apioneentity", arguments, (sender, args) -> {
				((Entity) args[0]).remove();
			});
			
			arguments = new LinkedHashMap<>();
			arguments.put("entities", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));
			CommandAPI.getInstance().register("apimanyentities", arguments, (sender, args) -> {
				((Collection<Entity>) args[0]).forEach(e -> e.remove());
			});
		
			arguments.clear();
			arguments.put("location", new LocationArgument());
			CommandAPI.getInstance().register("setcustloc", arguments, (sender, args) -> {
				Location loc = (Location) args[0];
				loc.getBlock().setType(Material.GOLD_BLOCK);
			});
			
			arguments.clear();
			CommandAPI.getInstance().register("noarg", arguments, (sender, args) -> {
				Bukkit.broadcastMessage("woop");
			});
			
			CommandAPI.getInstance().register("noarg2", null, (sender, args) -> {
				Bukkit.broadcastMessage("woop2");
			});
			
			CommandAPI.getInstance().register("custexec", null, (sender, args) -> {
				System.out.println(sender.getClass());
			});
		}
	}
	
}
