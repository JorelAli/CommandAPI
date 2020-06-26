package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when this version of Minecraft is unsupported
 */
@SuppressWarnings("serial")
public class UnsupportedVersionException extends RuntimeException {

	/**
	 * Creates an UnsupportedVersionException
	 * @param version the version of Minecraft that is unsupported
	 */
	public UnsupportedVersionException(String version) {
		super("This version of Minecraft is unsupported: " + version);
	}
	
}
