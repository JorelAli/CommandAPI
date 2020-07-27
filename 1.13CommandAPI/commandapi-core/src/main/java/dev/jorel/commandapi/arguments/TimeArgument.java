package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.arguments.Time;

/**
 * An argument that represents a duration of time in ticks
 */
public class TimeArgument extends Argument implements ISafeOverrideableSuggestions<Time> {
	
	/**
	 * A Time argument. Represents the number of in game ticks 
	 */
	public TimeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentTime());
	}

	@Override
	public Class<?> getPrimitiveType() {
		return int.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.TIME;
	}

	public Argument safeOverrideSuggestions(Time... suggestions) {
		super.suggestions = (c, m) -> stringMap(suggestions, Time::toString);
		return this;
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Time[]> suggestions) {
		super.suggestions = (c, m) -> stringMap(suggestions.apply(c), Time::toString);
		return this;
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Time[]> suggestions) {
		super.suggestions = (c, m) -> stringMap(suggestions.apply(c, m), Time::toString);
		return this;
	}
}
