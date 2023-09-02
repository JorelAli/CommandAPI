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

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.AbstractArgumentTree;
import dev.jorel.commandapi.CommandAPIExecutor;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.exceptions.DuplicateNodeNameException;
import dev.jorel.commandapi.executors.CommandArguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * The core abstract class for Command API arguments
 *
 * @param <T>             The type of the underlying object that this argument casts to
 * @param <Impl>          The class extending this class, used as the return type for chain calls
 * @param <Argument>      The implementation of Argument used by the class extending this class
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractArgument<T, Impl
/// @cond DOX
extends AbstractArgument<T, Impl, Argument, CommandSender>
/// @endcond
, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> extends AbstractArgumentTree<Impl, Argument, CommandSender> {

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
	 * @param rawType  the NMS or brigadier type to be used for this argument
	 */
	protected AbstractArgument(String nodeName, ArgumentType<?> rawType) {
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
	 *
	 * @return the name of this argument's node
	 */
	public final String getNodeName() {
		return this.nodeName;
	}

	/**
	 * Parses an argument, returning the specific object that the argument
	 * represents. This is intended for use by the internals of the CommandAPI and
	 * isn't expected to be used outside the CommandAPI
	 *
	 * @param <Source>     the command source type
	 * @param cmdCtx       the context which ran this command
	 * @param key          the name of the argument node
	 * @param previousArgs a {@link CommandArguments} object holding previous parsed arguments
	 * @return the parsed object represented by this argument
	 * @throws CommandSyntaxException if parsing fails
	 */
	public abstract <Source> T parseArgument(CommandContext<Source> cmdCtx, String key, CommandArguments previousArgs) throws CommandSyntaxException;

	/////////////////
	// Suggestions //
	/////////////////

	private ArgumentSuggestions<CommandSender> replaceSuggestions = null;
	private ArgumentSuggestions<CommandSender> includedSuggestions = null;

	/**
	 * Include suggestions to add to the list of default suggestions represented by this argument.
	 *
	 * @param suggestions An {@link ArgumentSuggestions} object representing the suggestions. Use the
	 *                    Static methods on ArgumentSuggestions to create these.
	 * @return the current argument
	 */
	public Impl includeSuggestions(ArgumentSuggestions<CommandSender> suggestions) {
		this.includedSuggestions = suggestions;
		return instance();
	}

	/**
	 * Returns an optional function which produces an array of suggestions which should be added
	 * to existing suggestions.
	 *
	 * @return An Optional containing a function which generates suggestions
	 */
	public Optional<ArgumentSuggestions<CommandSender>> getIncludedSuggestions() {
		return Optional.ofNullable(includedSuggestions);
	}


	/**
	 * Replace the suggestions of this argument.
	 *
	 * @param suggestions An {@link ArgumentSuggestions} object representing the suggestions. Use the static methods in
	 *                    ArgumentSuggestions to create these.
	 * @return the current argument
	 */
	public Impl replaceSuggestions(ArgumentSuggestions<CommandSender> suggestions) {
		this.replaceSuggestions = suggestions;
		return instance();
	}

	/**
	 * Returns an optional function that maps the command sender to an IStringTooltip array of
	 * suggestions for the current command
	 *
	 * @return a function that provides suggestions, or <code>Optional.empty()</code> if there
	 * are no overridden suggestions.
	 */
	public final Optional<ArgumentSuggestions<CommandSender>> getOverriddenSuggestions() {
		return Optional.ofNullable(replaceSuggestions);
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
	public final Impl withPermission(CommandPermission permission) {
		this.permission = permission;
		return instance();
	}

	/**
	 * Assigns the given permission as a requirement to execute this command.
	 *
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public final Impl withPermission(String permission) {
		this.permission = CommandPermission.fromString(permission);
		return instance();
	}

	/**
	 * Returns the permission required to run this command
	 *
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
	 *
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
	public final Impl withRequirement(Predicate<CommandSender> requirement) {
		this.requirements = this.requirements.and(requirement);
		return instance();
	}

	/////////////////
	// Listability //
	/////////////////

	private boolean isListed = true;

	/**
	 * Returns true if this argument will be listed in the Object args[] of the command executor
	 *
	 * @return true if this argument will be listed in the Object args[] of the command executor
	 */
	public boolean isListed() {
		return this.isListed;
	}

	/**
	 * Sets whether this argument will be listed in the Object args[] of the command executor
	 *
	 * @param listed if true, this argument will be included in the Object args[] of the command executor
	 * @return this current argument
	 */
	public Impl setListed(boolean listed) {
		this.isListed = listed;
		return instance();
	}

	/////////////////
	// Optionality //
	/////////////////

	private boolean isOptional = false;
	private final List<Argument> combinedArguments = new ArrayList<>();

	/**
	 * Returns true if this argument will be optional when executing the command this argument is included in
	 *
	 * @return true if this argument will be optional when executing the command this argument is included in
	 */
	public boolean isOptional() {
		return isOptional;
	}

	/**
	 * Sets whether this argument will be optional when executing the command this argument is included in
	 *
	 * @param optional if true, this argument will be optional when executing the command this argument is included in
	 * @return this current argument
	 */
	public Impl setOptional(boolean optional) {
		this.isOptional = optional;
		return instance();
	}

	/**
	 * Returns a list of arguments linked to this argument.
	 *
	 * @return A list of arguments linked to this argument
	 */
	public List<Argument> getCombinedArguments() {
		return combinedArguments;
	}

	/**
	 * Returns true if this argument has linked arguments.
	 *
	 * @return true if this argument has linked arguments
	 */
	public boolean hasCombinedArguments() {
		return !combinedArguments.isEmpty();
	}

	/**
	 * Adds combined arguments to this argument. Combined arguments are used to have required arguments after optional arguments
	 * by ignoring they exist until they are added to the arguments array for registration.
	 * <p>
	 * This method also causes permissions and requirements from this argument to be copied over to the arguments you want to combine
	 * this argument with. Their permissions and requirements will be ignored.
	 *
	 * @param combinedArguments The arguments to combine to this argument
	 * @return this current argument
	 */
	@SafeVarargs
	public final Impl combineWith(Argument... combinedArguments) {
		this.combinedArguments.addAll(Arrays.asList(combinedArguments));
		return instance();
	}

	//////////////////////
	// Command Building //
	//////////////////////

	public String getHelpString() {
		return "<" + this.getNodeName() + ">";
	}

	@Override
	public String toString() {
		return this.getNodeName() + "<" + this.getClass().getSimpleName() + ">";
	}

	/**
	 * Adds this argument to the end of the all the current possible paths given. The added entry is built as {code nodeName:argumentClass}.
	 *
	 * @param argumentStrings A list of possible paths to this argument so far.
	 */
	public void appendToCommandPaths(List<List<String>> argumentStrings) {
		// Create paths for this argument
		String argumentString = nodeName + ":" + getClass().getSimpleName();
		for (List<String> path : argumentStrings) {
			path.add(argumentString);
		}

		// Add combined arguments
		for (Argument subArgument : combinedArguments) {
			subArgument.appendToCommandPaths(argumentStrings);
		}
	}

	/**
	 * Builds the Brigadier {@link CommandNode} structure for this argument. Note that the Brigadier node structure may
	 * contain multiple nodes, for example if {@link #combineWith(AbstractArgument[])} was called for this argument to
	 * merge it with other arguments.
	 * <p>
	 * This process is broken up into 4 other methods for the convenience of defining special node structures for specific
	 * arguments. Those methods are:
	 * <ul>
	 *     <li>{@link #checkPreconditions(List, List)}</li>
	 *     <li>{@link #createArgumentBuilder(List, List)}</li>
	 *     <li>{@link #finishBuildingNode(ArgumentBuilder, List, CommandAPIExecutor)}</li>
	 *     <li>{@link #linkNode(CommandNode, CommandNode, List, List, CommandAPIExecutor)}</li>
	 * </ul>
	 *
	 * @param previousNode                    The {@link CommandNode} to add this argument onto.
	 * @param previousArguments               A List of CommandAPI arguments that came before this argument.
	 * @param previousNonLiteralArgumentNames A List of Strings containing the node names that came before this argument.
	 * @param terminalExecutor                The {@link CommandAPIExecutor} to apply at the end of the node structure.
	 *                                        This parameter can be null to indicate that this argument should not be
	 *                                        executable.
	 * @param <Source>                        The Brigadier Source object for running commands.
	 * @return The last node in the Brigadier node structure for this argument.
	 */
	public <Source> CommandNode<Source> addArgumentNodes(CommandNode<Source> previousNode, List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames,
														 CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> terminalExecutor) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Check preconditions
		checkPreconditions(previousArguments, previousNonLiteralArgumentNames);

		// Handle previewable argument
		if (this instanceof Previewable<?, ?>) {
			handler.addPreviewableArgument(previousArguments, (Argument) this);
		}

		// Create node
		ArgumentBuilder<Source, ?> rootBuilder = createArgumentBuilder(previousArguments, previousNonLiteralArgumentNames);

		// Finish building node
		CommandNode<Source> rootNode = finishBuildingNode(rootBuilder, previousArguments, terminalExecutor);

		// Link node to previous
		previousNode = linkNode(previousNode, rootNode, previousArguments, previousNonLiteralArgumentNames, terminalExecutor);

		// Return last node
		return previousNode;
	}

	/**
	 * Checks for any conditions that mean this argument cannot be build properly.
	 *
	 * @param previousArguments               A List of CommandAPI arguments that came before this argument.
	 * @param previousNonLiteralArgumentNames A List of Strings containing the node names that came before this argument.
	 */
	public void checkPreconditions(List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames) {
		if (previousNonLiteralArgumentNames.contains(nodeName)) {
			throw new DuplicateNodeNameException(previousArguments, (Argument) this);
		}
	}

	/**
	 * Creates a Brigadier {@link ArgumentBuilder} representing this argument. Note: calling this method will also add
	 * this argument and its name to the end of the given {@code previousArguments} and {@code previousNonLiteralArgumentNames}
	 * lists.
	 *
	 * @param previousArguments               A List of CommandAPI arguments that came before this argument.
	 * @param previousNonLiteralArgumentNames A List of Strings containing the node names that came before this argument.
	 * @param <Source>                        The Brigadier Source object for running commands.
	 * @return The {@link ArgumentBuilder} for this argument.
	 */
	public <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Create node
		RequiredArgumentBuilder<Source, ?> rootBuilder = RequiredArgumentBuilder.argument(nodeName, rawType);

		// Add suggestions
		if (replaceSuggestions != null) {
			// Overridden suggestions take precedence
			rootBuilder.suggests(handler.generateBrigadierSuggestions(previousArguments, replaceSuggestions));
		} else if (includedSuggestions != null) {
			// Insert additional defined suggestions
			SuggestionProvider<Source> defaultSuggestions;
			if (this instanceof CustomProvidedArgument cPA) {
				defaultSuggestions = handler.getPlatform().getSuggestionProvider(cPA.getSuggestionProvider());
			} else {
				defaultSuggestions = rawType::listSuggestions;
			}

			SuggestionProvider<Source> includedSuggestions = handler.generateBrigadierSuggestions(previousArguments, this.includedSuggestions);

			rootBuilder.suggests((cmdCtx, builder) -> {
				// Heavily inspired by CommandDispatcher#getCompletionSuggestions, with combining
				// multiple CompletableFuture<Suggestions> into one.
				CompletableFuture<Suggestions> defaultSuggestionsFuture = defaultSuggestions.getSuggestions(cmdCtx, builder);
				CompletableFuture<Suggestions> includedSuggestionsFuture = includedSuggestions.getSuggestions(cmdCtx, builder);

				CompletableFuture<Suggestions> result = new CompletableFuture<>();
				CompletableFuture.allOf(defaultSuggestionsFuture, includedSuggestionsFuture).thenRun(() -> {
					List<Suggestions> suggestions = new ArrayList<>();
					suggestions.add(defaultSuggestionsFuture.join());
					suggestions.add(includedSuggestionsFuture.join());
					result.complete(Suggestions.merge(cmdCtx.getInput(), suggestions));
				});
				return result;
			});
		} else if (this instanceof CustomProvidedArgument cPA) {
			// Handle arguments with built-in suggestion providers
			rootBuilder.suggests(handler.getPlatform().getSuggestionProvider(cPA.getSuggestionProvider()));
		}

		// Add argument to previousArgument lists
		//  Note: this argument should not be in the previous arguments list when doing suggestions,
		//  since this argument is not going to be present in the cmdCtx while being suggested
		previousArguments.add((Argument) this);
		previousNonLiteralArgumentNames.add(nodeName);

		return rootBuilder;
	}

	/**
	 * Finishes building the Brigadier {@link ArgumentBuilder} representing this argument.
	 *
	 * @param rootBuilder       The {@link ArgumentBuilder} to finish building.
	 * @param previousArguments A List of CommandAPI arguments that came before this argument.
	 * @param terminalExecutor  The {@link CommandAPIExecutor} to apply at the end of the node structure. This parameter
	 *                          can be null to indicate that this argument should not be executable.
	 * @param <Source>          The Brigadier Source object for running commands.
	 * @return The {@link CommandNode} representing this argument created by building the given {@link ArgumentBuilder}.
	 */
	public <Source> CommandNode<Source> finishBuildingNode(ArgumentBuilder<Source, ?> rootBuilder, List<Argument> previousArguments, CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> terminalExecutor) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Add permission and requirements
		rootBuilder.requires(handler.generateBrigadierRequirements(permission, requirements));

		// Add the given executor if we are the last node
		if (combinedArguments.isEmpty() && terminalExecutor != null && terminalExecutor.hasAnyExecutors()) {
			rootBuilder.executes(handler.generateBrigadierCommand(previousArguments, terminalExecutor));
		}

		return rootBuilder.build();
	}

	/**
	 * Links this argument into the Brigadier {@link CommandNode} structure.
	 *
	 * @param previousNode                    The {@link CommandNode} to add this argument onto.
	 * @param rootNode                        The {@link CommandNode} representing this argument.
	 * @param previousArguments               A List of CommandAPI arguments that came before this argument.
	 * @param previousNonLiteralArgumentNames A List of Strings containing the node names that came before this argument.
	 * @param terminalExecutor                The {@link CommandAPIExecutor} to apply at the end of the node structure.
	 *                                        This parameter can be null to indicate that this argument should not be
	 *                                        executable.
	 * @param <Source>                        The Brigadier Source object for running commands.
	 * @return The last node in the Brigadier {@link CommandNode} structure representing this Argument. Note that this
	 * is not necessarily the {@code rootNode} for this argument, since the Brigadier node structure may contain multiple
	 * nodes. This might happen if {@link #combineWith(AbstractArgument[])} was called for this argument to merge it with
	 * other arguments.
	 */
	public <Source> CommandNode<Source> linkNode(CommandNode<Source> previousNode, CommandNode<Source> rootNode, List<Argument> previousArguments, List<String> previousNonLiteralArgumentNames,
												 CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> terminalExecutor) {
		// Add rootNode to the previous
		previousNode.addChild(rootNode);

		// Add combined arguments
		previousNode = rootNode;
		for (int i = 0; i < combinedArguments.size(); i++) {
			Argument subArgument = combinedArguments.get(i);
			previousNode = subArgument.addArgumentNodes(previousNode, previousArguments, previousNonLiteralArgumentNames,
				// Only apply the `terminalExecutor` to the last argument
				i == combinedArguments.size() - 1 ? terminalExecutor : null);
		}

		// Return last node
		return previousNode;
	}
}
