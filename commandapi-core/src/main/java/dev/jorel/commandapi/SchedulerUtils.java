package dev.jorel.commandapi;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.plugin.Plugin;

public class SchedulerUtils {

	public static int scheduleSyncRepeatingTask(Plugin plugin, long delay, long period, Runnable runnable) {
		if (CommandAPI.isFoliaServer()) {
			plugin.getServer().getGlobalRegionScheduler().runAtFixedRate(plugin, task -> runnable.run(), delay, period);
			return 1;
		} else return plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, delay, period);
	}

	public static void cancelTask(Plugin plugin, int id) {
		if (CommandAPI.isFoliaServer()) plugin.getServer().getGlobalRegionScheduler().cancelTasks(plugin);
		else plugin.getServer().getScheduler().cancelTask(id);
	}

	public static void execute(Runnable runnable, Plugin plugin) {
		if (CommandAPI.isFoliaServer()) plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
		else plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable);
	}

	public static void executeDelayed(Runnable runnable, Plugin plugin, long delay) {
		if (CommandAPI.isFoliaServer()) plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
		else plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
	}

	public static void executeAsync(Runnable runnable, Plugin plugin) {
		if (CommandAPI.isFoliaServer()) plugin.getServer().getGlobalRegionScheduler().execute(plugin, runnable);
		else plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
	}

}
