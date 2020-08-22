package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.StringArgumentType;

/**
 * An argument that represents arbitrary strings
 */
public class GreedyStringArgument extends Argument implements IGreedyArgument, ISafeOverrideableSuggestions<String> {
	
	/**
	 * A string argument for a string of any length
	 */
	public GreedyStringArgument() {
		super(StringArgumentType.greedyString());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return String.class;
	}

	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	public Argument safeOverrideSuggestions(String... suggestions) {
		return super.overrideSuggestions(suggestions);
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, String[]> suggestions) {
		return super.overrideSuggestions(suggestions);
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions) {
		return super.overrideSuggestions(suggestions);
	}
}
