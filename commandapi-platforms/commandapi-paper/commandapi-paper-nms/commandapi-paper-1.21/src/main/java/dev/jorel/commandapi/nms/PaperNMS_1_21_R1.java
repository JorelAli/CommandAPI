package dev.jorel.commandapi.nms;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.PaperCommandRegistration;
import io.papermc.paper.command.brigadier.bukkit.BukkitCommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.server.MinecraftServer;

public class PaperNMS_1_21_R1 extends NMS_1_21_R1 implements PaperNMS_Common {
	@Override
	public Component getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return GsonComponentSerializer.gson().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		return new PaperCommandRegistration<>(
			() -> this.<MinecraftServer>getMinecraftServer().getCommands().getDispatcher(),
			node -> {
				Command<?> command = node.getCommand();
				return command instanceof BukkitCommandNode.BukkitBrigCommand;
			}
		);
	}
}
