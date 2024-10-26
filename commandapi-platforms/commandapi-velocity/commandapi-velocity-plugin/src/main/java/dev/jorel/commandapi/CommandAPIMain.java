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
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.config.VelocityConfigurationAdapter;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.ConfigurationNode;
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

		new CommandTree("proxytag")
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

		// example from https://github.com/JorelAli/CommandAPI/issues/483
		new CommandAPICommand("proxyflag")
			.withArguments(
				new FlagsArgument("filters")
					.loopingBranch(
						new LiteralArgument("filter", "sort").setListed(true),
						new MultiLiteralArgument("sortType", "furthest", "nearest", "random")
					)
					.loopingBranch(
						new LiteralArgument("filter", "limit").setListed(true),
						new IntegerArgument("limitAmount", 0)
					)
					.loopingBranch(
						new LiteralArgument("filter", "distance").setListed(true),
						new IntegerArgument("low"),
						new IntegerArgument("high")
					)
			)
			.executes(info -> {
				for (CommandArguments branch : info.args().<List<CommandArguments>>getUnchecked("filters")) {
					String filterType = branch.getUnchecked("filter");
					info.sender().sendMessage(Component.text(switch (filterType) {
						case "sort" -> "Sort " + branch.<String>getUnchecked("sortType");
						case "limit" -> "Limit " + branch.<Integer>getUnchecked("limitAmount");
						case "distance" -> "Distance " + branch.<Integer>getUnchecked("low")
										+ " to " + branch.<Integer>getUnchecked("high");
						default -> "Unknown branch " + filterType;
					}));
				}
			})
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
