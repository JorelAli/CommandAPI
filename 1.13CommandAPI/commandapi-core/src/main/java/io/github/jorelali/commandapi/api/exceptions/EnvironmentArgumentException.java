package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class EnvironmentArgumentException extends RuntimeException {

	public EnvironmentArgumentException() {
		super("The EnvironmentArgument is only compatible with Minecraft 1.13.1 or later");
	}
	
}
