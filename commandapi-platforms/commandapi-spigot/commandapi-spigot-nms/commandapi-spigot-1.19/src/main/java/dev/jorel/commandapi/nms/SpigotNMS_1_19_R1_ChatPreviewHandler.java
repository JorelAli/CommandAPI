package dev.jorel.commandapi.nms;

import dev.jorel.commandapi.CommandAPISpigot;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.protocol.game.ClientboundChatPreviewPacket;
import net.minecraft.network.protocol.game.ServerboundChatPreviewPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotNMS_1_19_R1_ChatPreviewHandler extends SpigotNMS_1_19_Common_ChatPreviewHandler {

	public SpigotNMS_1_19_R1_ChatPreviewHandler(CommandAPISpigot<CommandSourceStack> platform, Plugin plugin, Player player) {
		super(platform, plugin, player);
	}

	@Override
	protected void handleChatPreviewPacket(ServerboundChatPreviewPacket chatPreview) {
		// We want to run this synchronously, just in case there's some funky async stuff going on here
		Bukkit.getScheduler().runTask(this.plugin, () -> this.connection.send(
			new ClientboundChatPreviewPacket(chatPreview.queryId(), parseChatPreviewQuery(chatPreview.query()))
		));
	}
}
