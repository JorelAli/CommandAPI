package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Handles logic for registering commands after Paper build 65, where <a href="https://github.com/PaperMC/Paper/pull/8235">https://github.com/PaperMC/Paper/pull/8235</a>
 * changed a bunch of the behind-the-scenes logic.
 */
public class PaperCommandRegistration<Source> extends CommandRegistrationStrategy<Source> {
	// References to necessary objects
	private final Supplier<CommandDispatcher<Source>> getBrigadierDispatcher;

	// Store registered commands nodes for eventual reloads
	private final List<LiteralCommandNode<Source>> registeredNodes = new ArrayList<>();

	public PaperCommandRegistration(Supplier<CommandDispatcher<Source>> getBrigadierDispatcher) {
		this.getBrigadierDispatcher = getBrigadierDispatcher;
	}

	@Override
	public CommandDispatcher<Source> getBrigadierDispatcher() {
		return getBrigadierDispatcher.get();
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
		LiteralCommandNode<Source> commandNode = getBrigadierDispatcher.get().register(node);
		LiteralCommandNode<Source> namespacedCommandNode = CommandAPIHandler.getInstance().namespaceNode(commandNode, namespace);

		// Add to registered command nodes
		registeredNodes.add(commandNode);
		registeredNodes.add(namespacedCommandNode);

		// Namespace is not empty on Bukkit forks
		getBrigadierDispatcher.get().getRoot().addChild(namespacedCommandNode);

		return commandNode;
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		// Remove nodes from the  dispatcher
		removeBrigadierCommands(getBrigadierDispatcher.get(), commandName, unregisterNamespaces, c -> true);

		// Update the dispatcher file
		CommandAPIHandler.getInstance().writeDispatcherToFile();
	}

	@Override
	public void preReloadDataPacks() {
		for (LiteralCommandNode<Source> commandNode : registeredNodes) {
			getBrigadierDispatcher.get().getRoot().addChild(commandNode);
		}
	}
}
