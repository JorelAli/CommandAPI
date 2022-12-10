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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.nms.NMS;

/**
 * An argument that represents a selection of entities
 * 
 * @apiNote The return type depends on the provided {@link EntitySelector}
 */
public class EntitySelectorArgument<T> extends Argument<T> {

	private final EntitySelector selector;

	/**
	 * An EntityType argument. Represents an entity specified by a selector.
	 * Defaults to using {@link EntitySelector#ONE_ENTITY}
	 * 
	 * @param nodeName the name of the node for this argument
	 * @deprecated Use {@code EntitySelectorArgument.}{@link OneEntity}
	 */
	@Deprecated(forRemoval = true, since = "8.7.0")
	public EntitySelectorArgument(String nodeName) {
		this(nodeName, EntitySelector.ONE_ENTITY);
	}

	/**
	 * An EntityType argument. Represents an entity specified by a selector
	 * 
	 * @param nodeName the name of the node for this argument
	 * @param selector the entity selector for this argument
	 * @deprecated Use {@code EntitySelectorArgument.}{@link OneEntity},
	 *             {@code EntitySelectorArgument.}{@link OnePlayer},
	 *             {@code EntitySelectorArgument.}{@link ManyEntities} or
	 *             {@code EntitySelectorArgument.}{@link ManyPlayers}
	 */
	@Deprecated(forRemoval = true, since = "8.7.0")
	public EntitySelectorArgument(String nodeName, EntitySelector selector) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntity(switch(selector) {
			case MANY_ENTITIES -> ArgumentSubType.ENTITYSELECTOR_MANY_ENTITIES;
			case MANY_PLAYERS -> ArgumentSubType.ENTITYSELECTOR_MANY_PLAYERS;
			case ONE_ENTITY -> ArgumentSubType.ENTITYSELECTOR_ONE_ENTITY;
			case ONE_PLAYER -> ArgumentSubType.ENTITYSELECTOR_ONE_PLAYER;
		}));
		this.selector = selector;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getPrimitiveType() {
		return (Class<T>) switch (selector) {
			case MANY_ENTITIES, MANY_PLAYERS -> Collection.class;
			case ONE_ENTITY -> Entity.class;
			case ONE_PLAYER -> Player.class;
			default -> Collection.class;
		};
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ENTITY_SELECTOR;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <CommandListenerWrapper> T parseArgument(NMS<CommandListenerWrapper> nms,
			CommandContext<CommandListenerWrapper> cmdCtx, String key, Object[] previousArgs)
			throws CommandSyntaxException {
		return (T) nms.getEntitySelector(cmdCtx, key, switch(selector) {
			case MANY_ENTITIES -> ArgumentSubType.ENTITYSELECTOR_MANY_ENTITIES;
			case MANY_PLAYERS -> ArgumentSubType.ENTITYSELECTOR_MANY_PLAYERS;
			case ONE_ENTITY -> ArgumentSubType.ENTITYSELECTOR_ONE_ENTITY;
			case ONE_PLAYER -> ArgumentSubType.ENTITYSELECTOR_ONE_PLAYER;
		});
	}

	@Override
	@Deprecated
	public List<String> getEntityNames(Object argument) {
		return switch (selector) {
			case MANY_ENTITIES:
				@SuppressWarnings("unchecked")
				List<Entity> entities = (List<Entity>) argument;
				List<String> entityNames = new ArrayList<>();
				for (Entity entity : entities) {
					entityNames.add(entity.getName());
				}
				yield entityNames;
			case MANY_PLAYERS:
				@SuppressWarnings("unchecked")
				List<Player> players = (List<Player>) argument;
				List<String> playerNames = new ArrayList<>();
				for (Player player : players) {
					playerNames.add(player.getName());
				}
				yield playerNames;
			case ONE_ENTITY:
				Entity entity = (Entity) argument;
				yield List.of(entity.getName());
			case ONE_PLAYER:
				Player player = (Player) argument;
				yield List.of(player.getName());
			default:
				throw new IllegalStateException("Invalid selector " + selector.name());
		};
	}

	/**
	 * An argument that represents a single entity
	 * 
	 * @apiNote Returns an {@link Entity} object
	 */
	public static class OneEntity extends Argument<Entity> {

		/**
		 * An argument that represents a single entity
		 * @param nodeName the name of the node for this argument
		 */
		public OneEntity(String nodeName) {
			super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntity(ArgumentSubType.ENTITYSELECTOR_ONE_ENTITY));
		}

		@Override
		public Class<Entity> getPrimitiveType() {
			return Entity.class;
		}

		@Override
		public CommandAPIArgumentType getArgumentType() {
			return CommandAPIArgumentType.ENTITY_SELECTOR;
		}

		@Override
		public <CommandSourceStack> Entity parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key,
			Object[] previousArgs) throws CommandSyntaxException {
			return (Entity) nms.getEntitySelector(cmdCtx, key, ArgumentSubType.ENTITYSELECTOR_ONE_ENTITY);
		}

		@Override
		public List<String> getEntityNames(Object argument) {
			return List.of(((Entity) argument).getName());
		}

	}

	/**
	 * An argument that represents a single player
	 * 
	 * @apiNote Returns a {@link Player} object
	 */
	public static class OnePlayer extends Argument<Player> {

		/**
		 * An argument that represents a single player
		 * @param nodeName the name of the node for this argument
		 */
		public OnePlayer(String nodeName) {
			super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntity(ArgumentSubType.ENTITYSELECTOR_ONE_PLAYER));
		}

		@Override
		public Class<Player> getPrimitiveType() {
			return Player.class;
		}

		@Override
		public CommandAPIArgumentType getArgumentType() {
			return CommandAPIArgumentType.ENTITY_SELECTOR;
		}

		@Override
		public <CommandSourceStack> Player parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key,
			Object[] previousArgs) throws CommandSyntaxException {
			return (Player) nms.getEntitySelector(cmdCtx, key, ArgumentSubType.ENTITYSELECTOR_ONE_PLAYER);
		}

		@Override
		public List<String> getEntityNames(Object argument) {
			return List.of(((Player) argument).getName());
		}

	}

	/**
	 * An argument that represents many entities
	 * 
	 * @apiNote Returns a {@link Collection}{@code <}{@link Entity}{@code >} object
	 */
	@SuppressWarnings("rawtypes")
	public static class ManyEntities extends Argument<Collection> {

		/**
		 * An argument that represents many entities
		 * @param nodeName the name of the node for this argument
		 */
		public ManyEntities(String nodeName) {
			super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntity(ArgumentSubType.ENTITYSELECTOR_MANY_ENTITIES));
		}

		@Override
		public Class<Collection> getPrimitiveType() {
			return Collection.class;
		}

		@Override
		public CommandAPIArgumentType getArgumentType() {
			return CommandAPIArgumentType.ENTITY_SELECTOR;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <CommandSourceStack> Collection<Entity> parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key,
			Object[] previousArgs) throws CommandSyntaxException {
			return (Collection<Entity>) nms.getEntitySelector(cmdCtx, key, ArgumentSubType.ENTITYSELECTOR_MANY_ENTITIES);
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> getEntityNames(Object argument) {
			List<String> entityNames = new ArrayList<>();
			for (Entity entity : (List<Entity>) argument) {
				entityNames.add(entity.getName());
			}
			return entityNames;
		}

	}

	/**
	 * An argument that represents many players
	 * 
	 * @apiNote Returns a {@link Collection}{@code <}{@link Player}{@code >} object
	 */
	@SuppressWarnings("rawtypes")
	public static class ManyPlayers extends Argument<Collection> {

		/**
		 * An argument that represents many players
		 * @param nodeName the name of the node for this argument
		 */
		public ManyPlayers(String nodeName) {
			super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntity(ArgumentSubType.ENTITYSELECTOR_MANY_PLAYERS));
		}

		@Override
		public Class<Collection> getPrimitiveType() {
			return Collection.class;
		}

		@Override
		public CommandAPIArgumentType getArgumentType() {
			return CommandAPIArgumentType.ENTITY_SELECTOR;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <CommandSourceStack> Collection<Player> parseArgument(NMS<CommandSourceStack> nms, CommandContext<CommandSourceStack> cmdCtx, String key,
			Object[] previousArgs) throws CommandSyntaxException {
			return (Collection<Player>) nms.getEntitySelector(cmdCtx, key, ArgumentSubType.ENTITYSELECTOR_MANY_PLAYERS);
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> getEntityNames(Object argument) {
			List<String> playerNames = new ArrayList<>();
			for (Player entity : (List<Player>) argument) {
				playerNames.add(entity.getName());
			}
			return playerNames;
		}

	}
}