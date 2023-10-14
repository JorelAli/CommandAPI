package dev.jorel.commandapi;

import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.exceptions.MissingCommandExecutorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the root node for creating a command as a tree
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <Argument> The implementation of AbstractArgument being used by this class
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractCommandTree<Impl
/// @cond DOX
extends AbstractCommandTree<Impl, Argument, CommandSender>
/// @endcond
, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
, CommandSender> extends ExecutableCommand<Impl, CommandSender> {

	private List<AbstractArgumentTree<?, Argument, CommandSender>> branches = new ArrayList<>();
	private List<Argument> optionalArguments = new ArrayList<>();

	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	protected AbstractCommandTree(final String commandName) {
		super(commandName);
	}

	/////////////////////
	// Builder methods //
	/////////////////////

	/**
	 * Create a child branch on the tree
	 *
	 * @param tree the child node
	 * @return this root node
	 */
	public Impl then(final AbstractArgumentTree<?, Argument, CommandSender> tree) {
		this.branches.add(tree);
		return instance();
	}

	/**
	 * Adds optional arguments onto this node.
	 *
	 * @param optionalArguments A List of Arguments to add as optional arguments at this node.
	 * @return this command builder
	 */
	public Impl withOptionalArguments(List<Argument> optionalArguments) {
		this.optionalArguments.addAll(optionalArguments);
		return instance();
	}

	/**
	 * Adds optional arguments onto this node.
	 *
	 * @param optionalArguments The Arguments to add as optional arguments at this node.
	 * @return this command builder
	 */
	@SafeVarargs
	public final Impl withOptionalArguments(Argument... optionalArguments) {
		return this.withOptionalArguments(Arrays.asList(optionalArguments));
	}

	/////////////////////////
	// Getters and setters //
	/////////////////////////

	/**
	 * @return The child branches added to this tree by {@link #then(AbstractArgumentTree)}.
	 */
	public List<AbstractArgumentTree<?, Argument, CommandSender>> getArguments() {
		return branches;
	}

	/**
	 * Sets the child branches that this command has
	 *
	 * @param arguments A new list of branches for this command
	 */
	public void setArguments(List<AbstractArgumentTree<?, Argument, CommandSender>> arguments) {
		this.branches = arguments;
	}

	/**
	 * @return The optional arguments added to this tree by {@link #withOptionalArguments(List)}.
	 */
	public List<Argument> getOptionalArguments() {
		return optionalArguments;
	}

	/**
	 * Sets the optional arguments that this command has
	 *
	 * @param optionalArguments A new list of optional arguments for this command
	 */
	public void setOptionalArguments(List<Argument> optionalArguments) {
		this.optionalArguments = optionalArguments;
	}

	//////////////////
	// Registration //
	//////////////////

	@Override
	public List<List<String>> getArgumentsAsStrings() {
		// Return an empty list if we have no arguments
		if (branches.isEmpty() && optionalArguments.isEmpty()) return List.of(List.of());

		List<List<String>> argumentStrings = new ArrayList<>();

		// Build optional argument paths, if it is executable
		if (executor.hasAnyExecutors()) {
			List<List<String>> currentPaths = new ArrayList<>();
			currentPaths.add(new ArrayList<>());

			// Just the command is a valid path
			List<Integer> slicePositions = new ArrayList<>();
			// Note: Assumption that all paths are the same length
			slicePositions.add(0);

			// Each optional argument is a potential stopping point
			for (Argument argument : optionalArguments) {
				argument.appendToCommandPaths(currentPaths);
				slicePositions.add(currentPaths.get(0).size());
			}

			// Return each path as sublists of the main path
			for (List<String> path : currentPaths) {
				for (int slicePos : slicePositions) {
					argumentStrings.add(path.subList(0, slicePos));
				}
			}
		}

		// Add branching paths
		for (AbstractArgumentTree<?, Argument, CommandSender> argument : branches) {
			argumentStrings.addAll(argument.getBranchesAsStrings());
		}

		return argumentStrings;
	}

	@Override
	protected void checkPreconditions() {
		if (!executor.hasAnyExecutors() && (branches.isEmpty() || !optionalArguments.isEmpty())) {
			// If we don't have any executors then:
			//  No branches is bad because this path can't be run at all
			//  Having arguments is bad because developer intended this path to be executable with arguments
			throw new MissingCommandExecutorException(name);
		}
	}

	@Override
	protected boolean isRootExecutable() {
		return executor.hasAnyExecutors();
	}

	@Override
	protected <Source> void createArgumentNodes(LiteralCommandNode<Source> rootNode) {
		CommandAPIHandler<Argument, CommandSender, Source> handler = CommandAPIHandler.getInstance();

		// The previous arguments include an unlisted MultiLiteral representing the command name and aliases
		//  This doesn't affect how the command acts, but it helps represent the command path in exceptions
		String[] literals = new String[aliases.length + 1];
		literals[0] = name;
		System.arraycopy(aliases, 0, literals, 1, aliases.length);
		Argument commandNames = handler.getPlatform().newConcreteMultiLiteralArgument(name, literals);
		commandNames.setListed(false);

		// Build branches
		for (AbstractArgumentTree<?, Argument, CommandSender> argument : branches) {
			// We need new previousArguments lists for each branch so they don't interfere
			List<Argument> previousArguments = new ArrayList<>();
			List<String> previousNonLiteralArgumentNames = new ArrayList<>();
			previousArguments.add(commandNames);

			argument.buildBrigadierNode(rootNode, previousArguments, previousNonLiteralArgumentNames);
		}

		// Build optional argument paths
		if (!optionalArguments.isEmpty()) {
			CommandNode<Source> previousNode = rootNode;
			List<Argument> previousArguments = new ArrayList<>();
			List<String> previousNonLiteralArgumentNames = new ArrayList<>();
			previousArguments.add(commandNames);

			for (Argument argument : optionalArguments) {
				// All optional arguments are executable
				previousNode = argument.addArgumentNodes(previousNode, previousArguments, previousNonLiteralArgumentNames, executor);
			}
		}
	}
}
