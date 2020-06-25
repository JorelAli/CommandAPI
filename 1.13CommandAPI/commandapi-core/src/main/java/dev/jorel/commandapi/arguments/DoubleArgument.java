package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.DoubleArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java doubles
 */
public class DoubleArgument extends Argument {

	/**
	 * A double argument
	 */
	public DoubleArgument() {
		super(DoubleArgumentType.doubleArg());
	}
	
	/**
	 * A double argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public DoubleArgument(double min) {
		super(DoubleArgumentType.doubleArg(min));
	}
	
	/**
	 * A double argument with a minimum and maximum value 
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public DoubleArgument(double min, double max) {
		super(DoubleArgumentType.doubleArg(min, max));
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
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}

}
