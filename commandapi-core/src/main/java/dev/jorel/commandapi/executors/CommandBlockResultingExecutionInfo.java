package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.BlockCommandSender;
import java.util.Map;

public interface CommandBlockResultingExecutionInfo extends IExecutorResulting<BlockCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param sender
	 *            The sender of this command (a player, the console etc.)
	 * @param args
	 *            The arguments given to this command. The objects are
	 *            determined by the hashmap of arguments IN THE ORDER of
	 *            insertion into the hashmap
	 * @return the result of this command
	 */
	int run(BlockCommandSender sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.BLOCK;
	}

}
