package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.inventory.Recipe;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class RecipeArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A recipe argument. Represents a Bukkit registered recipe for an item
	 */
	public RecipeArgument() {
		try {
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentMinecraftKeyRegistered").getDeclaredMethod("a").invoke(null);
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
		return (Class<V>) Recipe.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
