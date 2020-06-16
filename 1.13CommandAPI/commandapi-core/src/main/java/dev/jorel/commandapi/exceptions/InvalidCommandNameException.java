package dev.jorel.commandapi.exceptions;

@SuppressWarnings("serial")
public class InvalidCommandNameException extends RuntimeException {
		
	public InvalidCommandNameException(String commandName) {
		super("Invalid command with name '" + commandName + "' cannot be registered!");
	}
	
}
