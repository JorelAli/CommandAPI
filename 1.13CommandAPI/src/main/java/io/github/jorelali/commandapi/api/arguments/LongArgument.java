package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.exceptions.InvalidRangeException;

@SuppressWarnings("unchecked")
public class LongArgument implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	/**
	 * An long argument
	 */
	public LongArgument() {
		rawType = LongArgumentType.longArg();
	}
	
	/**
	 * An long argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public LongArgument(int min) {
		rawType = LongArgumentType.longArg(min);
	}
	
	/**
	 * An long argument with a minimum and maximum value
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public LongArgument(int min, int max) {
		if(max < min) {
			throw new InvalidRangeException();
		}
		rawType = LongArgumentType.longArg(min, max);
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) long.class;
	}
	
	@Override
	public boolean isSimple() {
		return true;
	}

	private String[] suggestions;
	
	@Override
	public LongArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = null;
	
	@Override
	public LongArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
	
	@Override
	public CommandAPIArgumentType getArgumentType() {
		return CommandAPIArgumentType.SIMPLE_TYPE;
	}
	
}
