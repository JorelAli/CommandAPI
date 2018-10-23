package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.FunctionWrapper;

@SuppressWarnings("unchecked")
public class FunctionArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A Minecraft 1.12 function
	 */
	public FunctionArgument() {
		try {
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentTag").getDeclaredMethod("a").invoke(null);
		} catch (IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
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
