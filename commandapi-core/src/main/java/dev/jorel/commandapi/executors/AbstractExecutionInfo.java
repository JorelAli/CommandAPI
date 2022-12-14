package dev.jorel.commandapi.executors;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;

/**
 * This interface represents an AbstractExecutionInfo for a command. It provides the sender of a command, as well as it's arguments
 *
 * @param <Sender> The type of the sender of a command this AbstractExecutionInfo belongs to
 */
public interface AbstractExecutionInfo<Sender, WrapperType extends AbstractCommandSender<? extends Sender>> {

	/**
	 * @return The sender of this command
	 */
	Sender sender();

	/**
	 * @return The arguments of this command
	 */
	CommandArguments args();

}
