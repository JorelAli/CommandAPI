package dev.jorel.commandapi.test;

import dev.jorel.commandapi.CommandAPIPaper;

public abstract class PaperMockPlatform<CLW> extends CommandAPIPaper<CLW> {

	private static MockPlatform<?> bukkit = null;
	private static PaperMockPlatform<?> instance = null;

	public static <CLW> MockPlatform<CLW> getBukkit() {
		return (MockPlatform<CLW>) bukkit;
	}

	public static <CLW> PaperMockPlatform<CLW> getPaper() {
		return (PaperMockPlatform<CLW>) instance;
	}

	protected PaperMockPlatform() {
		if (PaperMockPlatform.instance == null) {
			bukkit = (MockPlatform<?>) bukkitNMS();
			instance = this;
		} else {
			return;
		}
	}

}
