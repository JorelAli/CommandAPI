package dev.jorel.commandapi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import dev.jorel.commandapi.commandsenders.*;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.abstractions.AbstractPlatform;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.nms.NMS;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

// BukkitPlatform is an AbstractPlatform, but also needs all of the methods from
// NMS, so it implements NMS. Our implementation of BukkitPlatform is now derived
// using the version handler (and thus, deferred to our NMS-specific implementations)
public abstract class BukkitPlatform<Source> extends AbstractPlatform<Source> implements NMS<Source> {
	
	// Blah blah access instance directly and all of that stuff
	private static BukkitPlatform<?> instance;

	public BukkitPlatform() {
		instance = this;
	}

	private PaperImplementations paper;

	public static BukkitPlatform<?> get() {
		return instance;
	}

	public PaperImplementations getPaper() {
		return paper;
	}

	@Override
	public void onLoad() {
		checkDependencies();
	}

	private void checkDependencies() {
		// Log successful hooks
		final String nmsClassHierarchy;
		{
			List<String> nmsClassHierarchyList = new ArrayList<>();
			Class<?> nmsClass = getClass();
			while (nmsClass != BukkitPlatform.class) {
				nmsClassHierarchyList.add(nmsClass.getSimpleName());
				nmsClass = nmsClass.getSuperclass();
			}
			nmsClassHierarchyList.add("NMS");
			nmsClassHierarchy = String.join(" > ", nmsClassHierarchyList);
		}

		CommandAPI.logInfo("Hooked into NMS " + nmsClassHierarchy + " (compatible with "
			+ String.join(", ", compatibleVersions()) + ")");

		try {
			Class.forName("org.spigotmc.SpigotConfig");
			CommandAPI.logNormal("Hooked into Spigot successfully for Chat/ChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Spigot for Chat/ChatComponents");
			}
		}

		try {
			Class.forName("net.kyori.adventure.text.Component");
			CommandAPI.logNormal("Hooked into Adventure for AdventureChat/AdventureChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Adventure for AdventureChat/AdventureChatComponents");
			}
		}

		try {
			Class.forName("io.papermc.paper.event.server.ServerResourcesReloadedEvent");
			paper = new PaperImplementations(true, this);
			CommandAPI.logNormal("Hooked into Paper for paper-specific API implementations");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Paper for /minecraft:reload. Consider upgrading to Paper: https://papermc.io/");
			}
		}
	}

	@Override
	public void onEnable(Object pluginObject) {
		JavaPlugin plugin = (JavaPlugin) pluginObject;

		// Prevent command registration after server has loaded
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			CommandAPI.stopCommandRegistration();

			// Sort out permissions after the server has finished registering them all
			// TODO: Where are the permissions fixed and help updated?
			BaseHandler.fixPermissions();
			reloadDataPacks();
			BaseHandler.updateHelpForCommands();
		}, 0L);

		// (Re)send command graph packet to players when they join
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

			// For some reason, any other priority doesn't work
			@EventHandler(priority = EventPriority.MONITOR)
			public void onPlayerJoin(PlayerJoinEvent e) {
				resendPackets(e.getPlayer());
			}

		}, plugin);

		// On 1.19+, enable chat preview if the server allows it
		if(canUseChatPreview()) {
			Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

				@EventHandler
				public void onPlayerJoin(PlayerJoinEvent e) {
					if(Bukkit.shouldSendChatPreviews()) {
						hookChatPreview(plugin, e.getPlayer());
					}
				}

				@EventHandler
				public void onPlayerQuit(PlayerQuitEvent e) {
					if(Bukkit.shouldSendChatPreviews()) {
						unhookChatPreview(e.getPlayer());
					}
				}

			}, plugin);
			CommandAPI.logNormal("Chat preview enabled");
		} else {
			CommandAPI.logNormal("Chat preview is not available");
		}

		paper.registerReloadHandler(plugin);
	}

	@Override
	public void onDisable() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			unhookChatPreview(player);
		}
	}

	@Override
	@Unimplemented(because = Unimplemented.REASON.REQUIRES_CSS)
	public abstract AbstractCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<Source> cmdCtx, boolean forceNative);

	@Override
	@Unimplemented(because = Unimplemented.REASON.REQUIRES_CSS)
	public abstract AbstractCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(Source cs);

	@Override
	@Unimplemented(because = Unimplemented.REASON.REQUIRES_CRAFTBUKKIT)
	public abstract Source getBrigadierSourceFromCommandSender(AbstractCommandSender<?> sender);

	public AbstractCommandSender<? extends CommandSender> wrapCommandSender(CommandSender sender) {
		if (sender instanceof BlockCommandSender block)
			return new BukkitBlockCommandSender(block);
		if (sender instanceof ConsoleCommandSender console)
			return new BukkitConsoleCommandSender(console);
		if (sender instanceof Player player)
			return new BukkitPlayer(player);
		if (sender instanceof org.bukkit.entity.Entity entity)
			return new BukkitEntity(entity);
		if (sender instanceof NativeProxyCommandSender nativeProxy)
			return new BukkitNativeProxyCommandSender(nativeProxy);
		if (sender instanceof ProxiedCommandSender proxy)
			return new BukkitProxiedCommandSender(proxy);
		return null;
	}

	@Override
	@Unimplemented(because = Unimplemented.REASON.REQUIRES_MINECRAFT_SERVER)
	public abstract CommandDispatcher<Source> getBrigadierDispatcher();

	@Override
	public void registerPermission(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public SuggestionProvider<Source> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postCommandRegistration(LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {
		// TODO Auto-generated method stub

	}

	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerHelp() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister(String commandName, boolean force) {
		// TODO Auto-generated method stub

	}

	/**
	 * TODO: There's probably a much better place to put this, but I don't
	 *  really fancy subclassing SafeOverrideableArgument for Bukkit specifically,
	 *  so I'll dump it here and hope nobody cares because the CommandAPI doesn't
	 *  really have a centralized "utils" class or anything
	 * <p>
	 * Composes a <code>S</code> to a <code>NamespacedKey</code> mapping function to
	 * convert <code>S</code> to a <code>String</code>
	 *
	 * @param mapper the mapping function from <code>S</code> to
	 *               <code>NamespacedKey</code>
	 * @return a composed function that converts <code>S</code> to
	 * <code>String</code>
	 */
	public static <S> Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
		return mapper.andThen(NamespacedKey::toString);
	}
}
