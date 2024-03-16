package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPISpigotConfig extends CommandAPIBukkitConfig<CommandAPISpigotConfig> {

	public CommandAPISpigotConfig(JavaPlugin plugin) {
		super(plugin);
	}

	@Override
	public CommandAPISpigotConfig instance() {
		return this;
	}
}
