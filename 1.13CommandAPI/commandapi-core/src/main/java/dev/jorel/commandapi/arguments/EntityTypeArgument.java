package dev.jorel.commandapi.arguments;

import org.bukkit.entity.EntityType;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit EntityType object
 */
public class EntityTypeArgument extends Argument implements ICustomProvidedArgument {

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

	@Override
	public SuggestionProviders getSuggestionProvider() {
		return SuggestionProviders.ENTITIES;
	}
}
