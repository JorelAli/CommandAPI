package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of an objective criteria
 */
public class ObjectiveCriteriaArgument extends Argument {

	/**
	 * An Objective criteria argument. Represents an objective criteria
	 * @param nodeName the name of the node for this argument
	 */
	public ObjectiveCriteriaArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentScoreboardCriteria());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.OBJECTIVE_CRITERIA;
	}
}
