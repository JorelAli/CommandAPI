package dev.jorel.commandapi.commandsenders;
import org.bukkit.command.BlockCommandSender;

import dev.jorel.commandapi.abstractions.AbstractPlayer;

public class BukkitBlockCommandSender extends AbstractPlayer<BlockCommandSender> {

	private final BlockCommandSender commandBlock;
	
	public BukkitBlockCommandSender(BlockCommandSender commandBlock) {
		this.commandBlock = commandBlock;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.commandBlock.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		return this.commandBlock.isOp();
	}

	@Override
	public BlockCommandSender getSource() {
		return this.commandBlock;
	}
	
}
