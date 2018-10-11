package io.github.jorelali.commandapi.api.exceptions;

public class BadLiteralException extends RuntimeException {
	
	private boolean isNull;
	
	public BadLiteralException(boolean isNull) {
		this.isNull = isNull;
	}
	
	@Override
    public String getMessage() {
		if(isNull)
			return "Cannot create a LiteralArgument with an empty string";
		else
			return "Cannot create a LiteralArgument with a null string";
    }
	
}
