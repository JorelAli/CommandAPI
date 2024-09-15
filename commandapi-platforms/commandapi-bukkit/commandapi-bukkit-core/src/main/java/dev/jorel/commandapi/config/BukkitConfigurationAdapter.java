package dev.jorel.commandapi.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
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
		List<String> comments = config.getStringList(key);
		comments.removeIf(Objects::isNull);
		return comments.toArray(new String[0]);
	}

	@Override
	public Set<String> getKeys() {
		return config.getKeys(false);
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
		ConfigurationSection root = null;
		StringBuilder pathSoFar = new StringBuilder();
		for (String sectionCandidate : sectionCandidates) {
			if (pathSoFar.isEmpty()) {
				pathSoFar.append(sectionCandidate);
			} else {
				pathSoFar.append(".").append(sectionCandidate);
			}
			if (keys.contains(sectionCandidate) && root == null) {
				root = config.getConfigurationSection(sectionCandidate);
			} else if (root == null) {
				root = config.createSection(sectionCandidate);
				root.setComments(sectionCandidate, defaultedBukkitConfig.getAllSections().get(pathSoFar.toString()).comment());
			} else {
				ConfigurationSection section = root.getConfigurationSection(sectionCandidate);
				if (section == null) {
					root = root.createSection(sectionCandidate);
					root.setComments(sectionCandidate, defaultedBukkitConfig.getAllSections().get(pathSoFar.toString()).comment());
				} else {
					root = section;
				}
			}
		}
	}

	@Override
	public ConfigurationAdapter<YamlConfiguration, DefaultedBukkitConfig> createNew() {
		return new BukkitConfigurationAdapter(new YamlConfiguration());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BukkitConfigurationAdapter that = (BukkitConfigurationAdapter) o;
		String thisConfigString = config.saveToString();
		String thatConfigString = that.config.saveToString();
		return thisConfigString.equals(thatConfigString);
	}

}
