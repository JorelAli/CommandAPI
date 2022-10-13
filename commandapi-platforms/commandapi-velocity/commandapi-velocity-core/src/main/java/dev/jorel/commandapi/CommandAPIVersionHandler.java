package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractPlatform;

public interface CommandAPIVersionHandler {
	static AbstractPlatform<?> getPlatform() {
		return new VelocityPlatform();
	}
}
