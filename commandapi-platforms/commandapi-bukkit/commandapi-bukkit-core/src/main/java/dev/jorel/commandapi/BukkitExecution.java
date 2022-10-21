package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.arguments.Argument;
import org.bukkit.command.CommandSender;

import java.util.List;

public class BukkitExecution extends Execution<CommandSender> {
	public BukkitExecution(List<Argument<?, CommandSender>> arguments, CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor) {
		super(arguments, executor);
	}

	@Override
	protected AbstractCommandAPICommand<?, CommandSender> newConcreteCommandAPICommand(CommandMetaData meta) {
		return new CommandAPICommand(meta);
	}

	@Override
	protected Execution<CommandSender> newConcreteExecution(List<Argument<?, CommandSender>> arguments, CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor) {
		return new BukkitExecution(arguments, executor);
	}
}
