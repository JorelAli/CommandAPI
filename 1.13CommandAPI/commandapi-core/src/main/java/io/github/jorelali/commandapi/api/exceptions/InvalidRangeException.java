package io.github.jorelali.commandapi.api.exceptions;

public class InvalidRangeException extends RuntimeException {
	
	private static final long serialVersionUID = 1966188186545669972L;

	@Override
    public String getMessage() {
		return "Cannot have a maximum value smaller than a minimum value";
    }
	
}
