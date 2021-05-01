  
package dev.jorel.commandapi.exceptions;

/**
 * An exception that occurs when Paper's Adventure API is not present and has been set to be used
 */
@SuppressWarnings("serial")
public class PaperAdventureNotFoundException extends RuntimeException {
	
	/**
	 * Creates a PaperAdventureNotFoundException
	 * @param c the class that requires the Paper Adventure API
	 */
	public PaperAdventureNotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires Paper's Adventure API.");
	}
	
}