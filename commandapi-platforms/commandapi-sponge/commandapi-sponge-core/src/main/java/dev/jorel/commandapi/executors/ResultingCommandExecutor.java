package dev.jorel.commandapi.executors;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.commandsenders.VelocityCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface ResultingCommandExecutor extends ResultingExecutor<CommandSource, VelocityCommandSender<? extends CommandSource>> {
	/**
	 * Executes the command.
	 *
	 * @param commandSource the CommandSource for this command
	 * @param args          the arguments provided to this command
	 * @return the value returned by this command if the command succeeds, 0 if the command fails
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	@Override
	int run(CommandSource commandSource, Object[] args) throws WrapperCommandSyntaxException;

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
