package dev.jorel.commandapi.arguments;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

/**
 * An interface declaring methods required to override argument suggestions
 */
public interface ISafeOverrideableSuggestions<S> {

	/**
	 * Override the suggestions of this argument with a custom array. Typically,
	 * this is the supplier <code>s -> suggestions</code>.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 */
	@SuppressWarnings("unchecked")
	Argument safeOverrideSuggestions(S... suggestions);

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	Argument safeOverrideSuggestions(Function<CommandSender, S[]> suggestions);
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], S[]> suggestions);
	
	/**
	 * Composes a <code>S</code> to a <code>NamespacedKey</code> mapping function to convert <code>S</code> to a <code>String</code>
	 * @param mapper the mapping function from <code>S</code> to <code>NamespacedKey</code>
	 * @return a composed function that converts <code>S</code> to <code>String</code>
	 */
	default Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
		return mapper.andThen(NamespacedKey::toString);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a S[] of objects to suggest to the command sender
	 * @return the current argument
	 */
	@SuppressWarnings("unchecked")
	default BiFunction<CommandSender, Object[], String[]> sMap0(Function<S, String> mapper, S... suggestions) {
		return (c, m) -> Arrays.stream(suggestions).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -> S[]</code> of objects to suggest to the command sender where <code>sender</code> is the command sender
	 * @return the current argument
	 */
	default BiFunction<CommandSender, Object[], String[]> sMap1(Function<S, String> mapper, Function<CommandSender, S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c)).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -> S[]</code> of objects to suggest to the command sender where <code>sender</code> is the command sender and <code>args</code> is the array of previously defined arguments 
	 * @return the current argument
	 */
	default BiFunction<CommandSender, Object[], String[]> sMap2(Function<S, String> mapper, BiFunction<CommandSender, Object[], S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(mapper).toArray(String[]::new);
	}
	
}
