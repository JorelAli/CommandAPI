package dev.jorel.commandapi.arguments;

import org.bukkit.scoreboard.Objective;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of a scoreboard objective
 */
public class ObjectiveArgument extends SafeOverrideableArgument<Objective> {

	/**
	 * An Objective argument. Represents a scoreboard objective
	 * @param nodeName the name of the node for this argument
	 */
	public ObjectiveArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentScoreboardObjective(), Objective::getName);
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.OBJECTIVE;
	}
}
