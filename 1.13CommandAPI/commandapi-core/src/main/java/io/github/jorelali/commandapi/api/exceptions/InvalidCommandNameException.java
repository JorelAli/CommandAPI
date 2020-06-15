package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class InvalidCommandNameException extends RuntimeException {
		
	public InvalidCommandNameException(String commandName) {
		super("Invalid command with name '" + commandName + "' cannot be registered!");
	}
	
}
