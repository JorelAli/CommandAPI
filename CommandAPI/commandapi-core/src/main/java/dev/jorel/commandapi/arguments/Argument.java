/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
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

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.IStringTooltip;
import dev.jorel.commandapi.StringTooltip;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.nms.NMS;

/**
 * The core abstract class for Command API arguments
 */
public abstract class Argument {

	/**
	 * Returns the primitive type of the current Argument. After executing a
	 * command, this argument should yield an object of this returned class.
	 * 
	 * @return the type that this argument yields when the command is run
	 */
	public abstract Class<?> getPrimitiveType();

	/**
	 * Returns the argument type for this argument.
	 * 
	 * @return the argument type for this argument
	 */
	public abstract CommandAPIArgumentType getArgumentType();

	////////////////////////
	// Raw Argument Types //
	////////////////////////

	private final String nodeName;
	private final ArgumentType<?> rawType;

	/**
	 * Constructs an argument with a given NMS/brigadier type.
	 * 
	 * @param nodeName the name to assign to this argument node 
	 * @param rawType the NMS or brigadier type to be used for this argument
	 */
	protected Argument(String nodeName, ArgumentType<?> rawType) {
		this.nodeName = nodeName;
		this.rawType = rawType;
	}

	/**
	 * Returns the NMS or brigadier type for this argument.
	 * 
	 * @return the NMS or brigadier type for this argument
	 */
	public final ArgumentType<?> getRawType() {
		return this.rawType;
	}
	
	/**
	 * Returns the name of this argument's node
	 * @return the name of this argument's node
	 */
	public final String getNodeName() {
		return this.nodeName;
	}
	
	/**
	 * Parses an argument, returning the specific Bukkit object that the argument
	 * represents. This is intended for use by the internals of the CommandAPI and
	 * isn't expected to be used outside the CommandAPI
	 * 
	 * @param <CommandSourceStack> the command source type
	 * @param nms                      an instance of NMS
	 * @param cmdCtx                   the context which ran this command
	 * @param key                      the name of the argument node
	 * @return the parsed object represented by this argument
	 * @throws CommandSyntaxException if parsing fails
	 */
	public abstract <CommandSourceStack> Object parseArgument(NMS<CommandSourceStack> nms,
			CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	/////////////////
	// Suggestions //
	/////////////////

	private Optional<Function<SuggestionInfo, IStringTooltip[]>> suggestions = Optional.empty();
	private Optional<Function<SuggestionInfo, IStringTooltip[]>> addedSuggestions = Optional.empty();
		
	/**
	 * Maps a String[] of suggestions to a StringTooltip[], using StringTooltip.none.
	 * This is used internally by the CommandAPI.
	 * 
	 * @param suggestions the array of suggestions to convert
	 * @return a StringTooltip[] representation of the provided suggestions
	 */
	private final StringTooltip[] fromSuggestions(String[] suggestions) {
		StringTooltip[] result = new StringTooltip[suggestions.length];
		for (int i = 0; i < suggestions.length; i++) {
			result[i] = StringTooltip.none(suggestions[i]);
		}
		return result;
	}
	
	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in SuggestionInfo which includes
	 *                    information about the current state at the time the
	 *                    suggestions are run and returns a String[] of suggestions
	 *                    to add
	 * @return the current argument
	 */
	public Argument includeSuggestions(Function<SuggestionInfo, String[]> suggestions) {
		this.addedSuggestions = Optional.of(suggestionInfo -> fromSuggestions(suggestions.apply(suggestionInfo)));
		return this;
	}
	
	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in SuggestionInfo which includes
	 *                    information about the current state at the time the
	 *                    suggestions are run and returns an IStringTooltip[] of
	 *                    suggestions (with tooltips) to add
	 * @return the current argument
	 */
	public Argument includeSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions) {
		this.addedSuggestions = Optional.of(suggestions);
		return this;
	}

	/**
	 * Returns an optional function which produces an array of suggestions which should be added
	 * to existing suggestions.
	 * @return An Optional containing a function which generates suggestions
	 */
	public Optional<Function<SuggestionInfo, IStringTooltip[]>> getIncludedSuggestions() {
		return addedSuggestions;
	}
	
	/**
	 * Override the suggestions of this argument with a String array.
	 * 
	 * @param suggestions the string array to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestions(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestions(String... suggestions) {
		this.suggestions = Optional.of(suggestionInfo -> fromSuggestions(suggestions));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a <code>Collection&lt;String&gt;</code>.
	 * 
	 * @param suggestions the collection of suggestions to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestions(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestions(Collection<String> suggestions) {
		this.suggestions = Optional.of(suggestionInfo -> fromSuggestions(suggestions.toArray(new String[0])));
		return this;
	}

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestions(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestions(Function<CommandSender, String[]> suggestions) {
		this.suggestions =  Optional.of(suggestionInfo -> fromSuggestions(suggestions.apply(suggestionInfo.sender())));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender and a data set of previously declared arguments to a String array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestions(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestions(BiFunction<CommandSender, Object[], String[]> suggestions) {
		this.suggestions =  Optional.of(suggestionInfo -> fromSuggestions(suggestions.apply(suggestionInfo.sender(), suggestionInfo.previousArgs())));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a <code>Collection&lt;IStringTooltip&gt;</code>.
	 * 
	 * @param suggestions the collection of IStringTooltip to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestionsT(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestionsT(Collection<IStringTooltip> suggestions) {
		this.suggestions = Optional.of(suggestionInfo -> suggestions.toArray(new IStringTooltip[0]));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with an IStringTooltip array.
	 * 
	 * @param suggestions the IStringTooltip array to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestionsT(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestionsT(IStringTooltip... suggestions) {
		this.suggestions = Optional.of(suggestionInfo -> suggestions);
		return this;
	}

	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender to an IStringTooltip array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestionsT(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestionsT(Function<CommandSender, IStringTooltip[]> suggestions) {
		this.suggestions =  Optional.of(suggestionInfo -> suggestions.apply(suggestionInfo.sender()));
		return this;
	}
	
	/**
	 * Override the suggestions of this argument with a function that maps the
	 * command sender and a data set of previously declared arguments to an IStringTooltip array.
	 * 
	 * @param suggestions the function to override suggestions with
	 * @return the current argument
	 * @deprecated use {@link Argument#replaceSuggestionsT(Function)}
	 */
	@Deprecated(forRemoval = true)
	public final Argument overrideSuggestionsT(BiFunction<CommandSender, Object[], IStringTooltip[]> suggestions) {
		this.suggestions =  Optional.of(suggestionInfo -> suggestions.apply(suggestionInfo.sender(), suggestionInfo.previousArgs()));
		return this;
	}

	/**
	 * Replaces the suggestions of this argument with an array of suggestions.
	 * @param suggestions a function that takes in {@link SuggestionInfo} and returns a {@link String[]} of suggestions
	 * @return the current argument
	 */
	public Argument replaceSuggestions(Function<SuggestionInfo, String[]> suggestions) {
		this.suggestions = Optional.of(suggestionInfo -> fromSuggestions(suggestions.apply(suggestionInfo)));
		return this;
	}
	
	/**
	 * Replaces the suggestions of this argument with an array of suggestions with tooltips.
	 * @param suggestions a function that takes in {@link SuggestionInfo} and returns a {@link IStringTooltip[]} of suggestions
	 * @return the current argument
	 */
	public Argument replaceSuggestionsT(Function<SuggestionInfo, IStringTooltip[]> suggestions) {
		this.suggestions = Optional.of(suggestions);
		return this;
	}

	/**
	 * Returns an optional function that maps the command sender to an IStringTooltip array of
	 * suggestions for the current command
	 * 
	 * @return a function that provides suggestions, or <code>Optional.empty()</code> if there
	 *         are no overridden suggestions.
	 */
	public final Optional<Function<SuggestionInfo, IStringTooltip[]>> getOverriddenSuggestions() {
		return suggestions;
	}

	/////////////////
	// Permissions //
	/////////////////

	private CommandPermission permission = CommandPermission.NONE;

	/**
	 * Assigns the given permission as a requirement to execute this command.
	 * 
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public final Argument withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * Assigns the given permission as a requirement to execute this command.
	 * 
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public final Argument withPermission(String permission) {
		this.permission = CommandPermission.fromString(permission);
		return this;
	}

	/**
	 * Returns the permission required to run this command
	 * @return the permission required to run this command
	 */
	public final CommandPermission getArgumentPermission() {
		return permission;
	}
	
	//////////////////
	// Requirements //
	//////////////////
	
	private Predicate<CommandSender> requirements = s -> true;
		
	/**
	 * Returns the requirements required to run this command
	 * @return the requirements required to run this command
	 */
	public final Predicate<CommandSender> getRequirements() {
		return this.requirements;
	}
	
	/**
	 * Adds a requirement that has to be satisfied to use this argument. This method
	 * can be used multiple times and each use of this method will AND its
	 * requirement with the previously declared ones
	 * 
	 * @param requirement the predicate that must be satisfied to use this argument
	 * @return this current argument
	 */
	public final Argument withRequirement(Predicate<CommandSender> requirement) {
		this.requirements = this.requirements.and(requirement);
		return this;
	}
	
	/////////////////
	// Listability //
	/////////////////
	
	private boolean isListed = true;
	
	/**
	 * Returns true if this argument will be listed in the Object args[] of the command executor
	 * @return true if this argument will be listed in the Object args[] of the command executor
	 */
	public boolean isListed() {
		return this.isListed;
	}
	
	/**
	 * Sets whether this argument will be listed in the Object args[] of the command executor
	 * @param listed if true, this argument will be included in the Object args[] of the command executor
	 * @return this current argument
	 */
	public Argument setListed(boolean listed) {
		this.isListed = listed;
		return this;
	}
	

}