package dev.jorel.commandapi.exceptions;

public class OptionalArgumentException extends RuntimeException {

	public OptionalArgumentException() {
		super("Optional argument can not be followed by a required argument!");
	}

}
