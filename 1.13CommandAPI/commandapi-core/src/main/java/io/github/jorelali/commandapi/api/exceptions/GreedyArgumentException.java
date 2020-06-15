package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class GreedyArgumentException extends RuntimeException {

    public GreedyArgumentException() {
		super("Only one GreedyStringArgument or ChatArgument can be declared, at the end of a LinkedHashMap");
    }
	
}
