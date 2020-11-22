package dev.jorel.commandapi.arguments;

import java.util.Collection;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents a scoreholder's name, or a collection of scoreholder names
 */
public class ScoreHolderArgument extends Argument {
		
	private final boolean single;
	
	/**
	 * A Score Holder argument. Represents a single score holder or a collection of score holders.
	 * Defaults to using ScoreHolderType.SINGLE
	 * @param nodeName the name of the node for this argument
	 */
	public ScoreHolderArgument(String nodeName) {
		this(nodeName, ScoreHolderType.SINGLE);
	}
	
	/**
	 * A Score Holder argument. Represents a single score holder or a collection of score holders
	 * @param nodeName the name of the node for this argument
	 * @param type whether this argument represents a single score holder or a collection of score holders
	 */
	public ScoreHolderArgument(String nodeName, ScoreHolderType type) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentScoreholder(type == ScoreHolderType.SINGLE));
		single = (type == ScoreHolderType.SINGLE);
	}
	
	/**
	 * Returns whether this argument represents a single score holder or a collection of score holders
	 * @return true if this argument represents a single score holder
	 */
	public boolean isSingle() {
		return this.single;
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return single ? String.class : Collection.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SCORE_HOLDER;
	}

	/**
	 * An enum specifying whether a score holder consists of a single score holder or a collection of score holders
	 */
	public static enum ScoreHolderType {
		/**
		 * A single score holder name
		 */
		SINGLE, 
		
		/**
		 * A collection of score holder names
		 */
		MULTIPLE;
	}
}
