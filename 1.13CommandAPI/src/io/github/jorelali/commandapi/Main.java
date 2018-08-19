package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.ArgumentType;
import io.github.jorelali.commandapi.api.CommandAPI;

public class Main extends JavaPlugin  {
			
	/**
	 * TODO: Add ALIASES!
	 */
	
	
	@Override
	public void onEnable() {
		
		//Class used to manage command registration
		CommandAPI commandRegister = new CommandAPI();
		
		//Creating a list of arguments
		LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
		arguments.put("speed", ArgumentType.FLOAT); //"speed" is just a description of what the argument is
		arguments.put("output", ArgumentType.BOOLEAN);
		
		//Register the command
		commandRegister.register("flyspeed", arguments, (sender, args) -> {
			((Player) sender).setFlySpeed((float) args[0]);
			if((boolean) args[1]) {
				System.out.println("yay!");
			}
			});
		
		//Registering same command with different args
		arguments = new LinkedHashMap<>();
		arguments.put("speed", ArgumentType.FLOAT);
		
		commandRegister.register("flyspeed", arguments, (sender, args) -> {
			((Player) sender).setFlySpeed((float) args[0]);
			});
		
	}		
	
}
