package dev.jorel.commandapi.test;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_16_R3;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

public class PaperMockNMS extends PaperMockPlatform<CommandListenerWrapper> {

	private final CommandAPIPaper<CommandListenerWrapper> basePaperNMS;

	public PaperMockNMS(CommandAPIPaper<?> basePaperNMS) {
		this.basePaperNMS = (CommandAPIPaper<CommandListenerWrapper>) basePaperNMS;
	}

	@Override
	public Component getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return basePaperNMS.getChat(cmdCtx, key);
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return basePaperNMS.getChatColor(cmdCtx, key);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return basePaperNMS.getChatComponent(cmdCtx, key);
	}

	@Override
	public NMS<?> bukkitNMS() {
		return new MockNMS(new NMS_1_16_R3());
	}
}
