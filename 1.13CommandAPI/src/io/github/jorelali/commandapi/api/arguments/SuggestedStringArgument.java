package io.github.jorelali.commandapi.api.arguments;

import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;

@SuppressWarnings("unchecked")
public class SuggestedStringArgument implements Argument {

	com.mojang.brigadier.arguments.ArgumentType<?> rawType;
	String[] suggestions;
	
	/**
	 * A string argument for one word, which has suggested values
	 */
	public SuggestedStringArgument(String... suggestions) {
		rawType = StringArgumentType.word();
		this.suggestions = suggestions;
	}
	
	public SuggestedStringArgument(List<String> suggestions) {
		this(suggestions.toArray(new String[suggestions.size()]));
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
	
	public String[] getSuggestions() {
		return suggestions;
	}
}
