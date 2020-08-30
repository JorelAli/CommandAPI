package dev.jorel.commandapi.arguments;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.jorel.commandapi.NativeTooltip;
import dev.jorel.commandapi.Tooltip;

/**
 * An interface declaring methods required to override argument suggestions
 */
public abstract class SafeOverrideableArgument<S> extends Argument {
	
	private final Function<S, String> mapper;

	protected SafeOverrideableArgument(ArgumentType<?> rawType, Function<S, String> mapper) {
		super(rawType);
		this.mapper = mapper;
	}

	/**
	 * Override the suggestions of this argument with a custom array. Typically,
	 * this is the supplier <code>s -> suggestions</code>.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 */
	@SuppressWarnings("unchecked")
	public final Argument safeOverrideSuggestions(S... suggestions) {
		return super.overrideSuggestions(sMap0(mapper, suggestions));
	}

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	public final Argument safeOverrideSuggestions(Function<CommandSender, S[]> suggestions) {
		return super.overrideSuggestions(sMap1(mapper, suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	public final Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], S[]> suggestions) {
		return super.overrideSuggestions(sMap2(mapper, suggestions));
	}
	
	@SafeVarargs
	public final Argument safeOverrideSuggestionsT(Tooltip<S>... suggestions) {
		return super.overrideSuggestionsT(tMap0(mapper, suggestions));
	};
	
	public final Argument safeOverrideSuggestionsT(Function<CommandSender, Tooltip<S>[]> suggestions) {
		return super.overrideSuggestionsT(tMap1(mapper, suggestions));
	}
	
	public final Argument safeOverrideSuggestionsT(BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions) {
		return super.overrideSuggestionsT(tMap2(mapper, suggestions));
	}
	
	/**
	 * Composes a <code>S</code> to a <code>NamespacedKey</code> mapping function to convert <code>S</code> to a <code>String</code>
	 * @param mapper the mapping function from <code>S</code> to <code>NamespacedKey</code>
	 * @return a composed function that converts <code>S</code> to <code>String</code>
	 */
	static <S> Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
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
	private final BiFunction<CommandSender, Object[], String[]> sMap0(Function<S, String> mapper, S... suggestions) {
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
	private final BiFunction<CommandSender, Object[], String[]> sMap1(Function<S, String> mapper, Function<CommandSender, S[]> suggestions) {
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
	private final BiFunction<CommandSender, Object[], String[]> sMap2(Function<S, String> mapper, BiFunction<CommandSender, Object[], S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(mapper).toArray(String[]::new);
	}
	
	@SuppressWarnings("unchecked")
	private final BiFunction<CommandSender, Object[], NativeTooltip[]> tMap0(Function<S, String> mapper, Tooltip<S>... suggestions) {
		return (c, m) -> Arrays.stream(suggestions).map(x -> x.build(mapper)).toArray(NativeTooltip[]::new);
	}
	
	private final BiFunction<CommandSender, Object[], NativeTooltip[]> tMap1(Function<S, String> mapper, Function<CommandSender, Tooltip<S>[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c)).map(x -> x.build(mapper)).toArray(NativeTooltip[]::new);
	}
	
	private final BiFunction<CommandSender, Object[], NativeTooltip[]> tMap2(Function<S, String> mapper, BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(x -> x.build(mapper)).toArray(NativeTooltip[]::new);
	}
	
}
