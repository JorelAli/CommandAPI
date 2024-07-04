package io.github.jorelali;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class Main extends JavaPlugin {
	// Additional constructors required for MockBukkit
	public Main() {
		super();
	}

	public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable();

		// TODO: Test arguments, suggestions
		new CommandAPICommand("ping").executes((sender, args) -> {
			sender.sendMessage("pong!");
		}).register();
	}
}
