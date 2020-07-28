package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.World.Environment;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;

/**
 * An argument that represents the Bukkit Environment object
 */
public class EnvironmentArgument extends Argument implements ISafeOverrideableSuggestions<Environment> {
	
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

	@Override
	public Argument safeOverrideSuggestions(Environment... suggestions) {
		super.suggestions = sMap0(((Function<Environment, String>) Environment::name).andThen(String::toLowerCase), suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(Function<CommandSender, Environment[]> suggestions) {
		super.suggestions = sMap1(((Function<Environment, String>) Environment::name).andThen(String::toLowerCase), suggestions);
		return this;
	}

	@Override
	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Environment[]> suggestions) {
		super.suggestions = sMap2(((Function<Environment, String>) Environment::name).andThen(String::toLowerCase), suggestions);
		return this;
	}
}
