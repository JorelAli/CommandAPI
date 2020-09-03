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
		
		new CommandAPICommand("sbb")
			.executesNative((sender, args) -> {
				System.out.println(sender.getWorld());
				System.out.println(sender.getLocation());
				System.out.println(sender.getCallee().getName());
				System.out.println(sender.getCaller().getName());
			})
			.register();
	}
}
