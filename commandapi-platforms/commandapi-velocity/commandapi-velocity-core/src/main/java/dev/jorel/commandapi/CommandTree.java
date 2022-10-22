package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;

public class CommandTree extends AbstractCommandTree<CommandTree, CommandSource> implements VelocityExecutable<CommandTree> {
	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandTree(String commandName) {
		super(commandName);
	}
}
