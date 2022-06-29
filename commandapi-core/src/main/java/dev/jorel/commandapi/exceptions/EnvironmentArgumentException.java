package dev.jorel.commandapi.exceptions;

/**
 * An exception caused when using the EnvironmentArgument on Minecraft version 1.13
 */
@SuppressWarnings("serial")
public class EnvironmentArgumentException extends RuntimeException {

	/**
	 * Creates an EnvironmentArgumentException
	 */
	public EnvironmentArgumentException() {
		super("The EnvironmentArgument is only compatible with Minecraft 1.13.1 or later");
	}
	
}
