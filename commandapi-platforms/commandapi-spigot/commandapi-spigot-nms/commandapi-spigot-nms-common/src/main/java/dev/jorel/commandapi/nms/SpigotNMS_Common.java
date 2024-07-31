package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPISpigot;
import dev.jorel.commandapi.preprocessor.Overridden;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component.Serializer;
import org.bukkit.ChatColor;

public interface SpigotNMS_Common extends CommandAPISpigot<CommandSourceStack> {
	@Override
	@Overridden(in = "1.20.5+", because = "Serializer.toJson now needs a Provider")
	default BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	default ChatColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ChatColor.getByChar(ColorArgument.getColor(cmdCtx, key).getChar());
	}

	@Override
	@Overridden(in = "1.20.5+", because = "Serializer.toJson now needs a Provider")
	default BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ComponentSerializer.parse(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}
}
