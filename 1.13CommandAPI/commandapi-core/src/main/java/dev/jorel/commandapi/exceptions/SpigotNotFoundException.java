package dev.jorel.commandapi.exceptions;

@SuppressWarnings("serial")
public class SpigotNotFoundException extends RuntimeException {
	
	public SpigotNotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires Spigot.");
	}
	
}
