package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit EntityType object
 */
public class EntityTypeArgument extends Argument implements ICustomProvidedArgument, ISafeOverrideableSuggestions<EntityType> {

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
	
	public Argument safeOverrideSuggestions(EntityType... suggestions) {
		return super.overrideSuggestions(sMap0(fromKey(EntityType::getKey), suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, EntityType[]> suggestions) {
		return super.overrideSuggestions(sMap1(fromKey(EntityType::getKey), suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], EntityType[]> suggestions) {
		return super.overrideSuggestions(sMap2(fromKey(EntityType::getKey), suggestions));
	}
}
