package dev.jorel.commandapi;

import org.bukkit.command.CommandSender;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, CommandSender> implements BukkitExecutable<CommandAPICommand> {
	
	public CommandAPICommand(CommandMetaData meta) {
		super(meta);
	}
	
	public CommandAPICommand(String commandName) {
		super(commandName);
	}

	@Override
	protected CommandAPICommand newConcreteCommandAPICommand(CommandMetaData metaData) {
		return new CommandAPICommand(metaData);
	}
}
