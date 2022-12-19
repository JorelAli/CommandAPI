package dev.jorel.commandapi.commandsenders;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SpongePlayer extends AbstractPlayer<ServerPlayer> implements SpongeCommandSender<ServerPlayer> {

	private final ServerPlayer player;
	
	public SpongePlayer(ServerPlayer player) {
		this.player = player;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.player.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		// Sponge doesn't have the concept of "being op". There is only
		// permissions
		return false;
	}

	@Override
	public ServerPlayer getSource() {
		return this.player;
	}
	
}
