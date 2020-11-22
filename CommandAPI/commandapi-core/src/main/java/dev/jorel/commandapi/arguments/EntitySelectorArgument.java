package dev.jorel.commandapi.arguments;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a selection of entities
 */
public class EntitySelectorArgument extends Argument {
	
	private final EntitySelector selector;
	
	/**
	 * An EntityType argument. Represents an entity specified by a selector. Defaults to using EntitySelector.ONE_ENTITY
	 * @param nodeName the name of the node for this argument
	 */
	public EntitySelectorArgument(String nodeName) {
		this(nodeName, EntitySelector.ONE_ENTITY);
	}
	
	/**
	 * An EntityType argument. Represents an entity specified by a selector
	 * @param nodeName the name of the node for this argument
	 * @param selector the entity selector for this argument
	 */
	public EntitySelectorArgument(String nodeName, EntitySelector selector) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntity(selector));
		this.selector = selector;
	}

	@Override
	public Class<?> getPrimitiveType() {	
		switch(selector) {
			case MANY_ENTITIES:
			case MANY_PLAYERS:
			default:
				return Collection.class;
			case ONE_ENTITY:
				return Entity.class;
			case ONE_PLAYER:
				return Player.class;
		}
	}
	
	/**
	 * Returns the entity selector for this argument
	 * @return the entity selector for this argument
	 */
	public EntitySelector getEntitySelector() {
		return selector;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ENTITY_SELECTOR;
	}
	
	/*
	 * a = true false -> only one ENTITY is allowed
	 * b = false false -> multiple entities
	 * c = true true -> only one PLAYER is allowed
	 * d = false true -> multiple players
	 */	
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
}