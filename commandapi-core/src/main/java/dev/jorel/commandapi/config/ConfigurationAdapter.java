package dev.jorel.commandapi.config;

import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

@ApiStatus.Internal
public interface ConfigurationAdapter<Configuration, DefaultConfiguration extends DefaultedConfig> {

	void setValue(String key, Object value);

	void setComment(String key, String[] comment);

	Object getValue(String key);

	String[] getComment(String key);

	Set<String> getKeys();

	boolean contains(String key);

	void tryCreateSection(String key, DefaultConfiguration defaultConfiguration);

	ConfigurationAdapter<Configuration, DefaultConfiguration> complete();

	Configuration config();

	ConfigurationAdapter<Configuration, DefaultConfiguration> createNew();

}
