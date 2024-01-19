package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import dev.jorel.commandapi.CommandAPIBukkit;
import io.netty.channel.Channel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_19_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotNMS_1_19_R1 extends SpigotNMS_1_19_Common {

	private NMS_1_19_R1 bukkitNMS;

	@Override
	public BaseComponent[] getChat(CommandContext<CommandSourceStack> cmdCtx, String key) throws CommandSyntaxException {
		return ComponentSerializer.parse(Component.Serializer.toJson(MessageArgument.getMessage(cmdCtx, key)));
	}

	@Override
	public BaseComponent[] getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return ComponentSerializer.parse(Component.Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Override
	protected void hookChatPreview(Plugin plugin, Player player) {
		final Channel playerChannel = ((CraftPlayer) player).getHandle().connection.connection.channel;
		if (playerChannel.pipeline().get("CommandAPI_" + player.getName()) == null) {
			playerChannel.pipeline().addBefore("packet_handler", "CommandAPI_" + player.getName(), new SpigotNMS_1_19_R1_ChatPreviewHandler(this, plugin, player));
		}
	}

	@Override
	public NMS<?> bukkitNMS() {
		if (bukkitNMS == null) {
			this.bukkitNMS = new NMS_1_19_R1();
		}
		return bukkitNMS;
	}

}
