package dev.jorel.commandapi.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@ApiStatus.Internal
public record BukkitConfigurationAdapter(YamlConfiguration config) implements ConfigurationAdapter<YamlConfiguration, DefaultedBukkitConfig> {

	@Override
	public void setValue(String key, Object value) {
		config.set(key, value);
	}

	@Override
	public void setComment(String key, String[] comment) {
		config.setComments(key, Arrays.asList(comment));
	}

	@Override
	public Object getValue(String key) {
		return config.get(key);
	}

	@Override
	public String[] getComment(String key) {
		List<String> comments = new ArrayList<>(config.getComments(key));
		comments.removeIf(Objects::isNull);
		return comments.toArray(new String[0]);
	}

	@Override
	public Set<String> getKeys() {
		Set<String> keys = new HashSet<>(config.getKeys(true));
		keys.removeIf(config::isConfigurationSection);
		return keys;
	}

	@Override
	public boolean contains(String key) {
		return config.contains(key);
	}

	@Override
	public void tryCreateSection(String key, DefaultedBukkitConfig defaultedBukkitConfig) {
		if (!key.contains(".")) {
			return;
		}

		// Collect config keys
		Set<String> keys = getKeys();
		keys.removeIf(k -> !config.isConfigurationSection(k));

		// Collect sections
		String[] paths = key.split("\\.");
		List<String> sectionCandidates = new ArrayList<>(Arrays.asList(paths).subList(0, paths.length - 1));

		// Create new sections
		ConfigurationSection section = null;
		StringBuilder pathSoFar = new StringBuilder();
		for (String sectionCandidate : sectionCandidates) {
			if (pathSoFar.isEmpty()) {
				pathSoFar.append(sectionCandidate);
			} else {
				pathSoFar.append(".").append(sectionCandidate);
			}

			if (keys.contains(sectionCandidate) && section == null) {
				section = config.getConfigurationSection(sectionCandidate);
			} else if (section == null) {
				section = config.createSection(sectionCandidate);
				config.setComments(pathSoFar.toString(), defaultedBukkitConfig.getAllSections().get(pathSoFar.toString()).comment());
			} else {
				ConfigurationSection currentSection = section.getConfigurationSection(sectionCandidate);
				if (currentSection == null) {
					section = section.createSection(sectionCandidate);
					config.setComments(pathSoFar.toString(), defaultedBukkitConfig.getAllSections().get(pathSoFar.toString()).comment());
				} else {
					section = currentSection;
				}
			}
		}
	}

	@Override
	public ConfigurationAdapter<YamlConfiguration, DefaultedBukkitConfig> complete() {
		String[] configStrings = config.saveToString().split("\n");
		StringBuilder configBuilder = new StringBuilder();
		for (String configString : configStrings) {
			configBuilder.append(configString).append("\n");
			if (!configString.contains("#")) {
				configBuilder.append("\n");
			}
		}
		try {
			config.loadFromString(configBuilder.toString());
		} catch (InvalidConfigurationException e) {
			e.printStackTrace(System.err);
		}
		return this;
	}

	@Override
	public ConfigurationAdapter<YamlConfiguration, DefaultedBukkitConfig> createNew() {
		return new BukkitConfigurationAdapter(new YamlConfiguration());
	}

}
