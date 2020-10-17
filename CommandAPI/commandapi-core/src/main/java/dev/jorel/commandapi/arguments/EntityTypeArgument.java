package dev.jorel.commandapi.arguments;

import org.bukkit.entity.EntityType;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit EntityType object
 */
public class EntityTypeArgument extends SafeOverrideableArgument<EntityType> implements ICustomProvidedArgument {

	/**
	 * An EntityType argument. Represents the type of an Entity
	 * @param nodeName the name of the node for this argument
	 */
	public EntityTypeArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentEntitySummon(), fromKey(EntityType::getKey));
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
