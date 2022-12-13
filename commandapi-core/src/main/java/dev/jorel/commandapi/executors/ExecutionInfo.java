package dev.jorel.commandapi.executors;

import org.bukkit.command.CommandSender;

/**
 * This record represents an ExecutionInfo for a command. It provides the sender of a command, as well as it's arguments
 *
 * @param sender The sender for a command this ExecutionInfo belongs to
 * @param args The arguments for a command this ExecutionInfo belongs to
 * @param <Sender> The type of the sender of a command this ExecutionInfo belongs to
 */
public record ExecutionInfo<Sender extends CommandSender>(
	/**
	 * @return The sender of this command
	 */
	Sender sender,

	/**
	 * @return The arguments of this command
	 */
	CommandArguments args
) {}
