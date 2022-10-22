package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.arguments.Argument;

import java.util.List;

public class VelocityExecution extends Execution<CommandSource> {
	public VelocityExecution(List<Argument<?, ?, CommandSource>> arguments, CustomCommandExecutor<CommandSource, AbstractCommandSender<? extends CommandSource>> executor) {
		super(arguments, executor);
	}

	@Override
	protected AbstractCommandAPICommand<?, CommandSource> newConcreteCommandAPICommand(CommandMetaData meta) {
		return new CommandAPICommand(meta);
	}

	@Override
	protected Execution<CommandSource> newConcreteExecution(List<Argument<?, ?, CommandSource>> arguments, CustomCommandExecutor<CommandSource, AbstractCommandSender<? extends CommandSource>> executor) {
		return new VelocityExecution(arguments, executor);
	}
}
