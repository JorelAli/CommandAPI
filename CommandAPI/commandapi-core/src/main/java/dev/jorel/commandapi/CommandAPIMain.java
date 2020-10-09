package dev.jorel.commandapi;

import java.io.File;
import java.util.Arrays;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.DoubleArgument;

public class CommandAPIMain extends JavaPlugin implements Listener {
	
	@Override
	public void onLoad() {
		//Config loading
		saveDefaultConfig();
		CommandAPI.config = new Config(getConfig());
		CommandAPI.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		CommandAPI.logger = getLogger();
		
		//Check dependencies for CommandAPI
		CommandAPIHandler.checkDependencies();
		
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
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		new CommandAPICommand("d").withArguments(new DoubleArgument("dd"))
				.executesPlayer((s, a) -> {
					System.out.println(Arrays.deepToString(a));
				}).register();
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPluginEnable(PluginEnableEvent e) {
		if(CommandAPI.getConfiguration().getPluginForDeferredConversion().containsKey(e.getPlugin().getName())) {
			
			String[] commands = CommandAPI.getConfiguration().getPluginForDeferredConversion().get(e.getPlugin().getName());
			if(commands.length == 0) {
				Converter.convert(e.getPlugin());
			} else {
				for(String command : commands) {
					Converter.convert(e.getPlugin(), command);
				}
			}
		}
	}
}
