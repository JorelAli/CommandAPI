package dev.jorel.commandapi.executors;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import dev.jorel.commandapi.commandsenders.VelocityConsoleCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface ConsoleExecutionInfo extends NormalExecutor<ConsoleCommandSource, VelocityConsoleCommandSender> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(ExecutionInfo<ConsoleCommandSource, VelocityConsoleCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}

}
