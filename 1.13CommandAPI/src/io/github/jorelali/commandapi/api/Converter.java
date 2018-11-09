package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.jorelali.commandapi.api.CommandPermission.PermissionNode;
import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;

/**
 * 'Simple' conversion of Plugin commands
 */
public class Converter {

	/**
	 * Convert all commands stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param p The plugin which commands are to be converted
	 */
	public static void convert(Plugin p) {
		Set<String> commands = p.getDescription().getCommands().keySet();
		JavaPlugin plugin = (JavaPlugin) p;
		
		for(String cmdName : commands) {
			convertPluginCommand(plugin, cmdName);
		}
	}
	
	/**
	 * Convert a command stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param p The plugin where the command is registered
	 * @param cmdName The command to convert
	 */
	public static void convert(Plugin p, String cmdName) {
		JavaPlugin plugin = (JavaPlugin) p;
		convertPluginCommand(plugin, cmdName);
	}
	
	private static void convertPluginCommand(JavaPlugin plugin, String commandName) {
		
		/* Parse the commands */
		Map<String, Object> cmdData = plugin.getDescription().getCommands().get(commandName);

		//Convert stupid YAML aliases to a String[] for CommandAPI
		String[] aliases = null;
		Object aliasObj = cmdData.get("aliases");
		if(aliasObj == null) {
			aliases = new String[0];
		} else if(aliasObj instanceof String) {
			aliases = new String[] {(String) aliasObj};
		} else if(aliasObj instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) aliasObj;
			aliases = list.toArray(new String[list.size()]);
		}
		 
		//Convert YAML to CommandPermission
		CommandPermission permissionNode = null;
		String permission = (String) cmdData.get("permission");
		if(permission == null) {
			permissionNode = new CommandPermission(PermissionNode.NONE);
		} else {
			permissionNode = new CommandPermission(permission);
		}
		
		
		//Arguments (none)
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		
		//TODO: Bug here where aliases don't work properly with 0 arguments
		CommandAPI.getInstance().register(commandName, permissionNode, aliases, arguments, (sender, args) -> {
			plugin.getCommand(commandName).execute(sender, commandName, new String[0]);
		});
		
		//Arguments (all)
		arguments.put("args", new GreedyStringArgument());
		CommandAPI.getInstance().register(commandName, permissionNode, aliases, arguments, (sender, args) -> {
			plugin.getCommand(commandName).execute(sender, commandName, ((String) args[0]).split(" "));
		});
	}
	
}
