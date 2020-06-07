package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.exceptions.BadLiteralException;

public class LiteralArgument extends Argument {

	private String literal;
	
	/**
	 * A literal argument. Only takes one string value which cannot be modified 
	 */
	public LiteralArgument(final String literal) {
		/*
		 * The literal argument builder is NOT technically an argument.
		 * Therefore, it doesn't have an ArgumentType.
		 * 
		 * This is a wrapper for the object "LiteralArgumentBuilder<>"
		 */
		super(null);
		
		if(literal == null) {
			throw new BadLiteralException(true);
		}
		if(literal.isEmpty()) {
			throw new BadLiteralException(false);
		}
		this.literal = literal;
	}
	

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	public String getLiteral() {
		return literal;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LITERAL;
	}
}
