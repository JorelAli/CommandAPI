package io.github.jorelali.commandapi.api.arguments;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class EntitySelectorArgument extends Argument {
	
	private final EntitySelector selector;
	
	/**
	 * An EntityType argument. Represents an entity specified by a selector
	 */
	public EntitySelectorArgument(EntitySelector selector) {
		super(CommandAPIHandler.getNMS()._ArgumentEntity(selector));
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