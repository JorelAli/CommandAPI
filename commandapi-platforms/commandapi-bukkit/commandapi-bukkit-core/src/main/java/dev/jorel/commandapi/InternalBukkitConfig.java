package dev.jorel.commandapi;

import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Configuration wrapper class for Bukkit. The config.yml file used by the CommandAPI is
 * only ever read from, nothing is ever written to it. That's why there's only
 * getter methods.
 */
public class InternalBukkitConfig extends InternalConfig {
	// The plugin that is loading the CommandAPI
	private final JavaPlugin plugin;

	// Whether to hook into paper's reload event to reload datapacks when /minecraft:reload is run
	private final boolean shouldHookPaperReload;

	/**
	 * Creates an {@link InternalBukkitConfig} from a {@link CommandAPIBukkitConfig}
	 *
	 * @param config The configuration to use to set up this internal configuration
	 */
	public InternalBukkitConfig(CommandAPIBukkitConfig config) {
		super(config);
		this.plugin = config.plugin;
		this.shouldHookPaperReload = config.shouldHookPaperReload;
	}

	/**
	 * @return The {@link JavaPlugin} that is loading the CommandAPI
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}

	/**
	 * @return Whether the CommandAPI should hook into Paper's {@link ServerResourcesReloadedEvent}
	 * when available to perform the CommandAPI's custom datapack reload when {@code /minecraft:reload}
	 * is run.
	 */
	public boolean shouldHookPaperReload() {
		return shouldHookPaperReload;
	}
}
