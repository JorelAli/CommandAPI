package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Handles logic for registering commands after Paper build 65, where <a href="https://github.com/PaperMC/Paper/pull/8235">https://github.com/PaperMC/Paper/pull/8235</a>
 * changed a bunch of the behind-the-scenes logic.
 */
public class PaperCommandRegistration<Source> extends CommandRegistrationStrategy<Source> {
	// References to necessary methods
	private final Supplier<CommandDispatcher<Source>> getBrigadierDispatcher;
	private final Predicate<CommandNode<Source>> isBukkitCommand;

	// Store registered commands nodes for eventual reloads
	private final RootCommandNode<Source> registeredNodes = new RootCommandNode<>();

	public PaperCommandRegistration(Supplier<CommandDispatcher<Source>> getBrigadierDispatcher, Predicate<CommandNode<Source>> isBukkitCommand) {
		this.getBrigadierDispatcher = getBrigadierDispatcher;
		this.isBukkitCommand = isBukkitCommand;
	}

	// Provide access to internal functions that may be useful to developers
	/**
	 * Checks if a Brigadier command node came from wrapping a Bukkit command
	 *
	 * @param node The CommandNode to check
	 * @return true if the CommandNode is being handled by Paper's BukkitCommandNode
	 */
	public boolean isBukkitCommand(CommandNode<Source> node) {
		return isBukkitCommand.test(node);
	}

	// Implement CommandRegistrationStrategy methods
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
	public void registerCommandNode(LiteralCommandNode<Source> node, String namespace) {
		// Namespace is not empty on Bukkit forks
		CommandAPIHandler<?, ?, Source> commandAPIHandler = CommandAPIHandler.getInstance();
		LiteralCommandNode<Source> namespacedCommandNode = commandAPIHandler.namespaceNode(node, namespace);

		// Register nodes
		RootCommandNode<Source> dispatcherRoot = getBrigadierDispatcher.get().getRoot();
		dispatcherRoot.addChild(node);
		dispatcherRoot.addChild(namespacedCommandNode);

		// Track registered command nodes for reloads
		registeredNodes.addChild(node);
		registeredNodes.addChild(namespacedCommandNode);
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		// Remove nodes from the  dispatcher
		removeBrigadierCommands(getBrigadierDispatcher.get().getRoot(), commandName, unregisterNamespaces,
			// If we are unregistering a Bukkit command, ONLY unregister BukkitCommandNodes
			// If we are unregistering a Vanilla command, DO NOT unregister BukkitCommandNodes
			c -> !unregisterBukkit ^ isBukkitCommand.test(c));

		// CommandAPI commands count as non-Bukkit
		if (!unregisterBukkit) {
			// Don't add nodes back after a reload
			removeBrigadierCommands(registeredNodes, commandName, unregisterNamespaces, c -> true);
		}

		// Update the dispatcher file
		CommandAPIHandler.getInstance().writeDispatcherToFile();
	}

	@Override
	public void preReloadDataPacks() {
		RootCommandNode<Source> root = getBrigadierDispatcher.get().getRoot();
		for (CommandNode<Source> commandNode : registeredNodes.getChildren()) {
			root.addChild(commandNode);
		}
	}
}
