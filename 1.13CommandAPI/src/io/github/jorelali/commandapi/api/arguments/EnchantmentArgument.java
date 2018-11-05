package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.enchantments.Enchantment;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class EnchantmentArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * An Enchantment argument. Represents an enchantment for items 
	 */
	public EnchantmentArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentEnchantment");
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
