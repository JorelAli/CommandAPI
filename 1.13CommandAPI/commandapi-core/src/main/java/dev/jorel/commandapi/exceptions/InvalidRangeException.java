package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when the maximum value of a FloatRange or IntegerRange is less than its minimum value
 */
@SuppressWarnings("serial")
public class InvalidRangeException extends RuntimeException {

	/**
	 * Creates an InvalidRangeException
	 */
	public InvalidRangeException() {
		super("Cannot have a maximum value smaller than a minimum value");
	}
	
}
