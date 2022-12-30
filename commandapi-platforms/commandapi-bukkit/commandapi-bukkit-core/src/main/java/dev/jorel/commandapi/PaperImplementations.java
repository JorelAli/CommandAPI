package dev.jorel.commandapi;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import dev.jorel.commandapi.nms.NMS;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;

public class PaperImplementations {

	private final boolean isPaperPresent;
	private final NMS<?> nmsInstance;

	/**
	 * Constructs a PaperImplementations object
	 * 
	 * @param isPaperPresent Whether this is a Paper server or not
	 * @param nmsInstance    The instance of NMS
	 */
	public PaperImplementations(boolean isPaperPresent, NMS<?> nmsInstance) {
		this.isPaperPresent = isPaperPresent;
		this.nmsInstance = nmsInstance;
	}

	/**
	 * Hooks into Paper's {@link ServerResourcesReloadedEvent} to detect if
	 * {@code /minecraft:reload} is called, and registers a reload handler that
	 * automatically calls the CommandAPI's internal datapack reloading function
	 * 
	 * @param plugin the plugin that the CommandAPI is being used from
	 */
	public void registerReloadHandler(Plugin plugin) {
		if (isPaperPresent && CommandAPIBukkit.getConfiguration().shouldHookPaperReload()) {
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
	public boolean isPresent() {
		return this.isPaperPresent;
	}

}
