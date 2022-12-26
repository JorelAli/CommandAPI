package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class InternalBukkitConfig extends InternalConfig {
	private final JavaPlugin plugin;

	public InternalBukkitConfig(CommandAPIBukkitConfig config) {
		super(config);
		this.plugin = config.plugin;
	}

	public JavaPlugin getPlugin() {
		return plugin;
	}
}
