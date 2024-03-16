package dev.jorel.commandapi.test;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.nms.NMS_1_17;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.CommandSourceStack;

public class PaperMockNMS extends PaperMockPlatform<CommandSourceStack> {

	private final CommandAPIPaper<CommandSourceStack> basePaperNMS;

	public PaperMockNMS(CommandAPIPaper<?> basePaperNMS) {
		this.basePaperNMS = (CommandAPIPaper<CommandSourceStack>) basePaperNMS;
	}

	@Override
	public Component getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return basePaperNMS.getChat(cmdCtx, key);
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return basePaperNMS.getChatColor(cmdCtx, key);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return basePaperNMS.getChatComponent(cmdCtx, key);
	}

	@Override
	public NMS<?> bukkitNMS() {
		return new MockNMS(new NMS_1_17());
	}
}
