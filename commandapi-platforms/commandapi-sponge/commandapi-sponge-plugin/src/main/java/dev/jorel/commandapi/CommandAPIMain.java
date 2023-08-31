package dev.jorel.commandapi;

import dev.jorel.commandapi.arguments.LiteralArgument;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;

import com.google.inject.Inject;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

// TODO: Add plugin meta data, see: https://docs.spongepowered.org/stable/en/plugin/plugin-meta.html
/**
 * Main CommandAPI plugin entrypoint
 */
@Plugin("commandapi")
public class CommandAPIMain {

	private final Logger logger;
	private Server server;
	private final PluginContainer pluginContainer;

	@Inject
	CommandAPIMain(final PluginContainer container, final Logger logger) {
		this.pluginContainer = container;
		this.logger = logger;
	}

	@Listener
	public void onServerStarting(final StartingEngineEvent<Server> event) {
		server = event.engine();
		CommandAPI.setLogger(CommandAPILogger.fromApacheLog4jLogger(logger));

		// TODO: Save default config file if it doesn't exist then load config and apply settings to CommandAPIConfig()
		//  See: https://docs.spongepowered.org/stable/en/plugin/configuration/index.html
		CommandAPI.onLoad(new CommandAPISpongeConfig(server));
	}

	@Listener
	public void onServerStart(final StartedEngineEvent<Server> event) {
		CommandAPI.onEnable();
	}

	@Listener
	public void onServerStopping(final StoppingEngineEvent<Server> event) {
		CommandAPI.onDisable();
	}
}
