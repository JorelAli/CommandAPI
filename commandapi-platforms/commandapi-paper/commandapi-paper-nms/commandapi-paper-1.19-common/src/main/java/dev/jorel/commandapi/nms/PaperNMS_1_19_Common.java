package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;

public interface PaperNMS_1_19_Common extends PaperNMS_Common {
	@Override
	default NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}
}
