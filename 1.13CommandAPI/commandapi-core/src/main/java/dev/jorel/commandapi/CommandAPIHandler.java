package dev.jorel.commandapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.ICustomProvidedArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.nms.NMS;

/**
 * Handles the main backend of the CommandAPI. This constructs brigadier Command
 * objects, applies and generates arguments and handles suggestions. This also
 * handles permission registration for Bukkit, interactions for NMS and the
 * registration and unregistration of commands.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class CommandAPIHandler {
	
	private static final Map<ClassCache, Field> FIELDS = new HashMap<>();
	private static final Map<ClassCache, Method> METHODS = new HashMap<>();
	private static final TreeMap<String, CommandPermission> PERMISSIONS_TO_FIX = new TreeMap<>();
	private static final NMS NMS;
	private static final CommandDispatcher DISPATCHER;
	
	static {
		Object server;
		try {
			server = getMethod(Bukkit.getServer().getClass(), "getServer").invoke(Bukkit.getServer());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			CommandAPIMain.getLog().severe("Unable to hook into NMS properly!");
			server = null;
		}
		
		String version;
		try {
			version = (String) getMethod(Class.forName(server.getClass().getPackage().getName() + ".MinecraftServer"), "getVersion").invoke(server);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | ClassNotFoundException e) {
			CommandAPIMain.getLog().severe("Failed to find Minecraft version!");
			version = null;
		}
		
		NMS = CommandAPIVersionHandler.getNMS(version);
		DISPATCHER = NMS.getBrigadierDispatcher(server);
	}
	
	static void checkDependencies() {
		try {
			Class.forName("com.mojang.brigadier.CommandDispatcher");
		} catch (ClassNotFoundException e) {
			new ClassNotFoundException("Cannot hook into Brigadier (Are you running Minecraft 1.13 or above?)").printStackTrace();
		}

		// Log successful hooks
		if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
			String compatibleVersions = Arrays.toString(NMS.compatibleVersions());
			compatibleVersions = compatibleVersions.substring(1, compatibleVersions.length() - 1);
			CommandAPIMain.getLog().info(
					"Hooked into NMS " + NMS.getClass().getName() + " (compatible with " + compatibleVersions + ")");
		}

		// Checks other dependencies
		if (Bukkit.getPluginManager().getPlugin("NBTAPI") != null) {
			CommandAPIMain.getLog().info("Hooked into the NBTAPI successfully.");
		} else {
			CommandAPIMain.getLog().warning(
					"Couldn't hook into the NBTAPI for NBT support. See https://www.spigotmc.org/resources/nbt-api.7939/");
		}

		try {
			Class.forName("org.spigotmc.SpigotConfig");
			CommandAPIMain.getLog().info("Hooked into Spigot successfully for Chat/ChatComponents");
		} catch (ClassNotFoundException e) {
			CommandAPIMain.getLog().warning("Couldn't hook into Spigot for Chat/ChatComponents");
		}
	}
	
	/**
	 * Returns an instance of NMS.
	 * 
	 * @return an instance of NMS
	 */
	public static NMS getNMS() {
		return NMS;
	}
	
	/**
	 * Unregisters a command from the NMS command graph.
	 * 
	 * @param commandName the name of the command to unregister
	 * @param force       whether the unregistration system should attempt to remove
	 *                    all instances of the command, regardless of whether they
	 *                    have been registered by Minecraft, Bukkit or Spigot etc.
	 */
	static void unregister(String commandName, boolean force) {
		try {
			if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
				CommandAPIMain.getLog().info("Unregistering command /" + commandName);
			}

			// Get the child nodes from the loaded dispatcher class
			Field children = getField(CommandNode.class, "children");
			Map<String, CommandNode<?>> commandNodeChildren = (Map<String, CommandNode<?>>) children
					.get(DISPATCHER.getRoot());

			if (force) {
				// Remove them by force
				List<String> keysToRemove = new ArrayList<>();
				commandNodeChildren.keySet().stream().filter(s -> s.contains(":"))
						.filter(s -> s.split(":")[1].equalsIgnoreCase(commandName)).forEach(keysToRemove::add);
				keysToRemove.forEach(commandNodeChildren::remove);
			}

			// Otherwise, just remove them normally
			commandNodeChildren.remove(commandName);
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a command to be registered by the CommandAPI.
	 * 
	 * @param args     set of ordered argument pairs which contain the tooltip text
	 *                 and their argument types
	 * @param executor code to be ran when the command is executed
	 * @return a brigadier command which is registered internally
	 * @throws CommandSyntaxException if an error occurs when the command is ran
	 */
	static Command generateCommand(LinkedHashMap<String, Argument> args, CustomCommandExecutor executor)
			throws CommandSyntaxException {

		// Generate our command from executor
		return (cmdCtx) -> {

			// Array for arguments for executor
			List<Object> argList = new ArrayList<>();

			// Populate array
			for (Entry<String, Argument> entry : args.entrySet()) {
				Object result = parseArgument(cmdCtx, entry.getKey(), entry.getValue());
				if(result != null) {
					argList.add(result);
				}
			}

			return executor.execute(NMS.getSenderForCommand(cmdCtx), argList.toArray());
		};
	}
	
	/**
	 * Parses an argument and converts it into its standard Bukkit type (as defined in NMS.java)
	 * @param type the argument type
	 * @param cmdCtx the command context
	 * @param key the key (declared in arguments)
	 * @param value the value (the argument declared in arguments)
	 * @param sender the command sender
	 * @return the standard Bukkit type
	 * @throws CommandSyntaxException
	 */
	static Object parseArgument(CommandContext cmdCtx, String key, Argument value) throws CommandSyntaxException {
		CommandSender sender = NMS.getSenderForCommand(cmdCtx);
		switch (value.getArgumentType()) {
		case ADVANCEMENT:
			return NMS.getAdvancement(cmdCtx, key);
		case AXIS:
			return NMS.getAxis(cmdCtx, key);
		case BIOME:
			return NMS.getBiome(cmdCtx, key);
		case BLOCK_PREDICATE:
			return NMS.getBlockPredicate(cmdCtx, key);
		case BLOCKSTATE:
			return NMS.getBlockState(cmdCtx, key);
		case CHAT:
			return NMS.getChat(cmdCtx, key);
		case CHATCOLOR:
			return NMS.getChatColor(cmdCtx, key);
		case CHAT_COMPONENT:
			return NMS.getChatComponent(cmdCtx, key);
		case CUSTOM:
			CustomArgument arg = (CustomArgument) value;
			String customresult = (String) cmdCtx.getArgument(key, String.class);
			try {
				return arg.getParser().apply(customresult);
			} catch (CustomArgumentException e) {
				throw e.toCommandSyntax(customresult, cmdCtx);
			} catch (Exception e) {
				String errorMsg = new MessageBuilder("Error in executing command ").appendFullInput().append(" - ")
						.appendArgInput().appendHere().toString().replace("%input%", customresult)
						.replace("%finput%", cmdCtx.getInput());
				throw new SimpleCommandExceptionType(() -> {
					return errorMsg;
				}).create();
			}
		case ENCHANTMENT:
			return NMS.getEnchantment(cmdCtx, key);
		case ENTITY_SELECTOR:
			EntitySelectorArgument argument = (EntitySelectorArgument) value;
			return NMS.getEntitySelector(cmdCtx, key, argument.getEntitySelector());
		case ENTITY_TYPE:
			return NMS.getEntityType(cmdCtx, key, sender);
		case ENVIRONMENT:
			return NMS.getDimension(cmdCtx, key);
		case FLOAT_RANGE:
			return NMS.getFloatRange(cmdCtx, key);
		case FUNCTION:
			return NMS.getFunction(cmdCtx, key);
		case INT_RANGE:
			return NMS.getIntRange(cmdCtx, key);
		case ITEMSTACK:
			return NMS.getItemStack(cmdCtx, key);
		case ITEMSTACK_PREDICATE:
			return NMS.getItemStackPredicate(cmdCtx, key);
		case LITERAL:
			return null;
		case LOCATION:
			LocationType locationType = ((LocationArgument) value).getLocationType();
			return NMS.getLocation(cmdCtx, key, locationType, sender);
		case LOCATION_2D:
			LocationType locationType2d = ((Location2DArgument) value).getLocationType();
			return NMS.getLocation2D(cmdCtx, key, locationType2d, sender);
		case LOOT_TABLE:
			return NMS.getLootTable(cmdCtx, key);
		case MATH_OPERATION:
			return NMS.getMathOperation(cmdCtx, key);
		case NBT_COMPOUND:
			return NMS.getNBTCompound(cmdCtx, key);
		case OBJECTIVE:
			return NMS.getObjective(cmdCtx, key, sender);
		case OBJECTIVE_CRITERIA:
			return NMS.getObjectiveCriteria(cmdCtx, key);
		case PARTICLE:
			return NMS.getParticle(cmdCtx, key);
		case PLAYER:
			return NMS.getPlayer(cmdCtx, key);
		case POTION_EFFECT:
			return NMS.getPotionEffect(cmdCtx, key);
		case RECIPE:
			return NMS.getRecipe(cmdCtx, key);
		case ROTATION:
			return NMS.getRotation(cmdCtx, key);
		case SCORE_HOLDER:
			ScoreHolderArgument scoreHolderArgument = (ScoreHolderArgument) value;
			return scoreHolderArgument.isSingle() ? NMS.getScoreHolderSingle(cmdCtx, key)
					: NMS.getScoreHolderMultiple(cmdCtx, key);
		case SCOREBOARD_SLOT:
			return NMS.getScoreboardSlot(cmdCtx, key);
		case SIMPLE_TYPE:
			return cmdCtx.getArgument(key, value.getPrimitiveType());
		case SOUND:
			return NMS.getSound(cmdCtx, key);
		case TEAM:
			return NMS.getTeam(cmdCtx, key, sender);
		case TIME:
			return NMS.getTime(cmdCtx, key);
		case UUID:
			return NMS.getUUID(cmdCtx, key);
		}
		return null;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Permissions //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This permission generation setup ONLY works iff: 
	 * <ul>
	 * <li>You register the parent permission node FIRST.</li>
	 * <li>Example:<br>/mycmd - permission node: <code>my.perm</code> <br>/mycmd &lt;arg> - permission node: <code>my.perm.other</code></li>
	 * </ul>
	 *
	 * The <code>my.perm.other</code> permission node is revoked for the COMMAND REGISTRATION, however: 
	 * <ul>
	 * <li>The permission node IS REGISTERED.</li> 
	 * <li>The permission node, if used for an argument (as in this case), 
	 *  	will be used for suggestions for said argument</li></ul>
	 */
	static Predicate generatePermissions(String commandName, CommandPermission permission) {
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
		if (finalPermission.getPermission() != null) {
			try {
				Bukkit.getPluginManager().addPermission(new Permission(finalPermission.getPermission()));
			} catch (IllegalArgumentException e) {
			}
		}

		return (Object clw) -> permissionCheck(NMS.getCommandSenderForCLW(clw), finalPermission);
	}

	/**
	 * Checks if a sender has a given permission.
	 * 
	 * @param sender     the sender to check permissions of
	 * @param permission the CommandAPI CommandPermission permission to check
	 * @return true if the sender satisfies the provided permission
	 */
	static boolean permissionCheck(CommandSender sender, CommandPermission permission) {
		if (sender == null) {
			return true;
		}
		if (permission.equals(CommandPermission.NONE)) {
			return true;
		} else if (permission.equals(CommandPermission.OP)) {
			return sender.isOp();
		} else {
			return sender.hasPermission(permission.getPermission());
		}
	}

	static void fixPermissions() {
		/*
		 * Makes permission checks more "Bukkit" like and less "Vanilla Minecraft" like
		 */
		try {

			// Get the command map to find registered commands
			SimpleCommandMap map = NMS.getSimpleCommandMap();
			Field f = getField(SimpleCommandMap.class, "knownCommands");
			Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) f
					.get(map);

			if(!PERMISSIONS_TO_FIX.isEmpty()) {
				CommandAPIMain.getLog().info("Linking permissions to commands:");
			}
			PERMISSIONS_TO_FIX.forEach((cmdName, perm) -> {

				if (perm.equals(CommandPermission.NONE)) {
					if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
						CommandAPIMain.getLog().info("NONE -> /" + cmdName);
					}
					// Set the command permission to empty string (Minecraft standard for "no
					// permission required")
					if (NMS.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
					if (NMS.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
				} else {
					if (perm.getPermission() != null) {
						if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
							CommandAPIMain.getLog().info(perm.getPermission() + " -> /" + cmdName);
						} else {
							CommandAPIMain.getLog().info("OP -> /" + cmdName);
						}
						// Set the command permission to the (String) permission node
						if (NMS.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
						if (NMS.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
					} else {
						// Dafaq?
					}
				}
			});
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Registration //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// Builds our NMS command using the given arguments for this method, then
	// registers it
	static void register(String commandName, CommandPermission permissions, String[] aliases,
			final LinkedHashMap<String, Argument> args, CustomCommandExecutor executor) throws Exception {
		if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
			// Create a list of argument names
			StringBuilder builder = new StringBuilder();
			args.values().forEach(arg -> builder.append("<").append(arg.getClass().getSimpleName()).append("> "));
			CommandAPIMain.getLog().info("Registering command /" + commandName + " " + builder.toString());
		}

		Command command = generateCommand(args, executor);

		/*
		 * The innermost argument needs to be connected to the executor. Then that
		 * argument needs to be connected to the previous argument etc. Then the first
		 * argument needs to be connected to the command name
		 *
		 * CommandName -> Args1 -> Args2 -> ... -> ArgsN -> Executor
		 */

		LiteralCommandNode resultantNode;
		if (args.isEmpty()) {
			// Link command name to the executor
			resultantNode = DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permissions)).executes(command));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
					CommandAPIMain.getLog().info("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permissions)).executes(command));
			}
		} else {

			// List of keys for reverse iteration
			ArrayList<String> keys = new ArrayList<>(args.keySet());

			// Link the last element to the executor
			ArgumentBuilder inner;
			// New scope used here to prevent innerArg accidentally being used below
			{
				Argument innerArg = args.get(keys.get(keys.size() - 1));

				// Handle Literal arguments
				if (innerArg instanceof LiteralArgument) {
					String str = ((LiteralArgument) innerArg).getLiteral();
					inner = getLiteralArgumentBuilderArgument(str, innerArg.getArgumentPermission()).executes(command);
				}

				// Handle arguments with built-in suggestion providers
				else if (innerArg instanceof ICustomProvidedArgument && innerArg.getOverriddenSuggestions() == null) {
					inner = getRequiredArgumentBuilderWithProvider(keys.get(keys.size() - 1), innerArg.getRawType(),
							innerArg.getArgumentPermission(),
							NMS.getSuggestionProvider(((ICustomProvidedArgument) innerArg).getSuggestionProvider()))
									.executes(command);
				}

				// Handle every other type of argument
				else {
					inner = getRequiredArgumentBuilderDynamic(args, keys.get(keys.size() - 1), innerArg,
							innerArg.getArgumentPermission()).executes(command);
				}
			}

			// Link everything else up, except the first
			ArgumentBuilder outer = inner;
			for (int i = keys.size() - 2; i >= 0; i--) {
				Argument outerArg = args.get(keys.get(i));

				// Handle Literal arguments
				if (outerArg instanceof LiteralArgument) {
					String str = ((LiteralArgument) outerArg).getLiteral();
					outer = getLiteralArgumentBuilderArgument(str, outerArg.getArgumentPermission()).then(outer);
				}

				// Handle arguments with built-in suggestion providers
				else if (outerArg instanceof ICustomProvidedArgument && outerArg.getOverriddenSuggestions() == null) {
					outer = getRequiredArgumentBuilderWithProvider(keys.get(i), outerArg.getRawType(),
							outerArg.getArgumentPermission(),
							NMS.getSuggestionProvider(((ICustomProvidedArgument) outerArg).getSuggestionProvider()))
									.then(outer);
				}

				// Handle every other type of argument
				else {
					outer = getRequiredArgumentBuilderDynamic(args, keys.get(i), outerArg,
							outerArg.getArgumentPermission()).then(outer);
				}
			}

			// Link command name to first argument and register
			resultantNode = DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permissions)).then(outer));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
					CommandAPIMain.getLog().info("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permissions)).redirect(resultantNode));
			}
		}

		// Produce the commandDispatch.json file for debug purposes
		if (CommandAPIMain.getConfiguration().willCreateDispatcherFile()) {
			File file = CommandAPIMain.getDispatcherFile();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(System.out);
			}

			NMS.createDispatcherFile(file, DISPATCHER);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: SuggestionProviders //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// NMS ICompletionProvider.a()
	static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, String[] array) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (int i = 0; i < array.length; i++) {
			String str = array[i];
			if (str.toLowerCase(Locale.ROOT).startsWith(remaining)) {
				builder.suggest(str);
			}
		}
		return builder.buildFuture();
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
	static LiteralArgumentBuilder<?> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}

	/**
	 * Creates a literal for a given name that requires a specified permission.
	 * 
	 * @param commandName the name fo the literal to create
	 * @param permission  the permission required to use this literal
	 * @return a brigadier LiteralArgumentBuilder representing a literal
	 */
	static LiteralArgumentBuilder<?> getLiteralArgumentBuilderArgument(String commandName,
			CommandPermission permission) {
		return LiteralArgumentBuilder.literal(commandName)
				.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), permission));
	}

	// Gets a RequiredArgumentBuilder for a DynamicSuggestedStringArgument
	static <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderDynamic(
			final LinkedHashMap<String, Argument> args, String argumentName, Argument type,
			CommandPermission permission) {

		// If there are no changes to the default suggestions, return it as normal
		if (type.getOverriddenSuggestions() == null) {
			return RequiredArgumentBuilder.argument(argumentName, (ArgumentType<T>) type.getRawType())
					.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), permission));
		}

		// Otherwise, we have to handle arguments of the form BiFunction<CommandSender,
		// Object[], String[]>
		else {
			return getRequiredArgumentBuilderWithProvider(argumentName, type.getRawType(), permission,
					(CommandContext context, SuggestionsBuilder builder) -> {
						// Populate Object[], which is our previously filled arguments
						List<Object> previousArguments = new ArrayList<>();

						for (String s : args.keySet()) {
							if (s.equals(argumentName)) {
								break;
							}
							previousArguments.add(parseArgument(context, s, args.get(s)));
						}
						return getSuggestionsBuilder(builder, type.getOverriddenSuggestions()
								.apply(NMS.getCommandSenderForCLW(context.getSource()), previousArguments.toArray()));
					});
		}
	}

	// Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	static <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderWithProvider(String argumentName,
			ArgumentType<T> type, CommandPermission permission, SuggestionProvider provider) {
		return RequiredArgumentBuilder.argument(argumentName, type)
				.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), permission)).suggests(provider);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Reflection //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// Gets a field using reflection and caches it
	public static Field getField(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if (FIELDS.containsKey(key)) {
			return FIELDS.get(key);
		} else {
			Field result = null;
			try {
				result = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			result.setAccessible(true);
			FIELDS.put(key, result);
			return result;
		}
	}

	/**
	 * Caches a method using reflection if it is not already cached, then return the
	 * method of a given class. This will also make the method accessible.
	 * 
	 * @param clazz the class where the method is declared
	 * @param name the name of the method
	 * @return a Method reference
	 */
	public static Method getMethod(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if (METHODS.containsKey(key)) {
			return METHODS.get(key);
		} else {
			Method result = null;
			try {
				result = clazz.getDeclaredMethod(name);
			} catch (SecurityException | NoSuchMethodException e) {
				e.printStackTrace();
			}
			result.setAccessible(true);
			METHODS.put(key, result);
			return result;
		}
	}
}
