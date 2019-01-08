package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import io.github.jorelali.commandapi.api.CommandPermission;

@SuppressWarnings("unchecked")
public class DynamicSuggestedStringArgument implements Argument {

	@FunctionalInterface
	public interface DynamicSuggestions {
		String[] getSuggestions();
	}
	
	ArgumentType<?> rawType;
	private DynamicSuggestions suggestions;
	
	/**
	 * A string argument which has suggestions which are determined at runtime
	 */
	public DynamicSuggestedStringArgument(DynamicSuggestions suggestions) {
		rawType = StringArgumentType.word();
		this.suggestions = suggestions;
	}
		
	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) String.class;
	}

	@Override
	public boolean isSimple() {
		return true;
	}
	
	public DynamicSuggestions getDynamicSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = CommandPermission.NONE;
	
	@Override
	public DynamicSuggestedStringArgument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}

	@Override
	public CommandPermission getArgumentPermission() {
		return permission;
	}
}
