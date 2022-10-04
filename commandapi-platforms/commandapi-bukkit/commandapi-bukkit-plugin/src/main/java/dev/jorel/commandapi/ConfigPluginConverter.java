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

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Reads YAML, generates command objects
 */
public class ConfigPluginConverter {

	/**
	 * If this is a pre-8.6.0 configuration
	 */
	private boolean isLegacyConfig;

	private PluginToConvert[] pluginsToConvert;

	public ConfigPluginConverter(YamlConfiguration config) {
		// First, we need to figure out if we're dealing with legacy configs
		this.isLegacyConfig = config.getList("plugins-to-convert") != null;
		if (isLegacyConfig) {
			// TODO: Parse legacy config
		} else {

			List<PluginToConvert> pluginsToConvertList = new ArrayList<>();

			// Iterate through plugins in plugins-to-convert
			ConfigurationSection pluginsToConvertSection = config.getConfigurationSection("plugins-to-convert");
			for (String pluginName : pluginsToConvertSection.getKeys(false)) {

				// Check if the plugin exists
				Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
				if (plugin == null) {
					new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?")
						.printStackTrace();
					
					final Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
					String[] pluginNames = new String[plugins.length];
					for(int i = 0; i < pluginNames.length; i++) {
						pluginNames[i] = plugins[i].getName();
					}
					System.out.println("Did you mean \"" + closestStringTo(pluginName, pluginNames) + "\"?");
					continue;
				} else if (!(plugin instanceof JavaPlugin)) {
					new InvalidPluginException("Plugin " + pluginName + " is not a JavaPlugin!").printStackTrace();
				}

				/**
				 * Null-based plugins are "convert all"
				 * 
				 * <pre>
				 * plugins-to-convert:
				 *   Plugin1: ~
				 * </pre>
				 */
				if (pluginsToConvertSection.get(pluginName) == null) {
					pluginsToConvertList.add(new PluginToConvert(pluginName, new CommandToConvert[0], true));
					continue;
				}

				List<CommandToConvert> commandsToConvertList = new ArrayList<>();
				ConfigurationSection commandsToConvertSection = pluginsToConvertSection.getConfigurationSection(pluginName);
				for (String commandToConvert : commandsToConvertSection.getKeys(false)) {
					List<String> arguments = new ArrayList<>();
					List<String> aliases = new ArrayList<>();
					for (String key : commandsToConvertSection.getKeys(false)) {
						switch (key.toLowerCase()) {
							case "argument":
							case "arguments":
								arguments.addAll(commandsToConvertSection.getStringList(key));
								break;
							case "alias":
							case "aliases":
								aliases.addAll(commandsToConvertSection.getStringList(key));
								break;
							default:
								System.out.println("""
									Unexpected configuration entry:

										%s:
											%s:
												%s: <- [Here]

									Did you mean "%s"?
									""".formatted(pluginName, commandToConvert, key, closestStringTo(key, "arguments", "aliases")));
								break;
						}
					}

					commandsToConvertList.add(new CommandToConvert(commandToConvert, arguments.toArray(new String[0]), aliases.toArray(new String[0])));
				}

				pluginsToConvertList.add(new PluginToConvert(pluginName, commandsToConvertList.toArray(new CommandToConvert[0]), false));
			}

			this.pluginsToConvert = pluginsToConvertList.toArray(new PluginToConvert[0]);
		}
	}

	private String closestStringTo(String inputString, String... possibleStrings) {
		List<Entry<String, Integer>> costMap = new ArrayList<>();
		for (String possibleString : possibleStrings) {
			/**
			 * Levenshtein distance implementation from <a href=
			 * "https://rosettacode.org/wiki/Levenshtein_distance#Java">RosettaCode</a>.
			 * Licenced under Creative Commons Attribution-ShareAlike 4.0 International (CC
			 * BY-SA 4.0)
			 */
			String a = inputString.toLowerCase();
			String b = possibleString.toLowerCase();
			// i == 0
			int[] costs = new int[b.length() + 1];
			for (int j = 0; j < costs.length; j++)
				costs[j] = j;
			for (int i = 1; i <= a.length(); i++) {
				// j == 0; nw = lev(i - 1, j)
				costs[0] = i;
				int nw = i - 1;
				for (int j = 1; j <= b.length(); j++) {
					int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
					nw = costs[j];
					costs[j] = cj;
				}
			}
			costMap.add(new SimpleEntry<String, Integer>(possibleString, costs[b.length()]));
		}
		if (!costMap.isEmpty()) {
			costMap.sort(Entry.comparingByValue());
			return costMap.get(0).getKey();
		} else {
			return null;
		}
	}

	record PluginToConvert(String name, CommandToConvert[] commands, boolean convertAll) {
	}

	record CommandToConvert(String name, String[] arguments, String[] aliases) {
	}
}
