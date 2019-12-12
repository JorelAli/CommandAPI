package io.github.jorelali.commandapi.api.executors;

import org.bukkit.command.CommandSender;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface ResultingCommandExecutor extends IExecutorResulting<CommandSender> {

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
	int run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}
}
