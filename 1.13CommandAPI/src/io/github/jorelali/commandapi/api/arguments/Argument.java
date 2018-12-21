package io.github.jorelali.commandapi.api.arguments;

public interface Argument {
		
	/**
	 * Returns the brigadier equivalent argument type
	 */
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType();
	
	/**
	 * Returns the class of the primitive type that this enum represents
	 */
	public <V> Class<V> getPrimitiveType();
	
	/**
	 * Returns whether this argument is a "simple" argument. Simple arguments are primitive data types 
	 * @return Whether this argument is a simple argument
	 */
	public boolean isSimple();
		
}
