package dev.jorel.commandapi;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * Main CommandAPI plugin entrypoint
 */
@Plugin(
	id = "commandapi",
	name = "CommandAPI",
	version = "${project.version}", // Hopefully Maven is smart enough to substitute this TODO: Maven is not that smart
	url = "https://commandapi.jorel.dev",
	description = "An API to use Minecraft 1.13s new command UI",
	authors = {"Skepter"}
)
public class CommandAPIMain {
	@Inject
	public CommandAPIMain(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
		// Try to find the config file
		Path configFile = dataDirectory.resolve("config.yml");

		// If the config doesn't exist, load it from the resources
		if(!Files.exists(configFile)) {
			try {
				Files.createDirectories(configFile.getParent());
			} catch (IOException ignored) {
			}

			try (InputStream defaultConfig = getClass().getClassLoader().getResourceAsStream("config.yml")) {
				Files.copy(defaultConfig, configFile);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		// Load the file as a yaml node
		ConfigurationNode configYAML;
		try {
			configYAML = YAMLConfigurationLoader.builder().setPath(configFile).build().load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Configure the CommandAPI
		CommandAPIVelocityConfig config = new CommandAPIVelocityConfig(this, server)
			.verboseOutput(configYAML.getNode("verbose-outputs").getBoolean())
			.silentLogs(configYAML.getNode("silent-logs").getBoolean())
			.missingExecutorImplementationMessage(configYAML.getNode("messages", "missing-executor-implementation").getString())
			.dispatcherFile(configYAML.getNode("create-dispatcher-json").getBoolean() ? new File(dataDirectory.toFile(), "command_registration.json") : null);

		// Load
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(logger));
		CommandAPI.onLoad(config);
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		// Enable
		CommandAPI.onEnable();
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
