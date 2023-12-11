package dev.jorel.commandapi.commandsenders;

import org.bukkit.command.RemoteConsoleCommandSender;

public class BukkitRemoteConsoleCommandSender implements AbstractRemoteConsoleCommandSender<RemoteConsoleCommandSender>, BukkitCommandSender<RemoteConsoleCommandSender> {

	private final RemoteConsoleCommandSender remote;

	public BukkitRemoteConsoleCommandSender(RemoteConsoleCommandSender remote) {
		this.remote = remote;
	}

	@Override
	public boolean hasPermission(String permissionNode) {
		return remote.hasPermission(permissionNode);
	}

	@Override
	public boolean isOp() {
		return remote.isOp();
	}

	@Override
	public RemoteConsoleCommandSender getSource() {
		return remote;
	}
}
