package dev.jorel.commandapi.nms;

import io.netty.channel.Channel;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PaperNMS_1_19_R1 extends PaperNMS_1_19_Common {

	private NMS_1_19_R1 bukkitNMS;

	@Override
	protected void hookChatPreview(Plugin plugin, Player player) {
		final Channel playerChannel = ((CraftPlayer) player).getHandle().connection.connection.channel;
		if (playerChannel.pipeline().get("CommandAPI_" + player.getName()) == null) {
			playerChannel.pipeline().addBefore("packet_handler", "CommandAPI_" + player.getName(), new PaperNMS_1_19_R1_ChatPreviewHandler(this, plugin, player));
		}
	}

	@Override
	public NMS<?> bukkitNMS() {
		if (bukkitNMS == null) {
			bukkitNMS = new NMS_1_19_R1();
		}
		return bukkitNMS;
	}

}
