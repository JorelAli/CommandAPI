package io.github.jorelali;

import java.io.File;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;

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
		// Load the CommandAPI. We enable verbose logging and allow the CommandAPI
		// to generate a file command_registration.json for debugging purposes
		CommandAPI.onLoad(
			new CommandAPIBukkitConfig(this)
				.verboseOutput(true)
				.dispatcherFile(new File(getDataFolder(), "command_registration.json")));
	}

	@Override
	public void onEnable() {
		// Enable the CommandAPI
		CommandAPI.onEnable();

		MyCommands myCommands = new MyCommands(this);
		myCommands.registerAllCommands();
	}

	@Override
	public void onDisable() {
		CommandAPI.onDisable();
	}

}
