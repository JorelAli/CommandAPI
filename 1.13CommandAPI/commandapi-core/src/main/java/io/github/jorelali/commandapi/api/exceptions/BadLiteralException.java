package io.github.jorelali.commandapi.api.exceptions;

public class BadLiteralException extends RuntimeException {
	
	private static final long serialVersionUID = -8349408884121946716L;
	
	public BadLiteralException(boolean isNull) {
		super(isNull ? "Cannot create a LiteralArgument with a null string" : "Cannot create a LiteralArgument with an empty string");
	}
	
}
