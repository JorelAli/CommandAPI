package io.github.jorelali.commandapi.api.exceptions;

public class InvalidRangeException extends RuntimeException {
	
	@Override
    public String getMessage() {
		return "Cannot have a maximum value smaller than a minimum value";
    }
	
}
