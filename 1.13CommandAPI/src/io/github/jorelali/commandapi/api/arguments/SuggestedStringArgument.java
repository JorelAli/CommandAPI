package io.github.jorelali.commandapi.api.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.jorelali.commandapi.api.CommandPermission;

import java.util.List;

/**
 * Deprecated as of 1.9, due to implementation of OverrideableSuggestions. Use <br>
 * <code>new StringArgument().overrideSuggestions(String...)</code><br>
 * instead
 */
@SuppressWarnings("unchecked")
@Deprecated
public class SuggestedStringArgument implements Argument {

	ArgumentType<?> rawType;
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
	
	public String[] getSuggestions() {
		return suggestions;
	}
	
	private CommandPermission permission = CommandPermission.NONE;
	
	@Override
	public SuggestedStringArgument withPermission(CommandPermission permission) {
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
