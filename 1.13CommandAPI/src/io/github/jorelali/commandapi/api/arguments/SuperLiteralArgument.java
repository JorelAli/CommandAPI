package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.exceptions.BadLiteralException;

@SuppressWarnings("unchecked")
public class SuperLiteralArgument implements Argument {

	String[] literals;
	
	/**
	 * A literal argument. Only takes one string value which cannot be modified 
	 */
	public SuperLiteralArgument(final String[] literals) {
		if(literals == null) {
			throw new BadLiteralException(true);
		}
		if(literals.length == 0) {
			throw new BadLiteralException(false);
		}
		this.literals = literals;
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return null;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) String.class;
	}

	public String[] getLiterals() {
		return literals;
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public SuperLiteralArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SUPER_LITERAL;
	}
}
