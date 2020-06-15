package io.github.jorelali.commandapi.api.exceptions;

@SuppressWarnings("serial")
public class BadLiteralException extends RuntimeException {
	
	public BadLiteralException(boolean isNull) {
		super(isNull ? "Cannot create a LiteralArgument with a null string" : "Cannot create a LiteralArgument with an empty string");
	}
	
}
