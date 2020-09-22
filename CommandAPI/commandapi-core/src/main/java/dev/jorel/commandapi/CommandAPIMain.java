package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(this);
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		new CommandAPICommand("mycommand")
		.withPermission(CommandPermission.OP)
		.executes((s, a) -> {
			System.out.println("hi");
		}).register();
	}
}
