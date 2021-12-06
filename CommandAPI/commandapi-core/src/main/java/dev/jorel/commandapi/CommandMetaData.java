package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.function.Predicate;

final class CommandMetaData {

	final String commandName;
	CommandPermission permission = CommandPermission.NONE;
	String[] aliases = new String[0];
	Predicate<CommandSender> requirements = s -> true;
	Optional<String> shortDescription = Optional.empty();
	Optional<String> fullDescription = Optional.empty();

	CommandMetaData(final String commandName) {
		if(commandName == null || commandName.length() == 0) {
			throw new InvalidCommandNameException(commandName);
		}

		this.commandName = commandName;
	}

}
