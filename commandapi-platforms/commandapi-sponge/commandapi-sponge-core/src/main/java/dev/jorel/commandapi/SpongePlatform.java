package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.commandsenders.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.AbstractPlayer;
import org.spongepowered.api.command.manager.CommandManager;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.List;

// See https://docs.spongepowered.org/stable/en/plugin/migrating-from-7-to-8.html#command-creation-and-registration

// TODO: How does Sponge send commands and interact with Brigadier?
public class SpongePlatform extends AbstractPlatform<Object> {
	private CommandManager commandManager;

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable(Object pluginObject) {
		CommandAPISpongePluginWrapper plugin = (CommandAPISpongePluginWrapper) pluginObject;

		commandManager = plugin.getServer().commandManager();
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void registerPermission(String string) {
		return; // Unsurprisingly, Sponge doesn't have a dumb permission system!
	}


	@Override
	public void registerHelp() {
		return; // Nothing to do here - TODO: Sponge doesn't have help?
	}

	@Override
	public void unregister(String commandName, boolean force) {
//		commandManager.unregister(commandName);
	}

	@Override
	public CommandDispatcher<Object> getBrigadierDispatcher() {
		// TODO: How do we get this? Do we need access to sponge internals?
		return null;
	}

	@Override
	public CommandAPILogger getLogger() {
		// TODO: Make a default Logger
		return super.getLogger();
	}

	@Override
	public AbstractCommandSender<? extends Object> getSenderForCommand(CommandContext<Object> cmdCtx,
			boolean forceNative) {
		// TODO: Do something with this?
		cmdCtx.getSource();
		return null;
	}

	@Override
	public Object getBrigadierSourceFromCommandSender(AbstractCommandSender<?> sender) {
		return (Object) sender.getSource();
	}

	@Override
	public SuggestionProvider<Object> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postCommandRegistration(LiteralCommandNode<Object> resultantNode,
			List<LiteralCommandNode<Object>> aliasNodes) {
		return; // Say hi, do checks etc.
	}

	@Override
	public LiteralCommandNode<Object> registerCommandNode(LiteralArgumentBuilder<Object> node) {
		return null;
	}

	@Override
	public AbstractCommandSender<?> getCommandSenderFromCommandSource(Object cs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reloadDataPacks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRequirements(AbstractPlayer<?> player) {
		commandManager.updateCommandTreeForPlayer((ServerPlayer) player.getSource());
	}

}
