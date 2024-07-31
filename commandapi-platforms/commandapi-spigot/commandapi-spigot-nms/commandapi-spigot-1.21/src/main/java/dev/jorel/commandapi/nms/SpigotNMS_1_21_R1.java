package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.SpigotCommandRegistration;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.v1_21_R1.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_21_R1.command.VanillaCommandWrapper;

public class SpigotNMS_1_21_R1 extends NMS_1_21_R1 implements SpigotNMS_Common {
	@Override
	public BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		// TODO: Figure out if an empty provider is suitable here and in `getChatComponent`
		return ComponentSerializer.parse(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ComponentSerializer.parse(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		return new SpigotCommandRegistration<>(
			this.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher.getDispatcher(),
			getSimpleCommandMap(),
			() -> this.<MinecraftServer>getMinecraftServer().getCommands().getDispatcher(),
			command -> command instanceof VanillaCommandWrapper,
			node -> new VanillaCommandWrapper(this.<MinecraftServer>getMinecraftServer().vanillaCommandDispatcher, node),
			node -> node.getCommand() instanceof BukkitCommandWrapper
		);
	}
}
