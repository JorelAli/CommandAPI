package dev.jorel.commandapi.arguments.parser;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

// Slightly different interface to Brigadier's SuggestionProvider
@FunctionalInterface
public
interface SuggestionProvider {
	void addSuggestions(CommandContext<?> context, SuggestionsBuilder builder);
}
