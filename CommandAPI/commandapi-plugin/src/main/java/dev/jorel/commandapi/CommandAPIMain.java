package dev.jorel.commandapi;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.exceptions.ParseException;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
	@Override
	public void onLoad() {
		//Config loading
		saveDefaultConfig();
		CommandAPI.config = new Config(getConfig());
		CommandAPI.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		CommandAPI.logger = getLogger();
		
		//Check dependencies for CommandAPI
		CommandAPIHandler.getInstance().checkDependencies();
		
		//Convert all plugins to be converted
		for(Entry<Plugin, String[]> pluginToConvert : CommandAPI.config.getPluginsToConvert()) {
			if(pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for(String command : pluginToConvert.getValue()) {
					new AdvancedConverter(pluginToConvert.getKey(), command).convert();
				}
			}
		}
		
		Argument worldsArgument = new StringArgument("world")
			.overrideSuggestions(sender -> {
				return Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new);
			})
			.withParser((fullInput, range) -> {
				int start = fullInput.indexOf(" ") + 1;
				String input = fullInput.substring(start);
				List<World> worlds = Bukkit.getWorlds();
				boolean found = false;
				for(World world : worlds) {
					String worldName = world.getName().toLowerCase();
					if(worldName.startsWith(input)) {
						found = true;
					}
				}
				if(!found) {
					throw new ParseException(ChatColor.RED + "Invalid world at position " + start + ": " + fullInput.substring(0, start) + " <--[HERE]");
				}
			});
		
		new CommandAPICommand("mycommand")
			.withArguments(worldsArgument)
			.executes((sender, args) -> {
				String input = (String) args[0];
				Bukkit.broadcastMessage(input);
			})
			.register();
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
//	@EventHandler
//	public void onTabComplete(TabCompleteEvent event) {
//		System.out.println(event.getBuffer());
//		System.out.println(event.getCompletions());
//	}
}
