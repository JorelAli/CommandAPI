package io.github.jorelali;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		// Register commands using our MyCommands class
		MyCommands myCommands = new MyCommands(this);
		myCommands.registerAllCommands();
		myCommands.registerAllCommandTrees();
	}
}
