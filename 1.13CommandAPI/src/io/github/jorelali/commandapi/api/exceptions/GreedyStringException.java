package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class GreedyStringException extends RuntimeException {
	
	@Override
    public String getMessage() {
		return "GreedyStringArgument must be declared at the end of a LinkedHashMap";
    }
	
}
