package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;
import org.bukkit.command.CommandSender;

public class CommandTree extends AbstractCommandTree<CommandTree, Argument<?>, CommandSender> implements BukkitExecutable<CommandTree> {
	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandTree(String commandName) {
		super(commandName);
	}

	@Override
	public CommandTree instance() {
		return this;
	}
}
