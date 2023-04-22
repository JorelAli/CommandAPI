package dev.jorel.commandapi.commandsenders;
import org.bukkit.entity.Player;

public class BukkitPlayer implements AbstractPlayer<Player>, BukkitCommandSender<Player> {

	private final Player player;
	
	public BukkitPlayer(Player player) {
		this.player = player;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.player.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		return this.player.isOp();
	}

	@Override
	public Player getSource() {
		return this.player;
	}
	
}
