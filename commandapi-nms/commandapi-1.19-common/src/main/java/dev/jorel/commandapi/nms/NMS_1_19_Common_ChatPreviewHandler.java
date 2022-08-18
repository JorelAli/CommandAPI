package dev.jorel.commandapi.nms;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.arguments.PreviewInfo;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.PreviewableFunction;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundChatPreviewPacket;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class NMS_1_19_Common_ChatPreviewHandler extends ChannelDuplexHandler {

	protected final NMS<CommandSourceStack> nms;
	protected final Plugin plugin;
	protected final Player player;
	protected final Connection connection;

	public NMS_1_19_Common_ChatPreviewHandler(NMS<CommandSourceStack> nms, Plugin plugin, Player player) {
		this.nms = nms;
		this.plugin = plugin;
		this.player = player;
		this.connection = ((CraftPlayer) player).getHandle().connection.connection;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ServerboundChatPreviewPacket chatPreview) {
			// make sure the result is worth consuming here
			// Is command
			if (!chatPreview.query().isEmpty() && chatPreview.query().charAt(0) == '/') {
				final String fullInput = chatPreview.query().substring(1);
				ParseResults<CommandSourceStack> results = nms.getBrigadierDispatcher().parse(fullInput, nms.getCLWFromCommandSender(this.player));

				// Generate the path for lookup
				List<String> path = new ArrayList<>();
				for (ParsedCommandNode<CommandSourceStack> commandNode : results.getContext().getNodes()) {
					path.add(commandNode.getNode().getName());
				}
				Optional<PreviewableFunction<?>> preview = CommandAPIHandler.getInstance().lookupPreviewable(path);

				// Is previewable argument
				if (preview.isPresent()) {
					handleChatPreviewPacket(chatPreview);
					return;
				}
			}
		}

		// Normal packet handling
		super.channelRead(ctx, msg);
	}

	protected abstract void handleChatPreviewPacket(ServerboundChatPreviewPacket chatPreview);

	public MutableComponent parseChatPreviewQuery(String chatPreviewQuery) {
		// Substring 1 to get rid of the leading /
		final String fullInput = chatPreviewQuery.substring(1);
		ParseResults<CommandSourceStack> results = nms.getBrigadierDispatcher().parse(fullInput, nms.getCLWFromCommandSender(this.player));

		// Generate the path for lookup
		List<String> path = new ArrayList<>();
		for (ParsedCommandNode<CommandSourceStack> commandNode : results.getContext().getNodes()) {
			path.add(commandNode.getNode().getName());
		}
		Optional<PreviewableFunction<?>> preview = CommandAPIHandler.getInstance().lookupPreviewable(path);
		if (preview.isEmpty()) {
			return null;
		}
		// Calculate the (argument) input and generate the component to send
		String input = results.getContext().getNodes().get(results.getContext().getNodes().size() - 1).getRange().get(fullInput);

		final String jsonToSend;

		Object component;
		try {
			@SuppressWarnings("rawtypes") final PreviewInfo previewInfo;
			if (CommandAPIHandler.getInstance().lookupPreviewableLegacyStatus(path)) {
				BaseComponent[] parsedInput;
				try {
					parsedInput = nms.getChat(results.getContext().build(fullInput), path.get(path.size() - 1));
				} catch (CommandSyntaxException e) {
					throw new WrapperCommandSyntaxException(e);
				}
				previewInfo = new PreviewInfo<>(this.player, input, chatPreviewQuery, parsedInput);
			} else {
				Component parsedInput;
				try {
					parsedInput = nms.getAdventureChat(results.getContext().build(fullInput), path.get(path.size() - 1));
				} catch (CommandSyntaxException e) {
					throw new WrapperCommandSyntaxException(e);
				}
				previewInfo = new PreviewInfo<>(this.player, input, chatPreviewQuery, parsedInput);
			}

			component = preview.get().generatePreview(previewInfo);
		} catch (WrapperCommandSyntaxException e) {
			component = TextComponent.fromLegacyText(e.getMessage() == null ? "" : e.getMessage());
		}

		if (component != null) {
			if (component instanceof BaseComponent[] baseComponent) {
				jsonToSend = ComponentSerializer.toString(baseComponent);
			} else if (CommandAPIHandler.getInstance().getPaper().isPresent()) {
				if (component instanceof Component adventureComponent) {
					jsonToSend = GsonComponentSerializer.gson().serialize(adventureComponent);
				} else {
					throw new IllegalArgumentException("Unexpected type returned from chat preview, got: " + component.getClass().getSimpleName());
				}
			} else {
				throw new IllegalArgumentException("Unexpected type returned from chat preview, got: " + component.getClass().getSimpleName());
			}
		} else {
			throw new NullPointerException("Returned value from chat preview was null");
		}

		return Serializer.fromJson(jsonToSend);
	}
}