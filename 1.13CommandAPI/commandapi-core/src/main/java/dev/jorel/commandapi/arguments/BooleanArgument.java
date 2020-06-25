package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

/**
 * An argument that represents primitive Java booleans
 */
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
