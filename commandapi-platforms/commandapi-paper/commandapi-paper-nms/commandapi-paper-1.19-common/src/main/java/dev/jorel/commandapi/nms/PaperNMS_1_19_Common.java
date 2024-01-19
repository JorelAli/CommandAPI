package dev.jorel.commandapi.nms;

import com.mojang.brigadier.context.CommandContext;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandRegistrationStrategy;
import dev.jorel.commandapi.SpigotCommandRegistration;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_19_R1.command.BukkitCommandWrapper;
import org.bukkit.craftbukkit.v1_19_R1.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.VERSION_SPECIFIC_IMPLEMENTATION;

public abstract class PaperNMS_1_19_Common extends PaperNMS_CommonWithFunctions {

	@Override
	public NamedTextColor getChatColor(CommandContext<CommandSourceStack> cmdCtx, String key) {
		final Integer color = ColorArgument.getColor(cmdCtx, key).getColor();
		return color == null ? NamedTextColor.WHITE : NamedTextColor.namedColor(color);
	}

	@Override
	public final Component getChatComponent(CommandContext<CommandSourceStack> cmdCtx, String key) {
		return GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(cmdCtx, key)));
	}

	@Override
	public void onEnable() {
		super.onEnable();

		JavaPlugin plugin = getConfiguration().getPlugin();
		// Enable chat preview if the server allows it
		if (Bukkit.shouldSendChatPreviews()) {
			Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

				@EventHandler
				public void onPlayerJoin(PlayerJoinEvent e) {
					hookChatPreview(plugin, e.getPlayer());
				}

				@EventHandler
				public void onPlayerQuit(PlayerQuitEvent e) {
					unhookChatPreview(e.getPlayer());
				}

			}, plugin);
			CommandAPI.logNormal("Chat preview enabled");
		} else {
			CommandAPI.logNormal("Chat preview is not available");
		}
	}

	/**
	 * Hooks into the chat previewing system
	 *
	 * @param plugin the plugin (for async calls)
	 * @param player the player to hook
	 */
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION)
	protected abstract void hookChatPreview(Plugin plugin, Player player);

	/**
	 * Unhooks a player from the chat previewing system. This should be
	 * called when the player quits and when the plugin is disabled
	 *
	 * @param player the player to unhook
	 */
	private void unhookChatPreview(Player player) {
		final Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
		if (channel.pipeline().get("CommandAPI_" + player.getName()) != null) {
			channel.eventLoop().submit(() -> channel.pipeline().remove("CommandAPI_" + player.getName()));
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();

		for (Player player : Bukkit.getOnlinePlayers()) {
			unhookChatPreview(player);
		}
	}

	@Override
	@Differs(from = "1.18", by = "MinecraftServer#aA -> MinecraftServer#aC")
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
