package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.arguments.ArgumentType;

import io.github.jorelali.commandapi.api.SemiReflector;

@SuppressWarnings("unchecked")
public class ItemStackArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/*
	 * ItemStack is = CraftItemStack.asBukkitCopy(ArgumentItemStack.a(cmdCtx, "item").a(cmdCtx.getArgument("amount", int.class), false));
	 */
	
	/**
	 * An ItemStack argument
	 */
	public ItemStackArgument() {
		try {
			rawType = (ArgumentType<?>) SemiReflector.getNMSClass("ArgumentItemStack").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
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
