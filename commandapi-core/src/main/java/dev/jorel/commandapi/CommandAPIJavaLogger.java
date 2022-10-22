package dev.jorel.commandapi;

import java.util.logging.Logger;

public class CommandAPIJavaLogger implements CommandAPILogger {
	private final Logger parent;

	public CommandAPIJavaLogger(Logger parent) {
		this.parent = parent;
	}

	@Override
	public void info(String message) {
		parent.info(message);
	}

	@Override
	public void warning(String message) {
		parent.warning(message);
	}

	@Override
	public void severe(String message) {
		parent.severe(message);
	}
}

