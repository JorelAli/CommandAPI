package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when a literal is null or empty
 */
@SuppressWarnings("serial")
public class BadLiteralException extends RuntimeException {
	
	public BadLiteralException(boolean isNull) {
		super(isNull ? "Cannot create a LiteralArgument with a null string" : "Cannot create a LiteralArgument with an empty string");
	}
	
}
