package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.exceptions.BadLiteralException;

@SuppressWarnings("unchecked")
public class LiteralArgument implements Argument {

	String literal;
	
	/**
	 * A literal argument. Only takes one string value which cannot be modified 
	 */
	public LiteralArgument(final String literal) {
		if(literal == null) {
			throw new BadLiteralException(true);
		}
		if(literal.isEmpty()) {
			throw new BadLiteralException(false);
		}
		this.literal = literal;
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		/*
		 * The literal argument builder is NOT technically an argument.
		 * Therefore, it doesn't have an ArgumentType.
		 * 
		 * This is a wrapper for the object "LiteralArgumentBuilder<>"
		 */
		return null;
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	public String getLiteral() {
		return literal;
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public LiteralArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.LITERAL;
	}
}
