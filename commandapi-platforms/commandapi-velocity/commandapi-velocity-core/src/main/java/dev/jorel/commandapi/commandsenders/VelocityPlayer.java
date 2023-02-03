package dev.jorel.commandapi.commandsenders;
import com.velocitypowered.api.proxy.Player;

public class VelocityPlayer implements AbstractPlayer<Player>, VelocityCommandSender<Player> {

	private final Player player;
	
	public VelocityPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.player.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public Player getSource() {
		return this.player;
	}
	
}
