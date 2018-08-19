package io.github.jorelali.commandapi.api;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandExecutor {

	void run(CommandSender sender, Object[] args); 
	
}
