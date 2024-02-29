package dev.jorel.commandapi;

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.REQUIRES_CRAFTBUKKIT;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.REQUIRES_CSS;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.REQUIRES_MINECRAFT_SERVER;
import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.VERSION_SPECIFIC_IMPLEMENTATION;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import dev.jorel.commandapi.commandsenders.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.network.BukkitCommandAPIMessenger;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;

// CommandAPIBukkit is an CommandAPIPlatform, but also needs all of the methods from
// NMS, so it implements NMS. Our implementation of CommandAPIBukkit is now derived
// using the version handler (and thus, deferred to our NMS-specific implementations)

@RequireField(in = CommandNode.class, name = "children", ofType = Map.class)
@RequireField(in = CommandNode.class, name = "literals", ofType = Map.class)
@RequireField(in = CommandNode.class, name = "arguments", ofType = Map.class)
public abstract class CommandAPIBukkit<Source> implements CommandAPIPlatform<Argument<?>, CommandSender, Source>, NMS<Source> {

	// References to utility classes
	private static CommandAPIBukkit<?> instance;
	private static InternalBukkitConfig config;
	private PaperImplementations paper;
	private BukkitCommandAPIMessenger messenger;

	// Namespaces
	private final Set<String> namespacesToFix = new HashSet<>();
	private RootCommandNode<Source> minecraftCommandNamespaces = new RootCommandNode<>();

	// Static VarHandles
	// I'd like to make the Maps here `Map<String, CommandNode<Source>>`, but these static fields cannot use the type
	//  parameter Source. We still need to cast to that signature for map, so Map is raw.
	private static final SafeVarHandle<CommandNode<?>, Map> commandNodeChildren;
	private static final SafeVarHandle<CommandNode<?>, Map> commandNodeLiterals;
	private static final SafeVarHandle<CommandNode<?>, Map> commandNodeArguments;
	private static final SafeVarHandle<SimpleCommandMap, Map<String, Command>> commandMapKnownCommands;

	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		commandNodeChildren = SafeVarHandle.ofOrNull(CommandNode.class, "children", "children", Map.class);
		commandNodeLiterals = SafeVarHandle.ofOrNull(CommandNode.class, "literals", "literals", Map.class);
		commandNodeArguments = SafeVarHandle.ofOrNull(CommandNode.class, "arguments", "arguments", Map.class);
		commandMapKnownCommands = SafeVarHandle.ofOrNull(SimpleCommandMap.class, "knownCommands", "knownCommands", Map.class);
	}

	protected CommandAPIBukkit() {
		CommandAPIBukkit.instance = this;
	}

	@SuppressWarnings("unchecked")
	public static <Source> CommandAPIBukkit<Source> get() {
		if(CommandAPIBukkit.instance != null) {
			return (CommandAPIBukkit<Source>) instance;
		} else {
			throw new IllegalStateException("Tried to access CommandAPIBukkit instance, but it was null! Are you using CommandAPI features before calling CommandAPI#onLoad?");
		}
	}

	public PaperImplementations getPaper() {
		return paper;
	}

	public static InternalBukkitConfig getConfiguration() {
		if(config != null) {
			return config;
		} else {
			throw new IllegalStateException("Tried to access InternalBukkitConfig, but it was null! Did you load the CommandAPI properly with CommandAPI#onLoad?");
		}
	}

	@Override
	public void onLoad(CommandAPIConfig<?> config) {
		if(config instanceof CommandAPIBukkitConfig bukkitConfig) {
			// A little unconventional, but we really don't need to implement mojang mapping flags
			// all over the place, we want it to have as minimal interaction as possible so it can
			// be used by the test framework as a global static flag. Also, we want to set this
			// as early as possible in the CommandAPI's loading sequence!
			if (bukkitConfig.shouldUseMojangMappings) {
				SafeVarHandle.USING_MOJANG_MAPPINGS = true;
			}

			CommandAPIBukkit.setInternalConfig(new InternalBukkitConfig(bukkitConfig));
		} else {
			CommandAPI.logError("CommandAPIBukkit was loaded with non-Bukkit config!");
			CommandAPI.logError("Attempts to access Bukkit-specific config variables will fail!");
		}

		checkDependencies();
	}
	
	private static void setInternalConfig(InternalBukkitConfig internalBukkitConfig) {
		CommandAPIBukkit.config = internalBukkitConfig;
	}

	private void checkDependencies() {
		// Log successful hooks
		Class<?> nbtContainerClass = CommandAPI.getConfiguration().getNBTContainerClass();
		if (nbtContainerClass != null && CommandAPI.getConfiguration().getNBTContainerConstructor() != null) {
			CommandAPI.logNormal("Hooked into an NBT API with class " + nbtContainerClass.getName());
		}
		// We don't need to log if no NBT was found, constructing an NBTCompoundArgument without one will do that for us

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

		boolean isPaperPresent = false;

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

		boolean isFoliaPresent = false;

		try {
			Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
			isFoliaPresent = true;
			CommandAPI.logNormal("Hooked into Folia for folia-specific API implementations");
			CommandAPI.logNormal("Folia support is still in development. Please report any issues to the CommandAPI developers!");
		} catch (ClassNotFoundException e) {
			isFoliaPresent = false;
		}

		paper = new PaperImplementations(isPaperPresent, isFoliaPresent, this);
	}

	@Override
	public void onEnable() {
		JavaPlugin plugin = config.getPlugin();

		new Schedulers(paper).scheduleSyncDelayed(plugin, () -> {
			// Fix namespaces first thing when starting the server
			fixNamespaces();
			// Sort out permissions after the server has finished registering them all
			fixPermissions();
			if (paper.isFoliaPresent()) {
				CommandAPI.logNormal("Skipping initial datapack reloading because Folia was detected");
			} else {
				reloadDataPacks();
			}
			updateHelpForCommands(CommandAPI.getRegisteredCommands());
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

		paper.registerReloadHandler(plugin);
	}

	/*
	 * Makes permission checks more "Bukkit" like and less "Vanilla Minecraft" like
	 */
	private void fixPermissions() {
		// Get the command map to find registered commands
		CommandMap map = paper.getCommandMap();
		final Map<String, CommandPermission> permissionsToFix = CommandAPIHandler.getInstance().registeredPermissions;

		if (!permissionsToFix.isEmpty()) {
			CommandAPI.logInfo("Linking permissions to commands:");

			for (Map.Entry<String, CommandPermission> entry : permissionsToFix.entrySet()) {
				String cmdName = entry.getKey();
				CommandPermission perm = entry.getValue();
				CommandAPI.logInfo("  " + perm.toString() + " -> /" + cmdName);

				final String permNode = unpackInternalPermissionNodeString(perm);

				/*
				 * Sets the permission. If you have to be OP to run this command, we set the
				 * permission to null. Doing so means that Bukkit's "testPermission" will always
				 * return true, however since the command's permission check occurs internally
				 * via the CommandAPI, this isn't a problem.
				 *
				 * If anyone dares tries to use testPermission() on this command, seriously,
				 * what are you doing and why?
				 */
				Command command = map.getCommand(cmdName);
				if(command != null && isVanillaCommandWrapper(command)) {
					command.setPermission(permNode);
				}
			}
		}
		CommandAPI.logNormal("Linked " + permissionsToFix.size() + " Bukkit permissions to commands");
	}
	
	private String unpackInternalPermissionNodeString(CommandPermission perm) {
		final Optional<String> optionalPerm = perm.getPermission();
		if (perm.isNegated() || perm.equals(CommandPermission.NONE) || perm.equals(CommandPermission.OP)) {
			return "";
		} else if (optionalPerm.isPresent()) {
			return optionalPerm.get();
		} else {
			throw new IllegalStateException("Invalid permission detected: " + perm +
				"! This should never happen - if you're seeing this message, please" +
				"contact the developers of the CommandAPI, we'd love to know how you managed to get this error!");
		}
	}

	/*
	 * Generate and register help topics
	 */
	private String generateCommandHelpPrefix(String command) {
		return (Bukkit.getPluginCommand(command) == null ? "/" : "/minecraft:") + command;
	}

	private void generateHelpUsage(StringBuilder sb, RegisteredCommand command) {
		// Generate usages
		String[] usages = getUsageList(command);

		if (usages.length == 0) {
			// Might happen if the developer calls `.withUsage()` with no parameters
			// They didn't give any usage, so we won't put any there
			return;
		}

		sb.append(ChatColor.GOLD).append("Usage: ").append(ChatColor.WHITE);
		// If 1 usage, put it on the same line, otherwise format like a list
		if (usages.length == 1) {
			sb.append(usages[0]);
		} else {
			for (String usage : usages) {
				sb.append("\n- ").append(usage);
			}
		}
	}

	private String[] getUsageList(RegisteredCommand currentCommand) {
		List<RegisteredCommand> commandsWithIdenticalNames = new ArrayList<>();

		// Collect every command with the same name
		for (RegisteredCommand registeredCommand : CommandAPIHandler.getInstance().registeredCommands) {
			if (registeredCommand.commandName().equals(currentCommand.commandName())) {
				commandsWithIdenticalNames.add(registeredCommand);
			}
		}

		// Generate command usage or fill it with a user provided one
		final String[] usages;
		final Optional<String[]> usageDescription = currentCommand.usageDescription();
		if (usageDescription.isPresent()) {
			usages = usageDescription.get();
		} else {
			// TODO: Figure out if default usage generation should be updated
			final int numCommandsWithIdenticalNames = commandsWithIdenticalNames.size();
			usages = new String[numCommandsWithIdenticalNames];
			for (int i = 0; i < numCommandsWithIdenticalNames; i++) {
				final RegisteredCommand command = commandsWithIdenticalNames.get(i);
				StringBuilder usageString = new StringBuilder();
				usageString.append("/").append(command.commandName()).append(" ");
				for (String arg : command.argsAsStr()) {
					usageString.append("<").append(arg.split(":")[0]).append("> ");
				}
				usages[i] = usageString.toString().trim();
			}
		}
		return usages;
	}

	void updateHelpForCommands(List<RegisteredCommand> commands) {
		Map<String, HelpTopic> helpTopicsToAdd = new HashMap<>();

		for (RegisteredCommand command : commands) {
			// Generate short description
			final String shortDescription;
			final Optional<String> shortDescriptionOptional = command.shortDescription();
			final Optional<String> fullDescriptionOptional = command.fullDescription();
			if (shortDescriptionOptional.isPresent()) {
				shortDescription = shortDescriptionOptional.get();
			} else if (fullDescriptionOptional.isPresent()) {
				shortDescription = fullDescriptionOptional.get();
			} else {
				shortDescription = "A command by the " + config.getPlugin().getName() + " plugin.";
			}

			// Generate full description
			StringBuilder sb = new StringBuilder();
			if (fullDescriptionOptional.isPresent()) {
				sb.append(ChatColor.GOLD).append("Description: ").append(ChatColor.WHITE).append(fullDescriptionOptional.get()).append("\n");
			}

			generateHelpUsage(sb, command);
			sb.append("\n");

			// Generate aliases. We make a copy of the StringBuilder because we
			// want to change the output when we register aliases
			StringBuilder aliasSb = new StringBuilder(sb.toString());
			if (command.aliases().length > 0) {
				sb.append(ChatColor.GOLD).append("Aliases: ").append(ChatColor.WHITE).append(String.join(", ", command.aliases()));
			}

			// Must be empty string, not null as defined by OBC::CustomHelpTopic
			String permission = command.permission().getPermission().orElse("");

			// Don't override the plugin help topic
			String commandPrefix = generateCommandHelpPrefix(command.commandName());
			helpTopicsToAdd.put(commandPrefix, generateHelpTopic(commandPrefix, shortDescription, sb.toString().trim(), permission));

			for (String alias : command.aliases()) {
				StringBuilder currentAliasSb = new StringBuilder(aliasSb.toString());
				currentAliasSb.append(ChatColor.GOLD).append("Aliases: ").append(ChatColor.WHITE);

				// We want to get all aliases (including the original command name),
				// except for the current alias
				List<String> aliases = new ArrayList<>(Arrays.asList(command.aliases()));
				aliases.add(command.commandName());
				aliases.remove(alias);

				currentAliasSb.append(ChatColor.WHITE).append(String.join(", ", aliases));

				// Don't override the plugin help topic
				commandPrefix = generateCommandHelpPrefix(alias);
				helpTopicsToAdd.put(commandPrefix, generateHelpTopic(commandPrefix, shortDescription, currentAliasSb.toString().trim(), permission));
			}
		}

		// We have to use helpTopics.put (instead of .addTopic) because we're overwriting an existing help topic, not adding a new help topic
		getHelpMap().putAll(helpTopicsToAdd);
	}

	private void fixNamespaces() {
		Map<String, Command> knownCommands = commandMapKnownCommands.get((SimpleCommandMap) paper.getCommandMap());
		CommandDispatcher<Source> resourcesDispatcher = getResourcesDispatcher();
		// Remove namespaces
		for (String command : namespacesToFix) {
			knownCommands.remove(command);
			removeBrigadierCommands(resourcesDispatcher, command, false, c -> true);
		}

		// Add back certain minecraft: namespace commands
		RootCommandNode<Source> resourcesRootNode = resourcesDispatcher.getRoot();
		RootCommandNode<Source> brigadierRootNode = getBrigadierDispatcher().getRoot();
		for (CommandNode<Source> node : minecraftCommandNamespaces.getChildren()) {
			knownCommands.put(node.getName(), wrapToVanillaCommandWrapper(node));
			resourcesRootNode.addChild(node);

			// VanillaCommandWrappers in the CommandMap defer to the Brigadier dispatcher when executing.
			// While the minecraft namespace usually does not exist in the Brigadier dispatcher, in the case of a
			//  command conflict we do need this node to exist separately from the unnamespaced version to keep the
			//  commands separate.
			brigadierRootNode.addChild(node);
		}
		// Clear minecraftCommandNamespaces for dealing with command conflicts after the server is enabled
		//  See `CommandAPIBukkit#postCommandRegistration`
		minecraftCommandNamespaces = new RootCommandNode<>();
	}

	@Override
	public void onDisable() {
		// Nothing to do
	}

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<Source> cmdCtx, boolean forceNative);

	@Override
	@Unimplemented(because = REQUIRES_CSS)
	public abstract BukkitCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(Source cs);

	@Override
	@Unimplemented(because = REQUIRES_CRAFTBUKKIT)
	public abstract Source getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> sender);

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
		if (paper.isPaperPresent()) {
			final Class<? extends CommandSender> FeedbackForwardingSender = paper.getFeedbackForwardingCommandSender();
			if (FeedbackForwardingSender.isInstance(sender)) {
				// We literally cannot type this at compile-time, so let's use a placeholder CommandSender instance
				return new BukkitFeedbackForwardingCommandSender<CommandSender>(FeedbackForwardingSender.cast(sender));
			}
		}
		throw new RuntimeException("Failed to wrap CommandSender " + sender + " to a CommandAPI-compatible BukkitCommandSender");
	}

	@Override
	public void registerPermission(String string) {
		try {
			Bukkit.getPluginManager().addPermission(new Permission(string));
		} catch (IllegalArgumentException e) {
			assert true; // nop, not an error.
		}
	}

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract SuggestionProvider<Source> getSuggestionProvider(SuggestionProviders suggestionProvider);

	@Override
	public void preCommandRegistration(String commandName) {
		// Warn if the command we're registering already exists in this plugin's
		// plugin.yml file
		final PluginCommand pluginCommand = Bukkit.getPluginCommand(commandName);
		if (pluginCommand == null) {
			return;
		}
		String pluginName = pluginCommand.getPlugin().getName();
		if (config.getPlugin().getName().equals(pluginName)) {
			CommandAPI.logWarning(
				"Plugin command /%s is registered by Bukkit (%s). Did you forget to remove this from your plugin.yml file?"
					.formatted(commandName, pluginName));
		} else {
			CommandAPI.logNormal(
				"Plugin command /%s is registered by Bukkit (%s). You may have to use /minecraft:%s to execute your command."
					.formatted(commandName, pluginName, commandName));
		}
	}

	@Override
	public void postCommandRegistration(RegisteredCommand registeredCommand, LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {
		if(!CommandAPI.canRegister()) {
			// Usually, when registering commands during server startup, we can just put our commands into the
			// `net.minecraft.server.MinecraftServer#vanillaCommandDispatcher` and leave it. As the server finishes setup,
			// it and the CommandAPI do some extra stuff to make everything work, and we move on.
			// So, if we want to register commands while the server is running, we need to do all that extra stuff, and
			// that is what this code does.
			// We could probably call all those methods to sync everything up, but in the spirit of avoiding side effects
			// and avoiding doing things twice for existing commands, this is a distilled version of those methods.

			Map<String, Command> knownCommands = commandMapKnownCommands.get((SimpleCommandMap) paper.getCommandMap());
			RootCommandNode<Source> root = getResourcesDispatcher().getRoot();

			String name = resultantNode.getLiteral();
			String namespace = registeredCommand.namespace();
			String permNode = unpackInternalPermissionNodeString(registeredCommand.permission());

			registerCommand(knownCommands, root, name, permNode, namespace, resultantNode);

			// Do the same for the aliases
			for(LiteralCommandNode<Source> node: aliasNodes) {
				registerCommand(knownCommands, root, node.getLiteral(), permNode, namespace, node);
			}

			Collection<CommandNode<Source>> minecraftNamespacesToFix = minecraftCommandNamespaces.getChildren();
			if (!minecraftNamespacesToFix.isEmpty()) {
				// Adding new `minecraft` namespace nodes to the Brigadier dispatcher
				//  usually happens in `CommandAPIBukkit#fixNamespaces`.
				// Note that the previous calls to `CommandAPIBukkit#registerCommand` in this method
				//  will have already dealt with adding the nodes here to the resources dispatcher.
				// We also have to set the permission to simulate the result of `CommandAPIBukkit#fixPermissions`.
				RootCommandNode<Source> brigadierRootNode = getBrigadierDispatcher().getRoot();
				for (CommandNode<Source> node : minecraftNamespacesToFix) {
					Command minecraftNamespaceCommand = wrapToVanillaCommandWrapper(node);
					knownCommands.put(node.getName(), minecraftNamespaceCommand);
					minecraftNamespaceCommand.setPermission(permNode);
					brigadierRootNode.addChild(node);
				}
				minecraftCommandNamespaces = new RootCommandNode<>();
			}

			// Adding the command to the help map usually happens in `CommandAPIBukkit#onEnable`
			updateHelpForCommands(List.of(registeredCommand));

			// Sending command dispatcher packets usually happens when Players join the server
			for(Player p: Bukkit.getOnlinePlayers()) {
				p.updateCommands();
			}
		}
	}

	private void registerCommand(Map<String, Command> knownCommands, RootCommandNode<Source> root, String name, String permNode, String namespace, LiteralCommandNode<Source> resultantNode) {
		// Wrapping Brigadier nodes into VanillaCommandWrappers and putting them in the CommandMap usually happens
		// in `CraftServer#setVanillaCommands`
		Command command = wrapToVanillaCommandWrapper(resultantNode);
		knownCommands.putIfAbsent(name, command);

		// Adding permissions to these Commands usually happens in `CommandAPIBukkit#fixPermissions`
		command.setPermission(permNode);

		// Adding commands to the other (Why bukkit/spigot?!) dispatcher usually happens in `CraftServer#syncCommands`
		root.addChild(resultantNode);

		// Handle namespace
		LiteralCommandNode<Source> namespacedNode = CommandAPIHandler.getInstance().namespaceNode(resultantNode, namespace);
		if (namespace.equals("minecraft")) {
			// The minecraft namespace version should be registered as a straight alias of the original command, since
			//  the `minecraft:name` node does not exist in the Brigadier dispatcher, which is referenced by
			//  VanillaCommandWrapper (note this is not true if there is a command conflict, but
			//  `CommandAPIBukkit#postCommandRegistration` will deal with this later using `minecraftCommandNamespaces`).
			knownCommands.putIfAbsent("minecraft:" + name, command);
		} else {
			// A custom namespace should be registered like a separate command, so that it can reference the namespaced
			//  node, rather than the original unnamespaced node
			Command namespacedCommand = wrapToVanillaCommandWrapper(namespacedNode);
			knownCommands.putIfAbsent(namespacedCommand.getName(), namespacedCommand);
			namespacedCommand.setPermission(permNode);
		}
		// In both cases, add the node to the resources dispatcher
		root.addChild(namespacedNode);
	}

	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node, String namespace) {
		RootCommandNode<Source> rootNode = getBrigadierDispatcher().getRoot();

		LiteralCommandNode<Source> builtNode = node.build();
		String name = node.getLiteral();
		if (namespace.equals("minecraft")) {
			if (namespacesToFix.contains("minecraft:" + name)) {
				// This command wants to exist as `minecraft:name`
				// However, another command has requested that `minecraft:name` be removed
				// We'll keep track of everything that should be `minecraft:name` in
				//  `minecraftCommandNamespaces` and fix this later in `#fixNamespaces`
				minecraftCommandNamespaces.addChild(CommandAPIHandler.getInstance().namespaceNode(builtNode, "minecraft"));
			}
		} else {
			// Make sure to remove the `minecraft:name` and
			//  `minecraft:namespace:name` commands Bukkit will create
			fillNamespacesToFix(name, namespace + ":" + name);

			// Create the namespaced node
			rootNode.addChild(CommandAPIHandler.getInstance().namespaceNode(builtNode, namespace));
		}

		// Add the main node to dispatcher
		//  We needed to wait until after `fillNamespacesToFix` was called to do this, in case a previous 
		//  `minecraft:name` version of the command needed to be saved separately before this node was added
		rootNode.addChild(builtNode);
		
		return builtNode;
	}

	private void fillNamespacesToFix(String... namespacedCommands) {
		for (String namespacedCommand : namespacedCommands) {
			// We'll remove these commands later when fixNamespaces is called
			if (!namespacesToFix.add("minecraft:" + namespacedCommand)) {
				continue;
			}

			// If this is the first time considering this command for removal
			// and there is already a command with this name in the dispatcher
			// then, the command currently in the dispatcher is supposed to appear as `minecraft:command`
			CommandNode<Source> currentNode = getBrigadierDispatcher().getRoot().getChild(namespacedCommand);
			if(currentNode != null) {
				// We'll keep track of everything that should be `minecraft:command` in
				//  `minecraftCommandNamespaces` and fix this later in `#fixNamespaces`
				// TODO: Ideally, we should be working without this cast to LiteralCommandNode. I don't know if this can fail
				minecraftCommandNamespaces.addChild(CommandAPIHandler.getInstance().namespaceNode((LiteralCommandNode<Source>) currentNode, "minecraft"));
			}
		}
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces) {
		unregisterInternal(commandName, unregisterNamespaces, false);
	}

	/**
	 * Unregisters a command from the CommandGraph, so it can't be run anymore. This Bukkit-specific unregister has an
	 * additional parameter, {@code unregisterBukkit}, compared to {@link CommandAPI#unregister(String, boolean)}.
	 *
	 * @param commandName          the name of the command to unregister
	 * @param unregisterNamespaces whether the unregistration system should attempt to remove versions of the
	 *                                command that start with a namespace. E.g. `minecraft:command`, `bukkit:command`,
	 *                                or `plugin:command`. If true, these namespaced versions of a command are also
	 *                                unregistered.
	 * @param unregisterBukkit     whether the unregistration system should unregister Vanilla or Bukkit commands. If true,
	 *                             only Bukkit commands are unregistered, otherwise only Vanilla commands are unregistered.
	 *                             For the purposes of this parameter, commands registered using the CommandAPI are Vanilla
	 *                             commands, and commands registered by other plugin using Bukkit API are Bukkit commands.
	 */
	public static void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		CommandAPIBukkit.get().unregisterInternal(commandName, unregisterNamespaces, unregisterBukkit);
	}

	private void unregisterInternal(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		CommandAPI.logInfo("Unregistering command /" + commandName);

		if(!unregisterBukkit) {
			// Remove nodes from the Vanilla dispatcher
			// This dispatcher doesn't usually have namespaced version of commands (those are created when commands
			//  are transferred to Bukkit's CommandMap), but if they ask, we'll do it
			removeBrigadierCommands(getBrigadierDispatcher(), commandName, unregisterNamespaces, c -> true);

			// Update the dispatcher file
			CommandAPIHandler.getInstance().writeDispatcherToFile();
		}

		if(unregisterBukkit || !CommandAPI.canRegister()) {
			// We need to remove commands from Bukkit's CommandMap if we're unregistering a Bukkit command, or
			//  if we're unregistering after the server is enabled, because `CraftServer#setVanillaCommands` will have
			//  moved the Vanilla command into the CommandMap
			Map<String, Command> knownCommands = commandMapKnownCommands.get((SimpleCommandMap) paper.getCommandMap());

			// If we are unregistering a Bukkit command, DO NOT unregister VanillaCommandWrappers
			// If we are unregistering a Vanilla command, ONLY unregister VanillaCommandWrappers
			boolean isMainVanilla = isVanillaCommandWrapper(knownCommands.get(commandName));
			if(unregisterBukkit ^ isMainVanilla) knownCommands.remove(commandName);

			if(unregisterNamespaces) {
				removeCommandNamespace(knownCommands, commandName, c -> unregisterBukkit ^ isVanillaCommandWrapper(c));
			}
		}

		if(!CommandAPI.canRegister()) {
			// If the server is enabled, we have extra cleanup to do

			// Remove commands from the resources dispatcher
			// If we are unregistering a Bukkit command, ONLY unregister BukkitCommandWrappers
			// If we are unregistering a Vanilla command, DO NOT unregister BukkitCommandWrappers
			removeBrigadierCommands(getResourcesDispatcher(), commandName, unregisterNamespaces,
				c -> !unregisterBukkit ^ isBukkitCommandWrapper(c));

			// Help topics (from Bukkit and CommandAPI) are only setup after plugins enable, so we only need to worry
			//  about removing them once the server is loaded.
			getHelpMap().remove("/" + commandName);

			// Notify players
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.updateCommands();
			}
		}
	}

	private void removeBrigadierCommands(CommandDispatcher<Source> dispatcher, String commandName,
										 boolean unregisterNamespaces, Predicate<CommandNode<Source>> extraCheck) {
		RootCommandNode<?> root = dispatcher.getRoot();
		Map<String, CommandNode<Source>> children = (Map<String, CommandNode<Source>>) commandNodeChildren.get(root);
		Map<String, CommandNode<Source>> literals = (Map<String, CommandNode<Source>>) commandNodeLiterals.get(root);
		Map<String, CommandNode<Source>> arguments = (Map<String, CommandNode<Source>>) commandNodeArguments.get(root);

		removeCommandFromMapIfCheckPasses(children, commandName, extraCheck);
		removeCommandFromMapIfCheckPasses(literals, commandName, extraCheck);
		// Commands should really only be represented as literals, but it is technically possible
		// to put an ArgumentCommandNode in the root, so we'll check
		removeCommandFromMapIfCheckPasses(arguments, commandName, extraCheck);

		if (unregisterNamespaces) {
			removeCommandNamespace(children, commandName, extraCheck);
			removeCommandNamespace(literals, commandName, extraCheck);
			removeCommandNamespace(arguments, commandName, extraCheck);
		}
	}

	private static <T> void removeCommandNamespace(Map<String, T> map, String commandName, Predicate<T> extraCheck) {
		for (String key : new HashSet<>(map.keySet())) {
			if (!isThisTheCommandButNamespaced(commandName, key)) continue;

			removeCommandFromMapIfCheckPasses(map, key, extraCheck);
		}
	}

	private static <T> void removeCommandFromMapIfCheckPasses(Map<String, T> map, String key, Predicate<T> extraCheck) {
		T element = map.get(key);
		if (element == null) return;
		if (extraCheck.test(map.get(key))) map.remove(key);
	}

	private static boolean isThisTheCommandButNamespaced(String commandName, String key) {
		if(!key.contains(":")) return false;
		String[] split = key.split(":");
		if(split.length < 2) return false;
		return split[1].equalsIgnoreCase(commandName);
	}

	@Override
	public BukkitCommandAPIMessenger setupMessenger() {
		messenger = new BukkitCommandAPIMessenger(getConfiguration().getPlugin());
		return messenger;
	}

	public BukkitCommandAPIMessenger getMessenger() {
		return messenger;
	}

	@Override
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER)
	public abstract CommandDispatcher<Source> getBrigadierDispatcher();

	@Override
	@Unimplemented(because = {REQUIRES_MINECRAFT_SERVER, VERSION_SPECIFIC_IMPLEMENTATION})
	public abstract void createDispatcherFile(File file, CommandDispatcher<Source> brigadierDispatcher) throws IOException;
	
	@Unimplemented(because = REQUIRES_MINECRAFT_SERVER) // What are the odds?
	public abstract <T> T getMinecraftServer();

	@Override
	public CommandAPILogger getLogger() {
		return new DefaultLogger();
	}

	private static class DefaultLogger extends Logger implements CommandAPILogger {
		protected DefaultLogger() {
			super("CommandAPI", null);
			setParent(Bukkit.getServer().getLogger());
			setLevel(Level.ALL);
		}

		@Override
		public void severe(String message, Throwable exception) {
			super.log(Level.SEVERE, message, exception);
		}
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION)
	public abstract void reloadDataPacks();

	@Override
	public void updateRequirements(AbstractPlayer<?> player) {
		((Player) player.getSource()).updateCommands();
	}

	@Override
	public Argument<String> newConcreteMultiLiteralArgument(String nodeName, String[] literals) {
		return new MultiLiteralArgument(nodeName, literals);
	}

	@Override
	public Argument<String> newConcreteLiteralArgument(String nodeName, String literal) {
		return new LiteralArgument(nodeName, literal);
	}

	@Override
	public CommandAPICommand newConcreteCommandAPICommand(CommandMetaData<CommandSender> meta) {
		return new CommandAPICommand(meta);
	}

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message, formatted as an array of base components
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	public static WrapperCommandSyntaxException failWithBaseComponents(BaseComponent... message) {
		return CommandAPI.failWithMessage(BukkitTooltip.messageFromBaseComponents(message));
	}

	/**
	 * Forces a command to return a success value of 0
	 *
	 * @param message Description of the error message, formatted as an adventure chat component
	 * @return a {@link WrapperCommandSyntaxException} that wraps Brigadier's
	 * {@link CommandSyntaxException}
	 */
	public static WrapperCommandSyntaxException failWithAdventureComponent(Component message) {
		return CommandAPI.failWithMessage(BukkitTooltip.messageFromAdventureComponent(message));
	}
	
	/**
	 * Initializes the CommandAPI's implementation of an NBT API. If you are shading
	 * the CommandAPI, you should be using
	 * {@link CommandAPIConfig#initializeNBTAPI(Class, Function)} in your
	 * {@code onLoad} method instead of calling this method.
	 *
	 * @param <T>                     the type that the NBT compound container class
	 *                                is
	 * @param nbtContainerClass       the NBT compound container class. For example,
	 *                                {@code NBTContainer.class}
	 * @param nbtContainerConstructor a function that takes an Object (NMS
	 *                                {@code NBTTagCompound}) and returns an
	 *                                instance of the provided NBT compound
	 *                                container. For example,
	 *                                {@code NBTContainer::new}.
	 */
	public static <T> void initializeNBTAPI(Class<T> nbtContainerClass, Function<Object, T> nbtContainerConstructor) {
		getConfiguration().lateInitializeNBT(nbtContainerClass, nbtContainerConstructor);
	}
	
	protected void registerBukkitRecipesSafely(Iterator<Recipe> recipes) {
		Recipe recipe;
		while (recipes.hasNext()) {
			recipe = recipes.next();
			try {
				Bukkit.addRecipe(recipe);
				if (recipe instanceof Keyed keyedRecipe) {
					CommandAPI.logInfo("Re-registering recipe: " + keyedRecipe.getKey());
				}
			} catch (IllegalStateException e) { // From CraftingManager - "Duplicate recipe ignored with ID %id%"
				assert true; // Can't re-register registered recipes. Not an error.
			} catch (Exception e) {
				if (recipe instanceof Keyed keyedRecipe) {
					CommandAPI.logError("Failed to register recipe " + keyedRecipe.getKey() + ": " + e.getMessage());
				} else {
					CommandAPI.logError("Failed to register recipe: " + e.getMessage());
				}
			}
		}
	}

	boolean isInvalidNamespace(String commandName, String namespace) {
		if (namespace == null) {
			throw new NullPointerException("Parameter 'namespace' was null when registering command /" + commandName + "!");
		}
		if (namespace.isEmpty()) {
			CommandAPI.logNormal("Registering command '" + commandName + "' using the default namespace because an empty namespace was given!");
			return true;
		}
		if (!CommandAPIHandler.NAMESPACE_PATTERN.matcher(namespace).matches()) {
			CommandAPI.logNormal("Registering comand '" + commandName + "' using the default namespace because an invalid namespace (" + namespace + ") was given. Only 0-9, a-z, underscores, periods and hyphens are allowed!");
			return true;
		}
		return false;
	}

}
