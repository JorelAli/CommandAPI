package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when trying to register a command with an invalid name
 */
@SuppressWarnings("serial")
public class InvalidCommandNameException extends RuntimeException {
	
	/**
	 * Creates an InvalidCommandNameException
	 * @param commandName the invalid command name
	 */
	public InvalidCommandNameException(String commandName) {
		super("Invalid command with name '" + commandName + "' cannot be registered!");
	}
	
}
