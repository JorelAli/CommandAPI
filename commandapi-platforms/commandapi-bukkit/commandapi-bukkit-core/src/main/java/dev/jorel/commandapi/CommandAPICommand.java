package dev.jorel.commandapi;

import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.executors.*;
import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;

import java.util.ArrayList;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, CommandSender> implements BukkitExecutor<CommandAPICommand> {
	
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

	@Override
	public void addNormalExecutor(IExecutorNormal<? extends CommandSender, ? extends BukkitCommandSender<? extends CommandSender>> executor) {
		this.executor.addNormalExecutor(executor);
	}

	@Override
	public void addResultingExecutor(IExecutorResulting<? extends CommandSender, ? extends BukkitCommandSender<? extends CommandSender>> executor) {
		this.executor.addResultingExecutor(executor);
	}
}
