package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.LongArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java longs
 */
public class LongArgument extends Argument implements ISafeOverrideableSuggestions<Long> {

	/**
	 * A long argument
	 */
	public LongArgument() {
		super(LongArgumentType.longArg());
	}
	
	/**
	 * A long argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public LongArgument(int min) {
		super(LongArgumentType.longArg(min));
	}
	
	/**
	 * A long argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public LongArgument(int min, int max) {
		super(LongArgumentType.longArg(min, max));
		if(max < min) {
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return long.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	public Argument safeOverrideSuggestions(Long... suggestions) {
		super.suggestions = sMap0(String::valueOf, suggestions);
		return this;
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Long[]> suggestions) {
		super.suggestions = sMap1(String::valueOf, suggestions);
		return this;
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Long[]> suggestions) {
		super.suggestions = sMap2(String::valueOf, suggestions);
		return this;
	}
	
}
