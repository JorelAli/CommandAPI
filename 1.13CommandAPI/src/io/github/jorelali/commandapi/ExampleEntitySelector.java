package io.github.jorelali.commandapi;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument.EntitySelector;

public class ExampleEntitySelector extends JavaPlugin {

	/**
	 * Class showing the implementation of using the new EntitySelectorArgument
	 * 
	 * Command syntax:
	 * /destroy [entities]
	 * /cust1 [player]
	 * 
	 * Example command usage:
	 * /destroy @e[type=cow]						- Kills all cows
	 * /destroy @e[type=pig,limit=10,sort=furthest] - Kills the furthest 10 pigs
	 * /cust1 @r									- Sends "hello" to a random player
	 */
	
	@Override
	public void onEnable() {
		//LinkedHashMap to store arguments for the command
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		
		//Entity argument which accepts multiple entities
		arguments.put("entities", new EntitySelectorArgument(EntitySelector.MANY_ENTITIES));
		
		CommandAPI.getInstance().register("destroy", arguments, (sender, args) -> {
			
			//Checking if the sender of the command is a player
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				//The MANY_ENTITIES selector (as declared in its JavaDocs) returns a Collection<Entity>
				Collection<Entity> entities = (Collection<Entity>) args[0]; 
				player.sendMessage("killed " + entities.size() + " entities");
				for(Entity e : entities) {
					e.remove();
				}
			} else {
				sender.sendMessage("You must be a player to use this command");
			}
		});
		

		//Another example, using a single player selector
		arguments = new LinkedHashMap<>();
		arguments.put("player", new EntitySelectorArgument(EntitySelector.ONE_PLAYER));
		CommandAPI.getInstance().register("cust1", arguments, (sender, args) -> {
			//The ONE_PLAYER selector returns a Bukkit Player
			((Player) args[0]).sendMessage("hello");
		});
		
		
	}

	
	
}
