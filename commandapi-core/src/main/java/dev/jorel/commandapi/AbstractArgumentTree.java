package dev.jorel.commandapi;

import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a base class for arguments, allowing them to behave as tree nodes in
 * a {@link AbstractCommandTree}
 * @param <Impl>The class extending this class, used as the return type for chain calls
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractArgumentTree<Impl
/// @cond DOX
extends AbstractArgumentTree<Impl, Argument, CommandSender>
/// @endcond
, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> extends Executable<Impl, CommandSender> {

	private final Argument argument;
	private List<AbstractArgumentTree<?, Argument, CommandSender>> arguments = new ArrayList<>();

	/**
	 * Instantiates an {@link AbstractArgumentTree}. This can only be called if the class
	 * that extends this is an {@link AbstractArgument}
	 */
	@SuppressWarnings("unchecked")
	protected AbstractArgumentTree() {
		if (this instanceof AbstractArgument<?, ?, ?, ?>) {
			this.argument = (Argument) this;
		} else {
			throw new IllegalArgumentException("Implicit inherited constructor must be from AbstractArgument");
		}
	}

	/**
	 * Instantiates an {@link AbstractArgumentTree} with an underlying argument.
	 *
	 * @param argument the argument to use as the underlying argument for this
	 *                 argument tree
	 */
	protected AbstractArgumentTree(final Argument argument) {
		this.argument = argument;
		// Copy the executor in case any executions were defined on the argument
		this.executor = argument.executor;
	}

	/////////////////////
	// Builder methods //
	/////////////////////

	/**
	 * Create a child branch on this node
	 *
	 * @param tree The child branch
	 * @return this tree node
	 */
	public Impl then(final AbstractArgumentTree<?, Argument, CommandSender> tree) {
		this.arguments.add(tree);
		return instance();
	}

	/////////////////////////
	// Getters and setters //
	/////////////////////////

	/**
	 * @return The child branches added to this tree by {@link #then(AbstractArgumentTree)}.
	 */
	public List<AbstractArgumentTree<?, Argument, CommandSender>> getArguments() {
		return arguments;
	}

	/**
	 * Sets the child branches that this node has
	 *
	 * @param arguments A new list of branches for this node
	 */
	public void setArguments(List<AbstractArgumentTree<?, Argument, CommandSender>> arguments) {
		this.arguments = arguments;
	}

	//////////////////
	// Registration //
	//////////////////

	/**
	 * @return A list of paths that represent the possible branches of this argument tree as Strings, starting with the
	 * base argument held by this tree.
	 */
	public List<List<String>> getBranchesAsStrings() {
		List<List<String>> argumentStrings = new ArrayList<>();

		// Create paths for the argument at this node
		List<List<String>> baseArgumentPaths = new ArrayList<>();
		baseArgumentPaths.add(new ArrayList<>());
		argument.appendToCommandPaths(baseArgumentPaths);

		// If this node is executable, add it as a valid path
		if (this.executor.hasAnyExecutors()) argumentStrings.addAll(baseArgumentPaths);

		// Add paths for the branches
		for (AbstractArgumentTree<?, Argument, CommandSender> child : arguments) {
			for (List<String> subArgs : child.getBranchesAsStrings()) {
				for (List<String> basePath : baseArgumentPaths) {
					List<String> mergedPaths = new ArrayList<>();
					mergedPaths.addAll(basePath);
					mergedPaths.addAll(subArgs);
					argumentStrings.add(mergedPaths);
				}
			}
		}

		return argumentStrings;
	}

	/**
	 * Builds the Brigadier {@link CommandNode} structure for this argument tree.
	 *
	 * @param previousNodes         A List of {@link CommandNode}s to add this argument onto.
	 * @param previousArguments     A List of CommandAPI arguments that came before this argument tree.
	 * @param previousArgumentNames A List of Strings containing the node names that came before this argument.
	 * @param <Source>              The Brigadier Source object for running commands.
	 */
	public <Source> void buildBrigadierNode(
		List<CommandNode<Source>> previousNodes,
		List<Argument> previousArguments, List<String> previousArgumentNames
	) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// Check preconditions
		if (!executor.hasAnyExecutors() && arguments.isEmpty()) {
			// If we don't have any executors then no branches is bad because this path can't be run at all
			throw new MissingCommandExecutorException(previousArguments, argument);
		}

		// Create node for this argument
		previousNodes = argument.addArgumentNodes(previousNodes, previousArguments, previousArgumentNames,
			executor.hasAnyExecutors() ? args -> handler.generateBrigadierCommand(args, executor) : null);

		// Add our branches as children to the node
		for (AbstractArgumentTree<?, Argument, CommandSender> child : arguments) {
			// We need a new list for each branch of the tree
			List<Argument> newPreviousArguments = new ArrayList<>(previousArguments);
			List<String> newPreviousArgumentNames = new ArrayList<>(previousArgumentNames);

			child.buildBrigadierNode(previousNodes, newPreviousArguments, newPreviousArgumentNames);
		}
	}
}
