package dev.jorel.commandapi.executors;

import org.bukkit.command.ConsoleCommandSender;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A resulting command executor for a ConsoleCommandSender
 */
@FunctionalInterface
public interface ConsoleResultingCommandExecutor extends IExecutorResulting<ConsoleCommandSender> {

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
	int run(ConsoleCommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

	@Override
	default ExecutorType getType() {
		return ExecutorType.CONSOLE;
	}
}
