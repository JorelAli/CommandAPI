package dev.jorel.commandapi;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.jorel.commandapi.commandsenders.*;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.nms.PaperNMS;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface CommandAPIPaper<Source> extends BukkitPlatform<Source>, PaperNMS<Source> {
	// Loading logic

	// An interface does allow multi-class inheritance, but it does not permit static initialization blocks
	//  or fields that are not public static final :(. We can at least log success/failure later when we know
	//  the config and logger are properly set up.
	boolean isPaperPresent = getClass("io.papermc.paper.event.server.ServerResourcesReloadedEvent") != null;
	boolean isFoliaPresent = getClass("io.papermc.paper.threadedregions.RegionizedServerInitEvent") != null;
	Class<? extends CommandSender> feedbackForwardingCommandSender = getClass("io.papermc.paper.commands.FeedbackForwardingSender");
	Class<? extends CommandSender> nullCommandSender = getClass("io.papermc.paper.brigadier.NullCommandSender");

	@SuppressWarnings("unchecked")
	private static <T> Class<T> getClass(String name) {
		try {
			return (Class<T>) Class.forName(name);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static <Source> CommandAPIPaper<Source> getPaper() {
		return (CommandAPIPaper<Source>) CommandAPIBukkit.get();
	}

	static InternalPaperConfig getConfiguration() {
		return (InternalPaperConfig) CommandAPIBukkit.getConfiguration();
	}

	private static void setInternalConfig(InternalPaperConfig config) {
		CommandAPIBukkit.config = config;
	}

	// Implement BukkitPlatform methods
	@Override
	default void platformOnLoad(CommandAPIConfig<?> config) {
		if (config instanceof CommandAPIPaperConfig paperConfig) {
			CommandAPIPaper.setInternalConfig(new InternalPaperConfig(paperConfig));
		} else {
			CommandAPI.logError("CommandAPIPaper was loaded with non-Paper config!");
			CommandAPI.logError("Attempts to access Bukkit-specific config variables will fail!");
		}

		// Check dependencies
		try {
			Class.forName("net.kyori.adventure.text.Component");
			CommandAPI.logNormal("Hooked into Adventure for AdventureChat/AdventureChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Adventure for AdventureChat/AdventureChatComponents");
			}
		}

		if (isPaperPresent) {
			CommandAPI.logNormal("Hooked into Paper for paper-specific API implementations");
		} else {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Paper for /minecraft:reload. Consider upgrading to Paper: https://papermc.io/");
			}
		}

		if (isFoliaPresent) {
			CommandAPI.logNormal("Hooked into Folia for folia-specific API implementations");
			CommandAPI.logNormal("Folia support is still in development. Please report any issues to the CommandAPI developers!");
		}
	}

	@Override
	@Differs(from = "Spigot", by = "Scheduler logic considers Folia")
	@Differs(from = "Spigot", by = "ServerResourcesReloadedEvent used to detect `minecraft:reload`")
	default void platformOnEnable() {
		JavaPlugin plugin = getConfiguration().getPlugin();

		new Schedulers(isFoliaPresent).scheduleSyncDelayed(plugin, () -> {
			getCommandRegistrationStrategy().runTasksAfterServerStart();
			if (isFoliaPresent) {
				CommandAPI.logNormal("Skipping initial datapack reloading because Folia was detected");
			} else {
				if (!getConfiguration().skipReloadDatapacks()) {
					reloadDataPacks();
				}
			}
			updateHelpForCommands(CommandAPI.getRegisteredCommands());
		}, 0L);

		// Basically just a check to ensure we're actually running Paper
		if (isPaperPresent) {
			Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onServerReloadResources(ServerResourcesReloadedEvent event) {
					// This event is called after Paper is done with everything command related
					// which means we can put commands back
					getCommandRegistrationStrategy().preReloadDataPacks();

					// Normally, the reloadDataPacks() method is responsible for updating commands for
					// online players. If, however, datapacks aren't supposed to be reloaded upon /minecraft:reload
					// we have to do this manually here. This won't have any effect on Spigot and Paper version prior to
					// paper-1.20.6-65
					if (!CommandAPIPaper.getConfiguration().shouldHookPaperReload()) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.updateCommands();
						}
						return;
					}
					CommandAPI.logNormal("/minecraft:reload detected. Reloading CommandAPI commands!");
					reloadDataPacks();
				}
			}, plugin);
			CommandAPI.logNormal("Hooked into Paper ServerResourcesReloadedEvent");
		} else {
			// TODO: Should this note be added to CommandAPISpigot? Previously, running on Spigot would display this note.
			CommandAPI.logNormal("Did not hook into Paper ServerResourcesReloadedEvent while using commandapi-paper. Are you actually using Paper?");
		}
	}

	@Override
	default void platformOnDisable() {
		// Nothing to do
	}

	@Override
	@Differs(from = "Spigot", by = "FeedbackForwardingSender and NullCommandSender are possible CommandSender instances")
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
		if (sender instanceof org.bukkit.entity.Entity entity) {
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
		if (feedbackForwardingCommandSender.isInstance(sender)) {
			// We literally cannot type this at compile-time, so let's use a placeholder CommandSender instance
			return new BukkitFeedbackForwardingCommandSender<CommandSender>(feedbackForwardingCommandSender.cast(sender));
		}
		if (nullCommandSender != null && nullCommandSender.isInstance(sender)) {
			// Since this should only be during a function load, this is just a placeholder to evade the exception.
			return null;
		}
		throw new RuntimeException("Failed to wrap CommandSender " + sender + " to a CommandAPI-compatible BukkitCommandSender");
	}

	@Override
	default CommandMap getCommandMap() {
		return Bukkit.getCommandMap();
	}

	@Override
	default Platform activePlatform() {
		return Platform.PAPER;
	}

	// TODO: These methods were moved from CommandAPIBukkit. Update the docs that reference this if this is what we want to do.

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message, formatted as an adventure chat component
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	static WrapperCommandSyntaxException failWithAdventureComponent(Component message) {
		return CommandAPI.failWithMessage(BukkitTooltip.messageFromAdventureComponent(message));
	}

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message, formatted as an adventure chat component
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	static WrapperCommandSyntaxException failWithAdventureComponent(ComponentLike message) {
		return CommandAPI.failWithMessage(BukkitTooltip.messageFromAdventureComponent(message.asComponent()));
	}
}
