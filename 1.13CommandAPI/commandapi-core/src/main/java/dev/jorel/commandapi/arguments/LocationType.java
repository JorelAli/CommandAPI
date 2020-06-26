package dev.jorel.commandapi.arguments;

/**
 * An enum representing block position or precise position for location arguments
 */
public enum LocationType {
	/**
	 * Represents the integer coordinates of a block, for example: (10, 70, -19) or (5, 10)
	 */
	BLOCK_POSITION,

	/**
	 * Represents the exact coordinates of a position, for example: (10.24,
	 * 70.00, -18.79) or (24.3, 25.5)
	 */
	PRECISE_POSITION;
}