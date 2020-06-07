package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.LongArgumentType;

import io.github.jorelali.commandapi.api.exceptions.InvalidRangeException;

@SuppressWarnings("unchecked")
public class LongArgument extends Argument {

	/**
	 * A long argument
	 */
	public LongArgument() {
		rawType = LongArgumentType.longArg();
	}
	
	/**
	 * A long argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public LongArgument(int min) {
		rawType = LongArgumentType.longArg(min);
	}
	
	/**
	 * A long argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public LongArgument(int min, int max) {
		if(max < min) {
			throw new InvalidRangeException();
		}
		rawType = LongArgumentType.longArg(min, max);
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return long.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
}
