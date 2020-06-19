package dev.jorel.commandapi.exceptions;

@SuppressWarnings("serial")
public class UnsupportedVersionException extends RuntimeException {

	public UnsupportedVersionException(String version) {
		super("This version of Minecraft is unsupported: " + version);
	}
	
}
