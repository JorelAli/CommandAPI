package dev.jorel.commandapi;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.PlayerAvailableCommandsEvent;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandnodes.DifferentClientNode;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CommandAPIVelocity implements CommandAPIPlatform<Argument<?>, CommandSource, CommandSource> {

	// References to utility classes
	private static CommandAPIVelocity instance;
	private static InternalVelocityConfig config;

	private CommandManager commandManager;
	private CommandDispatcher<CommandSource> dispatcher;

	public CommandAPIVelocity() {
		CommandAPIVelocity.instance = this;
	}

	public static CommandAPIVelocity get() {
		if (instance != null) {
			return instance;
		} else {
			throw new IllegalStateException("Tried to access CommandAPIVelocity instance, but it was null! Are you using CommandAPI features before calling CommandAPI#onLoad?");
		}
	}

	public static InternalVelocityConfig getConfiguration() {
		if (config != null) {
			return config;
		} else {
			throw new IllegalStateException("Tried to access InternalVelocityConfig, but it was null! Did you load the CommandAPI properly with CommandAPI#onLoad?");
		}
	}

	@Override
	public void onLoad(CommandAPIConfig<?> config) {
		if (config instanceof CommandAPIVelocityConfig velocityConfig) {
			CommandAPIVelocity.setInternalConfig(new InternalVelocityConfig(velocityConfig));
		} else {
			CommandAPI.logError("CommandAPIVelocity was loaded with non-Velocity config!");
			CommandAPI.logError("Attempts to access Velocity-specific config variables will fail!");
		}

		commandManager = getConfiguration().getServer().getCommandManager();

		// We can't use a SafeVarHandle here because we don't have direct access to the
		//  `com.velocitypowered.proxy.command.VelocityCommandManager` class that holds the field.
		//  That only exists in the proxy dependency, but we are using velocity-api.
		//  However, we can get the class here through `commandManager.getClass()`.
		Field dispatcherField = CommandAPIHandler.getField(commandManager.getClass(), "dispatcher");
		try {
			dispatcher = (CommandDispatcher<CommandSource>) dispatcherField.get(commandManager);
		} catch (Exception ignored) {
			CommandAPI.logError("Could not access Velocity's Brigadier CommandDispatcher");
		}
	}

	private static void setInternalConfig(InternalVelocityConfig internalVelocityConfig) {
		CommandAPIVelocity.config = internalVelocityConfig;
	}

	@Override
	public void onEnable() {
		// Register events
		config.getServer().getEventManager().register(config.getPlugin(), this);
	}

	@Subscribe
	@SuppressWarnings("UnstableApiUsage") // This event is marked @Beta
	public void onCommandsSentToPlayer(PlayerAvailableCommandsEvent event) {
		// Rewrite nodes to their client-side version when commands are sent to a client
		DifferentClientNode.rewriteAllChildren(event.getPlayer(), (RootCommandNode<CommandSource>) event.getRootNode());
	}

	@Override
	public void onDisable() {
		// Nothing to do
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces) {
		commandManager.unregister(commandName);
	}

	@Override
	public CommandDispatcher<CommandSource> getBrigadierDispatcher() {
		return dispatcher;
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandSource> brigadierDispatcher) throws IOException {
		Files.asCharSink(file, StandardCharsets.UTF_8).write(new GsonBuilder().setPrettyPrinting().create()
			.toJson(serializeNodeToJson(dispatcher, dispatcher.getRoot())));
	}

	private static JsonObject serializeNodeToJson(CommandDispatcher<CommandSource> dispatcher, CommandNode<CommandSource> node) {
		JsonObject output = new JsonObject();
		if (node instanceof RootCommandNode) {
			output.addProperty("type", "root");
		} else if (node instanceof LiteralCommandNode) {
			output.addProperty("type", "literal");
		} else if (node instanceof ArgumentCommandNode) {
			ArgumentType<?> type = ((ArgumentCommandNode<?, ?>) node).getType();
			output.addProperty("type", "argument");
			output.addProperty("argumentType", type.getClass().getName());
			// In Bukkit, serializing to json is handled internally
			// They have an internal registry that connects ArgumentTypes to serializers that can
			//  include the specific properties of each argument as well (eg. min/max for an Integer)
			// Velocity doesn't seem to have an internal map like this, but we could create our own
			// In the meantime, I think it's okay to leave out properties here
		} else {
			CommandAPI.logError("Could not serialize node %s (%s)!".formatted(node, node.getClass()));
			output.addProperty("type", "unknown");
		}

		JsonObject children = new JsonObject();

		for (CommandNode<CommandSource> child : node.getChildren()) {
			children.add(child.getName(), serializeNodeToJson(dispatcher, child));
		}

		if (children.size() > 0) {
			output.add("children", children);
		}

		if (node.getCommand() != null) {
			output.addProperty("executable", true);
		}

		if (node.getRedirect() != null) {
			Collection<String> redirectPath = dispatcher.getPath(node.getRedirect());
			if (!redirectPath.isEmpty()) {
				JsonArray redirectInfo = new JsonArray();
				redirectPath.forEach(redirectInfo::add);
				output.add("redirect", redirectInfo);
			}
		}

		return output;
	}

	@Override
	public CommandAPILogger getLogger() {
		return CommandAPILogger.fromApacheLog4jLogger(LogManager.getLogger("CommandAPI"));
	}

	// Velocity's CommandSender and Source are the same, so these two methods are easy
	@Override
	public CommandSource getCommandSenderFromCommandSource(CommandSource commandSource) {
		return commandSource;
	}

	@Override
	public CommandSource getBrigadierSourceFromCommandSender(CommandSource sender) {
		return sender;
	}

	@Override
	public SuggestionProvider<CommandSource> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		// There isn't a Velocity Argument that implements CustomProvidedArgument yet, so this method is not used
		// If you want to use provided suggestions on an argument, implement this method.
		return (context, builder) -> Suggestions.empty();
	}

	/**
	 * {@inheritDoc}
	 * On Velocity, namespaces may be empty, but can only contain 0-9, a-z, underscores, periods, and hyphens.
	 */
	@Override
	public String validateNamespace(ExecutableCommand<?, CommandSource> command, String namespace) {
		if (!CommandAPIHandler.NAMESPACE_PATTERN.matcher(namespace).matches()) {
			CommandAPI.logNormal("Registering comand '" + command.getName() + "' using the default namespace because an invalid namespace (" + namespace + ") was given. Only 0-9, a-z, underscores, periods and hyphens are allowed!");
			return config.getNamespace();
		}

		// Namespace is good, return it
		return namespace;
	}

	@Override
	public void preCommandRegistration(String commandName) {
		// Nothing to do
	}

	@Override
	public void postCommandRegistration(RegisteredCommand<CommandSource> registeredCommand, LiteralCommandNode<CommandSource> resultantNode, List<LiteralCommandNode<CommandSource>> aliasNodes) {
		// Nothing to do
	}

	@Override
	public void registerCommandNode(LiteralCommandNode<CommandSource> node, String namespace) {
		// Register the main node
		getBrigadierDispatcher().getRoot().addChild(node);

		// Register the namespaced node if it is not empty
		if (!namespace.isEmpty()) {
			getBrigadierDispatcher().getRoot().addChild(
				CommandAPIHandler.<Argument<?>, CommandSource, CommandSource>getInstance().namespaceNode(node, namespace)
			);
		}
	}

	@Override
	public void reloadDataPacks() {
		// Nothing to do, Velocity does not have data packs
	}

	@Override
	public void updateRequirements(CommandSource player) {
		// TODO Auto-generated method stub
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
	public Predicate<CommandSource> getPermissionCheck(CommandPermission permission) {
		final Predicate<CommandSource> senderCheck;

		if (permission.equals(CommandPermission.NONE)) {
			// No permissions always passes
			senderCheck = CommandPermission.TRUE();
		} else if (permission.equals(CommandPermission.OP)) {
			// Console is op, and other senders (Players) are not
			senderCheck = ConsoleCommandSource.class::isInstance;
		} else {
			Optional<String> permissionStringWrapper = permission.getPermission();
			if (permissionStringWrapper.isPresent()) {
				String permissionString = permissionStringWrapper.get();
				// check permission
				senderCheck = sender -> sender.hasPermission(permissionString);
			} else {
				// No permission always passes
				senderCheck = CommandPermission.TRUE();
			}
		}

		// Negate if specified
		return permission.isNegated() ? senderCheck.negate() : senderCheck;
	}
}
