package dev.jorel.commandapi;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;

public interface IPlatformExecutable<CommandSender> {
	// Automatically links to Executable#getExecutor (make sure it has the same signature)
	CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> getExecutor();


}
