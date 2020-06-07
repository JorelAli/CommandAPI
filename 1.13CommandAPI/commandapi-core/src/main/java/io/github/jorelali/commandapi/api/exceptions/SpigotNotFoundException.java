package io.github.jorelali.commandapi.api.exceptions;

public class SpigotNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1422616815449211841L;
	
	public SpigotNotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires Spigot.");
	}
	
}
