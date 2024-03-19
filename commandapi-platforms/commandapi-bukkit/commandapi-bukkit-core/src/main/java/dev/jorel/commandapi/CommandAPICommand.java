package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, Argument<?>, CommandSender> implements BukkitExecutable<CommandAPICommand> {
	/**
	 * Creates a new command builder
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		super(commandName);
	}

	@Override
	public CommandAPICommand instance() {
		return this;
	}
	
	/**
	 * Sets the {@link HelpTopic} for this command. Using this method will override
	 * any declared short description, full description or usage description provided
	 * via the following methods:
	 * <ul>
	 *   <li>{@link CommandAPICommand#withShortDescription(String)}</li>
	 *   <li>{@link CommandAPICommand#withFullDescription(String)}</li>
	 *   <li>{@link CommandAPICommand#withUsage(String...)}</li>
	 *   <li>{@link CommandAPICommand#withHelp(String, String)}</li>
	 * </ul>
	 * @param helpTopic the help topic to use for this command
	 * @return this command builder
	 */
	public CommandAPICommand withHelp(HelpTopic helpTopic) {
		this.helpTopic = Optional.of(helpTopic);
		return instance();
	}

	/**
	 * Registers this command with the given namespace.
	 *
	 * @param namespace The namespace for this command. This cannot be null or empty, and can only contain 0-9, a-z, underscores, periods, and hyphens.
	 * @throws NullPointerException if the namespace is null.
	 */
	@Override
	public void register(String namespace) {
		super.register(namespace);
	}

	/**
	 * Registers this command with a given {@link JavaPlugin} instance
	 *
	 * @param plugin The plugin instance used to determine this command's namespace
	 */
	public void register(JavaPlugin plugin) {
		super.register(plugin.getName().toLowerCase());
	}
}
