package dev.jorel.commandapi;

import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.executors.IExecutorNormal;
import dev.jorel.commandapi.executors.IExecutorResulting;
import org.bukkit.command.CommandSender;

public class CommandTree extends AbstractCommandTree<CommandTree, CommandSender> implements BukkitExecutor<CommandTree> {
	/**
	 * Creates a main root node for a command tree with a given command name
	 *
	 * @param commandName The name of the command to create
	 */
	public CommandTree(String commandName) {
		super(commandName);
	}

	@Override
	public void addNormalExecutor(IExecutorNormal<? extends CommandSender, ? extends BukkitCommandSender<? extends CommandSender>> executor) {
		this.executor.addNormalExecutor(executor);
	}

	@Override
	public void addResultingExecutor(IExecutorResulting<? extends CommandSender, ? extends BukkitCommandSender<? extends CommandSender>> executor) {
		this.executor.addResultingExecutor(executor);
	}
}
