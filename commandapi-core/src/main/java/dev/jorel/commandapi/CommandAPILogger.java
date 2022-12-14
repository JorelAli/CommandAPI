package dev.jorel.commandapi;

public interface CommandAPILogger {
	void info(String message);

	void warning(String message);

	void severe(String message);
}
