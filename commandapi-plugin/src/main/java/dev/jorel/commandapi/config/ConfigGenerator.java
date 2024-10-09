package dev.jorel.commandapi.config;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class ConfigGenerator {

	private final DefaultConfig defaultConfig;

	private ConfigGenerator(DefaultConfig defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	public static ConfigGenerator createNew(DefaultConfig defaultConfig) {
		return new ConfigGenerator(defaultConfig);
	}

	public <T, C extends DefaultConfig> ConfigurationAdapter<T, C> generate(ConfigurationAdapter<T, C> existingConfig) {
		ConfigurationAdapter<T, C> updatedConfig = existingConfig.createNew();

		boolean shouldRemoveValues = shouldRemoveOptions(existingConfig);

		boolean wasConfigUpdated = false;
		for (Map.Entry<String, CommentedConfigOption<?>> commentedConfigOption : defaultConfig.getAllOptions().entrySet()) {
			String path = commentedConfigOption.getKey();

			// Update config option
			if (existingConfig.contains(path)) {
				updatedConfig.tryCreateSection(path);
				updatedConfig.setValue(path, existingConfig.getValue(path));
			} else {
				wasConfigUpdated = true;
				updatedConfig.tryCreateSection(path);
				updatedConfig.setValue(path, commentedConfigOption.getValue().option());
			}

			// Update config option comment
			String[] defaultComment = commentedConfigOption.getValue().comment();
			String[] configComment = existingConfig.getComment(path);

			if (!Arrays.equals(defaultComment, configComment)) {
				wasConfigUpdated = true;
			}

			updatedConfig.setComment(path, commentedConfigOption.getValue().comment());
		}
		for (Map.Entry<String, CommentedSection> commentedSection : defaultConfig.getAllSections().entrySet()) {
			String[] defaultComment = commentedSection.getValue().comment();
			String[] configComment = existingConfig.getComment(commentedSection.getKey());

			if (!Arrays.equals(defaultComment, configComment)) {
				wasConfigUpdated = true;
			}

			updatedConfig.setComment(commentedSection.getKey(), commentedSection.getValue().comment());
		}
		if (shouldRemoveValues) {
			wasConfigUpdated = true;
		}
		return (wasConfigUpdated) ? updatedConfig.complete() : null;
	}

	private <T, C extends DefaultConfig> boolean shouldRemoveOptions(ConfigurationAdapter<T, C> config) {
		Set<String> configOptions = config.getKeys();
		Set<String> defaultConfigOptions = defaultConfig.getAllOptions().keySet();

		return !defaultConfigOptions.containsAll(configOptions);
	}

}
