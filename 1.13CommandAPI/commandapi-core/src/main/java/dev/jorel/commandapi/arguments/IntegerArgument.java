package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.IntegerArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java ints
 */
public class IntegerArgument extends Argument implements ISafeOverrideableSuggestions<Integer> {

	/**
	 * An integer argument
	 */
	public IntegerArgument() {
		super(IntegerArgumentType.integer());
	}
	
	/**
	 * An integer argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min) {
		super(IntegerArgumentType.integer(min));
	}
	
	/**
	 * An integer argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public IntegerArgument(int min, int max) {
		super(IntegerArgumentType.integer(min, max));
		if(max < min) {
			throw new InvalidRangeException();
		}
	}
	
	@Override
	public Class<?> getPrimitiveType() {
		return int.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	public Argument safeOverrideSuggestions(Integer... suggestions) {
		return super.overrideSuggestions(sMap0(String::valueOf, suggestions));
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Integer[]> suggestions) {
		return super.overrideSuggestions(sMap1(String::valueOf, suggestions));
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Integer[]> suggestions) {
		return super.overrideSuggestions(sMap2(String::valueOf, suggestions));
	}
	
}
