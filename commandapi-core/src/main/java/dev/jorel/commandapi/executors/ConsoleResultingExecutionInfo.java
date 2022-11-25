package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Map;

@FunctionalInterface
public interface ConsoleResultingExecutionInfo extends IExecutorResulting<ConsoleCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 */
	int run(ExecutionInfo<ConsoleCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}
}
