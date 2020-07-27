package dev.jorel.commandapi.arguments;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.FloatArgumentType;

import dev.jorel.commandapi.exceptions.InvalidRangeException;

/**
 * An argument that represents primitive Java floats
 */
public class FloatArgument extends Argument implements ISafeOverrideableSuggestions<Float> {

	/**
	 * A float argument
	 */
	public FloatArgument() {
		super(FloatArgumentType.floatArg());
	}
	
	/**
	 * A float argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public FloatArgument(float min) {
		super(FloatArgumentType.floatArg(min));
	}
	
	/**
	 * A float argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public FloatArgument(float min, float max) {
		super(FloatArgumentType.floatArg(min, max));
		if(max < min) {
			throw new InvalidRangeException();
		}
	}

	@Override
	public Class<?> getPrimitiveType() {
		return float.class;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
	public Argument safeOverrideSuggestions(Float... suggestions) {
		super.suggestions = sMap0(String::valueOf, suggestions);
		return this;
	}

	public Argument safeOverrideSuggestions(Function<CommandSender, Float[]> suggestions) {
		super.suggestions = sMap1(String::valueOf, suggestions);
		return this;
	}

	public Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], Float[]> suggestions) {
		super.suggestions = sMap2(String::valueOf, suggestions);
		return this;
	}
}
