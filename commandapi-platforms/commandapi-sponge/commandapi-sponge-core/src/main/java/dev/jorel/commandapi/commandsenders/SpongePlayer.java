package dev.jorel.commandapi.commandsenders;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

public class SpongePlayer implements AbstractPlayer<CommandCause>, SpongeCommandSender<CommandCause> {

	private final CommandCause player;
	
	public SpongePlayer(CommandCause player) {
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
	public CommandCause getSource() {
		return this.player;
	}
	
}
