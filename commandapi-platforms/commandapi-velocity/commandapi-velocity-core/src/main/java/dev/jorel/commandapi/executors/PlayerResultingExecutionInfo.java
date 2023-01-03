package dev.jorel.commandapi.executors;

import com.velocitypowered.api.proxy.Player;
import dev.jorel.commandapi.commandsenders.VelocityPlayer;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface PlayerResultingExecutionInfo extends ResultingExecutor<Player, VelocityPlayer> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 * @throws WrapperCommandSyntaxException
	 */
	int run(ExecutionInfo<Player, VelocityPlayer> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}
}
