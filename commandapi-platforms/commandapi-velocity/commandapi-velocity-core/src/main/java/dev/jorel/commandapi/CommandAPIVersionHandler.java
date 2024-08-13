package dev.jorel.commandapi;

public interface CommandAPIVersionHandler {
	static LoadContext getPlatform() {
		return new LoadContext(new CommandAPIVelocity(), () -> {});
	}
}
