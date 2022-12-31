package dev.jorel.commandapi.executors;

import com.velocitypowered.api.proxy.ConsoleCommandSource;
import dev.jorel.commandapi.commandsenders.VelocityConsoleCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface ConsoleResultingExecutionInfo extends ResultingExecutor<ConsoleCommandSource, VelocityConsoleCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 * @throws WrapperCommandSyntaxException
	 */
	int run(ExecutionInfo<ConsoleCommandSource, VelocityConsoleCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}
}
