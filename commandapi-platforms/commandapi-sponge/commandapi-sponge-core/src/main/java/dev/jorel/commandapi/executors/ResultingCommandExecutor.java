package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.SpongeCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.spongepowered.api.command.CommandCause;

@FunctionalInterface
public interface ResultingCommandExecutor extends ResultingExecutor<CommandCause, SpongeCommandSender<? extends CommandCause>> {
	/**
	 * Executes the command.
	 *
	 * @param commandSource the CommandSource for this command
	 * @param args          the arguments provided to this command
	 * @return the value returned by this command if the command succeeds, 0 if the command fails
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	int run(CommandCause commandSource, CommandArguments args) throws WrapperCommandSyntaxException;

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 * @throws WrapperCommandSyntaxException
	 */
	@Override
	default int run(ExecutionInfo<CommandCause, SpongeCommandSender<? extends CommandCause>> info) throws WrapperCommandSyntaxException {
		return this.run(info.sender(), info.args());
	}

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
