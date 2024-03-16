package dev.jorel.commandapi;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandAPIPaperConfig extends CommandAPIBukkitConfig<CommandAPIPaperConfig> {

	boolean shouldHookPaperReload = true;

	public CommandAPIPaperConfig(JavaPlugin plugin) {
		super(plugin);
	}

	/**
	 * Sets the CommandAPI to hook into Paper's {@link io.papermc.paper.event.server.ServerResourcesReloadedEvent} when available
	 * if true. This helps CommandAPI commands to work in datapacks after {@code /minecraft:reload}
	 * is run.
	 *
	 * @param hooked whether the CommandAPI should hook into Paper's {@link io.papermc.paper.event.server.ServerResourcesReloadedEvent}
	 * @return this CommandAPIBukkitConfig
	 */
	public CommandAPIPaperConfig shouldHookPaperReload(boolean hooked) {
		this.shouldHookPaperReload = hooked;
		return this;
	}

	@Override
	public CommandAPIPaperConfig instance() {
		return this;
	}
}
