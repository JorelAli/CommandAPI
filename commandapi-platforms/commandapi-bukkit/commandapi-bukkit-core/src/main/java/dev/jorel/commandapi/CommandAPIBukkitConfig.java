package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIBukkitConfig extends CommandAPIConfig {
	JavaPlugin plugin;

	public CommandAPIBukkitConfig(JavaPlugin plugin) {
		this.plugin = plugin;
	}
}
