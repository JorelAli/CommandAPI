package dev.jorel.commandapi;

import org.apache.logging.log4j.Logger;

public class SpongeLogger implements CommandAPILogger {
	private final Logger parent;

	public SpongeLogger(Logger parent) {
		this.parent = parent;
	}

	@Override
	public void info(String message) {
		parent.info(message);
	}

	@Override
	public void warning(String message) {
		parent.warn(message);
	}

	@Override
	public void severe(String message) {
		parent.error(message);
	}
}
