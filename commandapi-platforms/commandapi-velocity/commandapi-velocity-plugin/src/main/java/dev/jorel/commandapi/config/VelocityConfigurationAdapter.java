package dev.jorel.commandapi.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ConfusingArgumentToVarargsMethod")
public record VelocityConfigurationAdapter(YamlConfigurationLoader loader, CommentedConfigurationNode config) implements ConfigurationAdapter<ConfigurationNode, DefaultedVelocityConfig> {

	@Override
	public void setValue(String key, Object value) {
		setValue0(key, value);
	}

	@Override
	public void setComment(String key, String[] comment) {
		config.node(key.split("\\.")).comment(String.join("\n", comment));
	}

	@Override
	public Object getValue(String key) {
		return getValue0(key);
	}

	@SuppressWarnings("DataFlowIssue")
	@Override
	public String[] getComment(String key) {
		return config.node(key.split("\\.")).comment().split("\n");
	}

	@Override
	public Set<String> getKeys() {
		Set<String> keys = new HashSet<>();
		for (Object key : config.childrenMap().keySet()) {
			keys.add((String) key);
		}
		return keys;
	}

	@Override
	public boolean contains(String key) {
		return !config.node(key.split("\\.")).virtual();
	}

	@Override
	public void tryCreateSection(String key, DefaultedVelocityConfig defaultedVelocityConfig) {
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
			if (config.node(pathSoFar.toString().split("\\.")).comment() == null) {
				config.node(pathSoFar.toString().split("\\.")).comment(String.join("\n", defaultedVelocityConfig.getAllSections().get(pathSoFar.toString()).comment()));
			}
		}
	}

	@Override
	public CommentedConfigurationNode config() {
		return config;
	}

	@Override
	public ConfigurationAdapter<ConfigurationNode, DefaultedVelocityConfig> createNew() {
		return new VelocityConfigurationAdapter(loader, loader.createNode());
	}

	private void setValue0(String key, Object value) {
		try {
			config.node(key.split("\\.")).set(value);
		} catch (SerializationException e) {
			e.printStackTrace(System.err);
		}
	}

	private Object getValue0(String key) {
		try {
			return config.node(key.split("\\.")).get(Object.class);
		} catch (SerializationException e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

}
