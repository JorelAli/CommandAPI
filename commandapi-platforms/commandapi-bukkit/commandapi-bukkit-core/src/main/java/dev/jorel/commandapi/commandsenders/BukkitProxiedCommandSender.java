package dev.jorel.commandapi.commandsenders;
import org.bukkit.command.ProxiedCommandSender;

public class BukkitProxiedCommandSender implements AbstractPlayer<ProxiedCommandSender>, BukkitCommandSender<ProxiedCommandSender> {

	private final ProxiedCommandSender proxySender;
	
	public BukkitProxiedCommandSender(ProxiedCommandSender player) {
		this.proxySender = player;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.proxySender.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		return this.proxySender.isOp();
	}

	@Override
	public ProxiedCommandSender getSource() {
		return this.proxySender;
	}
	
}
