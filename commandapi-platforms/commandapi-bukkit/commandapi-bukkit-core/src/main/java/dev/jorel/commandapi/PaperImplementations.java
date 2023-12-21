package dev.jorel.commandapi;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.paper.CommandDispatcherReadWriteManager;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class PaperImplementations {

	private final NMS<?> nmsInstance;

	private final boolean isPaperPresent;
	private final boolean isFoliaPresent;

	private final boolean canHookServerResourcesReloadedEvent;
	private final Class<? extends CommandSender> feedbackForwardingCommandSender;
	private final CommandDispatcherReadWriteManager commandDispatcherReadWriteManager;

	/**
	 * Constructs a PaperImplementations object
	 *
	 * @param nmsInstance The instance of NMS
	 */
	@SuppressWarnings("unchecked")
	public PaperImplementations(NMS<?> nmsInstance) {
		this.nmsInstance = nmsInstance;

		// General check for Paper
		boolean isPaperPresent;
		try {
			// This class has nothing to do with the CommandAPI, but it was added to Paper very early on
			//  and still exists today, making it a good indicator of when we're on Paper no matter the version
			//  other than that, it just happened to be the first thing I found :P
			Class.forName("com.destroystokyo.paper.event.block.BeaconEffectEvent");
			isPaperPresent = true;
			CommandAPI.logNormal("Hooked into Paper for paper-specific API implementations");
		} catch (ClassNotFoundException e) {
			isPaperPresent = false;
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Paper for for paper-specific API implementations. Consider upgrading to Paper: https://papermc.io/");
			}
		}
		this.isPaperPresent = isPaperPresent;

		// General check for Folia
		boolean isFoliaPresent;
		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
			isFoliaPresent = true;
			CommandAPI.logNormal("Hooked into Folia for folia-specific API implementations");
			CommandAPI.logNormal("Folia support is still in development. Please report any issues to the CommandAPI developers!");
		} catch (ClassNotFoundException e) {
			isFoliaPresent = false;
		}
		this.isFoliaPresent = isFoliaPresent;

		// Check for the ServerResourcesReloadedEvent
		boolean canHookServerResourcesReloadedEvent;
		try {
			Class.forName("io.papermc.paper.event.server.ServerResourcesReloadedEvent");
			canHookServerResourcesReloadedEvent = true;
		} catch (ClassNotFoundException e) {
			canHookServerResourcesReloadedEvent = false;
		}
		this.canHookServerResourcesReloadedEvent = canHookServerResourcesReloadedEvent;

		// Check for Paper's special CommandSender
		Class<? extends CommandSender> tempFeedbackForwardingCommandSender = null;
		try {
			tempFeedbackForwardingCommandSender = (Class<? extends CommandSender>) Class.forName("io.papermc.paper.commands.FeedbackForwardingSender");
		} catch (ClassNotFoundException e) {
			// uhh...
		}
		this.feedbackForwardingCommandSender = tempFeedbackForwardingCommandSender;

		// Catch Paper's async Commands packet building
		this.commandDispatcherReadWriteManager = new CommandDispatcherReadWriteManager();
		if(isPaperPresent) nmsInstance.setupPaperCommandDispatcherReadWriteManager(this.commandDispatcherReadWriteManager);
	}

	/**
	 * Hooks into Paper's {@link ServerResourcesReloadedEvent} to detect if
	 * {@code /minecraft:reload} is called, and registers a reload handler that
	 * automatically calls the CommandAPI's internal datapack reloading function
	 * 
	 * @param plugin the plugin that the CommandAPI is being used from
	 */
	public void registerReloadHandler(Plugin plugin) {
		if (canHookServerResourcesReloadedEvent && CommandAPIBukkit.getConfiguration().shouldHookPaperReload()) {
			Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
				@EventHandler
				public void onServerReloadResources(ServerResourcesReloadedEvent event) {
					CommandAPI.logNormal("/minecraft:reload detected. Reloading CommandAPI commands!");
					nmsInstance.reloadDataPacks();
				}
			}, plugin);
			CommandAPI.logNormal("Hooked into Paper ServerResourcesReloadedEvent");
		} else {
			CommandAPI.logNormal("Did not hook into Paper ServerResourcesReloadedEvent");
		}
	}

	/**
	 * @return Bukkit's {@link CommandMap}
	 */
	public CommandMap getCommandMap() {
		if (isPaperPresent) {
			return Bukkit.getServer().getCommandMap();
		} else {
			return nmsInstance.getSimpleCommandMap();
		}
	}
	
	/**
	 * @return whether we're using paper or not
	 */
	public boolean isPaperPresent() {
		return this.isPaperPresent;
	}
	
	/**
	 * @return whether we're using folia or not
	 */
	public boolean isFoliaPresent() {
		return this.isFoliaPresent;
	}
	
	/**
	 * @return a class reference pointing to {@code io.papermc.paper.commands.FeedbackForwardingSender}
	 */
	public Class<? extends CommandSender> getFeedbackForwardingCommandSender() {
		return this.feedbackForwardingCommandSender;
	}

	/**
	 * Builds a {@link WrapperCommandSyntaxException} from a message with colour codes like {@link ChatColor} or using the § symbol.
	 *
	 * @param message the error message to be displayed
	 * @return A {@link WrapperCommandSyntaxException} with the given message as error message
	 */
	public WrapperCommandSyntaxException getExceptionFromString(String message) {
		if (isPaperPresent) {
			// I don't know why, but if you set this to an Object first, then cast it to a Component,
			// running this code is totally fine on a Spigot server. If you don't do this (e.g. set
			// it to a Component or inline this), for some reason Java throws a stronk at runtime.
			// For your sanity and the sanity of whoever has to maintain this in the future, please
			// DO NOT try to simplify this statement:
			final Object adventureComponent = LegacyComponentSerializer.legacySection().deserialize(message);
			return new WrapperCommandSyntaxException(new SimpleCommandExceptionType(BukkitTooltip.messageFromAdventureComponent((Component) adventureComponent)).create());
		} else {
			return new WrapperCommandSyntaxException(new SimpleCommandExceptionType(BukkitTooltip.messageFromBaseComponents(TextComponent.fromLegacyText(message))).create());
		}
	}	

	/**
	 * Waits to make sure Paper is not reading from the Brigadier CommandDispatchers before running a task that
	 * modifies those CommandDispatchers. This ensures that the command trees are not modified while Paper is 
	 * building a Commands packet asynchronously, which may cause a ConcurrentModificationException.
	 * <p>
	 * If Paper isn't building Commands packets async (probably because we're on Spigot), 
	 * the task is run immediately, since there isn't any chance of conflict anyway.
	 *
	 * @param modifyTask The task to run that modifies the command trees.
	 */
	public void modifyCommandTreesAndAvoidPaperCME(Runnable modifyTask) {
		this.commandDispatcherReadWriteManager.runWriteTask(modifyTask);
	}

	/**
	 * Waits to make sure Paper is not reading from the Brigadier CommandDispatchers before running a task that
	 * modifies those CommandDispatchers. This ensures that the command trees are not modified while Paper is 
	 * building a Commands packet asynchronously, which may cause a ConcurrentModificationException.
	 * <p>
	 * If Paper isn't building Commands packets async (probably because we're on Spigot), 
	 * the task is run immediately, since there isn't any chance of conflict anyway.
	 *
	 * @param modifyTask The task to run that modifies the command trees.
	 * @return The result of running the {@code modifyTask}.
	 * @param <T> The class of the object returned by the {@code modifyTask}.
	 */
	public <T> T modifyCommandTreesAndAvoidPaperCME(Supplier<T> modifyTask) {
		return this.commandDispatcherReadWriteManager.runWriteTask(modifyTask);
    }
}
