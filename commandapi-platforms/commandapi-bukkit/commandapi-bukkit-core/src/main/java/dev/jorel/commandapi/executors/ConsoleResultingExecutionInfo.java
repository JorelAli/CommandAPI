package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitConsoleCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.ConsoleCommandSender;

@FunctionalInterface
public interface ConsoleResultingExecutionInfo extends IExecutorResulting<ConsoleCommandSender, BukkitConsoleCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The AbstractExecutionInfo for this command
	 * @return the result of this command
	 */
	int run(AbstractExecutionInfo<ConsoleCommandSender, BukkitConsoleCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}
}
