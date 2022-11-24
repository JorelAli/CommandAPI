package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.entity.Entity;

import java.util.Map;

public interface EntityResultingExecutionInfo extends IExecutorResulting<Entity> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param sender The sender of this command (a player, the console etc.)
	 * @param args The arguments given to this command.
	 * @param argsMap the arguments provided to this command mapped to their node names. This uses a LinkedHashMap
	 * @return the result of this command
	 */
	@Override
	int run(Entity sender, Object[] args, Map<String, Object> argsMap) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.ENTITY;
	}
}
