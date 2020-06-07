package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import io.github.jorelali.commandapi.api.exceptions.InvalidRangeException;

public class IntegerArgument extends Argument {

	/**
	 * An integer argument
	 */
	public IntegerArgument() {
		super(IntegerArgumentType.integer());
	}
	
	/**
	 * An integer argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min) {
		super(IntegerArgumentType.integer(min));
	}
	
	/**
	 * An integer argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min, int max) {
		super(IntegerArgumentType.integer(min, max));
		if(max < min) {
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return int.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
}
