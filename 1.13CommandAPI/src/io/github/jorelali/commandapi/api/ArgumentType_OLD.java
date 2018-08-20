package io.github.jorelali.commandapi.api;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

@SuppressWarnings("unchecked")
public enum ArgumentType_OLD {

	/**
	 * A String type argument (valid for one word)
	 */
	STRING(StringArgumentType.word(), String.class), 
	
	/**
	 * A whole number 
	 */
	INTEGER(IntegerArgumentType.integer(), int.class),
	
	/**
	 * A floating point number
	 */
	FLOAT(FloatArgumentType.floatArg(), float.class),
	
	/**
	 * A decimal number
	 */
	DOUBLE(DoubleArgumentType.doubleArg(), double.class), 
	
	/**
	 * A boolean value of true or false
	 */
	BOOLEAN(BoolArgumentType.bool(), boolean.class);
	
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
	
	private <T, V> ArgumentType_OLD(com.mojang.brigadier.arguments.ArgumentType<T> type, Class<V> primitive) {
		this.type = type;
		this.primitive = primitive;
	}
	
	/**
	 * Returns the brigadier equivalent argument type
	 */
	protected <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) type;
	}
	
	/**
	 * Returns the class of the primitive type that this enum represents
	 */
	protected <V> Class<V> getPrimitiveType() {
		return (Class<V>) primitive;
	}
	
	
}
