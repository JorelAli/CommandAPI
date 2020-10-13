package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.FloatArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java floats
 */
public class FloatArgument extends SafeOverrideableArgument<Float> {

	/**
	 * A float argument
	 * @param nodeName the name of the node for this argument
	 */
	public FloatArgument(String nodeName) {
		super(nodeName, FloatArgumentType.floatArg(), String::valueOf);
	}
	
	/**
	 * A float argument with a minimum value
	 * @param nodeName the name of the node for this argument
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public FloatArgument(String nodeName, float min) {
		super(nodeName, FloatArgumentType.floatArg(min), String::valueOf);
	}
	
	/**
	 * A float argument with a minimum and maximum value
	 * @param nodeName the name of the node for this argument
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public FloatArgument(String nodeName, float min, float max) {
		super(nodeName, FloatArgumentType.floatArg(min, max), String::valueOf);
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
		return CommandAPIArgumentType.PRIMITIVE_FLOAT;
	}
}
