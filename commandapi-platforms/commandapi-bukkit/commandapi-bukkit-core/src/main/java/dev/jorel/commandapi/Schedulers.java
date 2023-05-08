package dev.jorel.commandapi;

import org.bukkit.plugin.Plugin;

public class Schedulers {

	private final PaperImplementations paperImplementations;

	public Schedulers(PaperImplementations paperImplementations) {
		this.paperImplementations = paperImplementations;
	}

	public int scheduleSyncRepeatingTask(Plugin plugin, Runnable runnable, long delay, long period) {
		if (paperImplementations.isFoliaPresent()) {
			plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task -> runnable.run(), delay, period);
			return 1;
		} else {
			return plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, period);
		}
	}

	public void cancelTask(Plugin plugin, int id) {
		if (paperImplementations.isFoliaPresent()) {
			plugin.getServer().getGlobalRegionScheduler().cancelTasks(plugin);
		} else {
			plugin.getServer().getScheduler().cancelTask(id);
		}
	}

	public void scheduleSync(Plugin plugin, Runnable runnable) {
		if (paperImplementations.isFoliaPresent()) {
			plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
		} else {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable);
		}
	}

	public void scheduleSyncDelayed(Plugin plugin, Runnable runnable, long delay) {
		if (paperImplementations.isFoliaPresent()) {
			plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
		} else {
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
		}
	}

	public void scheduleAsync(Plugin plugin, Runnable runnable) {
		if (paperImplementations.isFoliaPresent()) {
			plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
		} else {
			plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
		}
	}

}
