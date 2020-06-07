package io.github.jorelali.commandapi.api.exceptions;

public class InvalidCommandNameException extends RuntimeException {
	
	private static final long serialVersionUID = -813921221648908805L;
		
	public InvalidCommandNameException(String commandName) {
		super("Invalid command with name '" + commandName + "' cannot be registered!");
	}
	
}
