package io.github.jorelali.commandapi.api.exceptions;

public class NBTAPINotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -7437448220422618632L;
	
	private String className;
	public NBTAPINotFoundException(Class<?> c) {
		className = c.getName();
	}
	
	@Override
    public String getMessage() {
		return "Cannot instantiate " + className + " because it requires the NBTAPI. See https://www.spigotmc.org/resources/nbt-api.7939/";
    }
	
}
