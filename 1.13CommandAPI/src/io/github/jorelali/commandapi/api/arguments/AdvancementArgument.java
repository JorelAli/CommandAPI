package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.advancement.Advancement;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class AdvancementArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An EntityType argument. Represents the type of an Entity
	 */
	public AdvancementArgument() {
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
		return (Class<V>) Advancement.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
