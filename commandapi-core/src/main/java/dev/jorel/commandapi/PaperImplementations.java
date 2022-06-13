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

	public PaperImplementations(boolean isPaperPresent, NMS<?> nmsInstance) {
		this.isPaperPresent = isPaperPresent;
		this.nmsInstance = nmsInstance;
	}

	public void registerReloadHandler(Plugin plugin) {
		if (isPaperPresent) {
			Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

				@EventHandler
				public void onServerReloadResources(ServerResourcesReloadedEvent event) {
					CommandAPI.logNormal("/minecraft:reload detected. Reloading CommandAPI commands!");
					nmsInstance.reloadDataPacks();
				}

			}, plugin);
		}
	}
	
	public CommandMap getCommandMap() {
		if (isPaperPresent) {
			return Bukkit.getServer().getCommandMap();
		} else {
			return nmsInstance.getSimpleCommandMap();
		}
	}

}
