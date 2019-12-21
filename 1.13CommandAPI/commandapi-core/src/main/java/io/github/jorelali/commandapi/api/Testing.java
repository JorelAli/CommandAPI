package io.github.jorelali.commandapi.api;

import java.util.LinkedHashMap;

import org.bukkit.entity.Player;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.PlayerArgument;

public class Testing {

	public void a() {
		
// Register the /god command with the permission node "command.god"
new CommandAPICommand("god")
	.withPermission(CommandPermission.fromString("command.god"))
	.executesPlayer((player, args) -> {
		player.setInvulnerable(true);
	})
	.register();



// Register /kill command normally. Since no permissions are applied, anyone can run this command
new CommandAPICommand("kill")
	.executesPlayer((player, args) -> {
		player.setHealth(0);
	})
	.register();

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("target", new PlayerArgument().withPermission(CommandPermission.OP));

//Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
new CommandAPICommand("kill")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		((Player) args[0]).setHealth(0);
	})
	.register();












	}
}
