package dev.jorel.commandapi;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

@Plugin(
	id = "commandapi-velocity",
	name = "CommandAPI",
	version = "8.2.0",
	url = "https://commandapi.jorel.dev",
	description = "An API to use Minecraft 1.13s new command UI",
	authors = {"Skepter"}
)
public class CommandAPIMain {

	private final ProxyServer server;
	private final CommandManager commandManager;
	private final Logger logger;

	@Inject
	public CommandAPIMain(ProxyServer server, Logger logger) {
		this.server = server;
		this.commandManager = server.getCommandManager();
		this.logger = logger;
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		// Start up
		
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
	}
	
	// On /velocity reload
	@Subscribe
	public void onProxyReload(ProxyReloadEvent event) {
		// Handle quirky stuff
	}
	
}
