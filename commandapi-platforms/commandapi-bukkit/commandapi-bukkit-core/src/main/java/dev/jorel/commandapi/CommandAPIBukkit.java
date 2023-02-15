package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.*;
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.preprocessor.Unimplemented;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Keyed;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.help.HelpTopic;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static dev.jorel.commandapi.preprocessor.Unimplemented.REASON.*;

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

	// Static VarHandles
	private static final VarHandle COMMANDNODE_CHILDREN;
	private static final VarHandle COMMANDNODE_LITERALS;
	private static final VarHandle COMMANDNODE_ARGUMENTS;

	// Compute all var handles all in one go so we don't do this during main server runtime
	static {
		VarHandle commandNodeChildren = null;
		VarHandle commandNodeLiterals = null;
		VarHandle commandNodeArguments = null;
		try {
			commandNodeChildren = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup())
				.findVarHandle(CommandNode.class, "children", Map.class);
			commandNodeLiterals = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup())
				.findVarHandle(CommandNode.class, "literals", Map.class);
			commandNodeArguments = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup())
				.findVarHandle(CommandNode.class, "arguments", Map.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		COMMANDNODE_CHILDREN = commandNodeChildren;
		COMMANDNODE_LITERALS = commandNodeLiterals;
		COMMANDNODE_ARGUMENTS = commandNodeArguments;
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
		} else {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning(
					"Could not hook into the NBT API for NBT support. Download it from https://www.spigotmc.org/resources/nbt-api.7939/");
			}
		}

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
			paper = new PaperImplementations(false, this);
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Paper for /minecraft:reload. Consider upgrading to Paper: https://papermc.io/");
			}
		}
	}

	@Override
	public void onEnable() {
		JavaPlugin plugin = config.getPlugin();

		// Prevent command registration after server has loaded
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			CommandAPI.stopCommandRegistration();

			// Sort out permissions after the server has finished registering them all
			fixPermissions();
			reloadDataPacks();
			updateHelpForCommands();
		}, 0L);

		// (Re)send command graph packet to players when they join
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {

			// For some reason, any other priority doesn't work
			@EventHandler(priority = EventPriority.MONITOR)
			public void onPlayerJoin(PlayerJoinEvent e) {
				resendPackets(e.getPlayer());
			}

		}, plugin);

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
				for(Command command : new Command[] { map.getCommand(cmdName), map.getCommand("minecraft:" + cmdName) }) {
					if (command != null && isVanillaCommandWrapper(command)) {
						command.setPermission(permNode);
					}
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
		sb.append(ChatColor.GOLD).append("Usage: ").append(ChatColor.WHITE);

		// Generate usages
		List<String> usages = new ArrayList<>();
		for (RegisteredCommand rCommand : CommandAPIHandler.getInstance().registeredCommands) {
			if (rCommand.commandName().equals(command.commandName())) {
				StringBuilder usageString = new StringBuilder();
				usageString.append("/").append(command.commandName()).append(" ");
				for (String arg : rCommand.argsAsStr()) {
					usageString.append("<").append(arg.split(":")[0]).append("> ");
				}
				usages.add(usageString.toString().trim());
			}
		}

		// If 1 usage, put it on the same line, otherwise format like a list
		if (usages.isEmpty()) {
			throw new IllegalStateException("Empty usage list when generating help! " + 
				"This should never happen - if you're seeing this message, please" +
				"contact the developers of the CommandAPI, we'd love to know how you managed to get this error!");
		} else if (usages.size() == 1) {
			sb.append(usages.get(0));
		} else {
			for (String usage : usages) {
				sb.append("\n- ").append(usage);
			}
		}
	}

	void updateHelpForCommands() {
		Map<String, HelpTopic> helpTopicsToAdd = new HashMap<>();

		for (RegisteredCommand command : CommandAPIHandler.getInstance().registeredCommands) {
			// Generate short description
			final String shortDescription;
			final Optional<String> shortDescriptionOptional = command.shortDescription();
			final Optional<String> fullDescriptionOptional = command.fullDescription();
			if (shortDescriptionOptional.isPresent()) {
				shortDescription = shortDescriptionOptional.get();
			} else if (fullDescriptionOptional.isPresent()) {
				shortDescription = fullDescriptionOptional.get();
			} else {
				shortDescription = "A Mojang provided command.";
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

		addToHelpMap(helpTopicsToAdd);
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
	public void postCommandRegistration(LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {
		// Nothing to do
	}

	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node) {
		return getBrigadierDispatcher().register(node);
	}

	@Override
	public void unregister(String commandName, boolean force) {
		if (CommandAPI.getConfiguration().hasVerboseOutput()) {
			CommandAPI.logInfo("Unregistering command /" + commandName);
		}

		// Get the child nodes from the loaded dispatcher class
		Map<String, CommandNode<?>> commandNodeChildren = (Map<String, CommandNode<?>>) COMMANDNODE_CHILDREN
			.get(getBrigadierDispatcher().getRoot());

		if (force) {
			// Remove them by force
			for (String key : new HashSet<>(commandNodeChildren.keySet())) {
				if (key.contains(":") && key.split(":")[1].equalsIgnoreCase(commandName)) {
					commandNodeChildren.remove(key);
				}
			}
		}

		// Otherwise, just remove them normally
		commandNodeChildren.remove(commandName);
		((Map<String, CommandNode<?>>) COMMANDNODE_LITERALS.get(getBrigadierDispatcher().getRoot())).remove(commandName);
		((Map<String, CommandNode<?>>) COMMANDNODE_ARGUMENTS.get(getBrigadierDispatcher().getRoot())).remove(commandName);
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
	}

	@Override
	@Unimplemented(because = VERSION_SPECIFIC_IMPLEMENTATION)
	public abstract void reloadDataPacks();

	@Override
	public void updateRequirements(AbstractPlayer<?> player) {
		resendPackets((Player) player.getSource());
	}

	@Override
	public Argument<String> newConcreteMultiLiteralArgument(String[] literals) {
		return new MultiLiteralArgument(literals);
	}

	@Override
	public Argument<String> newConcreteLiteralArgument(String literal) {
		return new LiteralArgument(literal);
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
			}
		}
		
	}
}
