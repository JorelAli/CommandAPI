package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.VelocityCommandSender;

/**
 * This record represents a VelocityExecutionInfo for a command. It provides the sender of a command, as well as it's arguments
 *
 * @param <Sender> The type of the sender of a command this BukkitExecutionInfo belongs to
 */
public record VelocityExecutionInfo<Sender>(

	/**
	 * @return The sender of this command
	 */
	Sender sender,

	/**
	 * This is not intended for public use and is only used internally. The {@link BukkitExecutionInfo#sender()} method should be used instead!
	 *
	 * @return The wrapper type of this command
	 */
	VelocityCommandSender<? extends Sender> senderWrapper,

	/**
	 * @return The arguments of this command
	 */
	CommandArguments args

) implements ExecutionInfo<Sender, VelocityCommandSender<? extends Sender>> {
}
