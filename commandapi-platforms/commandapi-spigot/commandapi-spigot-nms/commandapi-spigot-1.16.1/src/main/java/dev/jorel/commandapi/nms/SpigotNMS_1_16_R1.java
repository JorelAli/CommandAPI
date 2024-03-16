package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_16_R1.ArgumentChat;
import net.minecraft.server.v1_16_R1.ArgumentChatComponent;
import net.minecraft.server.v1_16_R1.ArgumentChatFormat;
import net.minecraft.server.v1_16_R1.CommandListenerWrapper;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;

@NMSMeta(compatibleWith = "1.16.1")
public class SpigotNMS_1_16_R1 extends NMSWrapper_1_16_R1 {

	@Override
	public BaseComponent[] getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public ChatColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return CraftChatMessage.getColor(ArgumentChatFormat.a(cmdCtx, key));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return ComponentSerializer.parse(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
	}

	@Override
	public NMS<?> bukkitNMS() {
		return (NMS<?>) new NMS_1_16_R1();
	}
}
