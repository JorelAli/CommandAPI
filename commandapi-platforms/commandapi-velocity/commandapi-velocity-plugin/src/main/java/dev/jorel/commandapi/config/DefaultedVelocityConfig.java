package dev.jorel.commandapi.config;

import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

@SuppressWarnings("ClassEscapesDefinedScope")
@ApiStatus.Internal
public class DefaultedVelocityConfig extends DefaultedConfig {

	private DefaultedVelocityConfig() {}

	public static DefaultedVelocityConfig createDefault() {
		return DefaultedVelocityConfig.create(
			Map.ofEntries(
				Map.entry("verbose-outputs", VERBOSE_OUTPUTS),
				Map.entry("silent-logs", SILENT_LOGS),
				Map.entry("messages.missing-executor-implementation", MISSING_EXECUTOR_IMPLEMENTATION),
				Map.entry("create-dispatcher-json", CREATE_DISPATCHER_JSON)
			),
			Map.ofEntries(
				Map.entry("messages", SECTION_MESSAGE)
			)
		);
	}

	public static DefaultedVelocityConfig create(Map<String, CommentedConfigOption<?>> options, Map<String, CommentedSection> sections) {
		DefaultedVelocityConfig config = new DefaultedVelocityConfig();

		config.allOptions.putAll(options);
		config.allSections.putAll(sections);

		return config;
	}

}
