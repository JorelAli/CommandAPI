package dev.jorel.commandapi.commandsenders;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;

public class BukkitNativeProxyCommandSender implements AbstractPlayer<NativeProxyCommandSender>, BukkitCommandSender<NativeProxyCommandSender> {

	private final NativeProxyCommandSender proxySender;
	
	public BukkitNativeProxyCommandSender(NativeProxyCommandSender player) {
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
	public NativeProxyCommandSender getSource() {
		return this.proxySender;
	}
	
}
