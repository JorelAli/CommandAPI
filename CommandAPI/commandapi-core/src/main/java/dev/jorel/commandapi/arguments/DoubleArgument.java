package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.DoubleArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java doubles
 */
public class DoubleArgument extends SafeOverrideableArgument<Double> {

	/**
	 * A double argument
	 * @param nodeName the name of the node for this argument
	 */
	public DoubleArgument(String nodeName) {
		super(nodeName, DoubleArgumentType.doubleArg(), String::valueOf);
	}
	
	/**
	 * A double argument with a minimum value
	 * @param nodeName the name of the node for this argument
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public DoubleArgument(String nodeName, double min) {
		super(nodeName, DoubleArgumentType.doubleArg(min), String::valueOf);
	}
	
	/**
	 * A double argument with a minimum and maximum value 
	 * @param nodeName the name of the node for this argument
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public DoubleArgument(String nodeName, double min, double max) {
		super(nodeName, DoubleArgumentType.doubleArg(min, max), String::valueOf);
		if(max < min) {
			throw new InvalidRangeException();
		}
	}

	@Override
	public Class<?> getPrimitiveType() {
		return double.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PRIMITIVE_DOUBLE;
	}

}
