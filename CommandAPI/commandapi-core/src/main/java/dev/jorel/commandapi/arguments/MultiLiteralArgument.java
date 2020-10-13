package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.exceptions.BadLiteralException;

/**
 * An argument that represents multiple LiteralArguments
 */
public class MultiLiteralArgument extends Argument {

	private String[] literals;
	
	/**
	 * A multiliteral argument. Takes in string literals which cannot be modified 
	 * @param literals the literals that this argument represents
	 */
	public MultiLiteralArgument(final String... literals) {
		super(null, null);
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
		return CommandAPIArgumentType.MULTI_LITERAL;
	}
}
