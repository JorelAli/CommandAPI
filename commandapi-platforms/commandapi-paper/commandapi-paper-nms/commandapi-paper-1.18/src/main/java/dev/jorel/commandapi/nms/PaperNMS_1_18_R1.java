package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;

public class PaperNMS_1_18_R1 extends NMS_1_18_R1 implements PaperNMS_Common {
	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.ofExact(color);
	}
}
