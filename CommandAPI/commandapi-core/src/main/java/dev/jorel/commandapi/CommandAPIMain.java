package dev.jorel.commandapi;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(this);
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		{
			new CommandAPICommand("break")
	            .executesNative((sender, args) -> {
	            	Location location = (Location) sender.getLocation();
	            	location.getBlock().breakNaturally();
	            })
	            .register();
		}
	}
}
