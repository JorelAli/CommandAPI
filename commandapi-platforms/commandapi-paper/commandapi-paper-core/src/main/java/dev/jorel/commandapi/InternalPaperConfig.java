package dev.jorel.commandapi;

public class InternalPaperConfig extends InternalBukkitConfig {

	// Whether to hook into paper's reload event to reload datapacks when /minecraft:reload is run
	private final boolean shouldHookPaperReload;

	private final boolean skipReloadDatapacks;

	public InternalPaperConfig(CommandAPIPaperConfig config) {
		super(config);
		this.shouldHookPaperReload = config.shouldHookPaperReload;
		this.skipReloadDatapacks = config.skipReloadDatapacks;
	}

	/**
	 * @return Whether the CommandAPI should hook into Paper's {@link io.papermc.paper.event.server.ServerResourcesReloadedEvent}
	 * when available to perform the CommandAPI's custom datapack reload when {@code /minecraft:reload}
	 * is run.
	 */
	public boolean shouldHookPaperReload() {
		return shouldHookPaperReload;
	}

	/**
	 * @return Whether the CommandAPI should skip reloading datapacks when the server has finished loading
	 */
	public boolean skipReloadDatapacks() {
		return skipReloadDatapacks;
	}

}
