package dev.jorel.commandapi.arguments;

import java.util.function.Predicate;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a <code>Predicate&lt;Block&gt;</code>
 */
public class BlockPredicateArgument extends Argument {
	
	/**
	 * Constructs a BlockPredicateArgument with a given node name. Represents a predicate for blocks 
	 * @param nodeName the name of the node for argument
	 */
	public BlockPredicateArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentBlockPredicate());
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
