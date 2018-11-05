package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.inventory.ItemStack;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class ItemStackArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;

	/**
	 * An ItemStack argument. Always returns an itemstack of size 1
	 */
	public ItemStackArgument() {
		rawType = SemiReflector.getNMSArgumentInstance("ArgumentItemStack");
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) ItemStack.class;
	}

	@Override
	public boolean isSimple() {
		return false;
	}
}
