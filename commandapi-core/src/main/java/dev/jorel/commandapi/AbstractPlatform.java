package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.AbstractArgument;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.AbstractPlayer;

import java.io.IOException;
import java.util.List;

/**
 * @param <Argument> The implementation of AbstractArgument used for the platform
 * @param <CommandSender> The class for running platforms commands
 * @param <Source> The class for running Brigadier commands
 */
public abstract class AbstractPlatform<Argument extends AbstractArgument<?, ?, Argument, CommandSender>, CommandSender, Source> {
	// Platform-specific loading, enabling, and disabling tasks
	public abstract void onLoad();

	public abstract void onEnable(Object plugin);

	public abstract void onDisable();

	// "Source" in this case (for CommandContext<Source>) is something like a
	// CommandListenerWrapper (Spigot mappings) or CommandSourceStack (Mojang mappings).
	// over
	public abstract AbstractCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<Source> cmdCtx, boolean forceNative);

	// Converts a command source into its source.
	public abstract AbstractCommandSender<? extends CommandSender> getCommandSenderFromCommandSource(Source cs);

	// Converts a CommandSender to a Brigadier Source
	public abstract Source getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> sender);

	public abstract AbstractCommandSender<? extends CommandSender> wrapCommandSender(CommandSender sender);

	// Registers a permission. Bukkit's permission system requires permissions to be "registered"
	// before they can be used.
	public abstract void registerPermission(String string);

	// Some commands have existing suggestion providers
	public abstract SuggestionProvider<Source> getSuggestionProvider(SuggestionProviders suggestionProvider);


	/**
	 * Stuff to run before a command is generated. For Bukkit, this involves checking
	 * if a command was declared in the plugin.yml when it isn't supposed to be.
	 *
	 * @param commandName The name of the command about to be registered
	 */
	public abstract void preCommandRegistration(String commandName);

	/**
	 * Stuff to run after a command has been generated. For Bukkit, this involves
	 * finding command ambiguities for logging and generating the command JSON
	 * dispatcher file.
	 *
	 * @param resultantNode the node that was registered
	 * @param aliasNodes    any alias nodes that were also registered as a part of this registration process
	 */
	public abstract void postCommandRegistration(LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) throws IOException;

	/**
	 * Registers a Brigadier command node and returns the built node.
	 */
	public abstract LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node);


	/**
	 * Unregisters a command from the CommandGraph so it can't be run anymore.
	 *
	 * @param commandName the name of the command to unregister
	 * @param force       whether the unregistration system should attempt to remove
	 *                    all instances of the command, regardless of whether they
	 *                    have been registered by Minecraft, Bukkit or Spigot etc.
	 */
	public abstract void unregister(String commandName, boolean force);

	public abstract CommandDispatcher<Source> getBrigadierDispatcher();

	/**
	 * Creates a default Logger for the CommandAPI
	 *
	 * @return A new Logger meant for the CommandAPI to use
	 */
	public CommandAPILogger getLogger() {
		return new CommandAPILogger(){
			private static final String PREFIX = "[CommandAPI] ";
			private static final String YELLOW = "\u001B[33m";
			private static final String RED = "\u001B[31m";
			private static final String RESET = "\u001B[0m";

			@Override
			public void info(String message) {
				System.out.println(PREFIX + message);
			}

			@Override
			public void warning(String message) {
				System.out.println(YELLOW + PREFIX + message + RESET);
			}

			@Override
			public void severe(String message) {
				System.out.println(RED + PREFIX + message + RESET);
			}
		};
	}

	public abstract void reloadDataPacks();

	public abstract void updateRequirements(AbstractPlayer<?> player);

	public abstract AbstractCommandAPICommand<?, Argument, CommandSender> newConcreteCommandAPICommand(CommandMetaData<CommandSender> meta);

	public abstract Argument newConcreteMultiLiteralArgument(String[] literals);

	public abstract Argument newConcreteLiteralArgument(String literal);
}
