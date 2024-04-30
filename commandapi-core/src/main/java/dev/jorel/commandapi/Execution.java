package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of arguments which results in an execution. This is used for building branches in a {@link AbstractCommandTree}
 */
public class Execution<CommandSender, Argument
/// @cond DOX
extends AbstractArgument<?, ?, Argument, CommandSender>
/// @endcond
> {

	private final List<Argument> arguments;
	private final CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor;

	public Execution(List<Argument> arguments, CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> executor) {
		this.arguments = arguments;
		this.executor = executor;
	}

	/**
	 * Register a command with the given arguments and executor to brigadier, by converting it into a {@link AbstractCommandAPICommand}
	 *
	 * @param meta The metadata to register the command with
	 * @param namespace The namespace to use for this command
	 */
	public void register(CommandMetaData<CommandSender> meta, String namespace) {
		@SuppressWarnings("unchecked")
		CommandAPIPlatform<Argument, CommandSender, ?> platform = (CommandAPIPlatform<Argument, CommandSender, ?>) CommandAPIHandler.getInstance().getPlatform();
		AbstractCommandAPICommand<?, Argument, CommandSender> command = platform.newConcreteCommandAPICommand(meta);
		command.withArguments(this.arguments);
		command.setExecutor(this.executor);
		command.register(namespace);
	}

	public Execution<CommandSender, Argument> prependedBy(Argument argument) {
		List<Argument> args = new ArrayList<>();
		args.add(argument);
		args.addAll(this.arguments);
		return new Execution<>(args, this.executor);
	}
}
