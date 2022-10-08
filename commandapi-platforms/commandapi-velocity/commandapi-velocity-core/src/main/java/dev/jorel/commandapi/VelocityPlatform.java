package dev.jorel.commandapi;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandSource;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.abstractions.AbstractPlatform;
import dev.jorel.commandapi.arguments.SuggestionProviders;

public class VelocityPlatform extends AbstractPlatform {
	
	private final CommandManager commandManager;
	
	public VelocityPlatform(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	@Override
	public void registerPermission(String string) {
		return; // Unsurprisingly, Velocity doesn't have a dumb permission system!
	}


	@Override
	public void registerHelp() {
		return; // Nothing to do here - TODO: Velocity doesn't have help?
	}

	@Override
	public void unregister(String commandName) {
		commandManager.unregister(commandName);
	}

	@Override
	public <Source> AbstractCommandSender<Source> getSenderForCommand(CommandContext<Source> cmdCtx,
			boolean forceNative) {
		// TODO: This method MAY be completely identical to getCommandSenderFromCommandSource.
		// In Bukkit, this is NOT the case - we have to apply certain changes based
		// on the command context - for example, if we're proxying another entity or
		// otherwise (e.g. native sender)
		
		// I'm fairly certain we don't have to do that in Velocity, so we'll just go straight
		// for this:
		return getCommandSenderFromCommandSource(cmdCtx.getSource());
	}

	@Override
	public <Source> AbstractCommandSender<Source> getCommandSenderFromCommandSource(Source cs) {
		// Given a Brigadier CommandContext source (result of CommandContext.getSource),
		// we need to convert that to an AbstractCommandSender.
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Source> SuggestionProvider<Source> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Source> void postCommandRegistration(LiteralCommandNode<Source> resultantNode,
			List<LiteralCommandNode<Source>> aliasNodes) {
		return; // Nothing left to do
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Source> LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node) {
		commandManager.register(new BrigadierCommand((LiteralArgumentBuilder<CommandSource>) node));
		return null; // TODO: Uhhhhhhhhhhhhhhhh how do we get this?
	}

}
