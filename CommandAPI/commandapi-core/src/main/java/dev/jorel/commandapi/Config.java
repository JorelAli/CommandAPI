/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
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
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Configuration wrapper class. The config.yml file used by the CommandAPI is
 * only ever read from, nothing is ever written to it. That's why there's only
 * getter methods.
 */
class Config {

	// Output registering and unregistering of commands
	private final boolean verboseOutput;
	
	// Whether we should suppress all logs
	private final boolean silentLogs;
	
	// Whether we should use the latest NMS version (which may not be compatible)
	private final boolean useLatestNMSVersion;
	
	// The message to display when an executor implementation is missing
	private final String message_missingExecutorImplementation;

	// Create a command_registration.json file
	private final boolean createDispatcherFile;

	// List of plugins to convert
	private final Map<JavaPlugin, String[]> pluginsToConvert;
	
	// List of plugins which should ignore proxied senders
	private final List<String> skipSenderProxy;
	
	// List of arbitrary commands to convert
	private final List<String> commandsToConvert;
	
	public Config(FileConfiguration fileConfig) {
		this.verboseOutput = fileConfig.getBoolean("verbose-outputs");
		this.silentLogs = fileConfig.getBoolean("silent-logs");
		this.useLatestNMSVersion = fileConfig.getBoolean("use-latest-nms-version");
		this.message_missingExecutorImplementation = fileConfig.getString("messages.missing-executor-implementation");
		this.createDispatcherFile = fileConfig.getBoolean("create-dispatcher-json");
		this.pluginsToConvert = new HashMap<>();
		this.skipSenderProxy = new ArrayList<>();
		this.commandsToConvert = new ArrayList<>();

		for (Map<?, ?> map : fileConfig.getMapList("plugins-to-convert")) {
			String[] pluginCommands;
			if (map.values() == null || (map.values().size() == 1 && map.values().iterator().next() == null)) {
				pluginCommands = new String[0];
			} else {
				@SuppressWarnings("unchecked")
				List<String> commands = (List<String>) map.values().iterator().next();
				pluginCommands = commands.toArray(new String[0]);
			}
			
			String pluginName = (String) map.keySet().iterator().next();
			Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
			if(plugin != null) { 
				if(plugin instanceof JavaPlugin javaPlugin) {
					pluginsToConvert.put(javaPlugin, pluginCommands);					
				} else {
					new InvalidPluginException("Plugin " + pluginName + " is not a JavaPlugin!").printStackTrace();
				}
			} else {
				new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?").printStackTrace();
			}
		}
		
		for (String pluginName : fileConfig.getStringList("skip-sender-proxy")) {
			if(Bukkit.getPluginManager().getPlugin(pluginName) != null) { 
				this.skipSenderProxy.add(pluginName);
			} else {
				new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?").printStackTrace();
			}
		}
		
		for (String commandName : fileConfig.getStringList("other-commands-to-convert")) {
			this.commandsToConvert.add(commandName);
		}
	}

	public Config(boolean verbose) {
		this(new CommandAPIConfig().verboseOutput(verbose));
	}

	public Config(CommandAPIConfig config) {
		this.verboseOutput = config.verboseOutput;
		this.silentLogs = config.silentLogs;
		this.useLatestNMSVersion = config.useLatestNMSVersion;
		this.message_missingExecutorImplementation = config.missingExecutorImplementationMessage;
		this.createDispatcherFile = false; // The dispatcher File is only declared in the plugin version
		this.pluginsToConvert = new HashMap<>();
		this.skipSenderProxy = new ArrayList<>();
		this.commandsToConvert = new ArrayList<>();
	}

	public boolean hasVerboseOutput() {
		return this.verboseOutput;
	}
	
	public boolean hasSilentLogs() {
		return this.silentLogs;
	}
	
	public boolean shouldUseLatestNMSVersion() {
		return this.useLatestNMSVersion;
	}
	
	public String getMissingImplementationMessage() {
		return this.message_missingExecutorImplementation;
	}

	public boolean willCreateDispatcherFile() {
		return this.createDispatcherFile;
	}
	
	public Set<Entry<JavaPlugin, String[]>> getPluginsToConvert() {
		return this.pluginsToConvert.entrySet();
	}
	
	public boolean shouldSkipSenderProxy(Plugin plugin) {
		return this.skipSenderProxy.contains(plugin.getName());
	}
	
	public boolean shouldSkipSenderProxy(String commandName) {
		return this.skipSenderProxy.contains(commandName);
	}
	
	public List<String> getCommandsToConvert() {
		return this.commandsToConvert;
	}

}