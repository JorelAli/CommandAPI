package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitConsoleCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.ConsoleCommandSender;

@FunctionalInterface
public interface ConsoleExecutionInfo extends IExecutorNormal<ConsoleCommandSender, BukkitConsoleCommandSender> {

	/**
	 * Executes the command.
	 *
	 * @param info The AbstractExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(AbstractExecutionInfo<ConsoleCommandSender, BukkitConsoleCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}

}
