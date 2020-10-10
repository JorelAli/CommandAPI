package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when an unknown argument type is provided
 */
@SuppressWarnings("serial")
public class UnknownArgumentException extends Exception {
	
	public UnknownArgumentException(String argument) {
		super("The argument type '" + argument + "' is not recognized!");
	}
	
}
