package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when the NBTAPI is not found, but an argument that uses it is declared
 */
@SuppressWarnings("serial")
public class NBTAPINotFoundException extends RuntimeException {
	
	/**
	 * Creates an NBTAPINotFoundException
	 * @param c the class that uses the NBTAPI
	 */
	public NBTAPINotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires the NBTAPI. See https://www.spigotmc.org/resources/nbt-api.7939/");
	}
	
}
