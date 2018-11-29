package io.github.jorelali.commandapi.api;

import org.bukkit.command.CommandSender;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface ResultingCommandExecutor {

	/**
	 * The code to run when this command is performed
	 * 
	 * @param sender
	 *            The sender of this command (a player, the console etc.)
	 * @param args
	 *            The arguments given to this command. The objects are
	 *            determined by the hashmap of arguments IN THE ORDER of
	 *            insertion into the hashmap
	 */
	int run(CommandSender sender, Object[] args) throws CommandSyntaxException;

}
