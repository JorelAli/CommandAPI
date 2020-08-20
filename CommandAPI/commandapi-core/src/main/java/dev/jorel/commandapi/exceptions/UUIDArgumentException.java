package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when trying to use the TimeArgument on Minecraft version < 1.14
 */
@SuppressWarnings("serial")
public class UUIDArgumentException extends RuntimeException {

	/**
	 * Creates a TimeArgumentException
	 */
	public UUIDArgumentException() {
		super("The UUIDArgument is only compatible with Minecraft 1.16 or later");
	}
	
}
