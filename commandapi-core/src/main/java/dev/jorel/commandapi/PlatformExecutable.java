package dev.jorel.commandapi;

import dev.jorel.commandapi.commandsenders.AbstractCommandSender;

public interface PlatformExecutable<Impl extends PlatformExecutable<Impl, CommandSender>, CommandSender> extends ChainableBuilder<Impl> {
	// Automatically links to Executable#getExecutor (make sure it has the same signature)
	CommandAPIExecutor<CommandSender, AbstractCommandSender<? extends CommandSender>> getExecutor();
}
