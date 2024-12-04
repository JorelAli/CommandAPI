package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.AbstractArgument;

import java.util.ArrayList;
import java.util.Arrays;
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

	final List<AbstractArgumentTree<?, Argument, CommandSender>> arguments = new ArrayList<>();
	final Argument argument;

	/**
	 * Instantiates an {@link AbstractArgumentTree}. This can only be called if the class
	 * that extends this is an {@link AbstractArgument}
	 */
	@SuppressWarnings("unchecked")
	protected AbstractArgumentTree() {
		if (this instanceof AbstractArgument<?, ?, Argument, CommandSender>) {
			this.argument = (Argument) this;
		} else {
			throw new IllegalArgumentException("Implicit inherited constructor must be from Argument");
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

	/**
	 * Creates a chain of child branches starting at this node
	 * <p>
	 * {@code thenNested(a, b, c)} is equivalent to {@link #then}{@code (a.then(b.then(c)))}.
	 *
	 * @param trees The child branches to add in a chain.
	 * @return this tree node
	 */
	public final Impl thenNested(List<AbstractArgumentTree<?, Argument, CommandSender>> trees) {
		int length = trees.size();
		if (length == 0) {
			return instance();
		}

		AbstractArgumentTree<?, Argument, CommandSender> combined = trees.get(length - 1);
		for (int i = length - 2; i >= 0; i--) {
			combined = trees.get(i).then(combined);
		}

		return then(combined);
	}

	/**
	 * Creates a chain of child branches starting at this node
	 * <p>
	 * {@code thenNested(a, b, c)} is equivalent to {@link #then}{@code (a.then(b.then(c)))}.
	 *
	 * @param trees The child branches to add in a chain.
	 * @return this tree node
	 */
	@SafeVarargs
	public final Impl thenNested(final AbstractArgumentTree<?, Argument, CommandSender>... trees) {
		return thenNested(Arrays.asList(trees));
	}

	List<Execution<CommandSender, Argument>> getExecutions() {
		List<Execution<CommandSender, Argument>> executions = new ArrayList<>();
		// If this is executable, add its execution
		if (this.executor.hasAnyExecutors()) {
			executions.add(new Execution<>(List.of(this.argument), this.executor));
		}
		// Add all executions from all arguments
		for (AbstractArgumentTree<?, Argument, CommandSender> tree : arguments) {
			for (Execution<CommandSender, Argument> execution : tree.getExecutions()) {
				// Prepend this argument to the arguments of the executions
				executions.add(execution.prependedBy(this.argument));
			}
		}
		return executions;
	}
}
