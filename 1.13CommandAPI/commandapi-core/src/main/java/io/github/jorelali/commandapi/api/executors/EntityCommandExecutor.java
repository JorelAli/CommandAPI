package io.github.jorelali.commandapi.api.executors;

import org.bukkit.entity.Entity;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface EntityCommandExecutor extends IExecutorNormal<Entity> {

	/**
	 * The code to run when this command is performed
	 * 
	 * @param sender
	 *            The sender of this command (a player, the console etc.)
	 * @param args
	 *            The arguments given to this command. The objects are
	 *            determined by the hashmap of arguments IN THE ORDER of
	 *            insertion into the hashmap
	 */
	void run(Entity sender, Object[] args) throws WrapperCommandSyntaxException;

	@Override
	default ExecutorType getType() {
		return ExecutorType.ENTITY;
	}
}
