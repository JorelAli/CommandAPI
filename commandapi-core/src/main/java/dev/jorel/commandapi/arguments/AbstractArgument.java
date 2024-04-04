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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.AbstractArgumentTree;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.commandnodes.UnnamedRequiredArgumentBuilder;
import dev.jorel.commandapi.exceptions.DuplicateNodeNameException;
import dev.jorel.commandapi.exceptions.GreedyArgumentException;
import dev.jorel.commandapi.executors.CommandArguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
	public Optional<ArgumentSuggestions<CommandSender>> getOverriddenSuggestions() {
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
	public Impl withPermission(CommandPermission permission) {
		this.permission = permission;
		return instance();
	}

	/**
	 * Assigns the given permission as a requirement to execute this command.
	 *
	 * @param permission the permission required to execute this command
	 * @return this current argument
	 */
	public Impl withPermission(String permission) {
		this.permission = CommandPermission.fromString(permission);
		return instance();
	}

	/**
	 * Returns the permission required to run this command
	 *
	 * @return the permission required to run this command
	 */
	public CommandPermission getArgumentPermission() {
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
	public Predicate<CommandSender> getRequirements() {
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
	public Impl withRequirement(Predicate<CommandSender> requirement) {
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
	private final List<Argument> combinedArguments = new ArrayList<>();

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
	 * Adds combined arguments to this argument. Combined arguments are used to have required arguments after optional
	 * arguments. If this argument is optional and the user includes it in their command, any arguments combined with
	 * this argument will be required to execute the command.
	 *
	 * @param combinedArguments The arguments to combine to this argument
	 * @return this current argument
	 */
	public Impl combineWith(List<Argument> combinedArguments) {
		this.combinedArguments.addAll(combinedArguments);
		return instance();
	}

	/**
	 * Adds combined arguments to this argument. Combined arguments are used to have required arguments after optional
	 * arguments. If this argument is optional and the user includes it in their command, any arguments combined with
	 * this argument will be required to execute the command.
	 *
	 * @param combinedArguments The arguments to combine to this argument
	 * @return this current argument
	 */
	@SafeVarargs
	public final Impl combineWith(Argument... combinedArguments) {
		return this.combineWith(Arrays.asList(combinedArguments));
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
	 * Any arguments combined with this one are also added.
	 *
	 * @param argumentStrings A list of possible paths up to this argument so far.
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
	 *     <li>{@link #checkPreconditions(List, List, List)}</li>
	 *     <li>{@link #createArgumentBuilder(List, List)}</li>
	 *     <li>{@link #finishBuildingNode(ArgumentBuilder, List, Function)}</li>
	 *     <li>{@link #linkNode(List, CommandNode, List, List, Function)}</li>
	 * </ul>
	 *
	 * @param previousNodes           A List of {@link CommandNode}s to add this argument onto.
	 * @param previousArguments       A List of CommandAPI arguments that came before this argument.
	 * @param previousArgumentNames   A List of Strings containing the node names that came before this argument.
	 * @param terminalExecutorCreator A function that transforms the list of {@code previousArguments} into an
	 *                                appropriate Brigadier {@link Command} which should be applied at the end of
	 *                                the node structure. This parameter can be null to indicate that this argument
	 *                                should not be executable.
	 * @param <Source>                The Brigadier Source object for running commands.
	 * @return The list of last nodes in the Brigadier node structure for this argument.
	 */
	public <Source> List<CommandNode<Source>> addArgumentNodes(
		List<CommandNode<Source>> previousNodes,
		List<Argument> previousArguments, List<String> previousArgumentNames,
		Function<List<Argument>, Command<Source>> terminalExecutorCreator
	) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Check preconditions
		checkPreconditions(previousNodes, previousArguments, previousArgumentNames);

		// Handle previewable argument
		if (this instanceof Previewable<?, ?>) {
			handler.addPreviewableArgument(previousArguments, (Argument) this);
		}

		// Create node
		ArgumentBuilder<Source, ?> rootBuilder = createArgumentBuilder(previousArguments, previousArgumentNames);

		// Finish building node
		CommandNode<Source> rootNode = finishBuildingNode(rootBuilder, previousArguments, terminalExecutorCreator);

		// Link node to previous
		previousNodes = linkNode(previousNodes, rootNode, previousArguments, previousArgumentNames, terminalExecutorCreator);

		// Return last nodes
		return previousNodes;
	}

	/**
	 * Checks for any conditions that mean this argument cannot be built properly.
	 *
	 * @param previousNodes         A List of {@link CommandNode}s to add this argument onto.
	 * @param previousArguments     A List of CommandAPI arguments that came before this argument.
	 * @param previousArgumentNames A List of Strings containing the node names that came before this argument.
	 */
	public <Source> void checkPreconditions(
		List<CommandNode<Source>> previousNodes, List<Argument> previousArguments, List<String> previousArgumentNames
	) {
		if(previousNodes.isEmpty()) {
			throw new GreedyArgumentException(previousArguments, (Argument) this);
		}
		if (isListed && previousArgumentNames.contains(nodeName)) {
			throw new DuplicateNodeNameException(previousArguments, (Argument) this);
		}
	}

	/**
	 * Creates a Brigadier {@link ArgumentBuilder} representing this argument. Note: calling this method will also add
	 * this argument and its name to the end of the given {@code previousArguments} and {@code previousArgumentNames}
	 * lists.
	 *
	 * @param previousArguments     A List of CommandAPI arguments that came before this argument.
	 * @param previousArgumentNames A List of Strings containing the node names that came before this argument.
	 * @param <Source>              The Brigadier Source object for running commands.
	 * @return The {@link ArgumentBuilder} for this argument.
	 */
	public <Source> ArgumentBuilder<Source, ?> createArgumentBuilder(List<Argument> previousArguments, List<String> previousArgumentNames) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Create node and add suggestions
		// Note: I would like to combine these two `build.suggests(...)` calls, but they are actually two unrelated
		//  methods since UnnamedRequiredArgumentBuilder does not extend RequiredArgumentBuilder (see
		//  UnnamedRequiredArgumentBuilder for why). If UnnamedRequiredArgumentBuilder *does* extend
		//  RequiredArgumentBuilder, please simplify this if statement, like what Literal#createArgumentBuilder does.
		ArgumentBuilder<Source, ?> rootBuilder;
		if(isListed) {
			RequiredArgumentBuilder<Source, ?> builder = RequiredArgumentBuilder.argument(nodeName, rawType);
			builder.suggests(handler.generateBrigadierSuggestions(previousArguments, (Argument) this));

			rootBuilder = builder;
		} else {
			UnnamedRequiredArgumentBuilder<Source, ?> builder = UnnamedRequiredArgumentBuilder.unnamedArgument(nodeName, rawType);
			builder.suggests(handler.generateBrigadierSuggestions(previousArguments, (Argument) this));

			rootBuilder = builder;
		}

		// Add argument to previousArgument lists
		//  Note: this argument should not be in the previous arguments list when doing suggestions,
		//  since this argument is not going to be present in the cmdCtx while being suggested
		previousArguments.add((Argument) this);
		if(isListed) previousArgumentNames.add(nodeName);

		return rootBuilder;
	}

	/**
	 * Finishes building the Brigadier {@link ArgumentBuilder} representing this argument.
	 *
	 * @param rootBuilder             The {@link ArgumentBuilder} to finish building.
	 * @param previousArguments       A List of CommandAPI arguments that came before this argument.
	 * @param terminalExecutorCreator A function that transforms the list of {@code previousArguments} into an
	 *                                appropriate Brigadier {@link Command} which should be applied at the end
	 *                                of the node structure. This parameter can be null to indicate that this
	 *                                argument should not be executable.
	 * @param <Source>                The Brigadier Source object for running commands.
	 * @return The {@link CommandNode} representing this argument created by building the given {@link ArgumentBuilder}.
	 */
	public <Source> CommandNode<Source> finishBuildingNode(ArgumentBuilder<Source, ?> rootBuilder, List<Argument> previousArguments, Function<List<Argument>, Command<Source>> terminalExecutorCreator) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Add permission and requirements
		rootBuilder.requires(handler.generateBrigadierRequirements(permission, requirements));

		// Add the given executor if we are the last node
		if (combinedArguments.isEmpty() && terminalExecutorCreator != null) {
			rootBuilder.executes(terminalExecutorCreator.apply(previousArguments));
		}

		return rootBuilder.build();
	}

	/**
	 * Links this argument into the Brigadier {@link CommandNode} structure.
	 *
	 * @param previousNodes           A List of {@link CommandNode}s to add this argument onto.
	 * @param rootNode                The {@link CommandNode} representing this argument.
	 * @param previousArguments       A List of CommandAPI arguments that came before this argument.
	 * @param previousArgumentNames   A List of Strings containing the node names that came before this argument.
	 * @param terminalExecutorCreator A function that transforms the list of {@code previousArguments} into an
	 *                                appropriate Brigadier {@link Command} which should be applied at the end
	 *                                of the node structure. This parameter can be null to indicate that this
	 *                                argument should not be executable.
	 * @param <Source>                The Brigadier Source object for running commands.
	 * @return The list of last nodes in the Brigadier {@link CommandNode} structure representing this Argument. Note
	 * that this is not necessarily the {@code rootNode} for this argument, since the Brigadier node structure may
	 * contain multiple nodes in a chain. This might happen if {@link #combineWith(AbstractArgument[])} was called
	 * for this argument to merge it with other arguments.
	 */
	public <Source> List<CommandNode<Source>> linkNode(
		List<CommandNode<Source>> previousNodes, CommandNode<Source> rootNode,
		List<Argument> previousArguments, List<String> previousArgumentNames,
		Function<List<Argument>, Command<Source>> terminalExecutorCreator
	) {
		// Add rootNode to the previous nodes
		for(CommandNode<Source> previousNode : previousNodes) {
			previousNode.addChild(rootNode);
		}

		// A GreedyArgument cannot have arguments after it
		previousNodes = this instanceof GreedyArgument ? List.of() : List.of(rootNode);
		// Stack on combined arguments
		previousNodes = stackArguments(combinedArguments, previousNodes, previousArguments, previousArgumentNames, terminalExecutorCreator);

		// Return last nodes
		return previousNodes;
	}

	/**
	 * Builds a linear Brigadier {@link CommandNode} structure using 
	 * CommandAPI arguments. Only the last argument will be executable.
	 *
	 * @param argumentsToStack        A List of CommandAPI arguments to put in sequence
	 * @param previousNodes           A List of {@link CommandNode}s that start the stack.
	 * @param previousArguments       A List of CommandAPI arguments that came before the {@code argumentsToStack}.
	 * @param previousArgumentNames   A List of Strings containing the node names that came before the {@code argumentsToStack}.
	 * @param terminalExecutorCreator A function that transforms the list of {@code previousArguments} into an
	 *                                appropriate Brigadier {@link Command} which should be applied to the last 
	 *                                stacked argument of the node structure. This parameter can be null to indicate 
	 *                                that the argument stack should not be executable.
	 * @param <Source>                The Brigadier Source object for running commands.
	 * @return The list of last nodes in the Brigadier {@link CommandNode} structure representing the built argument stack.
	 */
	public static <Argument extends AbstractArgument<?, ?, Argument, ?>, Source> List<CommandNode<Source>> stackArguments(
		List<Argument> argumentsToStack, List<CommandNode<Source>> previousNodes,
		List<Argument> previousArguments, List<String> previousArgumentNames,
		Function<List<Argument>, Command<Source>> terminalExecutorCreator
	) {
		int lastIndex = argumentsToStack.size() - 1;
		for (int i = 0; i < argumentsToStack.size(); i++) {
			Argument subArgument = argumentsToStack.get(i);
			previousNodes = subArgument.addArgumentNodes(previousNodes, previousArguments, previousArgumentNames,
				// Only apply the `terminalExecutor` to the last argument
				i == lastIndex ? terminalExecutorCreator : null);
		}
		return previousNodes;
	}
}
