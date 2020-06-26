package dev.jorel.commandapi.wrappers;

/**
 * A representation of the math operations for the Minecraft scoreboard
 */
public enum MathOperation {
	
	/**
	 * Addition of two values (+=)
	 */
	ADD, 
	
	/**
	 * Assignment of a value (=)
	 */
	ASSIGN, 
	
	/**
	 * Division of a value by another value (/=)
	 */
	DIVIDE, 
	
	/**
	 * The maximum value of two values (>)
	 */
	MAX, 
	
	/**
	 * The minimum value of two values (<)
	 */
	MIN, 
	
	/**
	 * Modulo of a value by another value (%=)
	 */
	MOD, 
	
	/**
	 * Multiplication of a value by another value (*=) 
	 */
	MULTIPLY, 
	
	/**
	 * Subtraction of a value by another value (-=)
	 */
	SUBTRACT, 
	
	/**
	 * Swap the results of two values (><)
	 */
	SWAP
}