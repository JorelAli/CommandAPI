package dev.jorel.commandapi.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

public class BukkitConfigurationAdapter extends ConfigurationAdapter<YamlConfiguration, DefaultBukkitConfig> {

	private final YamlConfiguration config;

	public BukkitConfigurationAdapter(YamlConfiguration config) {
		this.config = config;
	}

	public static BukkitConfigurationAdapter createDummyInstance() {
		return new BukkitConfigurationAdapter(null);
	}

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
	public void tryCreateSection(String key) {
		if (!key.contains(".")) {
			return;
		}

		// Collect config keys
		Set<String> keys = config.getKeys(true);
		keys.removeIf(k -> !config.isConfigurationSection(k));

		// Collect sections
		String[] sectionCandidates = key.split("\\.");
		sectionCandidates = Arrays.copyOf(sectionCandidates, sectionCandidates.length - 1);

		// Create new sections
		ConfigurationSection section = null;
		for (String sectionCandidate : sectionCandidates) {
			if (keys.contains(sectionCandidate) && section == null) {
				section = config.getConfigurationSection(sectionCandidate);
			} else if (section == null) {
				section = config.createSection(sectionCandidate);
			} else {
				ConfigurationSection currentSection = section.getConfigurationSection(sectionCandidate);
				if (currentSection == null) {
					section = section.createSection(sectionCandidate);
				} else {
					section = currentSection;
				}
			}
		}
	}

	@Override
	public ConfigurationAdapter<YamlConfiguration, DefaultBukkitConfig> complete() {
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
	public YamlConfiguration config() {
		return config;
	}

	@Override
	public ConfigurationAdapter<YamlConfiguration, DefaultBukkitConfig> createNew() {
		return new BukkitConfigurationAdapter(new YamlConfiguration());
	}

	@Override
	public DefaultBukkitConfig createDefaultConfig() {
		return DefaultBukkitConfig.createDefault();
	}

	@Override
	public ConfigurationAdapter<YamlConfiguration, DefaultBukkitConfig> loadFromFile(File file) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		return new BukkitConfigurationAdapter(config);
	}

	@Override
	public void saveToFile(File file) throws IOException {
		config.save(file);
	}

}