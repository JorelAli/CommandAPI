package io.github.jorelali;

import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;

public class Main extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIConfig().verboseOutput(true));
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		CommandAPI.registerCommand(TestCommand.class);
	}
	
	@Override
	public void onDisable() {
		CommandAPI.onDisable();
	}

}
