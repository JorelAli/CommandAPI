package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIBukkitConfig extends CommandAPIConfig<CommandAPIBukkitConfig> {
	JavaPlugin plugin;
	// Default configuration
	boolean shouldHookPaperReload = true;

	public CommandAPIBukkitConfig(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public CommandAPIBukkitConfig shouldHookPaperReload(boolean hooked) {
		this.shouldHookPaperReload = hooked;

		return this;
	}

	@Override
	public CommandAPIBukkitConfig instance() {
		return this;
	}
}
