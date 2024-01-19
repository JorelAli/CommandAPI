package dev.jorel.commandapi.nms;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.arguments.PreviewInfo;
import dev.jorel.commandapi.commandsenders.BukkitPlayer;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.wrappers.PreviewableFunction;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundChatPreviewPacket;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PaperNMS_1_19_Common_ChatPreviewHandler extends ChannelDuplexHandler {

	protected final CommandAPIPaper<CommandSourceStack> platform;
	protected final Plugin plugin;
	protected final Player player;
	protected final Connection connection;

	protected PaperNMS_1_19_Common_ChatPreviewHandler(CommandAPIPaper<?> platform, Plugin plugin, Player player) {
		this.platform = (CommandAPIPaper<CommandSourceStack>) platform;
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
				// Is previewable argument
				if (InitialParse.processChatPreviewQuery(chatPreview.query(), platform, player).preview.isPresent()) {
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
		final InitialParse ip = InitialParse.processChatPreviewQuery(chatPreviewQuery, platform, player);
		final Optional<PreviewableFunction<?>> preview = ip.preview;
		if (preview.isEmpty()) {
			return null;
		}

		final String fullInput = ip.fullInput;
		final ParseResults<CommandSourceStack> results = ip.results;
		final List<String> path = ip.path;

		// Calculate the (argument) input and generate the component to send
		String input = results.getContext().getNodes().get(results.getContext().getNodes().size() - 1).getRange().get(fullInput);

		final String jsonToSend;

		Object component;
		try {
			@SuppressWarnings("rawtypes") final PreviewInfo previewInfo;
			Component parsedInput;
			try {
				parsedInput = platform.getChat(results.getContext().build(fullInput), path.get(path.size() - 1));
			} catch (CommandSyntaxException e) {
				throw new WrapperCommandSyntaxException(e);
			}
			previewInfo = new PreviewInfo<>(new BukkitPlayer(player), input, chatPreviewQuery, parsedInput);

			component = preview.get().generatePreview(previewInfo);
		} catch (WrapperCommandSyntaxException e) {
			component = PlainTextComponentSerializer.plainText().deserialize(e.getMessage() == null ? "" : e.getMessage());
		}

		if (component != null) {
			if (component instanceof Component adventureComponent) {
				jsonToSend = GsonComponentSerializer.gson().serialize(adventureComponent);
			} else {
				throw new IllegalArgumentException("Unexpected type returned from chat preview, got: " + component.getClass().getSimpleName());
			}
		} else {
			throw new NullPointerException("Returned value from chat preview was null");
		}

		return net.minecraft.network.chat.Component.Serializer.fromJson(jsonToSend);
	}

	private record InitialParse(String fullInput, ParseResults<CommandSourceStack> results, List<String> path, Optional<PreviewableFunction<?>> preview){
		private static InitialParse cachedResult = null;
		public static InitialParse processChatPreviewQuery(String chatPreviewQuery, CommandAPIPaper<CommandSourceStack> platform, Player player){
			// Substring 1 to get rid of the leading /
			final String fullInput = chatPreviewQuery.substring(1);

			if(cachedResult != null && cachedResult.fullInput.equals(fullInput)) return cachedResult;

			ParseResults<CommandSourceStack> results = platform.getBrigadierDispatcher()
				.parse(fullInput, platform.getBrigadierSourceFromCommandSender(new BukkitPlayer(player)));

			// Generate the path for lookup
			List<String> path = new ArrayList<>();
			for (ParsedCommandNode<CommandSourceStack> commandNode : results.getContext().getNodes()) {
				path.add(commandNode.getNode().getName());
			}
			Optional<PreviewableFunction<?>> preview = CommandAPIHandler.getInstance().lookupPreviewable(path);

			cachedResult = new InitialParse(fullInput, results, path, preview);
			return cachedResult;
		}
	}

}
