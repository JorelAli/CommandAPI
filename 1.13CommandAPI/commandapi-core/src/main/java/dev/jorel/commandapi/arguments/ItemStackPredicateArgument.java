package dev.jorel.commandapi.arguments;

import java.util.function.Predicate;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a predicate for itemstacks
 */
public class ItemStackPredicateArgument extends Argument {
	
	/**
	 * A ItemStack Predicate argument. Represents a predicate for itemstacks 
	 */
	public ItemStackPredicateArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentItemPredicate());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Predicate.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ITEMSTACK_PREDICATE;
	}
}
