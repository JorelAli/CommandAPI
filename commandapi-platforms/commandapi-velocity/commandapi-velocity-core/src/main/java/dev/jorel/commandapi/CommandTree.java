package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
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
		if (!namespace.isEmpty() && !CommandAPIHandler.NAMESPACE_PATTERN.matcher(namespace).matches()) {
			super.register();
			return;
		}
		super.register(namespace);
	}

	/**
	 * Registers the command with the given plugin object
	 *
	 * @param plugin The plugin instance used to determine this command's namespace
	 */
	public void register(Object plugin) {
		ProxyServer server = CommandAPIVelocity.getConfiguration().getServer();
		if (server.getPluginManager().fromInstance(plugin).isEmpty()) {
			CommandAPI.logInfo("Using the default namespace to register commands since " + plugin.getClass().getSimpleName() + " is not a Velocity plugin!");
			super.register();
			return;
		}
		super.register(server.getPluginManager().fromInstance(plugin).get().getDescription().getId());
	}

	@Override
	public CommandTree instance() {
		return this;
	}
}
