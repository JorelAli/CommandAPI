package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.entity.EntityType;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class EntityTypeArgument extends Argument {

	/**
	 * An EntityType argument. Represents the type of an Entity
	 */
	public EntityTypeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentEntitySummon());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return EntityType.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ENTITY_TYPE;
	}
}
