package dev.jorel.commandapi.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@ApiStatus.Internal
public class ConfigGenerator {

	private final DefaultedBukkitConfig defaultedBukkitConfig;

	private ConfigGenerator() {
		this.defaultedBukkitConfig = DefaultedBukkitConfig.createDefault();
	}

	private ConfigGenerator(DefaultedBukkitConfig defaultedBukkitConfig) {
		this.defaultedBukkitConfig = defaultedBukkitConfig;
	}

	public static ConfigGenerator createNew() {
		return new ConfigGenerator();
	}

	public static ConfigGenerator createNew(DefaultedBukkitConfig defaultedBukkitConfig) {
		return new ConfigGenerator(defaultedBukkitConfig);
	}

	public YamlConfiguration generateDefaultConfig() throws InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		Set<String> sections = new HashSet<>();
		for (Map.Entry<String, CommentedConfigOption<?>> commentedConfigOption : defaultedBukkitConfig.getAllOptions().entrySet()) {
			String path = commentedConfigOption.getKey();

			tryCreateSection(config, path, sections);

			config.set(path, commentedConfigOption.getValue().option());
			config.setComments(path, commentedConfigOption.getValue().comment());
		}
		return process(config.saveToString());
	}

	public YamlConfiguration generateWithNewValues(YamlConfiguration existingConfig) throws InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();

		boolean shouldRemoveValues = shouldRemoveOptions(existingConfig);

		boolean wasConfigUpdated = false;
		Set<String> sections = new HashSet<>();
		for (Map.Entry<String, CommentedConfigOption<?>> commentedConfigOption : defaultedBukkitConfig.getAllOptions().entrySet()) {
			String path = commentedConfigOption.getKey();

			// Update config option
			if (existingConfig.contains(path)) {
				tryCreateSection(config, path, sections);
				config.set(path, existingConfig.get(path));
			} else {
				wasConfigUpdated = true;
				tryCreateSection(config, path, sections);
				config.set(path, commentedConfigOption.getValue().option());
			}

			// Update config option comments
			// Comments are kinda stupid, some elements are apparently null elements
			// Also, both, YamlConfiguration#getComments(String) and CommentedConfigOption#comments() return unmodifiable list
			// which by itself apparently aren't able to be checked for equality by the equals() method
			// As a result, we wrap them in new ArrayLists first to be able to compare them
			List<String> existingComment = new ArrayList<>(existingConfig.getComments(path));
			existingComment.removeIf(Objects::isNull);
			List<String> defaultComment = new ArrayList<>(commentedConfigOption.getValue().comment());

			if (!existingComment.equals(defaultComment)) {
				wasConfigUpdated = true;
			}
			config.setComments(path, commentedConfigOption.getValue().comment());
		}
		if (shouldRemoveValues) {
			wasConfigUpdated = true;
		}
		return (wasConfigUpdated) ? process(config.saveToString()) : null;
	}

	private YamlConfiguration process(String configAsString) throws InvalidConfigurationException {
		String[] configStrings = configAsString.split("\n");
		StringBuilder configBuilder = new StringBuilder();
		for (String configString : configStrings) {
			configBuilder.append(configString).append("\n");
			if (!configString.contains("#")) {
				configBuilder.append("\n");
			}
		}
		YamlConfiguration config = new YamlConfiguration();
		config.loadFromString(configBuilder.toString());
		return config;
	}

	private void tryCreateSection(YamlConfiguration config, String path, Set<String> existingSections) {
		if (path.contains(".")) {
			// We have to create a section, or multiple if applicable, first, if it doesn't exist already
			String[] sectionNames = path.split("\\.");
			// The last value is the config option
			for (int i = 0; i < sectionNames.length - 1; i++) {
				String sectionName = sectionNames[i];
				if (!existingSections.contains(sectionName)) {
					config.createSection(sectionName);
					List<String> comment = defaultedBukkitConfig.getAllSections().get(sectionName).comment();
					if (comment != null) {
						config.setComments(sectionName, comment);
					}
				}
				existingSections.add(sectionName);
			}
		}
	}

	private boolean shouldRemoveOptions(YamlConfiguration config) {
		Set<String> configOptions = config.getKeys(true);
		configOptions.removeIf(config::isConfigurationSection);

		Set<String> defaultConfigOptions = defaultedBukkitConfig.getAllOptions().keySet();

		boolean shouldRemoveOptions = false;
		for (String option : configOptions) {
			if (!defaultConfigOptions.contains(option)) {
				shouldRemoveOptions = true;
				break;
			}
		}
		return shouldRemoveOptions;
	}

}
