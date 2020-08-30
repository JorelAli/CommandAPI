package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java ints
 */
public class IntegerArgument extends SafeOverrideableArgument<Integer> {

	/**
	 * An integer argument
	 */
	public IntegerArgument() {
		super(IntegerArgumentType.integer(), String::valueOf);
	}
	
	/**
	 * An integer argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min) {
		super(IntegerArgumentType.integer(min), String::valueOf);
	}
	
	/**
	 * An integer argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min, int max) {
		super(IntegerArgumentType.integer(min, max), String::valueOf);
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
