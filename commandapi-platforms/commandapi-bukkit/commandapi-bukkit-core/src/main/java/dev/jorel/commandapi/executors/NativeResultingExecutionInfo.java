package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitNativeProxyCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;

public interface NativeResultingExecutionInfo extends IExecutorResulting<NativeProxyCommandSender, BukkitNativeProxyCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The AbstractExecutionInfo for this command
	 * @return the result of this command
	 */
	int run(AbstractExecutionInfo<NativeProxyCommandSender, BukkitNativeProxyCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.NATIVE;
	}
}
