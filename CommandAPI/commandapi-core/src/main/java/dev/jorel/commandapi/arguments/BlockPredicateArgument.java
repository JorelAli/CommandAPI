package dev.jorel.commandapi.arguments;

import java.util.function.Predicate;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a Predicate&lt;Block>
 */
public class BlockPredicateArgument extends Argument {
	
	/**
	 * A Block Predicate argument. Represents a predicate for blocks 
	 */
	public BlockPredicateArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getNMS()._ArgumentBlockPredicate());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return Predicate.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.BLOCK_PREDICATE;
	}
}
