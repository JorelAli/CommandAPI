package dev.jorel.commandapi;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.*;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

public class CommandAPIVelocity implements CommandAPIPlatform<Argument<?>, CommandSource, CommandSource> {
	private static CommandAPIVelocity instance;
	private static InternalVelocityConfig config;

	private CommandManager commandManager;
	private CommandDispatcher<CommandSource> dispatcher;

	public CommandAPIVelocity() {
		instance = this;
	}

	public static CommandAPIVelocity get() {
		if(instance != null) {
			return instance;
		} else {
			throw new IllegalStateException("Tried to access CommandAPIVelocity instance, but it was null! Are you using CommandAPI features before calling CommandAPI#onLoad?");
		}
	}

	public static InternalVelocityConfig getConfiguration() {
		if(config != null) {
			return config;
		} else {
			throw new IllegalStateException("Tried to access InternalVelocityConfig, but it was null! Did you load the CommandAPI properly with CommandAPI#onLoad?");
		}
	}

	@Override
	public void onLoad(CommandAPIConfig<?> config) {
		if(config instanceof CommandAPIVelocityConfig velocityConfig) {
			CommandAPIVelocity.config = new InternalVelocityConfig(velocityConfig);
		} else {
			CommandAPI.logError("CommandAPIVelocity was loaded with non-Velocity config!");
			CommandAPI.logError("Attempts to access Velocity-specific config variables will fail!");
		}

		commandManager = getConfiguration().getServer().getCommandManager();
		{
			Field dispatcherField = CommandAPIHandler.getField(commandManager.getClass(), "dispatcher");
			try {
				dispatcher = (CommandDispatcher<CommandSource>) dispatcherField.get(commandManager);
			} catch (Exception ignored) {
				CommandAPI.logError("Could not access Velocity's Brigadier CommandDispatcher");
			}
		}
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void registerPermission(String string) {
		// Unsurprisingly, Velocity doesn't have a dumb permission system!
	}

	@Override
	public void unregister(String commandName, boolean force) {
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

	@Override
	public VelocityCommandSender<? extends CommandSource> getSenderForCommand(CommandContext<CommandSource> cmdCtx, boolean forceNative) {
		// Velocity doesn't have proxy senders, so nothing needs to be done with forceNative
		return getCommandSenderFromCommandSource(cmdCtx.getSource());
	}

	@Override
	public VelocityCommandSender<? extends CommandSource> getCommandSenderFromCommandSource(CommandSource cs) {
		// Given a Brigadier CommandContext source (result of CommandContext.getSource),
		// we need to convert that to an AbstractCommandSender.
		if(cs instanceof ConsoleCommandSource ccs)
			return new VelocityConsoleCommandSender(ccs);
		if(cs instanceof Player p)
			return new VelocityPlayer(p);
		throw new IllegalArgumentException("Unknown CommandSource: " + cs);
	}

	@Override
	public VelocityCommandSender<? extends CommandSource> wrapCommandSender(CommandSource commandSource) {
		return getCommandSenderFromCommandSource(commandSource);
	}

	@Override
	public CommandSource getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSource> sender) {
		return sender.getSource();
	}

	@Override
	public SuggestionProvider<CommandSource> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		// There isn't a Velocity Argument that implements CustomProvidedArgument yet, so this method is not used
		// If you want to use provided suggestions on an argument, implement this method.
		return (context, builder) -> Suggestions.empty();
	}

	@Override
	public void preCommandRegistration(String commandName) {
		// Nothing to do
	}

	@Override
	public void postCommandRegistration(LiteralCommandNode<CommandSource> resultantNode, List<LiteralCommandNode<CommandSource>> aliasNodes) {
		// Nothing to do
	}

	@Override
	public LiteralCommandNode<CommandSource> registerCommandNode(LiteralArgumentBuilder<CommandSource> node) {
		BrigadierCommand command = new BrigadierCommand(node);
		commandManager.register(command);
		return command.getNode();
	}

	@Override
	public void reloadDataPacks() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateRequirements(AbstractPlayer<?> player) {
		// TODO Auto-generated method stub
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
	public AbstractCommandAPICommand<?, Argument<?>, CommandSource> newConcreteCommandAPICommand(CommandMetaData<CommandSource> meta) {
		return new CommandAPICommand(meta);
	}
}
