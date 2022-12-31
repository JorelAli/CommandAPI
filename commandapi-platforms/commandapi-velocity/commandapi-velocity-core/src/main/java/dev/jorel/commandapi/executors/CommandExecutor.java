package dev.jorel.commandapi.executors;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.commandsenders.VelocityCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

/**
 * A normal CommandExecutor for a CommandSource
 */
@FunctionalInterface
public interface CommandExecutor extends NormalExecutor<CommandSource, VelocityCommandSender<? extends CommandSource>> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param sender The sender of this command (a player, the console etc.)
	 * @param args The arguments given to this command.
	 */
	void run(CommandSource sender, CommandArguments args) throws WrapperCommandSyntaxException;

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 */
	@Override
	default void run(ExecutionInfo<CommandSource, VelocityCommandSender<? extends CommandSource>> info) throws WrapperCommandSyntaxException {
		this.run(info.sender(), info.args());
	}

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.ALL;
	}
}
