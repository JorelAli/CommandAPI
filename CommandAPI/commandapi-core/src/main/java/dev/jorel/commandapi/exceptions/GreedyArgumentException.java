package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when a greedy argument is not declared at the end of a List
 */
@SuppressWarnings("serial")
public class GreedyArgumentException extends RuntimeException {

	/**
	 * Creates a GreedyArgumentException
	 */
    public GreedyArgumentException() {
		super("Only one GreedyStringArgument or ChatArgument can be declared, and it must be the last declared argument for a command.");
    }
	
}
