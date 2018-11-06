package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.CommandAPIMain;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;

/**
 * 'Simple' conversion of Plugin commands
 *
 */
public class Converter {
	
	private static boolean canConvert = true;
	
	/*
	 * Prevent command conversion after the server has fully loaded.
	 * This is due to an 'issue' where commands won't register to the Minecraft command registry
	 * when the server has been fully loaded
	 */
	public static void scheduleConversion(Plugin p) {
		if(canConvert) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(p, () -> Converter.canConvert = false, 0L);
		} else {
			CommandAPIMain.getLog().warning("Cannot convert plugins after server has loaded");
		}
	}

	public static void convert(Plugin p) {
		if(!canConvert) {
			CommandAPIMain.getLog().warning("Cannot convert plugins after server has loaded");
		}
		Set<String> commands = p.getDescription().getCommands().keySet();
		JavaPlugin plugin = (JavaPlugin) p;
		
		for(String cmdName : commands) {
			
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			
			//TODO: Parse PluginDescriptionFile (from commands variable) to add aliases + permissions
			
			//Register the command twice: once for no arguments and once for any number of arguments
			CommandAPI.getInstance().register(cmdName, arguments, (sender, args) -> {
				plugin.getCommand(cmdName).execute(sender, cmdName, new String[0]);
			});
			arguments.put("args", new GreedyStringArgument());
			CommandAPI.getInstance().register(cmdName, arguments, (sender, args) -> {
				plugin.getCommand(cmdName).execute(sender, cmdName, ((String) args[0]).split(" "));
			});
		}
	}
	
	public static void convert(Plugin p, String cmdName) {
		if(!canConvert) {
			CommandAPIMain.getLog().warning("Cannot convert plugins after server has loaded");
		}
		JavaPlugin plugin = (JavaPlugin) p;
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		CommandAPI.getInstance().register(cmdName, arguments, (sender, args) -> {
			plugin.getCommand(cmdName).execute(sender, cmdName, new String[0]);
		});
		arguments.put("args", new GreedyStringArgument());
		CommandAPI.getInstance().register(cmdName, arguments, (sender, args) -> {
			plugin.getCommand(cmdName).execute(sender, cmdName, ((String) args[0]).split(" "));
		});
	}
	
}
