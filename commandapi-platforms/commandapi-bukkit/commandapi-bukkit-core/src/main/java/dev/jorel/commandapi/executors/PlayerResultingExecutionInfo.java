package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitPlayer;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.entity.Player;

public interface PlayerResultingExecutionInfo extends IExecutorResulting<Player, BukkitPlayer> {

	/**
	 * @param info The AbstractExecutionInfo for this command
	 * @return the result of this command
	 * @throws WrapperCommandSyntaxException
	 */
	int run(AbstractExecutionInfo<Player, BukkitPlayer> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.PLAYER;
	}
}
