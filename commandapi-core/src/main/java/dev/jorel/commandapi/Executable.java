package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.arguments.Argument;

import java.util.ArrayList;

/**
 * This class represents something that is executable. This is mostly, {@link AbstractCommandAPICommand} instances, or can also be {@link AbstractCommandTree} nodes and even {@link Argument} nodes in a tree
 *
 * @param <Impl> The class extending this class, used as the return type for chain calls
 * @param <CommandSender> The CommandSender class used by the class extending this class
 */
abstract class Executable<Impl extends Executable<Impl, CommandSender>, CommandSender> implements IChainableBuilder<Impl> {

	/**
	 * The CustomCommandExecutor for this executable implementation
	 */
	protected CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor = new CustomCommandExecutor<>();

	/**
	 * Returns the executors that this command has
	 * @return the executors that this command has
	 */
	public CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> getExecutor() {
		return executor;
	}

	/**
	 * Sets the executors for this command
	 * @param executor the executors for this command
	 */
	public void setExecutor(CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor) {
		this.executor = executor;
	}

	/**
	 * Clear all executors from the current command builder
	 * @return this command builder
	 */
	public Impl clearExecutors() {
		this.executor.setNormalExecutors(new ArrayList<>());
		this.executor.setResultingExecutors(new ArrayList<>());
		return instance();
	}

}
