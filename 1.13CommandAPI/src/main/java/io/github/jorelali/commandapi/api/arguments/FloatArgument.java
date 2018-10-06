package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.FloatArgumentType;

@SuppressWarnings("unchecked")
public class FloatArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An float argument
	 */
	public FloatArgument() {
		rawType = FloatArgumentType.floatArg();
	}
	
	/**
	 * A float argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public FloatArgument(int min) {
		rawType = FloatArgumentType.floatArg(min);
	}
	
	/**
	 * A float argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public FloatArgument(int min, int max) {
		rawType = FloatArgumentType.floatArg(min, max);
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) float.class;
	}

	@Override
	public boolean isSimple() {
		return true;
	}
}
