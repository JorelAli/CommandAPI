package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

// Note: Slightly different interface to Brigadier's SuggestionProvider
/**
 * A {@link FunctionalInterface} that generates suggestions for a {@link Parser}.
 * <p>
 * See the {@link #addSuggestions(CommandContext, SuggestionsBuilder)} method.
 */
@FunctionalInterface
public interface SuggestionProvider {
	/**
	 * Adds suggestions to the given {@link SuggestionsBuilder}.
	 *
	 * @param context The {@link CommandContext} that holds information about the command that needs suggestions.
	 * @param builder The {@link SuggestionsBuilder} to add suggestions to.
	 */
	void addSuggestions(CommandContext<?> context, SuggestionsBuilder builder);
}
