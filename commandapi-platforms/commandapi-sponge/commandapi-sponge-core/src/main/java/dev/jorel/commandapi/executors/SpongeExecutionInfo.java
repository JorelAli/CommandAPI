package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.SpongeCommandSender;

/**
 * This record represents a SpongeExecutionInfo for a command. It provides the sender of a command, as well as it's arguments
 *
 * @param <Sender> The type of the sender of a command this SpongeExecutionInfo belongs to
 */
public record SpongeExecutionInfo<Sender>(

	/**
	 * @return The sender of this command
	 */
	Sender sender,

	/**
	 * This is not intended for public use and is only used internally. The {@link SpongeExecutionInfo#sender()} method should be used instead!
	 *
	 * @return The wrapper type of this command
	 */
	SpongeCommandSender<? extends Sender> senderWrapper,

	/**
	 * @return The arguments of this command
	 */
	CommandArguments args

) implements ExecutionInfo<Sender, SpongeCommandSender<? extends Sender>> {
}
