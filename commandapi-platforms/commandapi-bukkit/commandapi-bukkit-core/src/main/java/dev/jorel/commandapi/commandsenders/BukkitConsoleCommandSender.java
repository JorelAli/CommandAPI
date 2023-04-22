package dev.jorel.commandapi.commandsenders;

import org.bukkit.command.ConsoleCommandSender;

public class BukkitConsoleCommandSender implements AbstractConsoleCommandSender<ConsoleCommandSender>, BukkitCommandSender<ConsoleCommandSender> {

	private final ConsoleCommandSender sender;

	public BukkitConsoleCommandSender(ConsoleCommandSender sender) {
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
	public ConsoleCommandSender getSource() {
		return sender;
	}

}
