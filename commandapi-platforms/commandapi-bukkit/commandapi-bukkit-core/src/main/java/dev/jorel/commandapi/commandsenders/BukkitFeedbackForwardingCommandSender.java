package dev.jorel.commandapi.commandsenders;

import org.bukkit.command.CommandSender;

public class BukkitFeedbackForwardingCommandSender<FeedbackForwardingSender extends CommandSender> implements AbstractFeedbackForwardingCommandSender<FeedbackForwardingSender>, BukkitCommandSender<FeedbackForwardingSender> {

	private final FeedbackForwardingSender sender;
	
	public BukkitFeedbackForwardingCommandSender(FeedbackForwardingSender sender) {
		this.sender = sender;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.sender.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		return this.sender.isOp();
	}

	@Override
	public FeedbackForwardingSender getSource() {
		return this.sender;
	}
	
}
