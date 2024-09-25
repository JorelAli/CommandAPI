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
		return new HashSet<>(nestedOptions(config));
	}

	@Override
	public boolean contains(String key) {
		return !node(key).virtual();
	}

	@Override
	public void tryCreateSection(String key, DefaultConfig defaultedVelocityConfig) {
		if (!key.contains(".")) {
			return;
		}
		String[] path = key.split("\\.");
		List<String> sectionCandidates = new ArrayList<>(Arrays.asList(path).subList(0, path.length - 1));

		StringBuilder pathSoFar = new StringBuilder();
		for (String section : sectionCandidates) {
			if (pathSoFar.isEmpty()) {
				pathSoFar.append(section);
			} else {
				pathSoFar.append(".").append(section);
			}
			if (node(pathSoFar.toString()).comment() == null) {
				node(pathSoFar.toString()).comment(String.join("\n", defaultedVelocityConfig.getAllSections().get(pathSoFar.toString()).comment()));
			}
		}
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
			directory.mkdirs();

			try {
				ConfigurationAdapter<ConfigurationNode> velocityConfigurationAdapter = new VelocityConfigurationAdapter(configLoader, configLoader.createNode(), defaultConfig);
				configGenerator.populateDefaultConfig(velocityConfigurationAdapter);
				configLoader.save(velocityConfigurationAdapter.config());
			} catch (IOException e) {
				throw new RuntimeException(e);
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
				throw new RuntimeException(e);
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
