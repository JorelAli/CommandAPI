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
	boolean shouldUseMojangMappings = false;

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
	 * Sets whether the CommandAPI should use Mojang mappings as opposed to Spigot's mappings
	 * for internal calls. If set to true, the CommandAPI will use Mojang mappings.
	 * 
	 * @param useMojangMappings whether the CommandAPI should use Mojang mappings for internal calls
	 * @return this CommandAPIBukkitConfig
	 */
	public CommandAPIBukkitConfig useMojangMappings(boolean useMojangMappings) {
		this.shouldUseMojangMappings = useMojangMappings;
		return this;
	}

	/**
	 * Sets whether the CommandAPI should use the plugin's name as the default namespace
	 * <p>
	 * If called, any call to {@link CommandAPIConfig#setNamespace(String)} will be ignored
	 *
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
		return super.setNamespace(namespace);
	}

	@Override
	public CommandAPIBukkitConfig instance() {
		return this;
	}
}
