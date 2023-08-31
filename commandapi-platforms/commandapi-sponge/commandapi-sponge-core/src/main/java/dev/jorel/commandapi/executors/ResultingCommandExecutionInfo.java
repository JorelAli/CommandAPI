package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.SpongeCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.spongepowered.api.command.CommandCause;

public interface ResultingCommandExecutionInfo extends ResultingExecutor<CommandCause, SpongeCommandSender<? extends CommandCause>> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 * @throws WrapperCommandSyntaxException
	 */
	@Override
	int run(ExecutionInfo<CommandCause, SpongeCommandSender<? extends CommandCause>> info) throws WrapperCommandSyntaxException;

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
