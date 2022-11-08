package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.AbstractTooltip;
import dev.jorel.commandapi.IChainableBuilder;
import dev.jorel.commandapi.SuggestionInfo;

import java.util.function.Function;

public interface ISafeOverrideableArgument<T, S, Impl extends AbstractArgument<T, Impl, Argument, CommandSender>,
	Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender> extends IChainableBuilder<Impl> {
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

	// TODO: Now's might a good time to remove these methods, since a project using them would need to recompile
	//  anyways since they're held in a different class now. Dunno though, do deprecated methods ever get removed anyways?
	// Deprecated legacy methods
	/**
	 * Replaces the suggestions of this argument with an array of suggestions.
	 *
	 * @param suggestions a function that takes in {@link SuggestionInfo} and
	 *                    returns a {@link S} array of suggestions, where S is your
	 *                    custom type
	 * @return the current argument
	 * @deprecated use {@link #replaceSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	default Impl replaceWithSafeSuggestions(Function<SuggestionInfo<CommandSender>, S[]> suggestions) {
		return replaceSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}

	/**
	 * Replaces the suggestions of this argument with an array of suggestions.
	 *
	 * @param suggestions a function that takes in {@link SuggestionInfo} and
	 *                    returns an {@link AbstractTooltip} array of suggestions,
	 *                    parameterized over {@link S} where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #replaceSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	default Impl replaceWithSafeSuggestionsT(Function<SuggestionInfo<CommandSender>, AbstractTooltip<S>[]> suggestions) {
		return replaceSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 *
	 * @param suggestions a function that takes in {@link SuggestionInfo} which
	 *                    includes information about the current state at the time
	 *                    the suggestions are run and returns a {@link S} array of
	 *                    suggestions to add, where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #includeSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	default Impl includeWithSafeSuggestions(Function<SuggestionInfo<CommandSender>, S[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.suggest(suggestions));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 *
	 * @param suggestions a function that takes in {@link SuggestionInfo} which
	 *                    includes information about the current state at the time
	 *                    the suggestions are run and returns an {@link AbstractTooltip}
	 *                    array of suggestions to add, parameterized over {@link S}
	 *                    where S is your custom type
	 * @return the current argument
	 * @deprecated use {@link #includeSafeSuggestions(SafeSuggestions)}
	 */
	@Deprecated(forRemoval = true)
	default Impl includeWithSafeSuggestionsT(Function<SuggestionInfo<CommandSender>, AbstractTooltip<S>[]> suggestions) {
		return includeSafeSuggestions(SafeSuggestions.tooltips(suggestions));
	}
}
