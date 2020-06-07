package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.inventory.ItemStack;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class ItemStackArgument extends Argument {

	/**
	 * An ItemStack argument. Always returns an itemstack of size 1
	 */
	public ItemStackArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentItemStack());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return ItemStack.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ITEMSTACK;
	}
}
