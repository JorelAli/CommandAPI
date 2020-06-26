package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when a literal is null or empty
 */
@SuppressWarnings("serial")
public class BadLiteralException extends RuntimeException {
	
	/**
	 * Creates a BadLiteralException
	 * @param isNull if true, state that the string is null. Otherwise, state that the string is empty
	 */
	public BadLiteralException(boolean isNull) {
		super(isNull ? "Cannot create a LiteralArgument with a null string" : "Cannot create a LiteralArgument with an empty string");
	}
	
}
