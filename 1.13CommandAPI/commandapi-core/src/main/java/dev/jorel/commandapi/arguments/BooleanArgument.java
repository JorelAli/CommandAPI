package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.BoolArgumentType;

/**
 * An argument that represents primitive Java booleans
 */
public class BooleanArgument extends Argument implements ISafeOverrideableSuggestions<Boolean> {
	
	/**
	 * An Boolean argument
	 */
	public BooleanArgument() {
		super(BoolArgumentType.bool());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return boolean.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	public Argument safeOverrideSuggestions(Boolean... suggestions) {
		super.suggestions = sMap0(String::valueOf, suggestions);
		return this;
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Boolean[]> suggestions) {
		super.suggestions = sMap1(String::valueOf, suggestions);
		return this;
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Boolean[]> suggestions) {
		super.suggestions = sMap2(String::valueOf, suggestions);
		return this;
	}
	
}
