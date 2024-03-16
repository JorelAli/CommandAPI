package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.SafeVarHandle;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_15_R1.ArgumentChat;
import net.minecraft.server.v1_15_R1.ArgumentChatComponent;
import net.minecraft.server.v1_15_R1.ArgumentChatFormat;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import net.minecraft.server.v1_15_R1.EnumChatFormat;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;

@NMSMeta(compatibleWith = { "1.15", "1.15.1", "1.15.2" })
@RequireField(in = EnumChatFormat.class, name = "D", ofType = Integer.class)
public class PaperNMS_1_15 extends PaperNMSWrapper_1_15 {

	private static final SafeVarHandle<EnumChatFormat, Integer> enumChatFormatD;

	static {
		enumChatFormatD = SafeVarHandle.ofOrNull(EnumChatFormat.class, "D", "D", Integer.class);
	}

	@Override
	public Component getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		final Integer color = enumChatFormatD.get(ArgumentChatFormat.a(cmdCtx, key));
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public NMS<?> bukkitNMS() {
		return new NMS_1_15();
	}
}
