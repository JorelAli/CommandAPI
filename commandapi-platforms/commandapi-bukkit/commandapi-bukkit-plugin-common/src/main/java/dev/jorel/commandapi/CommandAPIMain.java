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

import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.config.BukkitConfigurationAdapter;
import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.wrappers.IntegerRange;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;

/**
 * Main CommandAPI plugin entrypoint
 */
public class CommandAPIMain extends JavaPlugin {

	private static final String PLUGINS_TO_CONVERT = "plugins-to-convert";

	@Override
	public void onLoad() {
		// Read config file
		saveDefaultConfig();
		FileConfiguration fileConfig = getConfig();
		CommandAPIBukkitConfig config = new CommandAPIBukkitConfig(this)
			.verboseOutput(fileConfig.getBoolean("verbose-outputs"))
			.silentLogs(fileConfig.getBoolean("silent-logs"))
			.useLatestNMSVersion(fileConfig.getBoolean("use-latest-nms-version"))
			.missingExecutorImplementationMessage(fileConfig.getString("messages.missing-executor-implementation"))
			.dispatcherFile(fileConfig.getBoolean("create-dispatcher-json") ? new File(getDataFolder(), "command_registration.json") : null)
			.shouldHookPaperReload(fileConfig.getBoolean("hook-paper-reload"))
			.skipReloadDatapacks(fileConfig.getBoolean("skip-initial-datapack-reload"))
			.beLenientForMinorVersions(fileConfig.getBoolean("be-lenient-for-minor-versions"));

		for (String pluginName : fileConfig.getStringList("skip-sender-proxy")) {
			if (Bukkit.getPluginManager().getPlugin(pluginName) != null) {
				config.addSkipSenderProxy(pluginName);
			} else {
				new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?")
					.printStackTrace();
			}
		}

		// Main CommandAPI loading
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));
		CommandAPI.onLoad(config);

		// Convert all plugins to be converted
		if (!fileConfig.getList(PLUGINS_TO_CONVERT).isEmpty()
			&& fileConfig.getMapList(PLUGINS_TO_CONVERT).isEmpty()) {
			CommandAPI.logError("plugins-to-convert has an invalid type. Did you miss a colon (:) after a plugin name?");
		}

		convertCommands(fileConfig);
	}

	private void convertCommands(FileConfiguration fileConfig) {
		// Load all plugins at the same time
		Map<JavaPlugin, String[]> pluginsToConvert = new HashMap<>();
		for (Map<?, ?> map : fileConfig.getMapList(PLUGINS_TO_CONVERT)) {
			String[] pluginCommands;
			if (map.values().size() == 1 && map.values().iterator().next() == null) {
				pluginCommands = new String[0];
			} else {
				@SuppressWarnings("unchecked")
				List<String> commands = (List<String>) map.values().iterator().next();
				pluginCommands = commands.toArray(new String[0]);
			}

			// Get the plugin, if it doesn't exist, scream in the console (but
			// don't crash, we want to continue!)
			final JavaPlugin plugin = getAndValidatePlugin((String) map.keySet().iterator().next());
			if (plugin != null) {
				pluginsToConvert.put(plugin, pluginCommands);
			}
		}

		// Convert plugin commands
		for (Entry<JavaPlugin, String[]> pluginToConvert : pluginsToConvert.entrySet()) {
			if (pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for (String command : pluginToConvert.getValue()) {
					new AdvancedConverter(pluginToConvert.getKey(), command).convert();
				}
			}
		}

		// Convert all arbitrary commands
		for (String commandName : fileConfig.getStringList("other-commands-to-convert")) {
			new AdvancedConverter(commandName).convertCommand();
		}
	}

	private JavaPlugin getAndValidatePlugin(String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin != null) {
			if (plugin instanceof JavaPlugin javaPlugin) {
				return javaPlugin;
			} else {
				new InvalidPluginException("Plugin " + pluginName + " is not a JavaPlugin!").printStackTrace();
			}
		} else {
			new InvalidPluginException("Could not find a plugin " + pluginName + "! Has it been loaded properly?")
				.printStackTrace();
		}
		return null;
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable();

		// examples from https://github.com/JorelAli/CommandAPI/issues/483
		new CommandAPICommand("singleFlag")
			.withArguments(
				new FlagsArgument("filters")
					.loopingBranch(
						new LiteralArgument("filter", "sort").setListed(true),
						new MultiLiteralArgument("sortType", "furthest", "nearest", "random")
					)
					.loopingBranch(
						new LiteralArgument("filter", "limit").setListed(true),
						new IntegerArgument("limitAmount", 0)
					)
					.loopingBranch(
						new LiteralArgument("filter", "distance").setListed(true),
						new IntegerRangeArgument("distanceRange")
					)
			)
			.executes(info -> {
				for (CommandArguments branch : info.args().<List<CommandArguments>>getUnchecked("filters")) {
					String filterType = branch.getUnchecked("filter");
					info.sender().sendMessage(switch (filterType) {
						case "sort" -> "Sort " + branch.<String>getUnchecked("sortType");
						case "limit" -> "Limit " + branch.<Integer>getUnchecked("limitAmount");
						case "distance" -> "Distance " + branch.<IntegerRange>getUnchecked("distanceRange");
						default -> "Unknown branch " + filterType;
					});
				}
			})
			.register();

		new CommandAPICommand("nestedFlags")
			.withArguments(
				new FlagsArgument("execute")
					.loopingBranch(
						new LiteralArgument("subcommand", "as").setListed(true),
						new EntitySelectorArgument.ManyEntities("targets")
					)
					.loopingBranch(
						new FlagsArgument("if")
							.terminalBranch(
								new LiteralArgument("ifType", "block").setListed(true),
								new BlockPredicateArgument("predicate")
							)
							.terminalBranch(
								new LiteralArgument("ifType", "entity").setListed(true),
								new EntitySelectorArgument.ManyEntities("predicate")
							)
					)
					.terminalBranch(
						new LiteralArgument("run")
					),
				new CommandArgument("command")
			)
			.executes(info -> {
				info.sender().sendMessage(info.args().argsMap().toString());
			})
			.register();

		// example from NextdoorPsycho
		new CommandAPICommand("colorCommand")
			.withArguments(
				createColorArgument("color")
			)
			.executes(info -> {
				info.sender().sendMessage(info.args().<Color>getUnchecked("color").toString());
			})
			.register();
	}

	private static Argument<Color> createColorArgument(String nodeName) {
		return new CustomArgument<>(
			new FlagsArgument(nodeName)
				.loopingBranch(
					// A DynamicMultiLiteral would be perfect here :P
					//  https://github.com/JorelAli/CommandAPI/issues/513
					//  At least, this is how I imagine it would work
					new StringArgument("channel").replaceSuggestions(ArgumentSuggestions.strings(info -> {
						Set<String> channelsLeft = new HashSet<>(Set.of("-r", "-g", "-b"));
						for(CommandArguments previousChannels : info.previousArgs().<List<CommandArguments>>getUnchecked(nodeName)) {
							// Yes, you can reference previous versions of yourself
							channelsLeft.remove(previousChannels.<String>getUnchecked("channel"));
						}
						return channelsLeft.toArray(String[]::new);
					})),
					new IntegerArgument("value", 0, 255)
				),
			info -> {
				int red = 0, green = 0, blue = 0;
				for (CommandArguments channels : (List<CommandArguments>) info.currentInput()) {
					int value = channels.getUnchecked("value");
					String channel = channels.getUnchecked("channel");
					switch (channel) {
						case "-r" -> red = value;
						case "-g" -> green = value;
						case "-b" -> blue = value;
						default -> throw CustomArgument.CustomArgumentException.fromString("Unknown channel \"" + channel + "\"");
					}
				}
				return new Color(red, green, blue);
			}
		);
	}

	private record Color(int red, int green, int blue) {}

	/**
	 * In contrast to the superclass' method {@link org.bukkit.plugin.java.JavaPlugin#saveDefaultConfig()},
	 * this doesn't fail silently if the config.yml already exists but instead will update the config with
	 * new values if available.
	 * <p>
	 * This should fail silently if all values are set already.
	 */
	@Override
	public void saveDefaultConfig() {
		File configFile = new File(getDataFolder(), "config.yml");
		BukkitConfigurationAdapter.createMinimalInstance(configFile).saveDefaultConfig(getDataFolder(), getLogger());
	}
}
