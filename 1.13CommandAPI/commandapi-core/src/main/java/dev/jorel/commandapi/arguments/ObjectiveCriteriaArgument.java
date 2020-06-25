package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the name of an objective criteria
 */
public class ObjectiveCriteriaArgument extends Argument {

	/**
	 * An Objective criteria argument. Represents an objective criteria
	 */
	public ObjectiveCriteriaArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentScoreboardCriteria());
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
