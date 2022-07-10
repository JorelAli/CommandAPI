package io.github.jorelali;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;

public class Main extends JavaPlugin {

	@Override
	public void onLoad() {
		// Load the CommandAPI. We enable verbose logging and allow the CommandAPI
		// to generate a file command_registration.json for debugging purposes
		CommandAPI.onLoad(
			new CommandAPIConfig()
				.verboseOutput(true)
				.dispatcherFile(new File(getDataFolder(), "command_registration.json"))
		);
	}
	
	@Override
	public void onEnable() {
		// Enable the CommandAPI
		CommandAPI.onEnable(this);
		
		MyCommands myCommands = new MyCommands(this);
		myCommands.registerAllCommands();
		myCommands.registerAllCommandTrees();
	}

}
