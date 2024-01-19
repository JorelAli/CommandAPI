package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPIPaper;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.SpigotCommandRegistration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.v1_16_R3.ArgumentChat;
import net.minecraft.server.v1_16_R3.ArgumentChatComponent;
import net.minecraft.server.v1_16_R3.ArgumentChatFormat;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_16_R3.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_16_R3.command.VanillaCommandWrapper;

public class PaperNMS_1_16_R3 extends CommandAPIPaper<CommandListenerWrapper> {

	private NMS_1_16_R3 bukkitNMS;

	@Override
	public final Component getChat(CommandContext<CommandListenerWrapper> cmdCtx, String key) throws CommandSyntaxException {
		return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(cmdCtx, key)));
	}

	@Override
	public final NamedTextColor getChatColor(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		final Integer color = ArgumentChatFormat.a(cmdCtx, key).e();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.ofExact(color);
	}

	@Override
	public final Component getChatComponent(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(cmdCtx, key)));
	}

	@Override
	public NMS<?> bukkitNMS() {
		if (bukkitNMS == null) {
			this.bukkitNMS = new NMS_1_16_R3();
		}
		return bukkitNMS;
	}

	@Override
	public CommandRegistrationStrategy<CommandListenerWrapper> createCommandRegistrationStrategy() {
		return new SpigotCommandRegistration<>(
			bukkitNMS.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher.a(),
			(SimpleCommandMap) getCommandMap(),
			() -> bukkitNMS.<MinecraftServer>getMinecraftServer().getCommandDispatcher().a(),
			command -> command instanceof VanillaCommandWrapper,
			node -> new VanillaCommandWrapper(bukkitNMS.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher, node),
			node -> node.getCommand() instanceof BukkitCommandWrapper
		);
	}

}
