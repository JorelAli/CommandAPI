package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class CommandRegistrationStrategy<Source> {
	// Utility methods
	protected void removeBrigadierCommands(RootCommandNode<Source> root, String commandName,
										   boolean unregisterNamespaces, Predicate<CommandNode<Source>> extraCheck) {
		Map<String, CommandNode<Source>> children = CommandAPIHandler.getCommandNodeChildren(root);
		Map<String, LiteralCommandNode<Source>> literals = CommandAPIHandler.getCommandNodeLiterals(root);
		Map<String, ArgumentCommandNode<Source, ?>> arguments = CommandAPIHandler.getCommandNodeArguments(root);

		removeCommandFromMapIfCheckPasses(children, commandName, extraCheck);
		removeCommandFromMapIfCheckPasses(literals, commandName, extraCheck);
		// Commands should really only be represented as literals, but it is technically possible
		// to put an ArgumentCommandNode in the root, so we'll check
		removeCommandFromMapIfCheckPasses(arguments, commandName, extraCheck);

		if (unregisterNamespaces) {
			removeCommandNamespace(children, commandName, extraCheck);
			removeCommandNamespace(literals, commandName, extraCheck);
			removeCommandNamespace(arguments, commandName, extraCheck);
		}
	}

	protected static <T> void removeCommandNamespace(Map<String, ? extends T> map, String commandName, Predicate<T> extraCheck) {
		for (String key : new HashSet<>(map.keySet())) {
			if (!isThisTheCommandButNamespaced(commandName, key)) continue;

			removeCommandFromMapIfCheckPasses(map, key, extraCheck);
		}
	}

	protected static <T> void removeCommandFromMapIfCheckPasses(Map<String, ? extends T> map, String key, Predicate<T> extraCheck) {
		T element = map.get(key);
		if (element == null) return;
		if (extraCheck.test(element)) map.remove(key);
	}

	protected static boolean isThisTheCommandButNamespaced(String commandName, String key) {
		if (!key.contains(":")) return false;
		String[] split = key.split(":");
		if (split.length < 2) return false;
		return split[1].equalsIgnoreCase(commandName);
	}

	// Behavior methods
	public abstract CommandDispatcher<Source> getBrigadierDispatcher();

	public abstract void runTasksAfterServerStart();

	public abstract void postCommandRegistration(List<RegisteredCommand> registeredCommands, LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes);

	public abstract void registerCommandNode(LiteralCommandNode<Source> node, String namespace);

	public abstract void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit);

	public abstract void preReloadDataPacks();
}
