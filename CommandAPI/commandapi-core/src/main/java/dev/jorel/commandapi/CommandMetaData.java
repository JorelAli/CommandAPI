package dev.jorel.commandapi;

import dev.jorel.commandapi.exceptions.InvalidCommandNameException;
import org.bukkit.command.CommandSender;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class stores metadata about a command
 */
final class CommandMetaData {

	/**
	 * The command's name
	 */
	final String commandName;
	/**
	 * The command's permission
	 */
	CommandPermission permission = CommandPermission.NONE;
	/**
	 * The command's aliases
	 */
	String[] aliases = new String[0];
	/**
	 * A predicate that a {@link CommandSender} must pass in order to execute the command
	 */
	Predicate<CommandSender> requirements = s -> true;
	/**
	 * An optional short description for the command
	 */
	Optional<String> shortDescription = Optional.empty();
	/**
	 * An optional full description for the command
	 */
	Optional<String> fullDescription = Optional.empty();

	/**
	 * Create command metadata
	 * @param commandName The command's name
	 *
	 * @throws InvalidCommandNameException if the command name is not valid
	 */
	CommandMetaData(final String commandName) {
		if(commandName == null || commandName.length() == 0) {
			throw new InvalidCommandNameException(commandName);
		}

		this.commandName = commandName;
	}

}
