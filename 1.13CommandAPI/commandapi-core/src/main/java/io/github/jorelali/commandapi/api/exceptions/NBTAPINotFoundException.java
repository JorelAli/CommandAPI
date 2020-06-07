package io.github.jorelali.commandapi.api.exceptions;

public class NBTAPINotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = -7437448220422618632L;
	
	public NBTAPINotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires the NBTAPI. See https://www.spigotmc.org/resources/nbt-api.7939/");
	}
	
}
