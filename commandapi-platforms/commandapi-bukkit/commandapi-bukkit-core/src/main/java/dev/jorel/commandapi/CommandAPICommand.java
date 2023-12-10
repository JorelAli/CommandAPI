package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, Argument<?>, CommandSender> implements BukkitExecutable<CommandAPICommand> {
	
	public CommandAPICommand(CommandMetaData<CommandSender> meta) {
		super(meta);
	}
	
	public CommandAPICommand(String commandName) {
		super(commandName);
	}

	@Override
	protected CommandAPICommand newConcreteCommandAPICommand(CommandMetaData<CommandSender> metaData) {
		return new CommandAPICommand(metaData);
	}

	@Override
	public CommandAPICommand instance() {
		return this;
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
