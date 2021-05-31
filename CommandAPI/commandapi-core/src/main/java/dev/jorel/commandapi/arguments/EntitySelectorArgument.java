/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.arguments;

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

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
		return switch(selector) {
			case MANY_ENTITIES, 
			   MANY_PLAYERS -> Collection.class;
			case ONE_ENTITY -> Entity.class;
			case ONE_PLAYER -> Player.class;
			default         -> Collection.class;
		};
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
	
	@Override
	public <CommandListenerWrapper> Object parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return nms.getEntitySelector(cmdCtx, key, selector);
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