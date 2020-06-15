package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class TimeArgumentException extends RuntimeException {

	public TimeArgumentException() {
		super("The TimeArgument is only compatible with Minecraft 1.14 or later");
	}
	
}
