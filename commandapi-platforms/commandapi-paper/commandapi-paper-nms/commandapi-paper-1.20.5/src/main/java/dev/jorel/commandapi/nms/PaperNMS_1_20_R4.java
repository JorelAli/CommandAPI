package dev.jorel.commandapi.nms;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.PaperCommandRegistration;
import dev.jorel.commandapi.preprocessor.Differs;
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

public class PaperNMS_1_20_R4 extends NMS_1_20_R4 implements PaperNMS_Common {
	private static final boolean vanillaCommandDispatcherFieldExists;

	static {
		boolean fieldExists;
		try {
			MinecraftServer.class.getDeclaredField("vanillaCommandDispatcher");
			fieldExists = true;
		} catch (NoSuchFieldException | SecurityException e) {
			// Expected on Paper-1.20.6-65 or later due to https://github.com/PaperMC/Paper/pull/8235
			fieldExists = false;
		}
		vanillaCommandDispatcherFieldExists = fieldExists;
	}

	@Override
	@Differs(from = "1.20.4 and earlier", by = "Serializer.toJson now needs a Provider")
	public Component getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		// TODO: Figure out if an empty provider is suitable here and in `getChatComponent`
		return GsonComponentSerializer.gson().deserialize(Serializer.toJson(MessageArgument.getMessage(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}

	@Override
	@Differs(from = "1.20.4 and earlier", by = "Serializer.toJson now needs a Provider")
	public Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	@Differs(from = "Spigot", by = "Paper's command internals rewrite requires new registration strategy")
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		if (vanillaCommandDispatcherFieldExists) {
			// Before Paper rewrote command internals, so use Spigot-type strategy
			return super.createCommandRegistrationStrategy();
		} else {
			return new PaperCommandRegistration<>(
				() -> this.<MinecraftServer>getMinecraftServer().getCommands().getDispatcher(),
				node -> {
					Command<?> command = node.getCommand();
					return command instanceof BukkitCommandNode.BukkitBrigCommand;
				}
			);
		}
	}
}
