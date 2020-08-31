package dev.jorel.commandapi;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {
		CommandAPI.onLoad(this);
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		{
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("location", new LocationArgument()
	.safeOverrideSuggestionsT((sender) -> {
		return Tooltip.arrayOf(
			Tooltip.of(((Player) sender).getWorld().getSpawnLocation(), "World spawn"),
			Tooltip.of(((Player) sender).getBedSpawnLocation(), "Your bed"),
			Tooltip.of(((Player) sender).getTargetBlockExact(256).getLocation(), "Target block")
		);
	}));

new CommandAPICommand("warp")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		player.teleport((Location) args[0]);
	})
	.register();
		}
		
		{
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("emote", new StringArgument()
	.overrideSuggestionsT( 
		StringTooltip.arrayOf(
			StringTooltip.of("wave", "Waves at a player"),
			StringTooltip.of("hug", "Gives a player a hug"),
			StringTooltip.of("glare", "Gives a player the death glare")
		)
	));
arguments.put("target", new PlayerArgument());

new CommandAPICommand("emote")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		String emote = (String) args[0];
		Player target = (Player) args[1];
		
		switch(emote) {
		case "wave":
			target.sendMessage(player.getName() + " waves at you!");
			break;
		case "hug":
			target.sendMessage(player.getName() + " hugs you!");
			break;
		case "glare":
			target.sendMessage(player.getName() + " gives you the death glare...");
			break;
		}
	})
	.register();
		}
	}
	
}
