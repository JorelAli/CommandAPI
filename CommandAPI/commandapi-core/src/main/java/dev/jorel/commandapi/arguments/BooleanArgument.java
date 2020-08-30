package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

/**
 * An argument that represents primitive Java booleans
 */
public class BooleanArgument extends SafeOverrideableArgument<Boolean> {
	
	/**
	 * An Boolean argument
	 */
	public BooleanArgument() {
		super(BoolArgumentType.bool(), String::valueOf);
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
