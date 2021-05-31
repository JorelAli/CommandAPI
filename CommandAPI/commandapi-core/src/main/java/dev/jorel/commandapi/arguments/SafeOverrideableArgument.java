/*******************************************************************************
 * Copyright 2018, 2020 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi.arguments;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;

import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.Tooltip;

/**
 * An interface declaring methods required to override argument suggestions
 */
public abstract class SafeOverrideableArgument<S> extends Argument {
	
	private final Function<S, String> mapper;

	protected SafeOverrideableArgument(String nodeName, ArgumentType<?> rawType, Function<S, String> mapper) {
		super(nodeName, rawType);
		this.mapper = mapper;
	}
	
	public final Argument withSafeSuggestions(Function<SuggestionInfo, S[]> suggestions) {
		return super.replaceSuggestions(suggestionsInfo -> {
			S[] sArr = suggestions.apply(suggestionsInfo);
			String[] result = new String[sArr.length];
			for(int i = 0; i < sArr.length; i++) {
				result[i] = mapper.apply(sArr[i]);
			}
			return result;
		});
	}
	
	public final Argument withSafeSuggestionsT(Function<SuggestionInfo, Tooltip<S>[]> suggestions) {
		return super.replaceSuggestionsT(suggestionsInfo -> {
			Tooltip<S>[] tArr = suggestions.apply(suggestionsInfo);
			IStringTooltip[] result = new IStringTooltip[tArr.length];
			for(int i = 0; i < tArr.length; i++) {
				result[i] = Tooltip.build(mapper).apply(tArr[i]);
			}
			return result;
		});
	}

	/**
	 * Override the suggestions of this argument with a custom array.
	 * 
	 * @param suggestions the S array to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public final Argument safeOverrideSuggestions(S... suggestions) {
		return super.overrideSuggestions(sMap0(suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a custom <code>Collection&lt;S&gt;</code>.
	 * 
	 * @param suggestions the <code>Collection&lt;S&gt;</code> to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public final Argument safeOverrideSuggestions(Collection<S> suggestions) {
		return super.overrideSuggestions(sMap0(suggestions.toArray((S[]) new Object[0])));
	} 

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	public final Argument safeOverrideSuggestions(Function<CommandSender, S[]> suggestions) {
		return super.overrideSuggestions(sMap1(suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a custom array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	public final Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], S[]> suggestions) {
		return super.overrideSuggestions(sMap2(suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with an array of <code>Tooltip&lt;S&gt;</code>,
	 * that represents a safe suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 */
	@Deprecated
	@SafeVarargs
	public final Argument safeOverrideSuggestionsT(Tooltip<S>... suggestions) {
		return super.overrideSuggestionsT(tMap0(suggestions));
	};
	
	/**
	 * Override the suggestions of this argument with a <code>Collection&lt;Tooltip&lt;S&gt;&gt;</code>,
	 * that represents a safe suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with 
	 * @return the current argument
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	public final Argument safeOverrideSuggestionsT(Collection<Tooltip<S>> suggestions) {
		return super.overrideSuggestionsT(tMap0(suggestions.toArray(new Tooltip[0])));
	};
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender to an array of <code>Tooltip&lt;S&gt;</code>, that represents a safe suggestion and a
	 * hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	public final Argument safeOverrideSuggestionsT(Function<CommandSender, Tooltip<S>[]> suggestions) {
		return super.overrideSuggestionsT(tMap1(suggestions));
	}
	
	/**
	 * Override the suggestions of this argument with a function mapping the command
	 * sender an previously declared arguments to an array of <code>Tooltip&lt;S&gt;</code>, that
	 * represents a safe suggestion and a hover tooltip
	 * 
	 * @param suggestions the suggestions and tooltips to override suggestions with
	 * @return the current argument
	 */
	@Deprecated
	public final Argument safeOverrideSuggestionsT(BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions) {
		return super.overrideSuggestionsT(tMap2(suggestions));
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
	@Deprecated
	@SafeVarargs
	private final BiFunction<CommandSender, Object[], String[]> sMap0(S... suggestions) {
		return (c, m) -> Arrays.stream(suggestions).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -&gt; S[]</code> of objects to suggest to the command sender where <code>sender</code> is the command sender
	 * @return the current argument
	 */
	@Deprecated
	private final BiFunction<CommandSender, Object[], String[]> sMap1(Function<CommandSender, S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c)).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * Safely overrides the suggestions of this argument with a custom array.
	 * Arguments of type S are guaranteed to succeed in commands if and only if the
	 * mapping function does not fail.
	 * 
	 * @param mapper      the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -&gt; S[]</code> of objects to suggest to the command sender where <code>sender</code> is the command sender and <code>args</code> is the array of previously defined arguments 
	 * @return the current argument
	 */
	@Deprecated
	private final BiFunction<CommandSender, Object[], String[]> sMap2(BiFunction<CommandSender, Object[], S[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(mapper).toArray(String[]::new);
	}
	
	/**
	 * sMap0, but for <code>Tooltip&lt;S&gt;</code> instead of Strings
	 * @param mapper the mapping function from S to a String
	 * @param suggestions a <code>Tooltip<S>[]</code> of tooltips and suggestions to send to the user
	 * @return the current argument
	 * @see SafeOverrideableArgument#sMap0(Function, Object...)
	 */
	@Deprecated
	@SafeVarargs
	private final BiFunction<CommandSender, Object[], IStringTooltip[]> tMap0(Tooltip<S>... suggestions) {
		return (c, m) -> Arrays.stream(suggestions).map(Tooltip.build(mapper)).toArray(IStringTooltip[]::new);
	}
	
	/**
	 * sMap1, but for <code>Tooltip&lt;S&gt;</code> instead of Strings
	 * @param mapper the mapping function from S to a String
	 * @param suggestions a <code>(sender) -&gt; Tooltip&lt;S&gt;[]</code> of tooltips and suggestions to send to the user
	 * @return the current argument
	 * @see SafeOverrideableArgument#sMap1(Function, Function)
	 */
	@Deprecated
	private final BiFunction<CommandSender, Object[], IStringTooltip[]> tMap1(Function<CommandSender, Tooltip<S>[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c)).map(Tooltip.build(mapper)).toArray(IStringTooltip[]::new);
	}
	
	/**
	 * sMap2, but for <code>Tooltip&lt;S&gt;</code> instead of Strings
	 * @param mapper the mapping function from S to a String
	 * @param suggestions a <code>(sender, args) -&gt; Tooltip&lt;S&gt;[]</code> of tooltips and suggestions to send to the user
	 * @return the current argument
	 * @see SafeOverrideableArgument#sMap2(Function, BiFunction)
	 */
	@Deprecated
	private final BiFunction<CommandSender, Object[], IStringTooltip[]> tMap2(BiFunction<CommandSender, Object[], Tooltip<S>[]> suggestions) {
		return (c, m) -> Arrays.stream(suggestions.apply(c, m)).map(Tooltip.build(mapper)).toArray(IStringTooltip[]::new);
	}
	
}
