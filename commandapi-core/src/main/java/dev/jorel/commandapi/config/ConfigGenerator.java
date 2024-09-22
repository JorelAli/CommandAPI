package dev.jorel.commandapi.config;

import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApiStatus.Internal
public class ConfigGenerator {

	private final DefaultedConfig defaultedConfig;

	private ConfigGenerator(DefaultedConfig defaultedConfig) {
		this.defaultedConfig = defaultedConfig;
	}

	public static ConfigGenerator createNew(DefaultedConfig defaultedConfig) {
		return new ConfigGenerator(defaultedConfig);
	}

	public <T, C extends DefaultedConfig> void populateDefaultConfig(ConfigurationAdapter<T, C> adapter) {
		for (Map.Entry<String, CommentedConfigOption<?>> commentedConfigOption : defaultedConfig.getAllOptions().entrySet()) {
			adapter.setValue(commentedConfigOption.getKey(), commentedConfigOption.getValue().option());
			adapter.setComment(commentedConfigOption.getKey(), commentedConfigOption.getValue().comment());
		}
	}

	@SuppressWarnings("unchecked")
	public <T, C extends DefaultedConfig> ConfigurationAdapter<T, C> generateWithNewValues(ConfigurationAdapter<T, C> existingConfig) {
		ConfigurationAdapter<T, C> updatedConfig = existingConfig.createNew();

		boolean shouldRemoveValues = shouldRemoveOptions(existingConfig);

		boolean wasConfigUpdated = false;
		for (Map.Entry<String, CommentedConfigOption<?>> commentedConfigOption : defaultedConfig.getAllOptions().entrySet()) {
			String path = commentedConfigOption.getKey();

			// Update config option
			if (existingConfig.contains(path)) {
				updatedConfig.tryCreateSection(path, (C) defaultedConfig);
				updatedConfig.setValue(path, existingConfig.getValue(path));
			} else {
				wasConfigUpdated = true;
				updatedConfig.tryCreateSection(path, (C) defaultedConfig);
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
		if (shouldRemoveValues) {
			wasConfigUpdated = true;
		}
		System.out.println(wasConfigUpdated);
		return (wasConfigUpdated) ? updatedConfig.complete() : null;
	}

	private <T, C extends DefaultedConfig> boolean shouldRemoveOptions(ConfigurationAdapter<T, C> config) {
		Set<String> configOptions = config.getKeys();
		configOptions.forEach(System.out::println);
		Set<String> defaultConfigOptions = defaultedConfig.getAllOptions().keySet();

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
