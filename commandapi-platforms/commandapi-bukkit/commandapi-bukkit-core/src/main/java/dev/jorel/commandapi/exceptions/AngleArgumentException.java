package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when trying to use the AngleArgument on Minecraft version &lt; 1.16.2
 */
@SuppressWarnings("serial")
public class AngleArgumentException extends RuntimeException {

	/**
	 * Creates a AngleArgument
	 */
	public AngleArgumentException() {
		super("The AngleArgument is only compatible with Minecraft 1.16.2 or later");
	}
	
}
