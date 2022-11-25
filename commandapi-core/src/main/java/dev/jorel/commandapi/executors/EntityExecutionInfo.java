package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.entity.Entity;

import java.util.Map;

@FunctionalInterface
public interface EntityExecutionInfo extends IExecutorNormal<Entity> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(ExecutionInfo<Entity> info) throws WrapperCommandSyntaxException;

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
