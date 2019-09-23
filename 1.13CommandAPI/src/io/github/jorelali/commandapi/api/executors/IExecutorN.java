package io.github.jorelali.commandapi.api.executors;

import org.bukkit.command.CommandSender;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;

public interface IExecutorN<T extends CommandSender> extends IExecutor {
	
	void run(T sender, Object[] args) throws WrapperCommandSyntaxException;

}
