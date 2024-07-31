package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;

public class PaperNMS_1_20_R2 extends NMS_1_20_R2 implements PaperNMS_Common {
	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}
}
