package io.github.jorelali.commandapi.api.arguments;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.enchantments.Enchantment;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class EnchantmentArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An Enchantment argument. Represents an enchantment for items 
	 */
	public EnchantmentArgument() {
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentEnchantment").getDeclaredMethod("a").invoke(null);
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
		return (Class<V>) Enchantment.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
