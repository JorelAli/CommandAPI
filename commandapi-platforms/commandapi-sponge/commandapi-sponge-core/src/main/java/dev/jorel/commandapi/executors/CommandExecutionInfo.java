package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.SpongeCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.spongepowered.api.command.CommandCause;

@FunctionalInterface
public interface CommandExecutionInfo extends NormalExecutor<CommandCause, SpongeCommandSender<? extends CommandCause>> {

	/**
	 * Executes the command.
	 *
	 * @param info The ExecutionInfo for this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	@Override
	void run(ExecutionInfo<CommandCause, SpongeCommandSender<? extends CommandCause>> info) throws WrapperCommandSyntaxException;

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
