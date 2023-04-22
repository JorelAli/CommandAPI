package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitBlockCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.BlockCommandSender;

@FunctionalInterface
public interface CommandBlockResultingExecutionInfo extends ResultingExecutor<BlockCommandSender, BukkitBlockCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 */
	int run(ExecutionInfo<BlockCommandSender, BukkitBlockCommandSender> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.BLOCK;
	}

}
