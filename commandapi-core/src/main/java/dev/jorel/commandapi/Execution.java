package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.arguments.Argument;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of arguments which results in an execution. This is used for building branches in a {@link CommandTree}
 */
record Execution(List<Argument<?>> arguments, CustomCommandExecutor<? extends AbstractCommandSender> executor) {

	/**
	 * Register a command with the given arguments and executor to brigadier, by converting it into a {@link CommandAPICommand}
	 * @param meta The metadata to register the command with
	 */
	public void register(CommandMetaData meta) {
		CommandAPICommand command = new CommandAPICommand(meta).withArguments(arguments);
		command.setExecutor(executor);
		command.register();
	}

	public Execution prependedBy(Argument<?> argument) {
		List<Argument<?>> arguments = new ArrayList<>();
		arguments.add(argument);
		arguments.addAll(arguments());
		return new Execution(arguments, executor);
	}

}
