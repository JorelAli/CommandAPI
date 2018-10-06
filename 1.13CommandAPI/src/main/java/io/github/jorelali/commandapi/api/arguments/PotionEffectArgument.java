package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.potion.PotionEffectType;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class PotionEffectArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A PotionEffect argument. Represents status/potion effects 
	 */
	public PotionEffectArgument() {
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentMobEffect").getDeclaredMethod("a").invoke(null);
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
		return (Class<V>) PotionEffectType.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
