package dev.jorel.commandapi;

import com.velocitypowered.api.command.CommandSource;
import dev.jorel.commandapi.arguments.Argument;

public class CommandAPICommand extends AbstractCommandAPICommand<CommandAPICommand, Argument<?>, CommandSource> implements VelocityExecutable<CommandAPICommand> {
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
	protected CommandAPICommand newConcreteCommandAPICommand(CommandMetaData<CommandSource> metaData) {
		return new CommandAPICommand(metaData);
	}

	@Override
	public CommandAPICommand instance() {
		return this;
	}
}
