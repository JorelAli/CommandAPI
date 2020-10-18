package dev.jorel.commandapi.arguments;

import java.util.function.Function;

import org.bukkit.World.Environment;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Environment object
 */
public class EnvironmentArgument extends SafeOverrideableArgument<Environment> {
	
	/**
	 * An Environment argument. Represents Bukkit's Environment object
	 * @param nodeName the name of the node for this argument
	 */
	public EnvironmentArgument(String nodeName) {
		super(nodeName, CommandAPIHandler.getInstance().getNMS()._ArgumentDimension(), ((Function<Environment, String>) Environment::name).andThen(String::toLowerCase));
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
