package dev.jorel.commandapi.arguments;

import org.bukkit.World.Environment;

import dev.jorel.commandapi.CommandAPIHandler;

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
