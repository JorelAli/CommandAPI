package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.commandsenders.BukkitFeedbackForwardingCommandSender;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

@FunctionalInterface
public interface FeedbackForwardingResultingExecutionInfo extends ResultingExecutor<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>> {

	/**
	 * The code to run when this command is performed
	 *
	 * @param info The ExecutionInfo for this command
	 * @return the result of this command
	 */
	int run(ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>> info) throws WrapperCommandSyntaxException;

	/**
	 * Returns the type of the sender of the current executor.
	 * @return the type of the sender of the current executor
	 */
	@Override
	default ExecutorType getType() {
		return ExecutorType.FEEDBACK_FORWARDING;
	}

}
