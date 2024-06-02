package dev.jorel.commandapi;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.jorel.commandapi.preprocessor.RequireField;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.util.*;
import java.util.function.Supplier;

/**
 * Handles logic for registering commands on Spigot and old versions of Paper
 */
@RequireField(in = SimpleCommandMap.class, name = "knownCommands", ofType = Map.class)
public class SpigotCommandRegistration<Source> extends CommandRegistrationStrategy<Source> {
	private final CommandAPIBukkit<Source> commandAPIBukkit;

	// References to necessary objects
	private final CommandDispatcher<Source> brigadierDispatcher;
	private final Supplier<CommandDispatcher<Source>> getResourcesDispatcher;

	// Namespaces
	private final Set<String> namespacesToFix = new HashSet<>();
	private RootCommandNode<Source> minecraftCommandNamespaces = new RootCommandNode<>();

	// Reflection
	private final SafeVarHandle<SimpleCommandMap, Map<String, Command>> commandMapKnownCommands;

	public SpigotCommandRegistration(CommandAPIBukkit<Source> commandAPIBukkit, CommandDispatcher<Source> brigadierDispatcher, Supplier<CommandDispatcher<Source>> getResourcesDispatcher) {
		this.commandAPIBukkit = commandAPIBukkit;

		this.brigadierDispatcher = brigadierDispatcher;
		this.getResourcesDispatcher = getResourcesDispatcher;

		this.commandMapKnownCommands = SafeVarHandle.ofOrNull(SimpleCommandMap.class, "knownCommands", "knownCommands", Map.class);
	}

	// Utility methods
	private String unpackInternalPermissionNodeString(CommandPermission perm) {
		final Optional<String> optionalPerm = perm.getPermission();
		if (perm.isNegated() || perm.equals(CommandPermission.NONE) || perm.equals(CommandPermission.OP)) {
			return "";
		} else if (optionalPerm.isPresent()) {
			return optionalPerm.get();
		} else {
			throw new IllegalStateException("Invalid permission detected: " + perm +
				"! This should never happen - if you're seeing this message, please" +
				"contact the developers of the CommandAPI, we'd love to know how you managed to get this error!");
		}
	}

	// Implement CommandRegistrationStrategy methods
	@Override
	public CommandDispatcher<Source> getBrigadierDispatcher() {
		return brigadierDispatcher;
	}

	@Override
	public void runTasksAfterServerStart() {
		// Fix namespaces first thing when starting the server
		fixNamespaces();
		// Sort out permissions after the server has finished registering them all
		fixPermissions();
	}

	private void fixNamespaces() {
		Map<String, Command> knownCommands = commandMapKnownCommands.get((SimpleCommandMap) commandAPIBukkit.getPaper().getCommandMap());
		CommandDispatcher<Source> resourcesDispatcher = getResourcesDispatcher.get();

		// Remove namespaces
		for (String command : namespacesToFix) {
			knownCommands.remove(command);
			removeBrigadierCommands(resourcesDispatcher, command, false, c -> true);
		}

		// Add back certain minecraft: namespace commands
		RootCommandNode<Source> resourcesRootNode = resourcesDispatcher.getRoot();
		RootCommandNode<Source> brigadierRootNode = brigadierDispatcher.getRoot();
		for (CommandNode<Source> node : minecraftCommandNamespaces.getChildren()) {
			knownCommands.put(node.getName(), commandAPIBukkit.wrapToVanillaCommandWrapper(node));
			resourcesRootNode.addChild(node);

			// VanillaCommandWrappers in the CommandMap defer to the Brigadier dispatcher when executing.
			// While the minecraft namespace usually does not exist in the Brigadier dispatcher, in the case of a
			//  command conflict we do need this node to exist separately from the unnamespaced version to keep the
			//  commands separate.
			brigadierRootNode.addChild(node);
		}
		// Clear minecraftCommandNamespaces for dealing with command conflicts after the server is enabled
		//  See `CommandAPIBukkit#postCommandRegistration`
		minecraftCommandNamespaces = new RootCommandNode<>();
	}

	/*
	 * Makes permission checks more "Bukkit" like and less "Vanilla Minecraft" like
	 */
	private void fixPermissions() {
		// Get the command map to find registered commands
		CommandMap map = commandAPIBukkit.getPaper().getCommandMap();
		final Map<String, CommandPermission> permissionsToFix = CommandAPIHandler.getInstance().registeredPermissions;

		if (!permissionsToFix.isEmpty()) {
			CommandAPI.logInfo("Linking permissions to commands:");

			for (Map.Entry<String, CommandPermission> entry : permissionsToFix.entrySet()) {
				String cmdName = entry.getKey();
				CommandPermission perm = entry.getValue();
				CommandAPI.logInfo("  " + perm.toString() + " -> /" + cmdName);

				final String permNode = unpackInternalPermissionNodeString(perm);

				/*
				 * Sets the permission. If you have to be OP to run this command, we set the
				 * permission to null. Doing so means that Bukkit's "testPermission" will always
				 * return true, however since the command's permission check occurs internally
				 * via the CommandAPI, this isn't a problem.
				 *
				 * If anyone dares tries to use testPermission() on this command, seriously,
				 * what are you doing and why?
				 */
				Command command = map.getCommand(cmdName);
				if(command != null && commandAPIBukkit.isVanillaCommandWrapper(command)) {
					command.setPermission(permNode);
				}
			}
		}
		CommandAPI.logNormal("Linked " + permissionsToFix.size() + " Bukkit permissions to commands");
	}

	@Override
	public void postCommandRegistration(RegisteredCommand registeredCommand, LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {

		if(!CommandAPI.canRegister()) {
			// Usually, when registering commands during server startup, we can just put our commands into the
			// `net.minecraft.server.MinecraftServer#vanillaCommandDispatcher` and leave it. As the server finishes setup,
			// it and the CommandAPI do some extra stuff to make everything work, and we move on.
			// So, if we want to register commands while the server is running, we need to do all that extra stuff, and
			// that is what this code does.
			// We could probably call all those methods to sync everything up, but in the spirit of avoiding side effects
			// and avoiding doing things twice for existing commands, this is a distilled version of those methods.

			Map<String, Command> knownCommands = commandMapKnownCommands.get((SimpleCommandMap) commandAPIBukkit.getPaper().getCommandMap());
			RootCommandNode<Source> root = getResourcesDispatcher.get().getRoot();

			String name = resultantNode.getLiteral();
			String namespace = registeredCommand.namespace();
			String permNode = unpackInternalPermissionNodeString(registeredCommand.permission());

			registerCommand(knownCommands, root, name, permNode, namespace, resultantNode);

			// Do the same for the aliases
			for (LiteralCommandNode<Source> node : aliasNodes) {
				registerCommand(knownCommands, root, node.getLiteral(), permNode, namespace, node);
			}

			Collection<CommandNode<Source>> minecraftNamespacesToFix = minecraftCommandNamespaces.getChildren();
			if (!minecraftNamespacesToFix.isEmpty()) {
				// Adding new `minecraft` namespace nodes to the Brigadier dispatcher
				//  usually happens in `CommandAPIBukkit#fixNamespaces`.
				// Note that the previous calls to `CommandAPIBukkit#registerCommand` in this method
				//  will have already dealt with adding the nodes here to the resources dispatcher.
				// We also have to set the permission to simulate the result of `CommandAPIBukkit#fixPermissions`.
				RootCommandNode<Source> brigadierRootNode = brigadierDispatcher.getRoot();
				for (CommandNode<Source> node : minecraftNamespacesToFix) {
					Command minecraftNamespaceCommand = commandAPIBukkit.wrapToVanillaCommandWrapper(node);
					knownCommands.put(node.getName(), minecraftNamespaceCommand);
					minecraftNamespaceCommand.setPermission(permNode);
					brigadierRootNode.addChild(node);
				}
				minecraftCommandNamespaces = new RootCommandNode<>();
			}
		}
	}

	private void registerCommand(Map<String, Command> knownCommands, RootCommandNode<Source> root, String name, String permNode, String namespace, LiteralCommandNode<Source> resultantNode) {
		// Wrapping Brigadier nodes into VanillaCommandWrappers and putting them in the CommandMap usually happens
		// in `CraftServer#setVanillaCommands`
		Command command = commandAPIBukkit.wrapToVanillaCommandWrapper(resultantNode);
		knownCommands.putIfAbsent(name, command);

		// Adding permissions to these Commands usually happens in `CommandAPIBukkit#fixPermissions`
		command.setPermission(permNode);

		// Adding commands to the other (Why bukkit/spigot?!) dispatcher usually happens in `CraftServer#syncCommands`
		root.addChild(resultantNode);

		// Handle namespace
		LiteralCommandNode<Source> namespacedNode = CommandAPIHandler.getInstance().namespaceNode(resultantNode, namespace);
		if (namespace.equals("minecraft")) {
			// The minecraft namespace version should be registered as a straight alias of the original command, since
			//  the `minecraft:name` node does not exist in the Brigadier dispatcher, which is referenced by
			//  VanillaCommandWrapper (note this is not true if there is a command conflict, but
			//  `CommandAPIBukkit#postCommandRegistration` will deal with this later using `minecraftCommandNamespaces`).
			knownCommands.putIfAbsent("minecraft:" + name, command);
		} else {
			// A custom namespace should be registered like a separate command, so that it can reference the namespaced
			//  node, rather than the original unnamespaced node
			Command namespacedCommand = commandAPIBukkit.wrapToVanillaCommandWrapper(namespacedNode);
			knownCommands.putIfAbsent(namespacedCommand.getName(), namespacedCommand);
			namespacedCommand.setPermission(permNode);
		}
		// In both cases, add the node to the resources dispatcher
		root.addChild(namespacedNode);
	}

	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node, String namespace) {
		RootCommandNode<Source> rootNode = brigadierDispatcher.getRoot();

		LiteralCommandNode<Source> builtNode = node.build();
		String name = node.getLiteral();
		if (namespace.equals("minecraft")) {
			if (namespacesToFix.contains("minecraft:" + name)) {
				// This command wants to exist as `minecraft:name`
				// However, another command has requested that `minecraft:name` be removed
				// We'll keep track of everything that should be `minecraft:name` in
				//  `minecraftCommandNamespaces` and fix this later in `#fixNamespaces`
				minecraftCommandNamespaces.addChild(CommandAPIHandler.getInstance().namespaceNode(builtNode, "minecraft"));
			}
		} else {
			// Make sure to remove the `minecraft:name` and
			//  `minecraft:namespace:name` commands Bukkit will create
			fillNamespacesToFix(name, namespace + ":" + name);

			// Create the namespaced node
			rootNode.addChild(CommandAPIHandler.getInstance().namespaceNode(builtNode, namespace));
		}

		// Add the main node to dispatcher
		//  We needed to wait until after `fillNamespacesToFix` was called to do this, in case a previous
		//  `minecraft:name` version of the command needed to be saved separately before this node was added
		rootNode.addChild(builtNode);

		return builtNode;
	}

	private void fillNamespacesToFix(String... namespacedCommands) {
		for (String namespacedCommand : namespacedCommands) {
			// We'll remove these commands later when fixNamespaces is called
			if (!namespacesToFix.add("minecraft:" + namespacedCommand)) {
				continue;
			}

			// If this is the first time considering this command for removal
			// and there is already a command with this name in the dispatcher
			// then, the command currently in the dispatcher is supposed to appear as `minecraft:command`
			CommandNode<Source> currentNode = brigadierDispatcher.getRoot().getChild(namespacedCommand);
			if(currentNode != null) {
				// We'll keep track of everything that should be `minecraft:command` in
				//  `minecraftCommandNamespaces` and fix this later in `#fixNamespaces`
				// TODO: Ideally, we should be working without this cast to LiteralCommandNode. I don't know if this can fail
				minecraftCommandNamespaces.addChild(CommandAPIHandler.getInstance().namespaceNode((LiteralCommandNode<Source>) currentNode, "minecraft"));
			}
		}
	}

	@Override
	public void unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit) {
		if(!unregisterBukkit) {
			// Remove nodes from the Vanilla dispatcher
			// This dispatcher doesn't usually have namespaced version of commands (those are created when commands
			//  are transferred to Bukkit's CommandMap), but if they ask, we'll do it
			removeBrigadierCommands(brigadierDispatcher, commandName, unregisterNamespaces, c -> true);

			// Update the dispatcher file
			CommandAPIHandler.getInstance().writeDispatcherToFile();
		}

		if(unregisterBukkit || !CommandAPI.canRegister()) {
			// We need to remove commands from Bukkit's CommandMap if we're unregistering a Bukkit command, or
			//  if we're unregistering after the server is enabled, because `CraftServer#setVanillaCommands` will have
			//  moved the Vanilla command into the CommandMap
			Map<String, Command> knownCommands = commandMapKnownCommands.get((SimpleCommandMap) commandAPIBukkit.getPaper().getCommandMap());

			// If we are unregistering a Bukkit command, DO NOT unregister VanillaCommandWrappers
			// If we are unregistering a Vanilla command, ONLY unregister VanillaCommandWrappers
			boolean isMainVanilla = commandAPIBukkit.isVanillaCommandWrapper(knownCommands.get(commandName));
			if(unregisterBukkit ^ isMainVanilla) knownCommands.remove(commandName);

			if(unregisterNamespaces) {
				removeCommandNamespace(knownCommands, commandName, c -> unregisterBukkit ^ commandAPIBukkit.isVanillaCommandWrapper(c));
			}
		}

		if(!CommandAPI.canRegister()) {
			// If the server is enabled, we have extra cleanup to do

			// Remove commands from the resources dispatcher
			// If we are unregistering a Bukkit command, ONLY unregister BukkitCommandWrappers
			// If we are unregistering a Vanilla command, DO NOT unregister BukkitCommandWrappers
			removeBrigadierCommands(getResourcesDispatcher.get(), commandName, unregisterNamespaces,
				c -> !unregisterBukkit ^ commandAPIBukkit.isBukkitCommandWrapper(c));
		}
	}
}
