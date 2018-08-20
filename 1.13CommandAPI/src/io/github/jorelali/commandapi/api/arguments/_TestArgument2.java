package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class _TestArgument2 implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A complex argument of any NMS type
	 */
	public _TestArgument2(String argType) {
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS(argType).getDeclaredMethod("a").invoke(null);
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
		return null;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
