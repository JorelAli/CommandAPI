package dev.jorel.commandapi.arguments;

import java.util.function.Predicate;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a Predicate&lt;ItemStack>
 */
public class ItemStackPredicateArgument extends Argument {
	
	/**
	 * A ItemStack Predicate argument. Represents a predicate for itemstacks
	 * @param nodeName the name of the node for this argument 
	 */
	public ItemStackPredicateArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentItemPredicate());
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
