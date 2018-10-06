package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class EntitySelectorArgument implements Argument {

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
		ONE_ENTITY("a"),
		
		/**
		 * A single player. Returns a Bukkit Player
		 */
		ONE_PLAYER("c"),
		
		/**
		 * Many entities. Returns a Collection of Entities
		 */
		MANY_ENTITIES("b"),
		
		/**
		 * Many players. Returns a Collection of Players
		 */
		MANY_PLAYERS("d");
		
		private String function;
		
		EntitySelector(String nmsFunction) {
			this.function = nmsFunction;
		}
		
		private String getNMSFunction() {
			return function;
		}
	}
	
	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	private EntitySelector selector;
	
	/**
	 * An EntityType argument. Represents an entity specified by a selector
	 */
	public EntitySelectorArgument(EntitySelector selector) {
		this.selector = selector;
		
		try {
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentEntity").getDeclaredMethod(selector.getNMSFunction()).invoke(null);
		} catch (IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {	
		switch(selector) {
			case MANY_ENTITIES:
			case MANY_PLAYERS:
			default:
				return (Class<V>) Collection.class;
			case ONE_ENTITY:
				return (Class<V>) Entity.class;
			case ONE_PLAYER:
				return (Class<V>) Player.class;
		}
	}

	@Override
	public boolean isSimple() {
		return false;
	}
	
	public EntitySelector getEntitySelector() {
		return selector;
	}
}