package dev.jorel.commandapi.nms;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.PaperCommandRegistration;
import dev.jorel.commandapi.SpigotCommandRegistration;
import io.papermc.paper.command.brigadier.bukkit.BukkitCommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;

import java.lang.reflect.Field;

public class PaperNMS_1_20_R4 extends PaperNMS_Common {

	private static final CommandBuildContext COMMAND_BUILD_CONTEXT;
	private static final boolean vanillaCommandDispatcherFieldExists;
	private static final Commands vanillaCommandDispatcher;

	private NMS_1_20_R4 bukkitNMS;

	static {
		if (Bukkit.getServer() instanceof CraftServer server) {
			COMMAND_BUILD_CONTEXT = CommandBuildContext.simple(server.getServer().registryAccess(),
				server.getServer().getWorldData().enabledFeatures());
		} else {
			COMMAND_BUILD_CONTEXT = null;
		}

		boolean fieldExists;
		Commands commandDispatcher;
		try {
			Field vanillaCommandDispatcherField = MinecraftServer.class.getDeclaredField("vanillaCommandDispatcher");
			commandDispatcher = (Commands) vanillaCommandDispatcherField.get(getBukkit().getMinecraftServer());
			fieldExists = true;
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			// Expected on Paper-1.20.6-65 or later due to https://github.com/PaperMC/Paper/pull/8235
			commandDispatcher = null;
			fieldExists = false;
		}
		vanillaCommandDispatcher = commandDispatcher;
		vanillaCommandDispatcherFieldExists = fieldExists;
	}

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}

	@Override
	public Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key), COMMAND_BUILD_CONTEXT));
	}

	@Override
	public NMS<?> bukkitNMS() {
		if (bukkitNMS == null) {
			this.bukkitNMS = new NMS_1_20_R4();
		}
		return bukkitNMS;
	}

	@Override
	public CommandRegistrationStrategy<CommandSourceStack> createCommandRegistrationStrategy() {
		if (vanillaCommandDispatcherFieldExists) {
			return new SpigotCommandRegistration<>(
				vanillaCommandDispatcher.getDispatcher(),
				(SimpleCommandMap) getCommandMap(),
				() -> bukkitNMS.<MinecraftServer>getMinecraftServer().getCommands().getDispatcher(),
				command -> command instanceof VanillaCommandWrapper,
				node -> new VanillaCommandWrapper(vanillaCommandDispatcher, node),
				node -> node.getCommand() instanceof BukkitCommandWrapper
			);
		} else {
			return new PaperCommandRegistration<>(
				() -> bukkitNMS.<MinecraftServer>getMinecraftServer().getCommands().getDispatcher(),
				node -> {
					Command<?> command = node.getCommand();
					return command instanceof BukkitCommandNode.BukkitBrigCommand;
				}
			);
		}
	}

}
