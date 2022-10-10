package dev.jorel.commandapi.commandsenders;

import org.bukkit.command.CommandSender;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;

public class BukkitCommandSender implements AbstractCommandSender<CommandSender> {

	private final CommandSender sender;

	public BukkitCommandSender(CommandSender sender) {
		// TODO: We prooooooobably want to create the other Bukkit wrappers instead of using this
		this.sender = sender;
	}

	@Override
	public boolean hasPermission(String permissionNode) {
		return sender.hasPermission(permissionNode);
	}

	@Override
	public boolean isOp() {
		return sender.isOp();
	}

	@Override
	public CommandSender getSource() {
		return sender;
	}

}
