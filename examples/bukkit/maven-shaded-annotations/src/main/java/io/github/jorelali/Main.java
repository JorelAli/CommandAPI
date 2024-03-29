package io.github.jorelali;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPIConfig;
import dev.jorel.commandapi.CommandAPILogger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
	@Override
	public void onLoad() {
		// Set CommandAPI to use this plugin's logger
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(getLogger()));

		// Load the CommandAPI
		CommandAPI.onLoad(
			// Configure the CommandAPI
			new CommandAPIBukkitConfig(this)
				// Turn on verbose output for command registration logs
				.verboseOutput(true)
				// Give file where Brigadier's command registration tree should be dumped
				.dispatcherFile(new File(getDataFolder(), "command_registration.json"))
		);
	}

	@Override
	public void onEnable() {
		// Enable the CommandAPI
		CommandAPI.onEnable();

		// Register commands using a reference to their class
		CommandAPI.registerCommand(BreakCommand.class);
		CommandAPI.registerCommand(EffectCommand.class);
		CommandAPI.registerCommand(WarpCommand.class);
	}
}
