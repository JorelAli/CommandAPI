package dev.jorel.commandapi.arguments;

import org.bukkit.inventory.ItemStack;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit ItemStack object
 */
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
