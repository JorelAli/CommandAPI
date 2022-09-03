package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public record CommandResult(Command command, String[] args) {
	public void execute(CommandSender sender) {
		CommandAPI.logInfo("Executing command " + command.getName() + " with arguments: " + Arrays.toString(args));
		command.execute(sender, command.getLabel(), args);
	}
}
