package dev.jorel.commandapi;

import java.util.function.Consumer;

public interface CommandAPILogger {
	static CommandAPILogger fromJavaLogger(java.util.logging.Logger logger) {
		return bindToMethods(logger::info, logger::warning, logger::severe);
	}

	static CommandAPILogger fromApacheLog4jLogger(org.apache.logging.log4j.Logger logger) {
		return bindToMethods(logger::info, logger::warn, logger::error);
	}

	static CommandAPILogger fromSlf4jLogger(org.slf4j.Logger logger) {
		return bindToMethods(logger::info, logger::warn, logger::error);
	}

	static CommandAPILogger bindToMethods(Consumer<String> info, Consumer<String> warning, Consumer<String> severe) {
		return new CommandAPILogger() {
			@Override
			public void info(String message) {
				info.accept(message);
			}

			@Override
			public void warning(String message) {
				warning.accept(message);
			}

			@Override
			public void severe(String message) {
				severe.accept(message);
			}
		};
	}

	void info(String message);

	void warning(String message);

	void severe(String message);
}
