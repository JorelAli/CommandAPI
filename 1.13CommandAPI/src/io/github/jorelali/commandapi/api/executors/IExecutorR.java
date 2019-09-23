package io.github.jorelali.commandapi.api.executors;

import org.bukkit.command.CommandSender;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;

public interface IExecutorR<T extends CommandSender> {

	 int run(T sender, Object[] args) throws WrapperCommandSyntaxException;
	
}
