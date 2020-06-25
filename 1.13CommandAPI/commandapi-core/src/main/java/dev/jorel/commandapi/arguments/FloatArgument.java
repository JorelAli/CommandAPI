package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.FloatArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java floats
 */
public class FloatArgument extends Argument {

	/**
	 * A float argument
	 */
	public FloatArgument() {
		super(FloatArgumentType.floatArg());
	}
	
	/**
	 * A float argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public FloatArgument(float min) {
		super(FloatArgumentType.floatArg(min));
	}
	
	/**
	 * A float argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public FloatArgument(float min, float max) {
		super(FloatArgumentType.floatArg(min, max));
		if(max < min) {
			throw new InvalidRangeException();
		}
	}

	@Override
	public Class<?> getPrimitiveType() {
		return float.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
}
