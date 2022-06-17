package dev.jorel.commandapi.arguments;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * An enum that represents single entities or players, as well as collections of entities or players
 */
public enum EntitySelector {
	/**
	 * A single entity. Returns a {@link Entity}
	 */
	ONE_ENTITY,
	
	/**
	 * A single player. Returns a {@link Player}
	 */
	ONE_PLAYER,
	
	/**
	 * Many entities. Returns a {@link Collection}{@code <}{@link Entity}{@code >}
	 */
	MANY_ENTITIES,
	
	/**
	 * Many players. Returns a {@link Collection}{@code <}{@link Player}{@code >}
	 */
	MANY_PLAYERS;
}