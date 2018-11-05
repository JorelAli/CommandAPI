package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.FunctionWrapper;
import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class FunctionArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A Minecraft 1.12 function
	 */
	public FunctionArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentTag");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) FunctionWrapper[].class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
