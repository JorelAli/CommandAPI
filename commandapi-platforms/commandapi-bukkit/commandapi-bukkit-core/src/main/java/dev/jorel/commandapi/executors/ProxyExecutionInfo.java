package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitNativeProxyCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;

@FunctionalInterface
public interface ProxyExecutionInfo extends IExecutorNormal<NativeProxyCommandSender, BukkitNativeProxyCommandSender> {

	/**
	 * Executes the command.
	 *
	 * @param info The AbstractExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(AbstractExecutionInfo<NativeProxyCommandSender, BukkitNativeProxyCommandSender> info) throws WrapperCommandSyntaxException;

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
