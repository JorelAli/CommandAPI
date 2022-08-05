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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.jorel.commandapi.ArgumentTree;
import dev.jorel.commandapi.StringTooltip;
import org.bukkit.command.CommandSender;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.nms.NMS;

/**
 * The core abstract class for Command API arguments
 * 
 * @param <T> The type of the underlying object that this argument casts to
 */
public abstract class Argument<T> extends ArgumentTree {

	/**
	 * Returns the primitive type of the current Argument. After executing a
	 * command, this argument should yield an object of this returned class.
	 * 
	 * @return the type that this argument yields when the command is run
	 */
	public abstract Class<T> getPrimitiveType();

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
	 * @param nms                  an instance of NMS
	 * @param cmdCtx               the context which ran this command
	 * @param key                  the name of the argument node
	 * @param previousArgs         an array of previously declared arguments
	 * @return the parsed object represented by this argument
	 * @throws CommandSyntaxException if parsing fails
	 */
	public abstract <CommandSourceStack> T parseArgument(NMS<CommandSourceStack> nms,
			CommandContext<CommandSourceStack> cmdCtx, String key, Object[] previousArgs) throws CommandSyntaxException;

	/////////////////
	// Suggestions //
	/////////////////

	private Optional<ArgumentSuggestions> suggestions = Optional.empty();
	private Optional<ArgumentSuggestions> addedSuggestions = Optional.empty();

	/**
	 * Include suggestions to add to the list of default suggestions represented by this argument.
	 *
	 * @param suggestions An {@link ArgumentSuggestions} object representing the suggestions. Use the
	 * Static methods on ArgumentSuggestions to create these.
	 *
	 * @return the current argument
	 */
	public Argument<T> includeSuggestions(ArgumentSuggestions suggestions) {
		this.addedSuggestions = Optional.of(suggestions);
		return this;
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
	 * @deprecated use {@link #includeSuggestions(ArgumentSuggestions)} instead
	 */
	@Deprecated(forRemoval = true)
	public Argument<T> includeSuggestions(Function<SuggestionInfo, String[]> suggestions) {
		return includeSuggestions(ArgumentSuggestions.strings(suggestions));
	}

	/**
	 * Include suggestions to add to the list of default suggestions represented by
	 * this argument.
	 * 
	 * @param suggestions a function that takes in SuggestionInfo which includes
	 *                    information about the current state at the time the
	 *                    suggestions are run and returns an StringTooltip[] of
	 *                    suggestions (with tooltips) to add
	 * @return the current argument
	 * @deprecated use {@link #includeSuggestions(ArgumentSuggestions)} instead
	 */
	@Deprecated(forRemoval = true)
	public Argument<T> includeSuggestionsT(Function<SuggestionInfo, StringTooltip[]> suggestions) {
		return includeSuggestions(ArgumentSuggestions.stringsWithTooltips(suggestions));
	}

	/**
	 * Returns an optional function which produces an array of suggestions which should be added
	 * to existing suggestions.
	 * @return An Optional containing a function which generates suggestions
	 */
	public Optional<ArgumentSuggestions> getIncludedSuggestions() {
		return addedSuggestions;
	}


	/**
	 * Replace the suggestions of this argument.
	 * @param suggestions An {@link ArgumentSuggestions} object representing the suggestions. Use the static methods in
	 * ArgumentSuggestions to create these.
	 * @return the current argument
	 */
  
	public Argument<T> replaceSuggestions(ArgumentSuggestions suggestions) {
		this.suggestions = Optional.of(suggestions);
		return this;
	}

	/**
	 * Replaces the suggestions of this argument with an array of suggestions.
	 * @param suggestions a function that takes in {@link SuggestionInfo} and returns a {@link String[]} of suggestions
	 * @return the current argument
	 * @deprecated use {@link #replaceSuggestions(ArgumentSuggestions)} instead
	 */
	@Deprecated(forRemoval = true)
	public Argument<T> replaceSuggestions(Function<SuggestionInfo, String[]> suggestions) {
		return replaceSuggestions(ArgumentSuggestions.strings(suggestions));
	}

	/**
	 * Replaces the suggestions of this argument with an array of suggestions with tooltips.
	 * @param suggestions a function that takes in {@link SuggestionInfo} and returns a {@link StringTooltip[]} of suggestions
	 * @return the current argument
	 * @deprecated use {@link #replaceSuggestions(ArgumentSuggestions)} instead
	 */
	@Deprecated(forRemoval = true)
	public Argument<T> replaceSuggestionsT(Function<SuggestionInfo, StringTooltip[]> suggestions) {
		return replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(suggestions));
	}

	/**
	 * Returns an optional function that maps the command sender to an StringTooltip array of
	 * suggestions for the current command
	 * 
	 * @return a function that provides suggestions, or <code>Optional.empty()</code> if there
	 *         are no overridden suggestions.
	 */
	public final Optional<ArgumentSuggestions> getOverriddenSuggestions() {
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
	public final Argument<T> withPermission(CommandPermission permission) {
		this.permission = permission;
		return this;
	}
	
	/**
	 * Assigns the given permission as a requirement to execute this command.
	 * 
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public final Argument<T> withPermission(String permission) {
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
	public final Argument<T> withRequirement(Predicate<CommandSender> requirement) {
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
	public Argument<T> setListed(boolean listed) {
		this.isListed = listed;
		return this;
	}
	
	///////////
	// Other //
	///////////
	
	/**
	 * Gets a list of entity names for the current provided argument. This is
	 * expected to be implemented by EntitySelectorArgument, see
	 * {@link EntitySelectorArgument#getEntityNames(Object)}
	 * 
	 * @param argument a parsed (Bukkit) object representing the entity selector
	 *                 type. This is either a List, an Entity or a Player
	 * @return a list of strings representing the names of the entity or entities
	 *         from {@code argument}
	 */
	public List<String> getEntityNames(Object argument) {
		return Arrays.asList(new String[] { null });
	}
	

}