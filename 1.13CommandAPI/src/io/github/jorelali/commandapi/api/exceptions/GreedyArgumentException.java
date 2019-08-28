package io.github.jorelali.commandapi.api.exceptions;

public class GreedyArgumentException extends RuntimeException {
	
	private static final long serialVersionUID = 4133470520296930707L;

	@Override
    public String getMessage() {
		return "Only one GreedyStringArgument or ChatArgument can be declared, at the end of a LinkedHashMap";
    }
	
}
