package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.BoolArgumentType;

@SuppressWarnings("unchecked")
public class BooleanArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An Boolean argument
	 */
	public BooleanArgument() {
		rawType = BoolArgumentType.bool();
	}
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) boolean.class;
	}
	
	@Override
	public boolean isSimple() {
		return true;
	}

}
