package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.inventory.Recipe;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class RecipeArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A recipe argument. Represents a Bukkit registered recipe for an item
	 */
	public RecipeArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentMinecraftKeyRegistered");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) Recipe.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
