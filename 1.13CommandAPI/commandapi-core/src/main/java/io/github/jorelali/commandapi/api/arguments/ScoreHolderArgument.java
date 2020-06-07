package io.github.jorelali.commandapi.api.arguments;

import java.util.Collection;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class ScoreHolderArgument extends Argument {
		
	private final boolean single;
	
	/**
	 * A Score Holder argument. Represents a collection of score holders
	 */
	public ScoreHolderArgument(ScoreHolderType type) {
		super(CommandAPIHandler.getNMS()._ArgumentScoreholder(type == ScoreHolderType.SINGLE));
		single = (type == ScoreHolderType.SINGLE);
	}
	
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

	public static enum ScoreHolderType {
		SINGLE, MULTIPLE;
	}
}
