package io.github.jorelali.commandapi.api.arguments;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class EntitySelectorArgument extends Argument {
	
	ArgumentType<?> rawType;
	private EntitySelector selector;
	
	/**
	 * An EntityType argument. Represents an entity specified by a selector
	 */
	public EntitySelectorArgument(EntitySelector selector) {
		this.selector = selector;
		
		rawType = CommandAPIHandler.getNMS()._ArgumentEntity(selector);
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
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
	
	private String[] suggestions;
	
	@Override
	public EntitySelectorArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public EntitySelectorArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
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