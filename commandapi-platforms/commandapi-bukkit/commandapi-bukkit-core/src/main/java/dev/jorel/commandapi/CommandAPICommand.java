package dev.jorel.commandapi;

import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.executors.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, CommandSender> implements BukkitExecutable<CommandAPICommand> {
	
	public CommandAPICommand(CommandMetaData meta) {
		super(meta);
	}
	
	public CommandAPICommand(String commandName) {
		super(commandName);
	}

	@Override
	public CommandAPICommand copy() {
		CommandAPICommand command = new CommandAPICommand(new CommandMetaData(this.meta));
		command.args = new ArrayList<>(this.args);
		command.subcommands = new ArrayList<>(this.subcommands);
		command.isConverted = this.isConverted;
		return command;
	}
}
