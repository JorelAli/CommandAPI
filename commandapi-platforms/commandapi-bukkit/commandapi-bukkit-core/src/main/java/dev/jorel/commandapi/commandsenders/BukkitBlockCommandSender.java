package dev.jorel.commandapi.commandsenders;

import org.bukkit.command.BlockCommandSender;

public class BukkitBlockCommandSender implements AbstractBlockCommandSender<BlockCommandSender>, BukkitCommandSender<BlockCommandSender> {

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
