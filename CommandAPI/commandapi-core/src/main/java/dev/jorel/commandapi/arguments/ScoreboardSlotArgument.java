package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.ScoreboardSlot;

/**
 * An argument that represents the Bukkit ScoreboardSlot object
 */
public class ScoreboardSlotArgument extends SafeOverrideableArgument<ScoreboardSlot> {

	/**
	 * A Display slot argument. Represents scoreboard slots
	 * @param nodeName the name of the node for this argument
	 */
	public ScoreboardSlotArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentScoreboardSlot(), ScoreboardSlot::toString);
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
