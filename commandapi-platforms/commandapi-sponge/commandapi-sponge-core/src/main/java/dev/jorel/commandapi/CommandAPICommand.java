package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, Argument<?>, CommandSource> implements SpongeExecutable<CommandAPICommand> {
	/**
	 * Creates a new command builder
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandAPICommand(String commandName) {
		super(commandName);
	}

	/**
	 * Creates a new Command builder
	 *
	 * @param metaData The metadata of the command to create
	 */
	protected CommandAPICommand(CommandMetaData<CommandSource> metaData) {
		super(metaData);
	}

	@Override
	protected CommandAPICommand newConcreteCommandAPICommand(CommandMetaData<CommandSource> meta) {
		return new CommandAPICommand(meta);
	}

	@Override
	public CommandAPICommand instance() {
		return this;
	}
}
