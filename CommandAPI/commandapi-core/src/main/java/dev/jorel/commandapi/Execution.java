package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.Argument;

import java.util.List;

record Execution(List<Argument> arguments, CustomCommandExecutor executor) {

	public void register(CommandMetaData meta) {
		CommandAPICommand command = new CommandAPICommand(meta).withArguments(arguments);
		command.setExecutor(executor);
		command.register();
	}

}
