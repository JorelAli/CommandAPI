package io.github.jorelali.commandapi.api.exceptions;

public class InvalidCommandNameException extends RuntimeException {
	
	private static final long serialVersionUID = -813921221648908805L;
	
	private String commandName;
	
	public InvalidCommandNameException(String commandName) {
		this.commandName = commandName;
	}
	
	@Override
    public String getMessage() {
		return "Invalid command with name '" + commandName + "' cannot be registered!";
    }
	
}
