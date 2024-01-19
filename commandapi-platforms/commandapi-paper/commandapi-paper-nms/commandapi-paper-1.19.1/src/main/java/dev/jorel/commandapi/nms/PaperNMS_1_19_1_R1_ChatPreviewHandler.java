package dev.jorel.commandapi.nms;

import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.SafeVarHandle;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.ChatPreviewCache;
import net.minecraft.network.chat.ChatPreviewThrottler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundChatPreviewPacket;
import net.minecraft.network.protocol.game.ServerboundChatPreviewPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;

public class PaperNMS_1_19_1_R1_ChatPreviewHandler extends PaperNMS_1_19_Common_ChatPreviewHandler {

	private static final SafeVarHandle<ServerGamePacketListenerImpl, ChatPreviewThrottler> packetListenerPreviewThrottler;
	private static final SafeVarHandle<ServerGamePacketListenerImpl, ChatPreviewCache> packetListenerPreviewCache;

	static {
		packetListenerPreviewThrottler = SafeVarHandle.ofOrNull(ServerGamePacketListenerImpl.class, "M", "chatPreviewThrottler", ChatPreviewThrottler.class);
		packetListenerPreviewCache = SafeVarHandle.ofOrNull(ServerGamePacketListenerImpl.class, "L", "chatPreviewCache", ChatPreviewCache.class);
	}

	ChatPreviewThrottler throttler;

	public PaperNMS_1_19_1_R1_ChatPreviewHandler(CommandAPIPaper<CommandSourceStack> platform, Plugin plugin, Player player) {
		super(platform, plugin, player);

		throttler = packetListenerPreviewThrottler.get(((CraftPlayer) player).getHandle().connection);
	}

	@Override
	protected void handleChatPreviewPacket(ServerboundChatPreviewPacket chatPreview) {
		// We want to run this synchronously, just in case there's some funky async stuff going on here
		throttler.schedule(() -> {
			int i = chatPreview.queryId();
			CompletableFuture<Component> result = new CompletableFuture<>();

			// Get preview
			Bukkit.getScheduler().runTask(this.plugin, () -> result.complete(parseChatPreviewQuery(chatPreview.query())));

			// Update player's ChatPreviewCache
			result.thenAcceptAsync(component -> {
				if(component == null) return;
				ChatPreviewCache c = packetListenerPreviewCache.get(((CraftPlayer) player).getHandle().connection);
				c.set(chatPreview.query().substring(1), component);
			});

			// Send ChatPreviewPacket using the throttler
			return result.thenAccept(
				component -> {
					if(component == null) return;
					connection.send(
						new ClientboundChatPreviewPacket(i, component),
						PacketSendListener.exceptionallySend(() -> new ClientboundChatPreviewPacket(i, null))
					);
				}
			);
		});
	}

}
