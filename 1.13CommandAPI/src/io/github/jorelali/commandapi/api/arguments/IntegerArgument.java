package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.IntegerArgumentType;

@SuppressWarnings("unchecked")
public class IntegerArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An integer argument
	 */
	public IntegerArgument() {
		rawType = IntegerArgumentType.integer();
	}
	
	/**
	 * An integer argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min) {
		rawType = IntegerArgumentType.integer(min);
	}
	
	/**
	 * An integer argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min, int max) {
		rawType = IntegerArgumentType.integer(min, max);
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) int.class;
	}
	
	@Override
	public boolean isSimple() {
		return true;
	}

}
