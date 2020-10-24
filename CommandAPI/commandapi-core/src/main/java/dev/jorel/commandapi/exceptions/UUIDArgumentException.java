package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when trying to use the UUIDArgumentException on Minecraft version &lt; 1.16
 */
@SuppressWarnings("serial")
public class UUIDArgumentException extends RuntimeException {

	/**
	 * Creates a UUIDArgumentException
	 */
	public UUIDArgumentException() {
		super("The UUIDArgument is only compatible with Minecraft 1.16 or later");
	}
	
}
