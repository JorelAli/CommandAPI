package io.github.jorelali.commandapi.api.exceptions;

public class GreedyStringException extends RuntimeException {
	
	@Override
    public String getMessage() {
		return "GreedyStringArgument must be declared at the end of a LinkedHashMap";
    }
	
}
