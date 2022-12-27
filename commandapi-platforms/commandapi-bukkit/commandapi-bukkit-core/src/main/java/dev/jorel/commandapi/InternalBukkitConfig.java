package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class InternalBukkitConfig extends InternalConfig {
	private final JavaPlugin plugin;
	private final boolean shouldHookPaperReload;

	public InternalBukkitConfig(CommandAPIBukkitConfig config) {
		super(config);
		this.plugin = config.plugin;
		this.shouldHookPaperReload = config.shouldHookPaperReload;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}

	public boolean shouldHookPaperReload() {
		return shouldHookPaperReload;
	}
}
