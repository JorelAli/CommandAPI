package dev.jorel.commandapi;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.commandsenders.*;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.nms.SpigotNMS;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface CommandAPISpigot<Source> extends BukkitPlatform<Source>, SpigotNMS<Source> {
	// Loading logic
	@SuppressWarnings("unchecked")
	static <Source> CommandAPISpigot<Source> getSpigot() {
		return (CommandAPISpigot<Source>) CommandAPIBukkit.get();
	}

	static InternalSpigotConfig getConfiguration() {
		return (InternalSpigotConfig) CommandAPIBukkit.getConfiguration();
	}

	private static void setInternalConfig(InternalSpigotConfig config) {
		CommandAPIBukkit.config = config;
	}

	// Implement BukkitPlatform methods
	@Override
	default void platformOnLoad(CommandAPIConfig<?> config) {
		if (config instanceof CommandAPISpigotConfig spigotConfig) {
			CommandAPISpigot.setInternalConfig(new InternalSpigotConfig(spigotConfig));
		} else {
			CommandAPI.logError("CommandAPISpigot was loaded with non-Spigot config!");
			CommandAPI.logError("Attempts to access Bukkit-specific config variables will fail!");
		}

		// Check dependencies
		try {
			Class.forName("org.spigotmc.SpigotConfig");
			CommandAPI.logNormal("Hooked into Spigot successfully for Chat/ChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Spigot for Chat/ChatComponents");
			}
		}
	}

	@Override
	default void platformOnEnable() {
		JavaPlugin plugin = getConfiguration().getPlugin();

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			// Sort out permissions after the server has finished registering them all
			getCommandRegistrationStrategy().runTasksAfterServerStart();
			if (!getConfiguration().skipReloadDatapacks()) {
				reloadDataPacks();
			}
			updateHelpForCommands(CommandAPI.getRegisteredCommands());
		}, 0L);
	}

	@Override
	default void platformOnDisable() {
		// Nothing to do
	}

	@Override
	default BukkitCommandSender<? extends CommandSender> wrapCommandSender(CommandSender sender) {
		if (sender instanceof BlockCommandSender block) {
			return new BukkitBlockCommandSender(block);
		}
		if (sender instanceof ConsoleCommandSender console) {
			return new BukkitConsoleCommandSender(console);
		}
		if (sender instanceof Player player) {
			return new BukkitPlayer(player);
		}
		if (sender instanceof Entity entity) {
			return new BukkitEntity(entity);
		}
		if (sender instanceof NativeProxyCommandSender nativeProxy) {
			return new BukkitNativeProxyCommandSender(nativeProxy);
		}
		if (sender instanceof ProxiedCommandSender proxy) {
			return new BukkitProxiedCommandSender(proxy);
		}
		if (sender instanceof RemoteConsoleCommandSender remote) {
			return new BukkitRemoteConsoleCommandSender(remote);
		}
		throw new RuntimeException("Failed to wrap CommandSender " + sender + " to a CommandAPI-compatible BukkitCommandSender");
	}

	@Override
	default CommandMap getCommandMap() {
		return getSimpleCommandMap();
	}

	@Override
	default Platform activePlatform() {
		return Platform.SPIGOT;
	}

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message, formatted as an array of base components
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	// TODO: This method was moved from CommandAPIBukkit. Update the docs that reference this if this is what we want to do.
	static WrapperCommandSyntaxException failWithBaseComponents(BaseComponent... message) {
		return CommandAPI.failWithMessage(BukkitTooltip.messageFromBaseComponents(message));
	}
}
