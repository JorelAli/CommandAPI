package dev.jorel.commandapi;

import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A class that contains information needed to configure the CommandAPI on Bukkit-based servers.
 */
public class CommandAPIBukkitConfig extends CommandAPIConfig<CommandAPIBukkitConfig> {

	JavaPlugin plugin;

	// Default configuration
	boolean shouldHookPaperReload = true;
	boolean skipReloadDatapacks = false;

	/**
	 * Creates a new CommandAPIBukkitConfig object. Variables in this
	 * constructor are required to load the CommandAPI on Bukkit properly.
	 *
	 * @param plugin The {@link JavaPlugin} that is loading the CommandAPI
	 *               This is used when registering events.
	 */
	public CommandAPIBukkitConfig(JavaPlugin plugin) {
		this.plugin = plugin;
		super.setNamespace("minecraft");
	}

	/**
	 * Sets the CommandAPI to hook into Paper's {@link ServerResourcesReloadedEvent} when available
	 * if true. This helps CommandAPI commands to work in datapacks after {@code /minecraft:reload}
	 * is run.
	 *
	 * @param hooked whether the CommandAPI should hook into Paper's {@link ServerResourcesReloadedEvent}
	 * @return this CommandAPIBukkitConfig
	 */
	public CommandAPIBukkitConfig shouldHookPaperReload(boolean hooked) {
		this.shouldHookPaperReload = hooked;
		return this;
	}
	
	/**
	 * Sets whether the CommandAPI should skip its datapack reload step after the server
	 * has finished loading. This does not skip reloading of datapacks when invoked manually
	 * when {@link #shouldHookPaperReload(boolean)} is set.
	 * @param skip whether the CommandAPI should skip reloading datapacks when the server has finished loading
	 * @return this CommandAPIBukkitConfig
	 */
	public CommandAPIBukkitConfig skipReloadDatapacks(boolean skip) {
		this.skipReloadDatapacks = skip;
		return this;
	}
	
	/**
	 * Sets whether the CommandAPI should use Mojang mappings as opposed to Spigot's mappings
	 * for internal calls. If set to true, the CommandAPI will use Mojang mappings.
	 * 
	 * @param useMojangMappings whether the CommandAPI should use Mojang mappings for internal calls
	 * @return this CommandAPIBukkitConfig
	 * @deprecated Use the `commandapi-bukkit-shade-mojang-mapped` depdendency instead of `commandapi-bukkit-shade` if you want to use mojang mappings.
	 */
	@Deprecated(since = "9.4.1", forRemoval = true)
	public CommandAPIBukkitConfig useMojangMappings(boolean useMojangMappings) {
		// A little unconventional, but we really don't need to implement mojang mapping flags
		// all over the place, we want it to have as minimal interaction as possible so it can
		// be used by the test framework as a global static flag. Also, we want to set this
		// as early as possible in the CommandAPI's loading sequence, including before loading
		// an NMS class, which setup reflection based on `USING_MOJANG_MAPPINGS`.
		SafeVarHandle.USING_MOJANG_MAPPINGS = useMojangMappings;
		return this;
	}

	/**
	 * @return this CommandAPIBukkitConfig
	 */
	public CommandAPIBukkitConfig usePluginNamespace() {
		super.setNamespace(plugin.getName().toLowerCase());
		super.usePluginNamespace = true;
		return this;
	}

	@Override
	public CommandAPIBukkitConfig setNamespace(String namespace) {
		if (namespace == null) {
			throw new NullPointerException("Default namespace can't be null!");
		}
		if (namespace.isEmpty()) {
			CommandAPI.logNormal("Did not set namespace to an empty value! Namespace '" + super.namespace + "' is used as the default namespace!");
			return this;
		}
		if (!CommandAPIHandler.NAMESPACE_PATTERN.matcher(namespace).matches()) {
			CommandAPI.logNormal("Did not set namespace to the provided '" + namespace + "' namespace because only 0-9, a-z, underscores, periods and hyphens are allowed!");
			return this;
		}
		return super.setNamespace(namespace);
	}

	@Override
	public CommandAPIBukkitConfig instance() {
		return this;
	}
}
