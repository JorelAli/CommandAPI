package io.github.jorelali.commandapi.api.exceptions;

public class EmptyExecutorException extends RuntimeException {
		
	private static final long serialVersionUID = -5651237888090280749L;
	
	public EmptyExecutorException() {
		super("Cannot create a command that doesn't do anything!");
	}
	
}
