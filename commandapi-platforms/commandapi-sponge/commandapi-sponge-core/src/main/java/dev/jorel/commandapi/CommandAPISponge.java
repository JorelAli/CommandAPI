package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.AbstractPlayer;
import dev.jorel.commandapi.commandsenders.SpongeCommandSender;
import dev.jorel.commandapi.commandsenders.SpongePlayer;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.command.manager.CommandManager;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

// See https://docs.spongepowered.org/stable/en/plugin/migrating-from-7-to-8.html#command-creation-and-registration

// TODO: How does Sponge send commands and interact with Brigadier?
public class CommandAPISponge implements CommandAPIPlatform<Argument<?>, CommandCause, CommandCause> {
	private CommandManager commandManager;
	private CommandDispatcher<CommandCause> dispatcher;

	private static CommandAPISponge instance;
	private static InternalSpongeConfig config;

	public CommandAPISponge() {
		instance = this;
	}

	public static CommandAPISponge get() {
		if(instance != null) {
			return instance;
		} else {
			throw new IllegalStateException("Tried to access CommandAPISponge instance, but it was null! Are you using CommandAPI features before calling CommandAPI#onLoad?");
		}
	}

	public static InternalSpongeConfig getConfiguration() {
		if(config != null) {
			return config;
		} else {
			throw new IllegalStateException("Tried to access InternalSpongeConfig, but it was null! Did you load the CommandAPI properly with CommandAPI#onLoad?");
		}
	}

	@Override
	public void onLoad(CommandAPIConfig<?> config) {
		if(config instanceof CommandAPISpongeConfig spongeConfig) {
			CommandAPISponge.config = new InternalSpongeConfig(spongeConfig);
		} else {
			CommandAPI.logError("CommandAPISponge was loaded with non-Sponge config!");
			CommandAPI.logError("Attempts to access Sponge-specific config variables will fail!");
		}
	}

	@Override
	public void onEnable() {
		commandManager = config.getServer().commandManager();
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void registerPermission(String string) {
		return; // Unsurprisingly, Sponge doesn't have a dumb permission system!
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces) {
//		commandManager.unregister(commandName);
	}

	@Override
	public CommandDispatcher<CommandCause> getBrigadierDispatcher() {
		return null;
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandCause> brigadierDispatcher) throws IOException {
		// TODO: Implement, probably similar to Velocity unless there is a Sponge method to do this like in Bukkit.
	}

	@Override
	public CommandAPILogger getLogger() {
		// TODO: Make a default Logger
		return CommandAPILogger.fromApacheLog4jLogger(LogManager.getLogger("[CommandAPI]"));
	}

	@Override
	public AbstractCommandSender<? extends CommandCause> getSenderForCommand(CommandContext<CommandCause> cmdCtx,
			boolean forceNative) {
		// TODO: Do something with this?
		cmdCtx.getSource();
		return null;
	}

	@Override
	public CommandCause getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandCause> sender) {
		return sender.getSource();
	}

	@Override
	public SpongeCommandSender<? extends CommandCause> wrapCommandSender(CommandCause cause) {
		return getCommandSenderFromCommandSource(cause);
	}

	@Override
	public SuggestionProvider<CommandCause> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		return (context, builder) -> Suggestions.empty();
	}

	@Override
	public void preCommandRegistration(String commandName) {
		// Nothing to do?
	}

	@Override
	public void postCommandRegistration(RegisteredCommand registeredCommand, LiteralCommandNode<CommandCause> resultantNode, List<LiteralCommandNode<CommandCause>> aliasNodes) {
		// Nothing to do?
	}

	@Override
	public LiteralCommandNode<CommandCause> registerCommandNode(LiteralArgumentBuilder<CommandCause> node) {
		return getBrigadierDispatcher().register(node);
	}

	@Override
	public SpongeCommandSender<? extends CommandCause> getCommandSenderFromCommandSource(CommandCause cs) {
		return new SpongePlayer(CommandCause.create());
	}

	@Override
	public void reloadDataPacks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRequirements(AbstractPlayer<?> player) {
		commandManager.updateCommandTreeForPlayer((ServerPlayer) player.getSource());
	}

	@Override
	public CommandAPICommand newConcreteCommandAPICommand(CommandMetaData<CommandCause> meta) {
		return new CommandAPICommand(meta);
	}

	@Override
	public Argument<String> newConcreteMultiLiteralArgument(String nodeName, String[] literals) {
		return new MultiLiteralArgument(nodeName, literals);
	}

	@Override
	public Argument<String> newConcreteLiteralArgument(String nodeName, String literal) {
		return new LiteralArgument(nodeName, literal);
	}
}
