package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when a command has no executor
 */
@SuppressWarnings("serial")
public class EmptyExecutorException extends RuntimeException {
	
	public EmptyExecutorException() {
		super("Cannot create a command that doesn't do anything!");
	}
	
}
