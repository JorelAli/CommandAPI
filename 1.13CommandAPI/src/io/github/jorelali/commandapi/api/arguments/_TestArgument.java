package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class _TestArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A complex argument of any NMS type
	 */
	public _TestArgument(String argType) {
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS(argType).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException e) {
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
