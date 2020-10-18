package dev.jorel.commandapi.arguments;

import org.bukkit.inventory.ItemStack;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit ItemStack object
 */
public class ItemStackArgument extends SafeOverrideableArgument<ItemStack> {

	/**
	 * An ItemStack argument. Always returns an itemstack of size 1
	 * @param nodeName the name of the node for this argument
	 */
	public ItemStackArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentItemStack(), CommandAPIHandler.getInstance().getNMS()::convert);
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
