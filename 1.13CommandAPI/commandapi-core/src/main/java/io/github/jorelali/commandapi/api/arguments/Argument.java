package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

public interface Argument {
		
	/**
	 * Returns the brigadier equivalent argument type
	 */
	<T> ArgumentType<T> getRawType();
	
	/**
	 * Returns the class of the primitive type that this enum represents
	 */
	<V> Class<V> getPrimitiveType();
	
	<T extends Argument> T withPermission(CommandPermission permission);
	
	CommandPermission getArgumentPermission();
	
	CommandAPIArgumentType getArgumentType();
		
}
