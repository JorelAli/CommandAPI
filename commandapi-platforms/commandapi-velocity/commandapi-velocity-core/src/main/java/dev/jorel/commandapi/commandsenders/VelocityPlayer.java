package dev.jorel.commandapi.commandsenders;
import com.velocitypowered.api.proxy.Player;

import dev.jorel.commandapi.abstractions.AbstractPlayer;

public class VelocityPlayer extends AbstractPlayer<Player> {

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
