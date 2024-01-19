package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.SpigotCommandRegistration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_17_R1.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_17_R1.command.VanillaCommandWrapper;

public class PaperNMS_1_17_Common extends PaperNMS_Common {

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.ofExact(color);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		return new SpigotCommandRegistration<>(
			((CommandAPIBukkit<?>) bukkitNMS()).<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher.getDispatcher(),
			(SimpleCommandMap) getCommandMap(),
			() -> ((CommandAPIBukkit<?>) bukkitNMS()).<MinecraftServer>getMinecraftServer().getCommands().getDispatcher(),
			command -> command instanceof VanillaCommandWrapper,
			node -> new VanillaCommandWrapper(((CommandAPIBukkit<?>) bukkitNMS()).<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher, node),
			node -> node.getCommand() instanceof BukkitCommandWrapper
		);
	}

}
