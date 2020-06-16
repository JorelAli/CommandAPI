package dev.jorel.commandapi.exceptions;

@SuppressWarnings("serial")
public class EmptyExecutorException extends RuntimeException {
	
	public EmptyExecutorException() {
		super("Cannot create a command that doesn't do anything!");
	}
	
}
