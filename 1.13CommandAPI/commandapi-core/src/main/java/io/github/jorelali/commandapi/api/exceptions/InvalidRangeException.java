package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class InvalidRangeException extends RuntimeException {

	public InvalidRangeException() {
		super("Cannot have a maximum value smaller than a minimum value");
	}
	
}
