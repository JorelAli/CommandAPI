package dev.jorel.commandapi;

import java.util.logging.Logger;

// TODO: Since Bukkit and Velocity use Java's builtin logger, this and BukkitLogger are essentially the same
//  Should these classes be combined to reduce code repetition?
public class VelocityLogger implements CommandAPILogger{
	private final Logger parent;

	public VelocityLogger(Logger parent) {
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
