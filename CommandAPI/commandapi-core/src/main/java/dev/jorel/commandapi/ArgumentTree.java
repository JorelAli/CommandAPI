package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;

import java.util.LinkedList;
import java.util.List;

public class ArgumentTree extends Executable<ArgumentTree> {

	final List<Execution> executions = new LinkedList<>();
	final Argument argument;

	protected ArgumentTree() {
		if(!(this instanceof Argument argument)) {
			throw new IllegalArgumentException("Implicit inherited constructor must be from Argument");
		}
		this.argument = argument;
	}

	public ArgumentTree(final Argument argument) {
		this.argument = argument;
		//Copy the executor in case any executions were defined on the argument
		this.executor = argument.executor;
	}

	public ArgumentTree then(final ArgumentTree tree) {

		//Add all of the executions
		for(Execution execution : tree.executions) {
			//Prepend this argument to the arguments of the executions
			execution.arguments().add(0, this.argument);
			this.executions.add(execution);
		}
		if(!tree.executor.isEmpty()) {
			this.executions.add(new Execution(List.of(this.argument, tree.argument), tree.executor));
		}
		return this;
	}

}
