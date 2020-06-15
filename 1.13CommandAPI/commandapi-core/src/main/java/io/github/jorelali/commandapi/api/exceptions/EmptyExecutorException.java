package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class EmptyExecutorException extends RuntimeException {
	
	public EmptyExecutorException() {
		super("Cannot create a command that doesn't do anything!");
	}
	
}
