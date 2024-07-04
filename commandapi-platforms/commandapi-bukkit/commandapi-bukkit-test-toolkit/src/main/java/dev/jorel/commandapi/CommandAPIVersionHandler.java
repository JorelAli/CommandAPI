package dev.jorel.commandapi;

public interface CommandAPIVersionHandler {
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		return new MockCommandAPIBukkit();
	}
}
