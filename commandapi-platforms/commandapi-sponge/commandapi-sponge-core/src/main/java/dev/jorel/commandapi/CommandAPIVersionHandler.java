package dev.jorel.commandapi;

public abstract class CommandAPIVersionHandler {
	static CommandAPIPlatform<?, ?, ?> getPlatform() {
		return new CommandAPISponge();
	}
}
