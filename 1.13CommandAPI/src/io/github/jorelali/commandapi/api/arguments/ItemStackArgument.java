package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.inventory.ItemStack;

import com.mojang.brigadier.arguments.ArgumentType;

@SuppressWarnings("unchecked")
public class ItemStackArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/*
	 * ItemStack is = CraftItemStack.asBukkitCopy(ArgumentItemStack.a(cmdCtx, "item").a(cmdCtx.getArgument("amount", int.class), false));
	 */
	
	/**
	 * An ItemStack argument. Always returns an itemstack of size 1
	 */
	public ItemStackArgument() {
		try {
			
			rawType = (ArgumentType<?>) ArgumentUtil.getNMS("ArgumentItemStack").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException | SecurityException e) {
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
