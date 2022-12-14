package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandExecutionInfo extends IExecutorNormal<CommandSender, BukkitCommandSender<? extends CommandSender>> {

	/**
	 * Executes the command.
	 *
	 * @param info The AbstractExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(AbstractExecutionInfo<CommandSender, BukkitCommandSender<? extends CommandSender>> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}

}
