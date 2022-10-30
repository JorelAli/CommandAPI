package io.github.jorelali;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		// Register commands using a reference to their class
		CommandAPI.registerCommand(BreakCommand.class);
		CommandAPI.registerCommand(EffectCommand.class);
		CommandAPI.registerCommand(NBTCommand.class);
	}
}
