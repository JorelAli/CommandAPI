package io.github.jorelali.commandapi.api.arguments;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

@SuppressWarnings("unchecked")
public class LegacyArgument implements ArgumentType<String>, Argument {

	String[] suggestions;
	
	/**
	 * Represents a classic pre-1.13 Bukkit argument. This represents ANY String with no restrictions
	 * (so, any characters) and is separated by a space.
	 */
	public LegacyArgument() {
		suggestions = new String[0];
	}

	//A suggested version of a Legacy Argument
	public LegacyArgument(String... suggestions) {
		this.suggestions = suggestions;
	}
	
	@Override
	public <S> String parse(StringReader reader) {
		final int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
    	//populate this
        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return Collections.emptyList();
    }

	@Override
	public <T> ArgumentType<T> getRawType() {
		return (ArgumentType<T>) this;
	}

	@Override
	public <V> Class<V> getPrimitiveType() {
		return (Class<V>) String.class;
	}

	@Override
	public boolean isSimple() {
		return true;
	}
	
}
