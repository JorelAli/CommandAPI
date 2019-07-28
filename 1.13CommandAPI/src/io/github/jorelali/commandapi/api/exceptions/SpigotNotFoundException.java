package io.github.jorelali.commandapi.api.exceptions;

public class SpigotNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1422616815449211841L;
	
	private String className;
	public SpigotNotFoundException(Class<?> c) {
		className = c.getName();
	}
	
	@Override
    public String getMessage() {
		return "Cannot instantiate " + className + " because it requires Spigot.";
    }
	
}
