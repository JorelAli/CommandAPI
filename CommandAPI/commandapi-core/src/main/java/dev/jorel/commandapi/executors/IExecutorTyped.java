package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * An interface that includes the type of an executor (what command senders it
 * can execute) and has a method that executes an executor with a given command
 * sender and arguments
 */
public interface IExecutorTyped {
	
	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}
	
	/**
	 * Executes the command executor with the provided command sender and the provided arguments.
	 * @param sender the command sender for this command
	 * @param args the arguments provided to this command
	 * @return the value returned by this command if the command succeeds, 0 if the command fails
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	int executeWith(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

}
