package io.github.jorelali.commandapi.api.exceptions;

public class TimeArgumentException extends RuntimeException {
	
	private static final long serialVersionUID = -7119448266996966908L;

	public TimeArgumentException() {
		super("The TimeArgument is only compatible with Minecraft 1.14 or later");
	}
	
}
