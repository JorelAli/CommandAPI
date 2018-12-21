package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.StringArgumentType;

@SuppressWarnings("unchecked")
public class TextArgument implements Argument, OverrideableSuggestions {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	
	/**
	 * A string argument for one word, or multiple words encased in quotes
	 */
	public TextArgument() {
		rawType = StringArgumentType.string();
	}
	
	@Override
	public <T> com.mojang.brigadier.arguments.ArgumentType<T> getRawType() {
		return (com.mojang.brigadier.arguments.ArgumentType<T>) rawType;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) String.class;
	}

	@Override
	public boolean isSimple() {
		return true;
	}
	
	private String[] suggestions;
	
	@Override
	public TextArgument overrideSuggestions(String... suggestions) {
		this.suggestions = suggestions;
		return this;
	}
	
	@Override
	public String[] getOverriddenSuggestions() {
		return suggestions;
	}
}
