package dev.jorel.commandapi;

import java.util.ArrayList;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.executors.CommandBlockCommandExecutor;
import dev.jorel.commandapi.executors.CommandBlockResultingCommandExecutor;
import dev.jorel.commandapi.executors.CommandExecutor;
import dev.jorel.commandapi.executors.ConsoleCommandExecutor;
import dev.jorel.commandapi.executors.ConsoleResultingCommandExecutor;
import dev.jorel.commandapi.executors.EntityCommandExecutor;
import dev.jorel.commandapi.executors.EntityResultingCommandExecutor;
import dev.jorel.commandapi.executors.ExecutorType;
import dev.jorel.commandapi.executors.NativeCommandExecutor;
import dev.jorel.commandapi.executors.NativeResultingCommandExecutor;
import dev.jorel.commandapi.executors.PlayerCommandExecutor;
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor;
import dev.jorel.commandapi.executors.ProxyCommandExecutor;
import dev.jorel.commandapi.executors.ProxyResultingCommandExecutor;
import dev.jorel.commandapi.executors.ResultingCommandExecutor;

/**
 * This class represents something that is executable. This is mostly, {@link CommandAPICommand} instances, or can also be {@link CommandTree} nodes and even {@link Argument} nodes in a tree
 *
 * @param <T> return type for chain calls
 */
abstract class Executable<T extends Executable<T>> {

	/**
	 * The CustomCommandExecutor for this executable implementation
	 */
	protected CustomCommandExecutor<?> executor = new CustomCommandExecutor<>();

	/**
	 * Returns the executors that this command has
	 * @return the executors that this command has
	 */
	public CustomCommandExecutor<? extends AbstractCommandSender> getExecutor() {
		return executor;
	}

	/**
	 * Sets the executors for this command
	 * @param executor the executors for this command
	 */
	public void setExecutor(CustomCommandExecutor<? extends AbstractCommandSender> executor) {
		this.executor = executor;
	}

	/**
	 * Clear all executors from the current command builder
	 * @return this command builder
	 */
	@SuppressWarnings("unchecked")
	public T clearExecutors() {
		this.executor.setNormalExecutors(new ArrayList<>());
		this.executor.setResultingExecutors(new ArrayList<>());
		return (T) this;
	}

}
