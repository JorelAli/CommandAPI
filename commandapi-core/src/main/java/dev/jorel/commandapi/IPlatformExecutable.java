package dev.jorel.commandapi;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;

public interface IPlatformExecutable<Impl extends IPlatformExecutable<Impl, CommandSender>, CommandSender> extends IChainableBuilder<Impl> {
	// Automatically links to Executable#getExecutor (make sure it has the same signature)
	CustomCommandExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> getExecutor();
}
