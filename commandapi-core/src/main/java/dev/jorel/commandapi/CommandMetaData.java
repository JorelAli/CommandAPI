package dev.jorel.commandapi;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.exceptions.InvalidCommandNameException;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This class stores metadata about a command
 */
final class CommandMetaData<CommandSender> {

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
	 * A predicate that a {@link AbstractCommandSender} must pass in order to execute the command
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
		if(commandName == null || commandName.isEmpty() || commandName.contains(" ")) {
			throw new InvalidCommandNameException(commandName);
		}

		this.commandName = commandName;
	}
	
	public CommandMetaData(CommandMetaData<CommandSender> original) {
		this(original.commandName);
		this.permission = original.permission;
		this.aliases = Arrays.copyOf(original.aliases, original.aliases.length);
		this.requirements = original.requirements;
		this.shortDescription = original.shortDescription.isPresent() ? Optional.of(original.shortDescription.get()) : Optional.empty();
		this.fullDescription = original.fullDescription.isPresent() ? Optional.of(original.fullDescription.get()) : Optional.empty();
	}

}