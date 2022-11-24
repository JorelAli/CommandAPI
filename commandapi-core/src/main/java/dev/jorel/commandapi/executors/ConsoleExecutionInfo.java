package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Map;

public interface ConsoleExecutionInfo extends IExecutorNormal<ConsoleCommandSender> {

	/**
	 * Executes the command.
	 *
	 * @param sender  the command sender for this command
	 * @param args    the arguments provided to this command
	 * @param argsMap the arguments provided to this command mapped to their node names. This uses a LinkedHashMap
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	@Override
	void run(ConsoleCommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException;

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
