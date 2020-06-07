package io.github.jorelali.commandapi.api.exceptions;

public class EnvironmentArgumentException extends RuntimeException {
	
	private static final long serialVersionUID = -833423429832562039L;

	public EnvironmentArgumentException() {
		super("The EnvironmentArgument is only compatible with Minecraft 1.13.1 or later");
	}
	
}
