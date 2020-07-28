package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit ItemStack object
 */
public class ItemStackArgument extends Argument implements ISafeOverrideableSuggestions<ItemStack> {

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

	@Override
	public Argument safeOverrideSuggestions(ItemStack... suggestions) {
		super.suggestions = sMap0(CommandAPIHandler.getNMS()::convert, suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, ItemStack[]> suggestions) {
		super.suggestions = sMap1(CommandAPIHandler.getNMS()::convert, suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], ItemStack[]> suggestions) {
		super.suggestions = sMap2(CommandAPIHandler.getNMS()::convert, suggestions);
		return this;
	}
}
