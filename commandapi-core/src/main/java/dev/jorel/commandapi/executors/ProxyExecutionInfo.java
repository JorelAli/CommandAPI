package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;

import java.util.Map;

public interface ProxyExecutionInfo extends IExecutorNormal<NativeProxyCommandSender> {

	/**
	 * Executes the command.
	 *
	 * @param sender  the command sender for this command
	 * @param args    the arguments provided to this command
	 * @param argsMap the arguments provided to this command mapped to their node names. This uses a LinkedHashMap
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	@Override
	void run(NativeProxyCommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.PROXY;
	}

}
