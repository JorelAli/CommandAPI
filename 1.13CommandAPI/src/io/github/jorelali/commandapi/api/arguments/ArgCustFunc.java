package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class ArgCustFunc implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A ???
	 */
	public ArgCustFunc() {
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
		return (Class<V>) Collection.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
