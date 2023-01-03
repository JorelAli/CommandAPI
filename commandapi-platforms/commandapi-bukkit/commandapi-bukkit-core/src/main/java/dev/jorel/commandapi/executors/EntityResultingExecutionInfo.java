package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitEntity;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface EntityResultingExecutionInfo extends ResultingExecutor<Entity, BukkitEntity> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 */
	int run(ExecutionInfo<Entity, BukkitEntity> info) throws WrapperCommandSyntaxException;

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
