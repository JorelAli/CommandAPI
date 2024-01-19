package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.CommandAPIBukkit;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;

public interface PaperNMS<CommandListenerWrapper> {

	Component getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException;

	NamedTextColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	Component getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key);

	NMS<?> bukkitNMS();

}
