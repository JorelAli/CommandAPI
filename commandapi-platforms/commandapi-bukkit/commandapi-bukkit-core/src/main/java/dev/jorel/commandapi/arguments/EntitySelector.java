package dev.jorel.commandapi.arguments;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyEntities;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.ManyPlayers;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.OneEntity;
import dev.jorel.commandapi.arguments.EntitySelectorArgument.OnePlayer;

/**
 * An enum that represents single entities or players, as well as collections of
 * entities or players
 * 
 * @deprecated Use {@code EntitySelectorArgument.}{@link OneEntity},
 *             {@code EntitySelectorArgument.}{@link OnePlayer},
 *             {@code EntitySelectorArgument.}{@link ManyEntities} or
 *             {@code EntitySelectorArgument.}{@link ManyPlayers}
 */
@Deprecated(forRemoval = true, since = "8.7.0")
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