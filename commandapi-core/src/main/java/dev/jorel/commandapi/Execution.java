package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.arguments.Argument;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of arguments which results in an execution. This is used for building branches in a {@link CommandTree}
 */
public abstract class Execution<CommandSender> {

	private final List<Argument<?>> arguments;
	private final CustomCommandExecutor<CommandSender, ? extends AbstractCommandSender<? extends CommandSender>> executor;

	public Execution(List<Argument<?>> arguments, CustomCommandExecutor<CommandSender, ? extends AbstractCommandSender<? extends CommandSender>> executor) {
		this.arguments = arguments;
		this.executor = executor;
	}

	/**
	 * Register a command with the given arguments and executor to brigadier, by converting it into a {@link AbstractCommandAPICommand}
	 *
	 * @param meta The metadata to register the command with
	 */
	public void register(CommandMetaData meta) {
		AbstractCommandAPICommand<?, CommandSender> command = newConcreteCommandAPICommand(meta);
		command.withArguments(arguments);
		command.setExecutor(executor);
		command.register();
	}

	protected abstract AbstractCommandAPICommand<?, CommandSender> newConcreteCommandAPICommand(CommandMetaData meta);

	public Execution<CommandSender> prependedBy(Argument<?> argument) {
		List<Argument<?>> arguments = new ArrayList<>();
		arguments.add(argument);
		arguments.addAll(this.arguments);
		return newConcreteExecution(arguments, executor);
	}

	protected abstract Execution<CommandSender> newConcreteExecution(List<Argument<?>> arguments, CustomCommandExecutor<CommandSender, ? extends AbstractCommandSender<? extends CommandSender>> executor);
}
