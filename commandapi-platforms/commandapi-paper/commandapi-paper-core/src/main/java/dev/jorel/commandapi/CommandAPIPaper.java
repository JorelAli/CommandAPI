package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.AbstractPlayer;
import dev.jorel.commandapi.commandsenders.BukkitBlockCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitConsoleCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitEntity;
import dev.jorel.commandapi.commandsenders.BukkitFeedbackForwardingCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitNativeProxyCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitPlayer;
import dev.jorel.commandapi.commandsenders.BukkitProxiedCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitRemoteConsoleCommandSender;
import dev.jorel.commandapi.nms.PaperNMS;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommandAPIPaper<Source> implements BukkitPlatform<Source>, PaperNMS<Source> {

	private static CommandAPIBukkit<?> bukkit;
	private static CommandAPIPaper<?> paper;

	private boolean isPaperPresent = true;
	private boolean isFoliaPresent = false;
	private final Class<? extends CommandSender> feedbackForwardingCommandSender;

	@SuppressWarnings("unchecked")
	protected CommandAPIPaper() {
		CommandAPIPaper.bukkit = ((CommandAPIBukkit<?>) bukkitNMS());
		CommandAPIPaper.paper = this;

		Class<? extends CommandSender> tempFeedbackForwardingCommandSender = null;
		try {
			tempFeedbackForwardingCommandSender = (Class<? extends CommandSender>) Class.forName("io.papermc.paper.commands.FeedbackForwardingSender");
		} catch (ClassNotFoundException e) {
			// uhh...
		}

		this.feedbackForwardingCommandSender = tempFeedbackForwardingCommandSender;
		bukkit.setInstance(this);
	}

	@SuppressWarnings("unchecked")
	public static <Source> CommandAPIBukkit<Source> getBukkit() {
		return (CommandAPIBukkit<Source>) bukkit;
	}

	@SuppressWarnings("unchecked")
	public static <Source> CommandAPIPaper<Source> getPaper() {
		return (CommandAPIPaper<Source>) paper;
	}

	public static InternalPaperConfig getConfiguration() {
		return (InternalPaperConfig) CommandAPIBukkit.getConfiguration();
	}

	private static void setInternalConfig(InternalPaperConfig config) {
		CommandAPIBukkit.config = config;
	}

	@Override
	public <T extends CommandAPIBukkitConfig<T>> void onLoad(CommandAPIBukkitConfig<T> config) {
		if (config instanceof CommandAPIPaperConfig paperConfig) {
			// A little unconventional, but we really don't need to implement mojang mapping flags
			// all over the place, we want it to have as minimal interaction as possible so it can
			// be used by the test framework as a global static flag. Also, we want to set this
			// as early as possible in the CommandAPI's loading sequence!
			if (paperConfig.shouldUseMojangMappings) {
				SafeVarHandle.USING_MOJANG_MAPPINGS = true;
			}

			CommandAPIPaper.setInternalConfig(new InternalPaperConfig(paperConfig));
		} else {
			CommandAPI.logError("CommandAPIBukkit was loaded with non-Bukkit config!");
			CommandAPI.logError("Attempts to access Bukkit-specific config variables will fail!");
		}
		bukkit.onLoad();
		checkPaperDependencies();
	}

	@Override
	public void onEnable() {
		JavaPlugin plugin = getConfiguration().getPlugin();

		new Schedulers(isFoliaPresent).scheduleSyncDelayed(plugin, () -> {
			// Fix namespaces first thing when starting the server
			bukkit.fixNamespaces();
			// Sort out permissions after the server has finished registering them all
			bukkit.fixPermissions();
			if (isFoliaPresent) {
				CommandAPI.logNormal("Skipping initial datapack reloading because Folia was detected");
			} else {
				reloadDataPacks();
			}
			bukkit.updateHelpForCommands(CommandAPI.getRegisteredCommands());
		}, 0L);

		// Prevent command registration after server has loaded
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
			// We want the lowest priority so that we always get to this first, in case a dependent plugin is using
			//  CommandAPI features in their own ServerLoadEvent listener for some reason
			@EventHandler(priority = EventPriority.LOWEST)
			public void onServerLoad(ServerLoadEvent event) {
				CommandAPI.stopCommandRegistration();
			}
		}, getConfiguration().getPlugin());

		if (isPaperPresent && CommandAPIPaper.getConfiguration().shouldHookPaperReload()) {
			Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

				@EventHandler
				public void onServerReloadResources(ServerResourcesReloadedEvent event) {
					CommandAPI.logNormal("/minecraft:reload detected. Reloading CommandAPI commands!");
					reloadDataPacks();
				}

			}, plugin);
			CommandAPI.logNormal("Hooked into Paper ServerResourcesReloadedEvent");
		} else {
			CommandAPI.logNormal("Did not hook into Paper ServerResourcesReloadedEvent");
		}
	}

	@Override
	public void onDisable() {
		bukkit.onDisable();
	}

	private void checkPaperDependencies() {
		try {
			Class.forName("net.kyori.adventure.text.Component");
			CommandAPI.logNormal("Hooked into Adventure for AdventureChat/AdventureChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Adventure for AdventureChat/AdventureChatComponents");
			}
		}

		isPaperPresent = false;

		try {
			Class.forName("io.papermc.paper.event.server.ServerResourcesReloadedEvent");
			isPaperPresent = true;
			CommandAPI.logNormal("Hooked into Paper for paper-specific API implementations");
		} catch (ClassNotFoundException e) {
			isPaperPresent = false;
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Paper for /minecraft:reload. Consider upgrading to Paper: https://papermc.io/");
			}
		}

		isFoliaPresent = false;

		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
			isFoliaPresent = true;
			CommandAPI.logNormal("Hooked into Folia for folia-specific API implementations");
			CommandAPI.logNormal("Folia support is still in development. Please report any issues to the CommandAPI developers!");
		} catch (ClassNotFoundException e) {
			isFoliaPresent = false;
		}
	}

	public CommandMap getCommandMap() {
		return Bukkit.getCommandMap();
	}

	@Override
	public Platform activePlatform() {
		return Platform.PAPER;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<Source> cmdCtx, boolean forceNative) {
		return getBukkit().getSenderForCommand((CommandContext<Object>) cmdCtx, forceNative);
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(Source source) {
		return getBukkit().getCommandSenderFromCommandSource(source);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Source getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> sender) {
		return (Source) bukkit.getBrigadierSourceFromCommandSender(sender);
	}

	@Override
	public BukkitCommandSender<? extends CommandSender> wrapCommandSender(CommandSender sender) {
		if (sender instanceof BlockCommandSender block) {
			return new BukkitBlockCommandSender(block);
		}
		if (sender instanceof ConsoleCommandSender console) {
			return new BukkitConsoleCommandSender(console);
		}
		if (sender instanceof Player player) {
			return new BukkitPlayer(player);
		}
		if (sender instanceof org.bukkit.entity.Entity entity) {
			return new BukkitEntity(entity);
		}
		if (sender instanceof NativeProxyCommandSender nativeProxy) {
			return new BukkitNativeProxyCommandSender(nativeProxy);
		}
		if (sender instanceof ProxiedCommandSender proxy) {
			return new BukkitProxiedCommandSender(proxy);
		}
		if (sender instanceof RemoteConsoleCommandSender remote) {
			return new BukkitRemoteConsoleCommandSender(remote);
		}
		final Class<? extends CommandSender> FeedbackForwardingSender = feedbackForwardingCommandSender;
		if (FeedbackForwardingSender.isInstance(sender)) {
			// We literally cannot type this at compile-time, so let's use a placeholder CommandSender instance
			return new BukkitFeedbackForwardingCommandSender<CommandSender>(FeedbackForwardingSender.cast(sender));
		}
		throw new RuntimeException("Failed to wrap CommandSender " + sender + " to a CommandAPI-compatible BukkitCommandSender");
	}

	@Override
	public void registerPermission(String string) {
		bukkit.registerPermission(string);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SuggestionProvider<Source> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		return (SuggestionProvider<Source>) bukkit.getSuggestionProvider(suggestionProvider);
	}

	@Override
	public void preCommandRegistration(String commandName) {
		bukkit.preCommandRegistration(commandName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postCommandRegistration(RegisteredCommand registeredCommand, LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {
		List<LiteralCommandNode<Object>> aliases = new ArrayList<>();
		for (LiteralCommandNode<?> commandNode : aliasNodes) {
			aliases.add((LiteralCommandNode<Object>) commandNode);
		}
		getBukkit().postCommandRegistration(registeredCommand, (LiteralCommandNode<Object>) resultantNode, aliases);
	}

	@SuppressWarnings("unchecked")
	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node, String namespace) {
		return (LiteralCommandNode<Source>) getBukkit().registerCommandNode((LiteralArgumentBuilder<Object>) node, namespace);
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces) {
		bukkit.unregister(commandName, unregisterNamespaces);
	}

	@SuppressWarnings("unchecked")
	@Override
	public CommandDispatcher<Source> getBrigadierDispatcher() {
		return (CommandDispatcher<Source>) bukkit.getBrigadierDispatcher();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void createDispatcherFile(File file, CommandDispatcher<Source> dispatcher) throws IOException {
		getBukkit().createDispatcherFile(file, (CommandDispatcher<Object>) dispatcher);
	}

	@Override
	public void reloadDataPacks() {
		bukkit.reloadDataPacks();
	}

	@Override
	public void updateRequirements(AbstractPlayer<?> player) {
		bukkit.updateRequirements(player);
	}

	@Override
	public CommandAPICommand newConcreteCommandAPICommand(CommandMetaData<CommandSender> meta) {
		return bukkit.newConcreteCommandAPICommand(meta);
	}

	@Override
	public MultiLiteralArgument newConcreteMultiLiteralArgument(String nodeName, String[] literals) {
		return (MultiLiteralArgument) bukkit.newConcreteMultiLiteralArgument(nodeName, literals);
	}

	@Override
	public LiteralArgument newConcreteLiteralArgument(String nodeName, String literal) {
		return (LiteralArgument) bukkit.newConcreteLiteralArgument(nodeName, literal);
	}
}
