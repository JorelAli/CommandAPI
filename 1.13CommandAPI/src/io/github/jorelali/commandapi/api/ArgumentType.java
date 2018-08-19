package io.github.jorelali.commandapi.api;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

@SuppressWarnings("unchecked")
public enum ArgumentType {

	STRING(StringArgumentType.word(), String.class), INTEGER(IntegerArgumentType.integer(), int.class), FLOAT(FloatArgumentType.floatArg(), float.class),
	DOUBLE(DoubleArgumentType.doubleArg(), double.class), BOOLEAN(BoolArgumentType.bool(), boolean.class);
	
	/*
	 * TODO (v1.1): Ranged primitives (e.g. 2 to 10)
	 * 
	 * TODO (v1.2): Enumerations
	 * 
	 * TODO (v1.?): Complex ArgumentTypes:
	 * ArgumentProfile
	 * ArgumentItemStack
	 * 
	 *  TODO(v???): Explore super complex ArgumentTypes:
	 *  - Block
	 *  - Chat
	 */
	
	private com.mojang.brigadier.arguments.ArgumentType<?> type;
	private Class<?> primitive;
	
	<T, V> ArgumentType(com.mojang.brigadier.arguments.ArgumentType<T> type, Class<V> primitive) {
		this.type = type;
		this.primitive = primitive;
	}
	
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) type;
	}
	
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) primitive;
	}
	
	
}
