package dev.jorel.commandapi;

import java.nio.file.Path;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
	id = "commandapi-velocity",
	name = "CommandAPI",
	version = "${project.version}", // Hopefully Maven is smart enough to substitute this
	url = "https://commandapi.jorel.dev",
	description = "An API to use Minecraft 1.13s new command UI",
	authors = {"Skepter"}
)
public class CommandAPIMain {

	private final ProxyServer server;
	private final Logger logger;

	@Inject
	public CommandAPIMain(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
		this.server = server;
		this.logger = logger;

		// TODO: Save default and load config file from dataDirectory
		//  does Velocity have an API for this?
		CommandAPIConfig config = new CommandAPIConfig()
			.verboseOutput()
			.silentLogs()
			.missingExecutorImplementationMessage()
			.dispatcherFile()
			.initializeNBTAPI();

		CommandAPI.onLoad(config, new VelocityLogger(logger));
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		CommandAPI.onEnable(this);
		
		// Command can be registered using the following:
		//
		//   commandManager.register(new BrigadierCommand( /* Node */ ));
		//
		// This is effectively what we'll need for our velocity implementation
		// of CommandAPIHandler);
	}
	
	@Subscribe
	public void onProxyShutdown(ProxyShutdownEvent event) {
		// Shut down
		CommandAPI.onDisable();
	}
	
	// On /velocity reload
	@Subscribe
	public void onProxyReload(ProxyReloadEvent event) {
		// Handle quirky stuff
	}
	
}
