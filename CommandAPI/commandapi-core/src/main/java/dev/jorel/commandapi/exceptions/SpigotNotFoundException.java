package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when Spigot is not present and Spigot is required
 */
@SuppressWarnings("serial")
public class SpigotNotFoundException extends RuntimeException {
	
	/**
	 * Creates a SpigotNotFoundException
	 * @param c the class that requires Spigot
	 */
	public SpigotNotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires Spigot.");
	}
	
}
