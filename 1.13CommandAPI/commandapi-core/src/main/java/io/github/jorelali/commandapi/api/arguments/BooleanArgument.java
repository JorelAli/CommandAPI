package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

public class BooleanArgument extends Argument {
	
	/**
	 * An Boolean argument
	 */
	public BooleanArgument() {
		super(BoolArgumentType.bool());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return boolean.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
}
