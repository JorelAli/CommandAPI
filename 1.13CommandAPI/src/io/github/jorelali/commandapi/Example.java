package io.github.jorelali.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandAPI;
import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.CommandPermission.PermissionNode;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.BooleanArgument;

/**
 * An example class showing how to register commands with the CommandAPI
 * In this example, we create a godmode command, with the syntax of:
 * 
 * /god true - turn god mode on
 * /god false - turn god mode off
 */
public class Example extends JavaPlugin {

	@Override
	public void onEnable() {
		
		//LinkedHashMap to store arguments for the command
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		
		//Our syntax requires a boolean value of true or false.
		//We'll put "status" as our description for this command
		arguments.put("status", new BooleanArgument());
		
		//Register our command, god, with the arguments and a CommandExecutor which
		//determines what happens when the command is run
		CommandAPI.getInstance().register("god", new CommandPermission(PermissionNode.NONE), arguments, (sender, args) -> {
			
			//Checking if the sender of the command is a player
			if(sender instanceof Player) {
				Player player = (Player) sender;
				
				//To access our argument, since it was the first argument
				//registered in our LinkedHashMap (and is a boolean), we
				//access the first index (index 0) and cast it to a boolean
				player.setInvulnerable((boolean) args[0]);
			} else {
				sender.sendMessage("You must be a player to use this command");
			}
		});
		
		//Same implementation, except using an anonymous class instead of a lambda expression
//		commandRegister.register("god", arguments, new CommandExecutor() {
//
//			@Override
//			public void run(CommandSender sender, Object[] args) {
//				if(sender instanceof Player) {
//					Player player = (Player) sender;
//					player.setInvulnerable((boolean) args[0]);
//				} else {
//					sender.sendMessage("You must be a player to use this command");
//				}
//			}
//		});
		
	}
	
	
}
