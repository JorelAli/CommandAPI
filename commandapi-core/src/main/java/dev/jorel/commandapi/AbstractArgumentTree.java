package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractPlatform;
import dev.jorel.commandapi.arguments.Argument;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a base class for arguments, allowing them to behave as tree nodes in
 * a {@link AbstractCommandTree}
 * @param <Impl>The class extending this class, used as the return type for chain calls
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractArgumentTree<Impl extends AbstractArgumentTree<Impl, CommandSender>, CommandSender> extends Executable<Impl, CommandSender> {

	final List<AbstractArgumentTree<?, CommandSender>> arguments = new ArrayList<>();
	final Argument<?, ?, CommandSender> argument;

	/**
	 * Instantiates an {@link AbstractArgumentTree}. This can only be called if the class
	 * that extends this is an {@link Argument}
	 */
	protected AbstractArgumentTree() {
		if (!(this instanceof Argument<?, ?, CommandSender> argument)) {
			throw new IllegalArgumentException("Implicit inherited constructor must be from Argument");
		}
		this.argument = argument;
	}

	/**
	 * Instantiates an {@link AbstractArgumentTree} with an underlying argument.
	 * 
	 * @param argument the argument to use as the underlying argument for this
	 *                 argument tree
	 */
	public AbstractArgumentTree(final Argument<?, ?, CommandSender> argument) {
		this.argument = argument;
		// Copy the executor in case any executions were defined on the argument
		this.executor = argument.executor;
	}

	/**
	 * Create a child branch on this node
	 * 
	 * @param tree The child branch
	 * @return this tree node
	 */
	public Impl then(final AbstractArgumentTree<?, CommandSender> tree) {
		this.arguments.add(tree);
		return (Impl) this;
	}

	List<Execution<CommandSender>> getExecutions() {
		List<Execution<CommandSender>> executions = new ArrayList<>();
		// If this is executable, add its execution
		if (this.executor.hasAnyExecutors()) {
			// Cast platform to make it realize we're using the same CommandSender
			AbstractPlatform<CommandSender, ?> platform = (AbstractPlatform<CommandSender, ?>) BaseHandler.getInstance().getPlatform();
			executions.add(platform.newConcreteExecution(List.of(this.argument), this.executor));
		}
		// Add all executions from all arguments
		for (AbstractArgumentTree<?, CommandSender> tree : arguments) {
			for (Execution<CommandSender> execution : tree.getExecutions()) {
				// Prepend this argument to the arguments of the executions
				executions.add(execution.prependedBy(this.argument));
			}
		}
		return executions;
	}
}
