package dev.jorel.commandapi.nms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;

import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.PreviewInfo;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.Preview;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.protocol.game.ClientboundChatPreviewPacket;
import net.minecraft.network.protocol.game.ServerboundChatPreviewPacket;

public class NMS_1_19_R1_ChatPreviewHandler extends ChannelDuplexHandler {
	
	private final NMS<CommandSourceStack> nms;
	private final Plugin plugin;
	private final Player player;
	
	public NMS_1_19_R1_ChatPreviewHandler(NMS<CommandSourceStack> nms, Plugin plugin, Player player) {
		this.nms = nms;
		this.plugin = plugin;
		this.player = player;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ServerboundChatPreviewPacket chatPreview) {
			// Substring 1 because we want to get rid of the leading /
			final String fullInput = chatPreview.query().substring(1);
			ParseResults<CommandSourceStack> results = nms.getBrigadierDispatcher().parse(fullInput, nms.getCLWFromCommandSender(this.player));

			// Generate the path for lookup
			List<String> path = new ArrayList<>();
			for (ParsedCommandNode<CommandSourceStack> commandNode : results.getContext().getNodes()) {
				path.add(commandNode.getNode().getName());
			}
			Preview preview = CommandAPIHandler.getInstance().lookupPreviewable(path);

			// Calculate the (argument) input and generate the component to send
			String input = results.getContext().getNodes().get(results.getContext().getNodes().size() - 1).getRange().get(fullInput);

			final Component finalComponent;
			{
				Component component;
				try {
					component = preview.generatePreview(new PreviewInfo(this.player, input, chatPreview.query()));
				} catch (WrapperCommandSyntaxException e) {
					// TODO: We may have to do legacy chat format parsing here
					component = Component.text(e.getMessage());
				}
				finalComponent = component;
			}

			if (finalComponent != null) {
				Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> ((CraftPlayer) player).getHandle().connection.connection
					.send(new ClientboundChatPreviewPacket(chatPreview.queryId(), Serializer.fromJson(GsonComponentSerializer.gson().serialize(finalComponent)))));
			}
		}

		// Normal packet handling
		super.channelRead(ctx, msg);
	}

}
