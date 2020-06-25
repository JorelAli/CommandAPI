package dev.jorel.commandapi.exceptions;

@SuppressWarnings("serial")
public class BiomeArgumentException extends RuntimeException {

	public BiomeArgumentException() {
		super("The BiomeArgument is only compatible with Minecraft 1.16 or later");
	}
	
}
