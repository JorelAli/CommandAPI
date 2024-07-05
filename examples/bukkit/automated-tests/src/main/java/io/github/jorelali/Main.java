package io.github.jorelali;

import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		// TODO: Test arguments, suggestions
		new CommandAPICommand("ping").executes((sender, args) -> {
			sender.sendMessage("pong!");
		}).register();
	}
}
