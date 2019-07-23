package io.github.jorelali.commandapi.api;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.GreedyStringArgument;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 'Simple' conversion of Plugin commands
 */
public class Converter {

	private final JavaPlugin plugin;

	public Converter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Convert all commands stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 */
	public void convert() {
		for(String cmdName : plugin.getDescription().getCommands().keySet()) {
			convertPluginCommand(cmdName);
		}
	}
	
	/**
	 * Convert a command stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param cmdName The command to convert
	 */
	public void convert(String cmdName) {
		convertPluginCommand(cmdName);
	}
	
	private void convertPluginCommand(String commandName) {
		
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
			aliases = list.toArray(new String[0]);
		}
		 
		//Convert YAML to CommandPermission
		CommandPermission permissionNode = null;
		String permission = (String) cmdData.get("permission");
		if(permission == null) {
			permissionNode = CommandPermission.NONE;
		} else {
			permissionNode = CommandPermission.fromString(permission);
		}
		
		
		//Arguments (none)
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		
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
