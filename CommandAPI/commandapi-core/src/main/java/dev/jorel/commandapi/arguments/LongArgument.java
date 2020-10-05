package dev.jorel.commandapi.arguments;

import com.mojang.brigadier.arguments.LongArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java longs
 */
public class LongArgument extends SafeOverrideableArgument<Long> {

	/**
	 * A long argument
	 */
	public LongArgument(String nodeName) {
		super(nodeName, LongArgumentType.longArg(), String::valueOf);
	}
	
	/**
	 * A long argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public LongArgument(String nodeName, int min) {
		super(nodeName, LongArgumentType.longArg(min), String::valueOf);
	}
	
	/**
	 * A long argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public LongArgument(String nodeName, int min, int max) {
		super(nodeName, LongArgumentType.longArg(min, max), String::valueOf);
		if(max < min) {
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return long.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.PRIMITIVE_LONG;
	}
	
}
