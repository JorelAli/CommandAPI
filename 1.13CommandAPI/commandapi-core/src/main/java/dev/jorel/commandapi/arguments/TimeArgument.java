package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.Time;

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
		return super.overrideSuggestions(sMap0(Time::toString, suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Time[]> suggestions) {
		return super.overrideSuggestions(sMap1(Time::toString, suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Time[]> suggestions) {
		return super.overrideSuggestions(sMap2(Time::toString, suggestions));
	}
}
