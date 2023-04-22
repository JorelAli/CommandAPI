package dev.jorel.commandapi.executors;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.commandsenders.VelocityCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface CommandExecutionInfo extends NormalExecutor<CommandSource, VelocityCommandSender<? extends CommandSource>> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(ExecutionInfo<CommandSource, VelocityCommandSender<? extends CommandSource>> info) throws WrapperCommandSyntaxException;

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
