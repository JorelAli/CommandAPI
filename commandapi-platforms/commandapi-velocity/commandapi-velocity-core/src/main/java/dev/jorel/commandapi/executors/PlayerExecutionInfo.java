package dev.jorel.commandapi.executors;

import com.velocitypowered.api.proxy.Player;
import dev.jorel.commandapi.commandsenders.VelocityPlayer;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface PlayerExecutionInfo extends NormalExecutor<Player, VelocityPlayer> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(ExecutionInfo<Player, VelocityPlayer> info) throws WrapperCommandSyntaxException;

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
