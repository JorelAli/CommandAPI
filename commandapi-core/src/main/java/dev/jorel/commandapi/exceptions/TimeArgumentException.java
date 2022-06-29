package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when trying to use the TimeArgument on Minecraft version &lt; 1.14
 */
@SuppressWarnings("serial")
public class TimeArgumentException extends RuntimeException {

	/**
	 * Creates a TimeArgumentException
	 */
	public TimeArgumentException() {
		super("The TimeArgument is only compatible with Minecraft 1.14 or later");
	}
	
}
