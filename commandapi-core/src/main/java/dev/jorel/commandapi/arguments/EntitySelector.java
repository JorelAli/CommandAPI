package dev.jorel.commandapi.arguments;

/**
 * An enum that represents single entities or players, as well as collections of entities or players
 */
public enum EntitySelector {
	/**
	 * A single entity. Returns a Bukkit Entity
	 */
	ONE_ENTITY,
	
	/**
	 * A single player. Returns a Bukkit Player
	 */
	ONE_PLAYER,
	
	/**
	 * Many entities. Returns a Collection of Entities
	 */
	MANY_ENTITIES,
	
	/**
	 * Many players. Returns a Collection of Players
	 */
	MANY_PLAYERS;
}