package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.ItemStackArgument;

public class TestClass extends JavaPlugin  {
			
	/**
	 * TODO: Add ALIASES!
	 */
	
	
	@Override
	public void onEnable() {
		
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("item", new ItemStackArgument()); 
		
		//Register the command
		CommandAPI.getInstance().register("gimme", arguments, (sender, args) -> {
			((Player) sender).getInventory().addItem((ItemStack) args[0]);
			});
		
//		//Registering same command with different args
//		arguments = new LinkedHashMap<>();
//		//TODO: test if this REQUIRES no spaces or not
//		arguments.put("speed", new FloatArgument());
//		
//		CommandAPI.getInstance().register("flyspeed", arguments, (sender, args) -> {
//			((Player) sender).setFlySpeed((float) args[0]);
//			});
		
	}		
	
}
