package dev.jorel.commandapi.executors;

import org.bukkit.command.ConsoleCommandSender;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A normal command executor for a ConsoleCommandSender
 */
@FunctionalInterface
public interface ConsoleCommandExecutor extends IExecutorNormal<ConsoleCommandSender> {

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
	void run(ConsoleCommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}
}
