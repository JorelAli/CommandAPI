package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitRemoteConsoleCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.RemoteConsoleCommandSender;

@FunctionalInterface
public interface RemoteConsoleResultingExecutionInfo extends ResultingExecutor<RemoteConsoleCommandSender, BukkitRemoteConsoleCommandSender> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the value returned by this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	int run(ExecutionInfo<RemoteConsoleCommandSender, BukkitRemoteConsoleCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.REMOTE;
	}
}
