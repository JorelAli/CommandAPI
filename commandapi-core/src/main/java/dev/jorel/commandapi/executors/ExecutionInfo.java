package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;

/**
 * This interface represents an ExecutionInfo for a command. It provides the sender of a command, as well as it's arguments
 *
 * @param <Sender> The type of the sender of a command this ExecutionInfo belongs to
 */
public interface ExecutionInfo<Sender, WrapperType extends AbstractCommandSender<? extends Sender>> {

	/**
	 * @return The sender of this command
	 */
	Sender sender();

	/**
	 * This is not intended for public use and is only used internally. The {@link ExecutionInfo#sender()} method should be used instead!
	 *
	 * @return The wrapper type of this command
	 */
	WrapperType senderWrapper();

	/**
	 * @return The arguments of this command
	 */
	CommandArguments args();

}
