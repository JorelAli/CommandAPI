package io.github.jorelali.commandapi.api;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;

/**
 * For lazy developers who want to convert their regular plugin commands to minecraft plugin commands
 * @author Jorel
 *
 */
public class Converter {

	public void convert(Plugin p) throws Exception {
		Set<String> commands = p.getDescription().getCommands().keySet();
		JavaPlugin plugin = (JavaPlugin) p;
		
		//Get command map
		Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
		f.setAccessible(true);
		CommandMap commandMap = (CommandMap) f.get(Bukkit.getServer());
		
		for(String str : commands) {
			boolean result = plugin.getCommand(str).unregister(commandMap);
			System.out.println("Unregistered command " + str + " " + (result ? "successfully" : "unsuccessfully"));
			
			LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
			arguments.put("args", new GreedyStringArgument());
			
			//TODO: Parse PluginDescriptionFile (from commands variable) to add aliases + permissions
			CommandAPI.getInstance().register(str, arguments, (sender, args) -> {
				plugin.getCommand(str).execute(sender, str, ((String) args[0]).split(" "));
			});
			
		}			
		
		
	}
	
	
}
