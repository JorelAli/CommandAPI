package io.github.jorelali;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandAPILogger;
import dev.jorel.commandapi.CommandAPIVelocityConfig;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import net.kyori.adventure.text.Component;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import org.slf4j.Logger;

@Plugin(id = "maven-example", description = "An example for shading the CommandAPI with maven")
public class Main {
	@Inject
	public Main(ProxyServer server, Logger logger, @DataDirectory Path dataFolder) {
		// Set logger
		CommandAPI.setLogger(CommandAPILogger.fromSlf4jLogger(logger));

		// Load the CommandAPI first
		CommandAPI.onLoad(
			// Configure the CommandAPI
			new CommandAPIVelocityConfig(server, this)
				// Turn on verbose output for command registration logs
				.verboseOutput(true)
				// Give file where Brigadier's command registration tree should be dumped
				.dispatcherFile(new File(dataFolder.toFile(), "command_registration.json"))
		);

		logger.info("Creating commands");
		// Create commands
		new CommandAPICommand("echo")
			.withArguments(new GreedyStringArgument("message"))
			.executes(((sender, args) -> {
				sender.sendMessage(Component.text((String) args.get(0)));
			}))
			.register();
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		// Enable the CommandAPI
		CommandAPI.onEnable();
	}
}
