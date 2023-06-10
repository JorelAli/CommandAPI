package io.github.jorelali;

import io.github.jorelali.delayhandler.DelayHandlerCommands;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		DelayHandlerCommands.registerCommands();
	}
}
