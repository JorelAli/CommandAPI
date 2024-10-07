package dev.jorel.commandapi.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public record VelocityConfigurationAdapter(YamlConfigurationLoader loader, CommentedConfigurationNode config, DefaultVelocityConfig defaultVelocityConfig) implements ConfigurationAdapter<ConfigurationNode> {

	public static VelocityConfigurationAdapter createDummyInstance() {
		return new VelocityConfigurationAdapter(null, null, null);
	}

	@Override
	public void setValue(String key, Object value) {
		try {
			node(key).set(value);
		} catch (SerializationException e) {
			e.printStackTrace(System.err);
		}
	}

	@Override
	public void setComment(String key, String[] comment) {
		StringBuilder commentBuilder = new StringBuilder();
		for (int i = 0; i < comment.length; i++) {
			if (i > 0) {
				commentBuilder.append("\n");
			}
			commentBuilder.append(comment[i]);
		}
		node(key).comment(commentBuilder.toString());
	}

	@Override
	public Object getValue(String key) {
		try {
			return node(key).get(Object.class);
		} catch (SerializationException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	@Override
	public String[] getComment(String key) {
		CommentedConfigOption<?> option = defaultVelocityConfig.getAllOptions().get(key);
		CommentedSection section = defaultVelocityConfig.getAllSections().get(key);
		return option != null ? option.comment() : section.comment();
	}

	@Override
	public Set<String> getKeys() {
		return nestedOptions(config);
	}

	@Override
	public boolean contains(String key) {
		return !node(key).virtual();
	}

	@Override
	public void tryCreateSection(String key) {
	}

	@Override
	public CommentedConfigurationNode config() {
		return config;
	}

	@Override
	public ConfigurationAdapter<ConfigurationNode> complete() {
		return this;
	}

	@Override
	public ConfigurationAdapter<ConfigurationNode> createNew() {
		return new VelocityConfigurationAdapter(loader, loader.createNode(), DefaultVelocityConfig.createDefault());
	}

	@Override
	public void saveDefaultConfig(File directory, File configFile, Logger logger) {
		YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
			.nodeStyle(NodeStyle.BLOCK)
			.file(configFile)
			.build();
		DefaultVelocityConfig defaultConfig = DefaultVelocityConfig.createDefault();
		ConfigGenerator configGenerator = ConfigGenerator.createNew(defaultConfig);
		if (!directory.exists()) {
			boolean createdDirectory = directory.mkdirs();
			if (!createdDirectory) {
				logger.severe("Failed to create directory for the CommandAPI's config.yml file!");
			}
			try {
				ConfigurationAdapter<ConfigurationNode> velocityConfigurationAdapter = new VelocityConfigurationAdapter(configLoader, configLoader.createNode(), defaultConfig);
				configGenerator.populateDefaultConfig(velocityConfigurationAdapter);
				configLoader.save(velocityConfigurationAdapter.config());
			} catch (IOException e) {
				logger.severe("Could not create default config file! This is (probably) a bug.");
				logger.severe("Error message: " + e.getMessage());
				logger.severe("Stacktrace:");
				for (StackTraceElement element : e.getStackTrace()) {
					logger.severe(element.toString());
				}
			}
		} else {
			try {
				// If the config does exist, update it if necessary
				CommentedConfigurationNode existingYamlConfig = configLoader.load();
				ConfigurationAdapter<ConfigurationNode> existingConfig = new VelocityConfigurationAdapter(configLoader, existingYamlConfig, defaultConfig);
				ConfigurationAdapter<ConfigurationNode> updatedConfig = configGenerator.generateWithNewValues(existingConfig);
				if (updatedConfig != null) {
					configLoader.save(updatedConfig.config());
				}
			} catch (IOException e) {
				logger.severe("Could not update config! This is (probably) a bug.");
				logger.severe("Error message: " + e.getMessage());
				logger.severe("Stacktrace:");
				for (StackTraceElement element : e.getStackTrace()) {
					logger.severe(element.toString());
				}
			}
		}
	}

	@SuppressWarnings("ConfusingArgumentToVarargsMethod")
	private CommentedConfigurationNode node(String path) {
		return config.node(path.split("\\."));
	}

	private Set<String> nestedOptions(ConfigurationNode node) {
		Set<String> keys = new HashSet<>();
		for (Object key : node.childrenMap().keySet()) {
			ConfigurationNode nestedNode = node.childrenMap().get(key);
			if (nestedNode.childrenMap().isEmpty()) {
				keys.add((String) key);
			} else {
				for (String nestedKey : nestedOptions(nestedNode)) {
					keys.add(key + "." + nestedKey);
				}
			}
		}
		return keys;
	}

}
