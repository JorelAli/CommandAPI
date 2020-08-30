package dev.jorel.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LocationArgument;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
		arguments.put("locations", new LocationArgument()
			.safeOverrideSuggestionsT((sender) -> {
				return new Tooltip[] {
					Tooltip.of(((Player) sender).getWorld().getSpawnLocation(), "World spawn"),
					Tooltip.of(((Player) sender).getLocation(), "Your location")
				};
			}));
		
		new CommandAPICommand("qtp")
			.withArguments(arguments)
			.executesPlayer((player, args) -> {
				Location location = (Location) args[0];
				player.teleport(location);
			})
			.register();
	}
	
}
