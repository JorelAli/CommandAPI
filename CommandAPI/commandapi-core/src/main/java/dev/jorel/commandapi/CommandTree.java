package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the root node for creating a command as a tree
 */
public class CommandTree extends ExecutableCommand<CommandTree> {

	private final List<Execution> executions = new ArrayList<>();

	public CommandTree(final String commandName) {
		super(commandName);
	}

	/**
	 * Create a child branch on the tree
	 * @param tree the child node
	 * @return this root node
	 */
	public CommandTree then(final ArgumentTree tree) {
		executions.addAll(tree.executions);
		if(tree.executor.hasAnyExecutors()) {
			this.executions.add(new Execution(List.of(tree.argument), tree.executor));
		}
		return this;
	}

	/**
	 * Registers the command
	 */
	public void register() {
		if(this.executor.hasAnyExecutors()) {
			executions.add(new Execution(List.of(), this.executor));
		}
		for(Execution execution : executions) {
			execution.register(this.meta);	
		}
	}

}
