package dev.jorel.commandapi;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.SafeOverrideableArgument;

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

	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		getServer().getPluginManager().registerEvents(this, this);
		
SafeOverrideableArgument<World> customArg = new CustomArgument<World>("world", Bukkit::getWorld);
customArg.overrideSuggestionsT(/*blah*/);


new CommandAPICommand("tpworld")
	.withOptionalArgument(customArg, Bukkit.getWorld("world"))
	.withOptionalArgument(new IntegerArgument("arg").overrideSuggestions(""), 2)
	.executesPlayer((player, args) -> {
		// ... code
	})
	.register();

	}
}
