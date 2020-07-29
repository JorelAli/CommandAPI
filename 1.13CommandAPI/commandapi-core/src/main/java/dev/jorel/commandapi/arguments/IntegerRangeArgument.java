package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.wrappers.IntegerRange;

/**
 * An argument that represents a range of integer values
 */
public class IntegerRangeArgument extends Argument implements ISafeOverrideableSuggestions<IntegerRange> {

	/**
	 * A Time argument. Represents the number of ingame ticks 
	 */
	public IntegerRangeArgument() {
		super(CommandAPIHandler.getNMS()._ArgumentIntRange());
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return IntegerRange.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.INT_RANGE;
	}
	
	public Argument safeOverrideSuggestions(IntegerRange... suggestions) {
		return super.overrideSuggestions(sMap0(IntegerRange::toString, suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, IntegerRange[]> suggestions) {
		return super.overrideSuggestions(sMap1(IntegerRange::toString, suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], IntegerRange[]> suggestions) {
		return super.overrideSuggestions(sMap2(IntegerRange::toString, suggestions));
	}
}
