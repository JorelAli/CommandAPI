package dev.jorel.commandapi.executors;

/**
 * An enum representing the type of an executor
 */
public enum ExecutorType {

	/**
	 * An executor where the CommandSender is a Player
	 */
	PLAYER,

	/**
	 * An executor where the CommandSender is an Entity
	 */
	ENTITY, 
	
	/**
	 * An executor where the CommandSender is a ConsoleCommandSender
	 */
	CONSOLE, 
	
	/**
	 * An executor where the CommandSender is a BlockCommandSender
	 */
	BLOCK, 
	
	/**
	 * An executor where the CommandSender is a CommandSender
	 */
	ALL, 
	
	/**
	 * An executor where the CommandSender is a NativeProxyCommandSender
	 */
	PROXY,
	
	/**
	 * An executor where the CommandSender is always a NativeProxyCommandSender
	 */
	NATIVE;
}
