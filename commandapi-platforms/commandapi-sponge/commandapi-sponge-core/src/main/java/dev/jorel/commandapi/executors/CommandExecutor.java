package dev.jorel.commandapi.executors;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.commandsenders.VelocityCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface CommandExecutor extends NormalExecutor<CommandSource, VelocityCommandSender<? extends CommandSource>> {
	/**
	 * Executes the command.
	 *
	 * @param commandSource the CommandSource for this command
	 * @param args          the arguments provided to this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	@Override
	void run(CommandSource commandSource, Object[] args) throws WrapperCommandSyntaxException;

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
