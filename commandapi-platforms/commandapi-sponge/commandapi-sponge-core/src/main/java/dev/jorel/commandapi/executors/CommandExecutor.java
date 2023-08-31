package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.SpongeCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import org.spongepowered.api.command.CommandCause;

@FunctionalInterface
public interface CommandExecutor extends NormalExecutor<CommandCause, SpongeCommandSender<? extends CommandCause>> {
	/**
	 * Executes the command.
	 *
	 * @param commandCause the CommandCause for this command
	 * @param args          the arguments provided to this command
	 * @throws WrapperCommandSyntaxException if an error occurs during the execution of this command
	 */
	void run(CommandCause commandCause, CommandArguments args) throws WrapperCommandSyntaxException;

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 */
	@Override
	default void run(ExecutionInfo<CommandCause, SpongeCommandSender<? extends CommandCause>> info) throws WrapperCommandSyntaxException {
		this.run(info.sender(), info.args());
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
