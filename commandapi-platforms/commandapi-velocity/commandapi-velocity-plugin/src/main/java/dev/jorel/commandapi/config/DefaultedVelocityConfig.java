package dev.jorel.commandapi.config;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("ClassEscapesDefinedScope")
public class DefaultedVelocityConfig extends DefaultedConfig {

	private DefaultedVelocityConfig() {}

	public static DefaultedVelocityConfig createDefault() {
		Map<String, CommentedConfigOption<?>> options = new LinkedHashMap<>();
		options.put("verbose-outputs", VERBOSE_OUTPUTS);
		options.put("silent-logs", SILENT_LOGS);
		options.put("messages.missing-executor-implementation", MISSING_EXECUTOR_IMPLEMENTATION);
		options.put("create-dispatcher-json", CREATE_DISPATCHER_JSON);

		Map<String, CommentedSection> sections = new LinkedHashMap<>();
		sections.put("messages", SECTION_MESSAGE);

		return DefaultedVelocityConfig.create(
			options,
			sections
		);
	}

	public static DefaultedVelocityConfig create(Map<String, CommentedConfigOption<?>> options, Map<String, CommentedSection> sections) {
		DefaultedVelocityConfig config = new DefaultedVelocityConfig();

		config.allOptions.putAll(options);
		config.allSections.putAll(sections);

		return config;
	}

}
