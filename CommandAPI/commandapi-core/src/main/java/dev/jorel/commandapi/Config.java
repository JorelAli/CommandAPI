package dev.jorel.commandapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
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

	public Config(FileConfiguration fileConfig) {
		verboseOutput = fileConfig.getBoolean("verbose-outputs");
		createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
		pluginsToConvert = new HashMap<>();

		for (Map<?, ?> map : fileConfig.getMapList("plugins-to-convert")) {
			String pluginName = (String) map.keySet().stream().findFirst().get();
			Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
			if (plugin == null) {
				CommandAPI.getLog()
						.severe("Plugin '" + pluginName + "' was not found. Did you add loadbefore: [CommandAPI] to "
								+ pluginName + "'s plugin.yml file?");
				continue;
			}

			String[] pluginCommands;
			if (map.values() == null) {
				pluginCommands = new String[0];
			} else {
				@SuppressWarnings("unchecked")
				List<String> commands = (List<String>) map.values().stream().findFirst().get();
				pluginCommands = commands.toArray(new String[0]);
			}
			pluginsToConvert.put(plugin, pluginCommands);
		}
	}

	public Config() {
		verboseOutput = false;
		createDispatcherFile = false;
		pluginsToConvert = new HashMap<>();
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

}