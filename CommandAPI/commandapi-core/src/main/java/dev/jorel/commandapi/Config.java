package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

/**
 * Configuration wrapper class. The config.yml file used by the CommandAPI is
 * only ever read from, nothing is ever written to it. That's why there's only
 * getter methods.
 */
class Config {

	// Output registering and unregistering of commands
	private final boolean verboseOutput;

	// Create a command_registration.json file
	private final boolean createDispatcherFile;

	// List of plugins to convert
	private final Map<Plugin, String[]> pluginsToConvert;
	
	// List of plugins which should ignore proxied senders
	private final List<String> skipSenderProxy;
	
	public Config(FileConfiguration fileConfig) {
		verboseOutput = fileConfig.getBoolean("verbose-outputs");
		createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
		pluginsToConvert = new HashMap<>();
		skipSenderProxy = new ArrayList<>();

		for (Map<?, ?> map : fileConfig.getMapList("plugins-to-convert")) {
			String[] pluginCommands;
			if (map.values() == null || (map.values().size() == 1 && map.values().iterator().next() == null)) {
				pluginCommands = new String[0];
			} else {
				@SuppressWarnings("unchecked")
				List<String> commands = (List<String>) map.values().stream().findFirst().get();
				pluginCommands = commands.toArray(new String[0]);
			}
			
			String pluginName = (String) map.keySet().stream().findFirst().get();
			Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
			if(plugin != null) { 
				pluginsToConvert.put(plugin, pluginCommands);
			} else {
				new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?").printStackTrace();
			}
		}
		
		for (String pluginName : fileConfig.getStringList("skip-sender-proxy:")) {
			Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
			if(plugin != null) { 
				skipSenderProxy.add(pluginName);
			} else {
				new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?").printStackTrace();
			}
		}
	}

	public Config(boolean verbose) {
		verboseOutput = verbose;
		createDispatcherFile = false;
		pluginsToConvert = new HashMap<>();
		skipSenderProxy = new ArrayList<>();
	}

	public boolean hasVerboseOutput() {
		return this.verboseOutput;
	}

	public boolean willCreateDispatcherFile() {
		return this.createDispatcherFile;
	}
	
	public Set<Entry<Plugin, String[]>> getPluginsToConvert() {
		return this.pluginsToConvert.entrySet();
	}
	
	public boolean shouldSkipSenderProxy(Plugin plugin) {
		return skipSenderProxy.contains(plugin.getName());
	}

}