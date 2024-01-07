package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.arguments.Argument;

public class CommandTree extends AbstractCommandTree<CommandTree, Argument<?>, CommandSource> implements VelocityExecutable<CommandTree> {
	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandTree(String commandName) {
		super(commandName);
	}

	/**
	 * Registers the command with a given namespace
	 *
	 * @param namespace The namespace of this command. This cannot be null or empty
	 *
	 */
	public void register(String namespace) {
		if (!CommandAPIHandler.getInstance().namespacePattern.matcher(namespace).matches()) {
			super.register();
			return;
		}
		super.register(namespace);
	}

	@Override
	public CommandTree instance() {
		return this;
	}
}
