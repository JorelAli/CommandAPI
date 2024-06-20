package dev.jorel.commandapi;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.SafeSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.config.VelocityConfigurationAdapter;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
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

		YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
			.nodeStyle(NodeStyle.BLOCK)
			.path(configFile)
			.build();

		// Create or update config
		VelocityConfigurationAdapter.createMinimalInstance(loader).saveDefaultConfig(configFile.getParent().toFile(), logger);

		// Load the file as a yaml node
		ConfigurationNode configYAML;
		try {
			configYAML = loader.load();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Configure the CommandAPI
		CommandAPIVelocityConfig config = new CommandAPIVelocityConfig(server, this)
			.verboseOutput(configYAML.node("verbose-outputs").getBoolean())
			.silentLogs(configYAML.node("silent-logs").getBoolean())
			.missingExecutorImplementationMessage(configYAML.node("messages", "missing-executor-implementation").getString())
			.dispatcherFile(configYAML.node("create-dispatcher-json").getBoolean() ? new File(dataDirectory.toFile(), "command_registration.json") : null);

		// Load
		CommandAPI.setLogger(CommandAPILogger.fromJavaLogger(logger));
		CommandAPI.onLoad(config);
	}

	@Subscribe
	public void onProxyInitialization(ProxyInitializeEvent event) {
		// Enable
		CommandAPI.onEnable();

		List<String> tags = new ArrayList<>();
		tags.add("hello");
		tags.add("world");

		new CommandTree("proxy")
			.then(
				new LiteralArgument("add").then(
					new StringArgument("tag").executes(info -> {
						String tag = info.args().getUnchecked("tag");

						tags.add(tag);
					})
				)
			)
			.then(
				new LiteralArgument("tag").then(
					new DynamicMultiLiteralArgument("tag", sender -> {
						if (sender instanceof Player player) {
							List<String> addPlayer = new ArrayList<>(tags);
							addPlayer.add(player.getUsername());
							return addPlayer;
						} else {
							return tags;
						}
					}).then(new IntegerArgument("extra").replaceSafeSuggestions(SafeSuggestions.suggest(1, 2, 3))
						.executes(info -> {
							String tag = info.args().getUnchecked("tag");
							int extra = info.args().getUnchecked("extra");

							info.sender().sendMessage(Component.text(tag + " " + extra));
						})
					)
				)
			)
			.register();
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
