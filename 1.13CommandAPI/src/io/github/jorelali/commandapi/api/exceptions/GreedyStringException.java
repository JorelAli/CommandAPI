package io.github.jorelali.commandapi.api.exceptions;

public class GreedyStringException extends RuntimeException {
	
	private static final long serialVersionUID = 4133470520296930707L;

	@Override
    public String getMessage() {
		return "GreedyStringArgument must be declared at the end of a LinkedHashMap";
    }
	
}
