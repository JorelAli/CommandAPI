package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.CommandAPIHandler;
import io.github.jorelali.commandapi.api.wrappers.ScoreboardSlot;

public class ScoreboardSlotArgument extends Argument {

	/**
	 * A Display slot argument. Represents scoreboard slots
	 */
	public ScoreboardSlotArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardSlot());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return ScoreboardSlot.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SCOREBOARD_SLOT;
	}
}
