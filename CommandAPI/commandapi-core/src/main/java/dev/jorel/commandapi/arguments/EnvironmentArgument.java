package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.World.Environment;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Environment object
 */
public class EnvironmentArgument extends SafeOverrideableArgument<Environment> {
	
	public EnvironmentArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentDimension(), ((Function<Environment, String>) Environment::name).andThen(String::toLowerCase));
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
