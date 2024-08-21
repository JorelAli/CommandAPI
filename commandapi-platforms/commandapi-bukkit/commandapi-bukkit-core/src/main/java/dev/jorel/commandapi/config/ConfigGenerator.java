package dev.jorel.commandapi.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApiStatus.Internal
public class ConfigGenerator {

	private ConfigGenerator() {}

	public static YamlConfiguration generateDefaultConfig() throws InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		Set<String> sections = new HashSet<>();
		for (CommentedConfigOption<?> commentedConfigOption : DefaultedBukkitConfig.ALL_OPTIONS) {
			String path = commentedConfigOption.path();

			tryCreateSection(config, path, sections);

			config.set(path, commentedConfigOption.option());
			config.setComments(path, commentedConfigOption.comment());
		}
		return process(config.saveToString());
	}

	public static YamlConfiguration generateWithNewValues(YamlConfiguration existingConfig) throws InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();

		boolean shouldRemoveValues = shouldRemoveOptions(existingConfig);

		boolean wasConfigUpdated = false;
		Set<String> sections = new HashSet<>();
		for (CommentedConfigOption<?> commentedConfigOption : DefaultedBukkitConfig.ALL_OPTIONS) {
			String path = commentedConfigOption.path();

			// Update config option
			if (existingConfig.contains(path)) {
				tryCreateSection(config, path, sections);
				config.set(path, existingConfig.get(path));
			} else {
				wasConfigUpdated = true;
				tryCreateSection(config, path, sections);
				config.set(path, commentedConfigOption.option());
			}

			// Update config option comments
			// Comments are kinda stupid, some elements are apparently null elements
			// Also, both, YamlConfiguration#getComments(String) and CommentedConfigOption#comments() return unmodifiable list
			// which by itself apparently aren't able to be checked for equality by the equals() method
			// As a result, we wrap them in new ArrayLists first to be able to compare them
			List<String> existingComment = new ArrayList<>(existingConfig.getComments(path));
			existingComment.removeIf(Objects::isNull);
			List<String> defaultComment = new ArrayList<>(commentedConfigOption.comment());

			if (!existingComment.equals(defaultComment)) {
				wasConfigUpdated = true;
			}
			config.setComments(path, commentedConfigOption.comment());
		}
		if (shouldRemoveValues) {
			wasConfigUpdated = true;
		}
		return (wasConfigUpdated) ? process(config.saveToString()) : null;
	}

	private static YamlConfiguration process(String configAsString) throws InvalidConfigurationException {
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

	private static void tryCreateSection(YamlConfiguration config, String path, Set<String> existingSections) {
		if (path.contains(".")) {
			// We have to create a section, or multiple if applicable, first, if it doesn't exist already
			String[] sectionNames = path.split("\\.");
			// The last value is the config option
			for (int i = 0; i < sectionNames.length - 1; i++) {
				String sectionName = sectionNames[i];
				if (!existingSections.contains(sectionName)) {
					config.createSection(sectionName);
					for (CommentedConfigOption<?> commentedSection : DefaultedBukkitConfig.ALL_SECTIONS) {
						if (commentedSection.path().equals(sectionName)) {
							config.setComments(sectionName, commentedSection.comment());
						}
					}
				}
				existingSections.add(sectionName);
			}
		}
	}

	private static boolean shouldRemoveOptions(YamlConfiguration config) {
		Set<String> configOptions = config.getKeys(true);
		List<String> sections = new ArrayList<>();
		for (String configOption : configOptions) {
			ConfigurationSection section = config.getConfigurationSection(configOption);
			if (section != null) {
				sections.add(configOption);
			}
		}
		for (String sectionName : sections) {
			configOptions.remove(sectionName);
		}
		Set<String> defaultConfigOptions = new HashSet<>();
		for (CommentedConfigOption<?> defaultConfigOption : DefaultedBukkitConfig.ALL_OPTIONS) {
			defaultConfigOptions.add(defaultConfigOption.path());
		}
		List<String> optionsToRemove = new ArrayList<>();
		for (String option : configOptions) {
			if (!defaultConfigOptions.contains(option)) {
				optionsToRemove.add(option);
			}
		}
		return !optionsToRemove.isEmpty();
	}

}
