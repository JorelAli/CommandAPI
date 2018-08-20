package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.BooleanArgument;
import io.github.jorelali.commandapi.api.arguments.FloatArgument;

public class TestClass extends JavaPlugin  {
			
	/**
	 * TODO: Add ALIASES!
	 */
	
	
	@Override
	public void onEnable() {
		
		//Class used to manage command registration
		//TODO: Make this static?		
		//Creating a list of arguments
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("speed", new FloatArgument()); //"speed" is just a description of what the argument is
		arguments.put("output", new BooleanArgument());
		
		//Register the command
		CommandAPI.getInstance().register("flyspeed", arguments, (sender, args) -> {
			((Player) sender).setFlySpeed((float) args[0]);
			if((boolean) args[1]) {
				System.out.println("yay!");
			}
			});
		
		//Registering same command with different args
		arguments = new LinkedHashMap<>();
		//TODO: test if this REQUIRES no spaces or not
		arguments.put("speed", new FloatArgument());
		
		CommandAPI.getInstance().register("flyspeed", arguments, (sender, args) -> {
			((Player) sender).setFlySpeed((float) args[0]);
			});
		
	}		
	
}
