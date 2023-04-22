package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.ChainableBuilder;

import java.util.function.Function;

public interface SafeOverrideable<T, S, Impl extends AbstractArgument<T, Impl, Argument, CommandSender>,
	Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender> extends ChainableBuilder<Impl> {
	// Links to methods in AbstractArgument (make sure they have the same signature)
	Impl replaceSuggestions(ArgumentSuggestions<CommandSender> suggestions);

	Impl includeSuggestions(ArgumentSuggestions<CommandSender> suggestions);


	// SafeOverrideableArgument info
	Function<S, String> getMapper();


	/**
	 * Replaces the suggestions with a safe {@link SafeSuggestions} object. Use the
	 * static methods in {@link SafeSuggestions} to create safe suggestions.
	 *
	 * @param suggestions The safe suggestions to use
	 * @return the current argument
	 */
	default Impl replaceSafeSuggestions(SafeSuggestions<S, CommandSender> suggestions) {
		return replaceSuggestions(suggestions.toSuggestions(getMapper()));
	}

	/**
	 * Includes the suggestions provided with the existing suggestions for this
	 * argument. Use the static methods in {@link SafeSuggestions} to create safe
	 * suggestions.
	 *
	 * @param suggestions The safe suggestions to use
	 * @return the current argument
	 */
	default Impl includeSafeSuggestions(SafeSuggestions<S, CommandSender> suggestions) {
		return includeSuggestions(suggestions.toSuggestions(getMapper()));
	}
}
