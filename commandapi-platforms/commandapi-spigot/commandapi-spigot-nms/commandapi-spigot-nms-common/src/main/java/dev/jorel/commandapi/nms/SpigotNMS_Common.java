package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandAPISpigot;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;
import org.bukkit.ChatColor;

public abstract class SpigotNMS_Common extends CommandAPISpigot<CommandSourceStack> {

	@Override
	public abstract BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException;

	@Override
	public final ChatColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ChatColor.getByChar(ColorArgument.getColor(cmdCtx, key).getChar());
	}

	@Override
	public abstract BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key);

	@Override
	public NMS<?> bukkitNMS() {
		return null;
	}
}
