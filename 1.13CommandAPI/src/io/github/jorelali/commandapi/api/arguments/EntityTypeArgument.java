package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.entity.EntityType;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class EntityTypeArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An EntityType argument. Represents the type of an Entity
	 */
	public EntityTypeArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentEntitySummon");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) EntityType.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
