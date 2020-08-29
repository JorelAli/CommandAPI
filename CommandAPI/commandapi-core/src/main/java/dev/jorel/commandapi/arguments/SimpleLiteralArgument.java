package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.BadLiteralException;

public class SimpleLiteralArgument extends Argument {

	String[] literals;
	
	/**
	 * A literal argument. Only takes one string value which cannot be modified 
	 */
	public SimpleLiteralArgument(final String... literals) {
		super(null);
		if(literals == null) {
			throw new BadLiteralException(true);
		}
		if(literals.length == 0) {
			throw new BadLiteralException(false);
		}
		this.literals = literals;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	public String[] getLiterals() {
		return literals;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_LITERAL;
	}
}
