package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractPlatform;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the root node for creating a command as a tree
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
public abstract class AbstractCommandTree<Impl extends AbstractCommandTree<Impl, CommandSender>, CommandSender> extends ExecutableCommand<Impl, CommandSender> {

	private final List<AbstractArgumentTree<?, CommandSender>> arguments = new ArrayList<>();

	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	public AbstractCommandTree(final String commandName) {
		super(commandName);
	}

	/**
	 * Create a child branch on the tree
	 *
	 * @param tree the child node
	 * @return this root node
	 */
	public Impl then(final AbstractArgumentTree<?, CommandSender> tree) {
		this.arguments.add(tree);
		return instance();
	}

	/**
	 * Registers the command
	 */
	public void register() {
		List<Execution<CommandSender>> executions = new ArrayList<>();
		if (this.executor.hasAnyExecutors()) {
			// Cast platform so that it realizes we're using the same CommandSender and executor is accepted
			AbstractPlatform<CommandSender, ?> platform = (AbstractPlatform<CommandSender, ?>) BaseHandler.getInstance().getPlatform();
			executions.add(platform.newConcreteExecution(new ArrayList<>(), this.executor));
		}
		for (AbstractArgumentTree<?, CommandSender> tree : arguments) {
			executions.addAll(tree.getExecutions());
		}
		for (Execution<CommandSender> execution : executions) {
			execution.register(this.meta);
		}
	}
}
