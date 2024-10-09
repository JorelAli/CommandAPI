package dev.jorel.commandapi.config;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Logger;

public abstract class ConfigurationAdapter<Configuration, DefaultConfig extends dev.jorel.commandapi.config.DefaultConfig> {

	public abstract void setValue(String key, Object value);

	public abstract void setComment(String key, String[] comment);

	public abstract Object getValue(String key);

	public abstract String[] getComment(String key);

	public abstract Set<String> getKeys();

	public abstract boolean contains(String key);

	public abstract void tryCreateSection(String key);

	public abstract ConfigurationAdapter<Configuration, DefaultConfig> complete();

	public abstract Configuration config();

	public abstract ConfigurationAdapter<Configuration, DefaultConfig> createNew();

	public abstract DefaultConfig createDefaultConfig();

	public abstract ConfigurationAdapter<Configuration, DefaultConfig> loadFromFile(File file) throws IOException;

	public abstract void saveToFile(File file) throws IOException;

	public void saveDefaultConfig(File directory, File configFile, Logger logger) {
		ConfigGenerator generator = ConfigGenerator.createNew(createDefaultConfig());
		ConfigurationAdapter<Configuration, DefaultConfig> existingConfig;
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				logger.severe("Failed to create directory for the CommandAPI's config.yml file!");
			}
			existingConfig = createNew();
		} else {
			try {
				existingConfig = loadFromFile(configFile);
			} catch (IOException e) {
				logger.severe("Failed to load the config file!");
				return;
			}
		}
		ConfigurationAdapter<Configuration, DefaultConfig> updatedConfig = generator.generate(existingConfig);
		if (updatedConfig == null) {
			return;
		}
		try {
			updatedConfig.saveToFile(configFile);
		} catch (IOException e) {
			logger.severe("Failed to save the config file!");
			logger.severe("Error message: " + e.getMessage());
			logger.severe("Stacktrace:");
			for (StackTraceElement element : e.getStackTrace()) {
				logger.severe(element.toString());
			}
		}
	}

}
