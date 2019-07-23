package io.github.jorelali.commandapi.api;

import io.github.jorelali.commandapi.api.exceptions.WrapperCommandSyntaxException;
import org.bukkit.command.CommandSender;

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
	int run(CommandSender sender, Object[] args) throws WrapperCommandSyntaxException;

}
