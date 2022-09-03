package dev.jorel.commandapi.arguments;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public record CommandResult(Command command, String[] args) {
	public void execute(CommandSender sender) {
		command.execute(sender, command.getLabel(), args);
	}
}
