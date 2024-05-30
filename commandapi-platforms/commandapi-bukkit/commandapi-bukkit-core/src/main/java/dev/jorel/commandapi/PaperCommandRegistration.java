package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.List;

/**
 * Handles logic for registering commands after Paper build 65, where https://github.com/PaperMC/Paper/pull/8235
 * changed a bunch of the behind-the-scenes logic.
 */
public class PaperCommandRegistration<Source> extends CommandRegistrationStrategy<Source> {
	// References to necessary objects
	private final CommandDispatcher<Source> brigadierDispatcher;

	public PaperCommandRegistration(CommandDispatcher<Source> brigadierDispatcher) {
		this.brigadierDispatcher = brigadierDispatcher;
	}

	@Override
	public CommandDispatcher<Source> getBrigadierDispatcher() {
		return brigadierDispatcher;
	}

	@Override
	public void runTasksAfterServerStart() {
		// Nothing to do
	}

	@Override
	public void postCommandRegistration(RegisteredCommand registeredCommand, LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {
		// Nothing to do
	}

	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node, String namespace) {
		LiteralCommandNode<Source> builtNode = brigadierDispatcher.register(node);

		// Namespace is not empty on Bukkit forks
		brigadierDispatcher.getRoot().addChild(CommandAPIHandler.getInstance().namespaceNode(builtNode, namespace));

		return builtNode;
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		// Remove nodes from the  dispatcher
		removeBrigadierCommands(brigadierDispatcher, commandName, unregisterNamespaces, c -> true);

		// Update the dispatcher file
		CommandAPIHandler.getInstance().writeDispatcherToFile();
	}
}
