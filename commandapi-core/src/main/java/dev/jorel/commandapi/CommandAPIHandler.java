/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.ICustomProvidedArgument;
import dev.jorel.commandapi.arguments.IPreviewable;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
import dev.jorel.commandapi.arguments.PreviewInfo;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.PreviewableFunction;
import net.kyori.adventure.text.Component;

/**
 * Handles the main backend of the CommandAPI. This constructs brigadier Command
 * objects, applies and generates arguments and handles suggestions. This also
 * handles permission registration for Bukkit, interactions for NMS and the
 * registration and unregistration of commands.
 */
@RequireField(in = CommandNode.class, name = "children", ofType = Map.class)
@RequireField(in = CommandNode.class, name = "literals", ofType = Map.class)
@RequireField(in = CommandNode.class, name = "arguments", ofType = Map.class)
@RequireField(in = CommandContext.class, name = "arguments", ofType = Map.class)
public class CommandAPIHandler<CommandSourceStack> {

	private final static VarHandle COMMANDNODE_CHILDREN;
	private final static VarHandle COMMANDNODE_LITERALS;
	private final static VarHandle COMMANDNODE_ARGUMENTS;
	private final static VarHandle COMMANDCONTEXT_ARGUMENTS;

	// Compute all var handles all in one go so we don't do this during main server
	// runtime
	static {
		VarHandle commandNodeChildren = null;
		VarHandle commandNodeLiterals = null;
		VarHandle commandNodeArguments = null;
		VarHandle commandContextArguments = null;
		try {
			commandNodeChildren = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup())
					.findVarHandle(CommandNode.class, "children", Map.class);
			commandNodeLiterals = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup())
					.findVarHandle(CommandNode.class, "literals", Map.class);
			commandNodeArguments = MethodHandles.privateLookupIn(CommandNode.class, MethodHandles.lookup())
					.findVarHandle(CommandNode.class, "arguments", Map.class);
			commandContextArguments = MethodHandles.privateLookupIn(CommandContext.class, MethodHandles.lookup())
					.findVarHandle(CommandContext.class, "arguments", Map.class);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		COMMANDNODE_CHILDREN = commandNodeChildren;
		COMMANDNODE_LITERALS = commandNodeLiterals;
		COMMANDNODE_ARGUMENTS = commandNodeArguments;
		COMMANDCONTEXT_ARGUMENTS = commandContextArguments;
	}

	/**
	 * Returns the raw input for an argument for a given command context and its
	 * key. This effectively returns the string value that is currently typed for
	 * this argument
	 * 
	 * @param <CommandSourceStack> the command source type
	 * @param cmdCtx               the command context which is used to run this
	 *                             command
	 * @param key                  the node name for the argument
	 * @return the raw input string for this argument
	 */
	public static <CommandSourceStack> String getRawArgumentInput(CommandContext<CommandSourceStack> cmdCtx,
			String key) {
		final Map<String, ParsedArgument<CommandSourceStack, ?>> commandContextArgs = (Map<String, ParsedArgument<CommandSourceStack, ?>>) COMMANDCONTEXT_ARGUMENTS.get(cmdCtx);
		final ParsedArgument<CommandSourceStack, ?> parsedArgument = commandContextArgs.get(key);

		// TODO: Issue #310: Parsing this argument via /execute run <blah> doesn't have the value in
		// the arguments for this command context (most likely because it's a redirected command).
		// We need to figure out how to handle this case.
		if(parsedArgument != null) {
			StringRange range = parsedArgument.getRange();
			return cmdCtx.getInput().substring(range.getStart(), range.getEnd());
		} else {
			return "";
		}
	}

	private static CommandAPIHandler<?> instance;

	/**
	 * Returns the Singleton instance of the CommandAPI's internal handler
	 * 
	 * @return the Singleton instance of the CommandAPI's internal handler
	 */
	public static CommandAPIHandler<?> getInstance() {
		if (instance == null) {
			instance = new CommandAPIHandler<>();
		}
		return instance;
	}
	
	public static void onDisable() {
		if(instance != null) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				instance.NMS.unhookChatPreview(player);
			}
		}
		
		instance = null;
	}

	final Map<ClassCache, Field> FIELDS = new HashMap<>();
	final TreeMap<String, CommandPermission> PERMISSIONS_TO_FIX = new TreeMap<>();
	final NMS<CommandSourceStack> NMS;
	final CommandDispatcher<CommandSourceStack> DISPATCHER;
	final List<RegisteredCommand> registeredCommands; // Keep track of what has been registered for type checking
	final Map<List<String>, IPreviewable<? extends Argument<?>, ?>> previewableArguments; // Arguments with previewable chat
	private PaperImplementations paper;

	@SuppressWarnings("unchecked")
	private CommandAPIHandler() {
		final String bukkit = Bukkit.getServer().toString();
		if(CommandAPI.getConfiguration().getCustomNMS() != null) {
			NMS = (NMS<CommandSourceStack>) CommandAPI.getConfiguration().getCustomNMS();
		} else {
			NMS = CommandAPIVersionHandler
					.getNMS(bukkit.substring(bukkit.indexOf("minecraftVersion") + 17, bukkit.length() - 1));
		}
		DISPATCHER = NMS.getBrigadierDispatcher();
		registeredCommands = new ArrayList<>();
		previewableArguments = new HashMap<>();
		this.paper = new PaperImplementations(false, NMS);
	}

	void checkDependencies() {
		try {
			Class.forName("com.mojang.brigadier.CommandDispatcher");
		} catch (ClassNotFoundException e) {
			new ClassNotFoundException("Could not hook into Brigadier (Are you running Minecraft 1.13 or above?)")
					.printStackTrace();
		}

		// Log successful hooks
		final String nmsClassHierarchy;
		{
			List<String> nmsClassHierarchyList = new ArrayList<>();
			Class<?> nmsClass = NMS.getClass();
			while(nmsClass.getSuperclass() != null) {
				nmsClassHierarchyList.add(nmsClass.getSimpleName());
				nmsClass = nmsClass.getSuperclass();
			}
			nmsClassHierarchy = String.join(" > ", nmsClassHierarchyList);
		}
		
		CommandAPI.logInfo("Hooked into NMS " + nmsClassHierarchy + " (compatible with "
				+ String.join(", ", NMS.compatibleVersions()) + ")");

		// Checks other dependencies
		Class<?> nbtContainerClass = CommandAPI.getConfiguration().getNBTContainerClass();
		if (nbtContainerClass != null && CommandAPI.getConfiguration().getNBTContainerConstructor() != null) {
			CommandAPI.logNormal("Hooked into an NBT API with class " + nbtContainerClass.getName());
		} else {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning(
						"Could not hook into the NBT API for NBT support. Download it from https://www.spigotmc.org/resources/nbt-api.7939/");
			}
		}

		try {
			Class.forName("org.spigotmc.SpigotConfig");
			CommandAPI.logNormal("Hooked into Spigot successfully for Chat/ChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Spigot for Chat/ChatComponents");
			}
		}

		try {
			Class.forName("net.kyori.adventure.text.Component");
			CommandAPI.logNormal("Hooked into Adventure for AdventureChat/AdventureChatComponents");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning("Could not hook into Adventure for AdventureChat/AdventureChatComponents");
			}
		}

		try {
			Class.forName("io.papermc.paper.event.server.ServerResourcesReloadedEvent");
			paper = new PaperImplementations(true, NMS);
			CommandAPI.logNormal("Hooked into Paper for paper-specific API implementations");
		} catch (ClassNotFoundException e) {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.logWarning(
						"Could not hook into Paper for /minecraft:reload. Consider upgrading to Paper: https://papermc.io/");
			}
		}
	}

	/**
	 * Returns an instance of NMS
	 * 
	 * @return an instance of NMS
	 */
	public NMS<CommandSourceStack> getNMS() {
		return this.NMS;
	}

	/**
	 * Returns an instance of PaperImplementations
	 * 
	 * @return an instance of PaperImplementations
	 */
	public PaperImplementations getPaper() {
		return this.paper;
	}

	/**
	 * Unregisters a command from the NMS command graph.
	 * 
	 * 
	 * @param commandName the name of the command to unregister
	 * @param force       whether the unregistration system should attempt to remove
	 *                    all instances of the command, regardless of whether they
	 *                    have been registered by Minecraft, Bukkit or Spigot etc.
	 */
	void unregister(String commandName, boolean force) {
		if (CommandAPI.getConfiguration().hasVerboseOutput()) {
			CommandAPI.logInfo("Unregistering command /" + commandName);
		}

		// Get the child nodes from the loaded dispatcher class
		Map<String, CommandNode<?>> commandNodeChildren = (Map<String, CommandNode<?>>) COMMANDNODE_CHILDREN
				.get(DISPATCHER.getRoot());

		if (force) {
			// Remove them by force
			for (String key : new HashSet<>(commandNodeChildren.keySet())) {
				if (key.contains(":") && key.split(":")[1].equalsIgnoreCase(commandName)) {
					commandNodeChildren.remove(key);
				}
			}
		}

		// Otherwise, just remove them normally
		commandNodeChildren.remove(commandName);
		((Map<String, CommandNode<?>>) COMMANDNODE_LITERALS.get(DISPATCHER.getRoot())).remove(commandName);
		((Map<String, CommandNode<?>>) COMMANDNODE_ARGUMENTS.get(DISPATCHER.getRoot())).remove(commandName);
	}

	/**
	 * Generates a command to be registered by the CommandAPI.
	 * 
	 * @param args       set of ordered argument pairs which contain the prompt text
	 *                   and their argument types
	 * @param actualArgs
	 * @param executor   code to be ran when the command is executed
	 * @return a brigadier command which is registered internally
	 * @throws CommandSyntaxException if an error occurs when the command is ran
	 */
	Command<CommandSourceStack> generateCommand(Argument<?>[] args,
												CommandAPIExecutor<? extends CommandSender> executor, boolean converted) throws CommandSyntaxException {

		// Generate our command from executor
		return (cmdCtx) -> {
			CommandSender sender = NMS.getSenderForCommand(cmdCtx, executor.isForceNative());
			if (converted) {
				Object[] argObjs = argsToObjectArr(cmdCtx, args);
				int resultValue = 0;

				// Return a String[] of arguments for converted commands
				String[] argsAndCmd = cmdCtx.getRange().get(cmdCtx.getInput()).split(" ");
				String[] result = new String[argsAndCmd.length - 1];
				System.arraycopy(argsAndCmd, 1, result, 0, argsAndCmd.length - 1);

				// As stupid as it sounds, it's more performant and safer to use
				// a List<?>[] instead of a List<List<?>>, due to NPEs and AIOOBEs.
				@SuppressWarnings("unchecked")
				List<String>[] entityNamesForArgs = new List[args.length];
				for (int i = 0; i < args.length; i++) {
					entityNamesForArgs[i] = args[i].getEntityNames(argObjs[i]);
				}
				List<List<String>> product = CartesianProduct.getDescartes(Arrays.asList(entityNamesForArgs));

				// These objects in obj are List<String>
				for (List<String> strings : product) {
					// We assume result.length == strings.size
					if (result.length == strings.size()) {
						for (int i = 0; i < result.length; i++) {
							if (strings.get(i) != null) {
								result[i] = strings.get(i);
							}
						}
					}
					resultValue += executor.execute(sender, result);
				}

				return resultValue;
			} else {
				return executor.execute(sender, argsToObjectArr(cmdCtx, args));
			}
		};
	}

	/**
	 * Converts the List&lt;Argument> into an Object[] for command execution
	 * 
	 * @param cmdCtx the command context that will execute this command
	 * @param args   the map of strings to arguments
	 * @return an Object[] which can be used in (sender, args) ->
	 * @throws CommandSyntaxException
	 */
	Object[] argsToObjectArr(CommandContext<CommandSourceStack> cmdCtx, Argument<?>[] args)
			throws CommandSyntaxException {
		// Array for arguments for executor
		List<Object> argList = new ArrayList<>();

		// Populate array
		for (Argument<?> argument : args) {
			if (argument.isListed()) {
				argList.add(parseArgument(cmdCtx, argument.getNodeName(), argument, argList.toArray()));
			}
		}

		return argList.toArray();
	}

	/**
	 * Parses an argument and converts it into its standard Bukkit type (as defined
	 * in NMS.java)
	 * 
	 * @param type   the argument type
	 * @param cmdCtx the command context
	 * @param key    the key (declared in arguments)
	 * @param value  the value (the argument declared in arguments)
	 * @param sender the command sender
	 * @return the standard Bukkit type
	 * @throws CommandSyntaxException
	 */
	Object parseArgument(CommandContext<CommandSourceStack> cmdCtx, String key, Argument<?> value, Object[] previousArgs)
			throws CommandSyntaxException {
		if (value.isListed()) {
			return value.parseArgument(NMS, cmdCtx, key, previousArgs);
		} else {
			return null;
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Permissions //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This permission generation setup ONLY works iff:
	 * <ul>
	 * <li>You register the parent permission node FIRST.</li>
	 * <li>Example:<br>
	 * /mycmd - permission node: <code>my.perm</code> <br>
	 * /mycmd &lt;arg> - permission node: <code>my.perm.other</code></li>
	 * </ul>
	 *
	 * The <code>my.perm.other</code> permission node is revoked for the COMMAND
	 * REGISTRATION, however:
	 * <ul>
	 * <li>The permission node IS REGISTERED.</li>
	 * <li>The permission node, if used for an argument (as in this case), will be
	 * used for suggestions for said argument</li>
	 * </ul>
	 * 
	 * @param requirements
	 */
	Predicate<CommandSourceStack> generatePermissions(String commandName, CommandPermission permission,
			Predicate<CommandSender> requirements) {
		// If we've already registered a permission, set it to the "parent" permission.
		if (PERMISSIONS_TO_FIX.containsKey(commandName.toLowerCase())) {
			if (!PERMISSIONS_TO_FIX.get(commandName.toLowerCase()).equals(permission)) {
				permission = PERMISSIONS_TO_FIX.get(commandName.toLowerCase());
			}
		} else {
			// Add permission to a list to fix conflicts with minecraft:permissions
			PERMISSIONS_TO_FIX.put(commandName.toLowerCase(), permission);
		}

		final CommandPermission finalPermission = permission;

		// Register it to the Bukkit permissions registry
		if (finalPermission.getPermission().isPresent()) {
			try {
				Bukkit.getPluginManager().addPermission(new Permission(finalPermission.getPermission().get()));
			} catch (IllegalArgumentException e) {
				assert true; // nop, not an error.
			}
		}

		return (CommandSourceStack css) -> permissionCheck(NMS.getCommandSenderFromCSS(css), finalPermission,
				requirements);
	}

	/**
	 * Checks if a sender has a given permission.
	 * 
	 * @param sender     the sender to check permissions of
	 * @param permission the CommandAPI CommandPermission permission to check
	 * @return true if the sender satisfies the provided permission
	 */
	boolean permissionCheck(CommandSender sender, CommandPermission permission, Predicate<CommandSender> requirements) {
		boolean satisfiesPermissions;
		if (sender == null) {
			satisfiesPermissions = true;
		} else {
			if (permission.equals(CommandPermission.NONE)) {
				// No permission set
				satisfiesPermissions = true;
			} else if (permission.equals(CommandPermission.OP)) {
				// Op permission set
				satisfiesPermissions = sender.isOp();
			} else {
				// A permission has been set
				if(permission.getPermission().isPresent()) {
					satisfiesPermissions = sender.hasPermission(permission.getPermission().get());
				} else {
					// TODO: This should assert, we should be defaulting to CommandPermission.NONE, but for some reason we're not.
					satisfiesPermissions = true;
				}
			}
		}
		if (permission.isNegated()) {
			satisfiesPermissions = !satisfiesPermissions;
		}
		return satisfiesPermissions && requirements.test(sender);
	}

	/*
	 * Makes permission checks more "Bukkit" like and less "Vanilla Minecraft" like
	 */
	void fixPermissions() {
		// Get the command map to find registered commands
		CommandMap map = paper.getCommandMap();

		if (!PERMISSIONS_TO_FIX.isEmpty()) {
			CommandAPI.logInfo("Linking permissions to commands:");
		}

		for (Entry<String, CommandPermission> entry : PERMISSIONS_TO_FIX.entrySet()) {
			String cmdName = entry.getKey();
			CommandPermission perm = entry.getValue();
			CommandAPI.logInfo(perm.toString() + " -> /" + cmdName);

			final String permNode;
			if (perm.isNegated() || perm.equals(CommandPermission.NONE) || perm.equals(CommandPermission.OP)) {
				permNode = "";
			} else if (perm.getPermission().isPresent()) {
				permNode = perm.getPermission().get();
			} else {
				CommandAPI.logError("Invalid state with internal CommandPermission: " + perm);
				CommandAPI.logError("This should never happen! If you're seeing this message, please get in contact with the CommandAPI developers, we'd love to know how you did this!");
				permNode = null;
			}

			/*
			 * Sets the permission. If you have to be OP to run this command, we set the
			 * permission to "null" (empty string). Doing so means that Bukkit's "testPermission"
			 * will always return true, however since the command's permission check
			 * occurs internally via the CommandAPI, this isn't a problem.
			 * 
			 * If anyone dares tries to use testPermission() on this command, seriously,
			 * what are you doing and why?
			 */
			org.bukkit.command.Command commandMapCommand = map.getCommand(cmdName);
			if (commandMapCommand != null && NMS.isVanillaCommandWrapper(commandMapCommand)) {
				commandMapCommand.setPermission(permNode);
			}
			commandMapCommand = map.getCommand("minecraft:" + cmdName);
			if (commandMapCommand != null && NMS.isVanillaCommandWrapper(commandMapCommand)) {
				commandMapCommand.setPermission(permNode);
			}
		}
		CommandAPI.logNormal("Linked " + PERMISSIONS_TO_FIX.size() + " Bukkit permissions to commands");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Registration //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	 * Expands multiliteral arguments and registers all expansions of
	 * MultiLiteralArguments throughout the provided command. Returns true if
	 * multiliteral arguments were present (and expanded) and returns false if
	 * multiliteral arguments were not present.
	 */
	private boolean expandMultiLiterals(CommandMetaData meta, final Argument<?>[] args,
			CommandAPIExecutor<? extends CommandSender> executor, boolean converted)
			throws CommandSyntaxException, IOException {

		// "Expands" our MultiLiterals into Literals
		for (int index = 0; index < args.length; index++) {
			// Find the first multiLiteral in the for loop
			if (args[index] instanceof MultiLiteralArgument superArg) {

				// Add all of its entries
				for (int i = 0; i < superArg.getLiterals().length; i++) {
					LiteralArgument litArg = (LiteralArgument) new LiteralArgument(superArg.getLiterals()[i])
							.setListed(superArg.isListed()).withPermission(superArg.getArgumentPermission())
							.withRequirement(superArg.getRequirements());

					// Reconstruct the list of arguments and place in the new literals
					Argument<?>[] newArgs = Arrays.copyOf(args, args.length);
					newArgs[index] = litArg;
					register(meta, newArgs, executor, converted);
				}
				return true;
			}
		}
		return false;
	}

	// Prevent nodes of the same name but with different types:
	// allow /race invite<LiteralArgument> player<PlayerArgument>
	// disallow /race invite<LiteralArgument> player<EntitySelectorArgument>
	// Return true if conflict was present, otherwise return false
	private boolean hasCommandConflict(String commandName, Argument<?>[] args, String argumentsAsString) {
		List<String[]> regArgs = new ArrayList<>();
		for (RegisteredCommand rCommand : registeredCommands) {
			if (rCommand.commandName().equals(commandName)) {
				for (String str : rCommand.argsAsStr()) {
					regArgs.add(str.split(":"));
				}
				// We just find the first entry that causes a conflict. If this
				// were some industry-level code, we would probably generate a
				// list of all commands first, then check for command conflicts
				// all in one go so we can display EVERY command conflict for
				// all commands, but this works perfectly and isn't important.
				break;
			}
		}
		for (int i = 0; i < args.length; i++) {
			// Avoid IAOOBEs
			if (regArgs.size() == i && regArgs.size() < args.length) {
				break;
			}
			// We want to ensure all node names are the same
			if (!regArgs.get(i)[0].equals(args[i].getNodeName())) {
				break;
			}
			// This only applies to the last argument
			if (i == args.length - 1) {
				if (!regArgs.get(i)[1].equals(args[i].getClass().getSimpleName())) {
					// Command it conflicts with
					StringBuilder builder2 = new StringBuilder();
					for (String[] arg : regArgs) {
						builder2.append(arg[0]).append("<").append(arg[1]).append("> ");
					}

					CommandAPI.logError("""
							Failed to register command:

							  %s %s

							Because it conflicts with this previously registered command:

							  %s %s
							""".formatted(commandName, argumentsAsString, commandName, builder2.toString()));
					return true;
				}
			}
		}
		return false;
	}

	// Links arg -> Executor
	private ArgumentBuilder<CommandSourceStack, ?> generateInnerArgument(Command<CommandSourceStack> command,
			Argument<?>[] args) {
		Argument<?> innerArg = args[args.length - 1];

		// Handle Literal arguments
		if (innerArg instanceof LiteralArgument literalArgument) {
			return getLiteralArgumentBuilderArgument(literalArgument.getLiteral(), innerArg.getArgumentPermission(),
					innerArg.getRequirements()).executes(command);
		}

		// Handle arguments with built-in suggestion providers
		else if (innerArg instanceof ICustomProvidedArgument customProvidedArg
				&& !innerArg.getOverriddenSuggestions().isPresent()) {
			return getRequiredArgumentBuilderWithProvider(innerArg, args,
					NMS.getSuggestionProvider(customProvidedArg.getSuggestionProvider())).executes(command);
		}

		// Handle every other type of argument
		else {
			return getRequiredArgumentBuilderDynamic(args, innerArg).executes(command);
		}
	}

	// Links arg1 -> arg2 -> ... argN -> innermostArgument
	private ArgumentBuilder<CommandSourceStack, ?> generateOuterArguments(
			ArgumentBuilder<CommandSourceStack, ?> innermostArgument, Argument<?>[] args) {
		ArgumentBuilder<CommandSourceStack, ?> outer = innermostArgument;
		for (int i = args.length - 2; i >= 0; i--) {
			Argument<?> outerArg = args[i];

			// Handle Literal arguments
			if (outerArg instanceof LiteralArgument literalArgument) {
				outer = getLiteralArgumentBuilderArgument(literalArgument.getLiteral(),
						outerArg.getArgumentPermission(), outerArg.getRequirements()).then(outer);
			}

			// Handle arguments with built-in suggestion providers
			else if (outerArg instanceof ICustomProvidedArgument customProvidedArg
					&& !outerArg.getOverriddenSuggestions().isPresent()) {
				outer = getRequiredArgumentBuilderWithProvider(outerArg, args,
						NMS.getSuggestionProvider(customProvidedArg.getSuggestionProvider())).then(outer);
			}

			// Handle every other type of argument
			else {
				outer = getRequiredArgumentBuilderDynamic(args, outerArg).then(outer);
			}
		}
		return outer;
	}
	
	/**
	 * Handles previewable arguments. This stores the path to previewable arguments
	 * in {@link CommandAPIHandler#previewableArguments} for runtime resolving
	 * @param commandName the name of the command
	 * @param args the declared arguments
	 * @param aliases the command's aliases
	 */
	private void handlePreviewableArguments(String commandName, Argument<?>[] args, String[] aliases) {
		if(args.length > 0 && args[args.length - 1] instanceof IPreviewable<?, ?> previewable) {
			List<String> path = new ArrayList<>();
			
			path.add(commandName);
			for(Argument<?> arg : args) {
				path.add(arg.getNodeName());
			}
			previewableArguments.put(List.copyOf(path), previewable);

			// And aliases
			for(String alias : aliases) {
				path.set(0, alias);
				previewableArguments.put(List.copyOf(path), previewable);
			}
		}
	}

	// Builds our NMS command using the given arguments for this method, then
	// registers it
	void register(CommandMetaData meta, final Argument<?>[] args,
			CommandAPIExecutor<? extends CommandSender> executor, boolean converted)
			throws CommandSyntaxException, IOException {

		// "Expands" our MultiLiterals into Literals
		if (expandMultiLiterals(meta, args, executor, converted)) {
			return;
		}

		// Create the human-readable command syntax of arguments
		final String humanReadableCommandArgSyntax; {
			StringBuilder builder = new StringBuilder();
			for (Argument<?> arg : args) {
				builder.append(arg.toString()).append(" ");
			}
			humanReadableCommandArgSyntax = builder.toString().trim();
		}
		
		// #312 Safeguard against duplicate node names. This only applies to
		// required arguments (i.e. not literal arguments)
		{
			Set<String> argumentNames = new HashSet<>();
			for(Argument<?> arg : args) {
				// We shouldn't find MultiLiteralArguments at this point, only
				// LiteralArguments
				if(!(arg instanceof LiteralArgument)) {
					if(argumentNames.contains(arg.getNodeName())) {
						CommandAPI.logError("""
							Failed to register command:

							  %s %s

							Because the following argument shares the same node name as another argument:

							  %s
							""".formatted(meta.commandName, humanReadableCommandArgSyntax, arg.toString()));
						return;
					} else {
						argumentNames.add(arg.getNodeName());
					}
				}
			}
		}

		// Expand metaData into named variables
		String commandName = meta.commandName;
		CommandPermission permission = meta.permission;
		String[] aliases = meta.aliases;
		Predicate<CommandSender> requirements = meta.requirements;
		Optional<String> shortDescription = meta.shortDescription;
		Optional<String> fullDescription = meta.fullDescription;

		// Handle command conflicts
		boolean hasRegisteredCommand = false;
		for (int i = 0, size = registeredCommands.size(); i < size && !hasRegisteredCommand; i++) {
			hasRegisteredCommand |= registeredCommands.get(i).commandName().equals(commandName);
		}
		if (hasRegisteredCommand && hasCommandConflict(commandName, args, humanReadableCommandArgSyntax)) {
			return;
		} else {
			List<String> argumentsString = new ArrayList<>();
			for (Argument<?> arg : args) {
				argumentsString.add(arg.getNodeName() + ":" + arg.getClass().getSimpleName());
			}
			registeredCommands.add(new RegisteredCommand(commandName, argumentsString, shortDescription, fullDescription, aliases, permission));
		}
		
		// Handle previewable arguments
		handlePreviewableArguments(commandName, args, aliases);

		// Warn if the command we're registering already exists in this plugin's
		// plugin.yml file
		{
			final PluginCommand pluginCommand = Bukkit.getPluginCommand(commandName);
			if (pluginCommand != null) {
				CommandAPI.logWarning("Plugin command /%s is registered by Bukkit (%s). Did you forget to remove this from your plugin.yml file?".formatted(commandName,
					pluginCommand.getPlugin().getName()));
			}
		}

		CommandAPI.logInfo("Registering command /" + commandName + " " + humanReadableCommandArgSyntax);

		// Generate the actual command
		Command<CommandSourceStack> command = generateCommand(args, executor, converted);

		/*
		 * The innermost argument needs to be connected to the executor. Then that
		 * argument needs to be connected to the previous argument etc. Then the first
		 * argument needs to be connected to the command name, so we get: CommandName ->
		 * Args1 -> Args2 -> ... -> ArgsN -> Executor
		 */
		LiteralCommandNode<CommandSourceStack> resultantNode;
		if (args.length == 0) {
			// Link command name to the executor
			resultantNode = DISPATCHER.register(getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permission, requirements)).executes(command));

			// Register aliases
			for (String alias : aliases) {
				CommandAPI.logInfo("Registering alias /" + alias + " -> " + resultantNode.getName());
				DISPATCHER.register(getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permission, requirements)).executes(command));
			}
		} else {

			// Generate all of the arguments, following each other and finally linking to
			// the executor
			ArgumentBuilder<CommandSourceStack, ?> commandArguments = generateOuterArguments(
					generateInnerArgument(command, args), args);

			// Link command name to first argument and register
			resultantNode = DISPATCHER.register(getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permission, requirements)).then(commandArguments));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPI.getConfiguration().hasVerboseOutput()) {
					CommandAPI.logInfo("Registering alias /" + alias + " -> " + resultantNode.getName());
				}

				DISPATCHER.register(getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permission, requirements)).then(commandArguments));
			}
		}

		// We never know if this is "the last command" and we want dynamic (even if
		// partial)
		// command registration. Generate the dispatcher file!
		generateDispatcherFile();
	}

	// Produce the commandDispatch.json file for debug purposes
	private void generateDispatcherFile() throws IOException {
		if (CommandAPI.getConfiguration().getDispatcherFile() != null) {
			File file = CommandAPI.getConfiguration().getDispatcherFile();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}

			NMS.createDispatcherFile(file, DISPATCHER);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Argument Builders //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a literal for a given name.
	 * 
	 * @param commandName the name of the literal to create
	 * @return a brigadier LiteralArgumentBuilder representing a literal
	 */
	LiteralArgumentBuilder<CommandSourceStack> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}

	/**
	 * Creates a literal for a given name that requires a specified permission.
	 * 
	 * @param commandName the name fo the literal to create
	 * @param permission  the permission required to use this literal
	 * @return a brigadier LiteralArgumentBuilder representing a literal
	 */
	LiteralArgumentBuilder<CommandSourceStack> getLiteralArgumentBuilderArgument(String commandName,
			CommandPermission permission, Predicate<CommandSender> requirements) {
		LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.literal(commandName);
		return builder.requires((CommandSourceStack css) -> permissionCheck(NMS.getCommandSenderFromCSS(css),
				permission, requirements));
	}

	// Gets a RequiredArgumentBuilder for a DynamicSuggestedStringArgument
	RequiredArgumentBuilder<CommandSourceStack, ?> getRequiredArgumentBuilderDynamic(final Argument<?>[] args,
			Argument<?> argument) {
		
		final SuggestionProvider<CommandSourceStack> suggestions;

		if (argument.getOverriddenSuggestions().isPresent()) {
			suggestions = toSuggestions(argument, args, true);
		} else if (argument.getIncludedSuggestions().isPresent()) {
			// TODO(#317): Merge the suggestions included here instead?
			suggestions = (cmdCtx, builder) -> argument.getRawType().listSuggestions(cmdCtx, builder);
		} else {
			suggestions = null;
		}
		
		return getRequiredArgumentBuilderWithProvider(argument, args, suggestions);
	}

	// Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	RequiredArgumentBuilder<CommandSourceStack, ?> getRequiredArgumentBuilderWithProvider(Argument<?> argument,
			Argument<?>[] args, SuggestionProvider<CommandSourceStack> provider) {
		SuggestionProvider<CommandSourceStack> newSuggestionsProvider = provider;

		// If we have suggestions to add, combine provider with the suggestions
		if (argument.getIncludedSuggestions().isPresent() && argument.getOverriddenSuggestions().isEmpty()) {
			SuggestionProvider<CommandSourceStack> addedSuggestions = toSuggestions(argument, args,
					false);

			newSuggestionsProvider = (cmdCtx, builder) -> {
				// Heavily inspired by CommandDispatcher#listSuggestions, with combining
				// multiple CompletableFuture<Suggestions> into one.

				CompletableFuture<Suggestions> addedSuggestionsFuture = addedSuggestions.getSuggestions(cmdCtx,
						builder);
				CompletableFuture<Suggestions> providerSuggestionsFuture = provider.getSuggestions(cmdCtx, builder);
				CompletableFuture<Suggestions> result = new CompletableFuture<>();
				CompletableFuture.allOf(addedSuggestionsFuture, providerSuggestionsFuture).thenRun(() -> {
					List<Suggestions> suggestions = new ArrayList<>();
					suggestions.add(addedSuggestionsFuture.join());
					suggestions.add(providerSuggestionsFuture.join());
					result.complete(Suggestions.merge(cmdCtx.getInput(), suggestions));
				});
				return result;
			};
		}

		RequiredArgumentBuilder<CommandSourceStack, ?> requiredArgumentBuilder = RequiredArgumentBuilder
				.argument(argument.getNodeName(), argument.getRawType());

		return requiredArgumentBuilder.requires(css -> permissionCheck(NMS.getCommandSenderFromCSS(css),
				argument.getArgumentPermission(), argument.getRequirements())).suggests(newSuggestionsProvider);
	}

	Object[] generatePreviousArguments(CommandContext<CommandSourceStack> context, Argument<?>[] args, String nodeName)
			throws CommandSyntaxException {
		// Populate Object[], which is our previously filled arguments
		List<Object> previousArguments = new ArrayList<>();

		for (Argument<?> arg : args) {
			if (arg.getNodeName().equals(nodeName) && !(arg instanceof LiteralArgument)) {
				break;
			}

			Object result;
			try {
				result = parseArgument(context, arg.getNodeName(), arg, previousArguments.toArray());
			} catch (IllegalArgumentException e) {
				/*
				 * Redirected commands don't parse previous arguments properly. Simplest way to
				 * determine what we should do is simply set it to null, since there's nothing
				 * else we can do. I thought about letting this simply be an empty array, but
				 * then it's even more annoying to deal with - I wouldn't expect an array of
				 * size n to suddenly, randomly be 0, but I would expect random NPEs because
				 * let's be honest, this is Java we're dealing with.
				 */
				result = null;
			}
			if (arg.isListed()) {
				previousArguments.add(result);
			}
		}
		return previousArguments.toArray();
	}

	SuggestionProvider<CommandSourceStack> toSuggestions(Argument<?> theArgument, Argument<?>[] args,
			boolean overrideSuggestions) {
		return (CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) -> {
			// Construct the suggestion info
			SuggestionInfo suggestionInfo = new SuggestionInfo(NMS.getCommandSenderFromCSS(context.getSource()),
					generatePreviousArguments(context, args, theArgument.getNodeName()), builder.getInput(), builder.getRemaining());
			
			// Get the suggestions
			Optional<ArgumentSuggestions> suggestionsToAddOrOverride = overrideSuggestions
					? theArgument.getOverriddenSuggestions()
					: theArgument.getIncludedSuggestions();
			return suggestionsToAddOrOverride.orElse(ArgumentSuggestions.empty()).suggest(suggestionInfo, builder);
		};
	}
	
	/**
	 * Looks up the function to generate a chat preview for a path of nodes in the
	 * command tree. This is a method internal to the CommandAPI and isn't expected
	 * to be used by plugin developers (but you're more than welcome to use it as
	 * you see fit).
	 * 
	 * @param path a list of Strings representing the path (names of command nodes)
	 *             to (and including) the previewable argument
	 * @return a function that takes in a {@link PreviewInfo} and returns a
	 *         {@link Component}. If such a function is not available, this will
	 *         return a function that always returns null.
	 */
	public Optional<PreviewableFunction<?>> lookupPreviewable(List<String> path) {
		final IPreviewable<? extends Argument<?>, ?> previewable = previewableArguments.get(path);
		if(previewable != null && previewable.getPreview().isPresent()) {
			// Yeah, don't even question this logic of getting the value of an
			// optional and then wrapping it in an optional again. Java likes it
			// and complains if you don't do this. Not sure why.
			return Optional.of(previewable.getPreview().get());
		} else {
			return Optional.empty();
		}
	}
	
	/**
	 * 
	 * @param path a list of Strings representing the path (names of command nodes)
	 *             to (and including) the previewable argument
	 * @return Whether a previewable is legacy (non-Adventure) or not
	 */
	public boolean lookupPreviewableLegacyStatus(List<String> path) {
		final IPreviewable<? extends Argument<?>, ?> previewable = previewableArguments.get(path);
		if(previewable != null && previewable.getPreview().isPresent()) {
			return previewable.isLegacy();
		} else {
			return true;
		}
	}

	/////////////////////////
	// SECTION: Reflection //
	/////////////////////////

	/**
	 * Caches a field using reflection if it is not already cached, then return the
	 * field of a given class. This will also make the field accessible.
	 * 
	 * @param clazz the class where the field is declared
	 * @param name  the name of the field
	 * @return a Field reference
	 */
	public Field getField(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if (FIELDS.containsKey(key)) {
			return FIELDS.get(key);
		} else {
			Field result = null;
			try {
				result = clazz.getDeclaredField(name);
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			result.setAccessible(true);
			FIELDS.put(key, result);
			return result;
		}
	}

	private String generateCommandHelpPrefix(String command) {
		return (Bukkit.getPluginCommand(command) == null ? "/" : "/minecraft:") + command;
	}

	private void generateHelpUsage(StringBuilder sb, RegisteredCommand command) {
		sb.append(ChatColor.GOLD + "Usage: " + ChatColor.WHITE);

		// Generate usages
		List<String> usages = new ArrayList<>();
		for (RegisteredCommand rCommand : registeredCommands) {
			if (rCommand.commandName().equals(command.commandName())) {
				StringBuilder usageString = new StringBuilder();
				usageString.append("/" + command.commandName() + " ");
				for (String arg : rCommand.argsAsStr()) {
					usageString.append("<" + arg.split(":")[0] + "> ");
				}
				usages.add(usageString.toString());
			}
		}

		// If 1 usage, put it on the same line, otherwise format like a list
		if (usages.size() == 1) {
			sb.append(usages.get(0));
		} else if (usages.size() > 1) {
			for (String usage : usages) {
				sb.append("\n- " + usage);
			}
		}
	}

	void updateHelpForCommands() {
		Map<String, HelpTopic> helpTopicsToAdd = new HashMap<>();

		for (RegisteredCommand command : this.registeredCommands) {
			// Generate short description
			final String shortDescription;
			if (command.shortDescription().isPresent()) {
				shortDescription = command.shortDescription().get();
			} else if (command.fullDescription().isPresent()) {
				shortDescription = command.fullDescription().get();
			} else {
				shortDescription = "A Mojang provided command.";
			}

			// Generate full description
			StringBuilder sb = new StringBuilder();
			if (command.fullDescription().isPresent()) {
				sb.append(ChatColor.GOLD + "Description: " + ChatColor.WHITE + command.fullDescription().get() + "\n");
			}

			generateHelpUsage(sb, command);
			sb.append("\n");

			// Generate aliases. We make a copy of the StringBuilder because we
			// want to change the output when we register aliases
			StringBuilder aliasSb = new StringBuilder(sb.toString());
			if (command.aliases().length > 0) {
				sb.append(ChatColor.GOLD + "Aliases: " + ChatColor.WHITE + String.join(", ", command.aliases()));
			}

			// Must be empty string, not null as defined by OBC::CustomHelpTopic
			String permission = command.permission().getPermission().orElseGet(() -> "");

			// Don't override the plugin help topic
			String commandPrefix = generateCommandHelpPrefix(command.commandName());
			helpTopicsToAdd.put(commandPrefix,
					NMS.generateHelpTopic(commandPrefix, shortDescription, sb.toString().trim(), permission));

			for (String alias : command.aliases()) {
				StringBuilder currentAliasSb = new StringBuilder(aliasSb.toString());
				if (command.aliases().length > 0) {
					currentAliasSb.append(ChatColor.GOLD + "Aliases: " + ChatColor.WHITE);

					// We want to get all aliases (including the original command name),
					// except for the current alias
					List<String> aliases = new ArrayList<>(Arrays.asList(command.aliases()));
					aliases.add(command.commandName());
					aliases.remove(alias);

					currentAliasSb.append(ChatColor.WHITE + String.join(", ", aliases));
				}

				// Don't override the plugin help topic
				commandPrefix = generateCommandHelpPrefix(alias);
				helpTopicsToAdd.put(commandPrefix, NMS.generateHelpTopic(commandPrefix, shortDescription,
						currentAliasSb.toString().trim(), permission));
			}
		}

		NMS.addToHelpMap(helpTopicsToAdd);
	}

	//////////////////////////////
	// SECTION: Private classes //
	//////////////////////////////

	/**
	 * Class to store cached methods and fields
	 * 
	 * This is required because each key is made up of a class and a field or method
	 * name
	 */
	private record ClassCache(Class<?> clazz, String name) {
	}

	/**
	 * A class to compute the Cartesian product of a number of lists. Source:
	 * https://www.programmersought.com/article/86195393650/
	 */
	private final class CartesianProduct {

		// Shouldn't be instantiated
		private CartesianProduct() {
		}

		/**
		 * Returns the Cartesian product of a list of lists
		 * 
		 * @param <T>  the underlying type of the list of lists
		 * @param list the list to calculate the Cartesian product of
		 * @return a List of lists which represents the Cartesian product of all
		 *         elements of the input
		 */
		public static final <T> List<List<T>> getDescartes(List<List<T>> list) {
			List<List<T>> returnList = new ArrayList<>();
			descartesRecursive(list, 0, returnList, new ArrayList<T>());
			return returnList;
		}

		/**
		 * Recursive implementation Principle: traverse sequentially from 0 of the
		 * original list to the end
		 * 
		 * @param <T>          the underlying type of the list of lists
		 * @param originalList original list
		 * @param position     The position of the current recursion in the original
		 *                     list
		 * @param returnList   return result
		 * @param cacheList    temporarily saved list
		 */
		private static final <T> void descartesRecursive(List<List<T>> originalList, int position,
				List<List<T>> returnList, List<T> cacheList) {
			List<T> originalItemList = originalList.get(position);
			for (int i = 0; i < originalItemList.size(); i++) {
				// The last one reuses cacheList to save memory
				List<T> childCacheList = (i == originalItemList.size() - 1) ? cacheList : new ArrayList<>(cacheList);
				childCacheList.add(originalItemList.get(i));
				if (position == originalList.size() - 1) {// Exit recursion to the end
					returnList.add(childCacheList);
					continue;
				}
				descartesRecursive(originalList, position + 1, returnList, childCacheList);
			}
		}

	}

}
