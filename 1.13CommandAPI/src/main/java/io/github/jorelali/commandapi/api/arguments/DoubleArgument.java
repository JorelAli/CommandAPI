package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.DoubleArgumentType;

@SuppressWarnings("unchecked")
public class DoubleArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A double argument
	 */
	public DoubleArgument() {
		rawType = DoubleArgumentType.doubleArg();
	}
	
	/**
	 * A double argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public DoubleArgument(int min) {
		rawType = DoubleArgumentType.doubleArg(min);
	}
	
	/**
	 * A double argument with a minimum and maximum value 
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public DoubleArgument(int min, int max) {
		rawType = DoubleArgumentType.doubleArg(min, max);
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) double.class;
	}

	@Override
	public boolean isSimple() {
		return true;
	}

}
