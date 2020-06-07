package io.github.jorelali.commandapi.api.arguments;

import org.bukkit.World.Environment;

import io.github.jorelali.commandapi.api.CommandAPIHandler;

public class EnvironmentArgument extends Argument {
	
	public EnvironmentArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentDimension());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return Environment.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.ENVIRONMENT;
	}
}
