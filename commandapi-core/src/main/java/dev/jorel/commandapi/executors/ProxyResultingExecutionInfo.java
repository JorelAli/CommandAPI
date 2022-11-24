package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;

import java.util.Map;

public interface ProxyResultingExecutionInfo extends IExecutorResulting<NativeProxyCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param sender  the command sender for this command
	 * @param args    the arguments provided to this command
	 * @param argsMap the arguments provided to this command mapped to their node names. This uses a LinkedHashMap
	 * @return
	 * @throws WrapperCommandSyntaxException
	 */
	@Override
	int run(NativeProxyCommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.PROXY;
	}
}
