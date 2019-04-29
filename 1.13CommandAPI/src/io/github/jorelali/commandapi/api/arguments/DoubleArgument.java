package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;
import io.github.jorelali.commandapi.api.exceptions.InvalidRangeException;

@SuppressWarnings("unchecked")
public class DoubleArgument implements Argument, OverrideableSuggestions {

	ArgumentType<?> rawType;
	
	/**
	 * A double argument
	 */
	public DoubleArgument() {
		rawType = DoubleArgumentType.doubleArg();
	}
	
	/**
	 * A double argument with a minimum value
	 * @param min The minimum value this argument can take (inclusive)
	 */
	public DoubleArgument(int min) {
		rawType = DoubleArgumentType.doubleArg(min);
	}
	
	/**
	 * A double argument with a minimum and maximum value 
	 * @param min The minimum value this argument can take (inclusive)
	 * @param max The maximum value this argument can take (inclusive)
	 */
	public DoubleArgument(int min, int max) {
		if(max < min) {
			throw new InvalidRangeException();
		}
		rawType = DoubleArgumentType.doubleArg(min, max);
	}
	
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) double.class;
	}

	@Override
	public boolean isSimple() {
		return true;
	}
	
	private String[] suggestions;
	
	@Override
	public DoubleArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = CommandPermission.NONE;
	
	@Override
	public DoubleArgument withPermission(CommandPermission permission) {
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
