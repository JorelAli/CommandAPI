package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

@FunctionalInterface
public interface PlayerExecutionInfo extends IExecutorNormal<Player> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(ExecutionInfo<Player> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.PLAYER;
	}
}
