package io.github.jorelali.commandapi.api.arguments;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

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
