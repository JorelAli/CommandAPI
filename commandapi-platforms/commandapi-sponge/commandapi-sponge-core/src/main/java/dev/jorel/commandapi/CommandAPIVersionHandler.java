package dev.jorel.commandapi;

public interface CommandAPIVersionHandler {
	static AbstractPlatform<?, ?, ?> getPlatform() {
		return new SpongePlatform();
	}
}
