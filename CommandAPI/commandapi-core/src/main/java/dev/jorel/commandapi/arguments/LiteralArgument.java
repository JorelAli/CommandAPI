package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.BadLiteralException;

/**
 * A pseudo-argument representing a single literal string
 */
public class LiteralArgument extends Argument {

	private String literal;
	
	/**
	 * A literal argument. Only takes one string value which cannot be modified 
	 * @param literal the string literal that this argument will represent
	 */
	public LiteralArgument(final String literal) {
		/*
		 * The literal argument builder is NOT technically an argument.
		 * Therefore, it doesn't have an ArgumentType.
		 * 
		 * This is a wrapper for the object "LiteralArgumentBuilder<>"
		 */
		super(literal, null);
		
		if(literal == null) {
			throw new BadLiteralException(true);
		}
		if(literal.isEmpty()) {
			throw new BadLiteralException(false);
		}
		this.literal = literal;
		this.setListed(false);
	}
	

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	/**
	 * Returns the literal string represented by this argument
	 * @return the literal string represented by this argument
	 */
	public String getLiteral() {
		return literal;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LITERAL;
	}
}
