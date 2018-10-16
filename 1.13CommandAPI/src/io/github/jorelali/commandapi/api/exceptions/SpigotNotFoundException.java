package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class SpigotNotFoundException extends RuntimeException {
		
	private String className;
	public SpigotNotFoundException(Class<?> c) {
		className = c.getName();
	}
	
	@Override
    public String getMessage() {
		return "Cannot instantiate " + className + " because it requires Spigot.";
    }
	
}
