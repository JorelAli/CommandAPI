package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when a greedy argument is not declared at the end of a LinkedHashMap
 */
@SuppressWarnings("serial")
public class GreedyArgumentException extends RuntimeException {

    public GreedyArgumentException() {
		super("Only one GreedyStringArgument or ChatArgument can be declared, at the end of a LinkedHashMap");
    }
	
}
