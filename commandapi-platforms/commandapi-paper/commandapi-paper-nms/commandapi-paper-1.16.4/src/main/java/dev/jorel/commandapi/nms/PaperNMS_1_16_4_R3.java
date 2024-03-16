package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_16_R3.ArgumentChat;
import net.minecraft.server.v1_16_R3.ArgumentChatComponent;
import net.minecraft.server.v1_16_R3.ArgumentChatFormat;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;

@NMSMeta(compatibleWith = "1.16.4")
public class PaperNMS_1_16_4_R3 extends PaperNMSWrapper_1_16_4_R3 {

	@Override
	public Component getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		final Integer color = ArgumentChatFormat.a(cmdCtx, key).e();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public NMS<?> bukkitNMS() {
		return (NMS<?>) new NMS_1_16_4_R3();
	}
}
