package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.BukkitRemoteConsoleCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.RemoteConsoleCommandSender;

@FunctionalInterface
public interface RemoteConsoleCommandExecutor extends NormalExecutor<RemoteConsoleCommandSender, BukkitRemoteConsoleCommandSender> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param sender The sender of this command (a player, the console etc.)
	 * @param args The arguments given to this command.
	 */
	void run(RemoteConsoleCommandSender sender, CommandArguments args) throws WrapperCommandSyntaxException;

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	@Override
	default void run(ExecutionInfo<RemoteConsoleCommandSender, BukkitRemoteConsoleCommandSender> info) throws WrapperCommandSyntaxException {
		this.run(info.sender(), info.args());
	}

	/**
	 * Returns the type of the sender of the current executor.
	 *
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.REMOTE;
	}
}
