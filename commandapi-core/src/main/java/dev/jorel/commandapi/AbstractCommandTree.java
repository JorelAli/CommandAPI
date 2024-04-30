package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.AbstractArgument;

import java.util.ArrayList;
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

	private final List<AbstractArgumentTree<?, Argument, CommandSender>> arguments = new ArrayList<>();

	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	protected AbstractCommandTree(final String commandName) {
		super(commandName);
	}

	/**
	 * Create a child branch on the tree
	 *
	 * @param tree the child node
	 * @return this root node
	 */
	public Impl then(final AbstractArgumentTree<?, Argument, CommandSender> tree) {
		this.arguments.add(tree);
		return instance();
	}

	/**
	 * Registers the command with a given namespace
	 *
	 * @param namespace The namespace of this command. This cannot be null
	 * @throws NullPointerException if the namespace is null
	 */
	@Override
	public void register(String namespace) {
		if (namespace == null) {
			// Only reachable through Velocity
			throw new NullPointerException("Parameter 'namespace' was null when registering command /" + this.meta.commandName + "!");
		}
		List<Execution<CommandSender, Argument>> executions = new ArrayList<>();
		if (this.executor.hasAnyExecutors()) {
			executions.add(new Execution<>(List.<Argument>of(), this.executor));
		}
		for (AbstractArgumentTree<?, Argument, CommandSender> tree : arguments) {
			executions.addAll(tree.getExecutions());
		}
		for (Execution<CommandSender, Argument> execution : executions) {
			execution.register(this.meta, namespace);
		}
	}
}
