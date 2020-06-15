package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class NBTAPINotFoundException extends RuntimeException {
	
	public NBTAPINotFoundException(Class<?> c) {
		super("Cannot instantiate " + c.getName() + " because it requires the NBTAPI. See https://www.spigotmc.org/resources/nbt-api.7939/");
	}
	
}
