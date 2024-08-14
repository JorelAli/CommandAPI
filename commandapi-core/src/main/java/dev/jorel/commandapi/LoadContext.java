package dev.jorel.commandapi;

public record LoadContext(CommandAPIPlatform<?, ?, ?> platform, Runnable context) {

	public LoadContext(CommandAPIPlatform<?, ?, ?> platform) {
		this(platform, () -> {});
	}

}
