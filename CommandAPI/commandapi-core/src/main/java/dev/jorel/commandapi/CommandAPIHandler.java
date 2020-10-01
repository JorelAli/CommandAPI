package dev.jorel.commandapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
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
import com.mojang.brigadier.tree.RootCommandNode;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.CommandAPIArgumentType;
import dev.jorel.commandapi.arguments.CustomArgument;
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException;
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.ICustomProvidedArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.Location2DArgument;
import dev.jorel.commandapi.arguments.LocationArgument;
import dev.jorel.commandapi.arguments.LocationType;
import dev.jorel.commandapi.arguments.MultiLiteralArgument;
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
			CommandAPI.getLog().severe("Unable to hook into NMS properly!");
			server = null;
		}
		
		String version;
		try {
			version = (String) getMethod(Class.forName(server.getClass().getPackage().getName() + ".MinecraftServer"), "getVersion").invoke(server);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | ClassNotFoundException e) {
			CommandAPI.getLog().severe("Failed to find Minecraft version!");
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
		if (CommandAPI.getConfiguration().hasVerboseOutput()) {
			String compatibleVersions = Arrays.toString(NMS.compatibleVersions());
			compatibleVersions = compatibleVersions.substring(1, compatibleVersions.length() - 1);
			CommandAPI.getLog().info(
					"Hooked into NMS " + NMS.getClass().getName() + " (compatible with " + compatibleVersions + ")");
		}

		// Checks other dependencies
		try {
			Class.forName("de.tr7zw.nbtapi.NBTContainer");
			CommandAPI.getLog().info("Hooked into the NBTAPI successfully.");
		} catch(ClassNotFoundException e) {
			CommandAPI.getLog().warning(
					"Couldn't hook into the NBTAPI for NBT support. See https://www.spigotmc.org/resources/nbt-api.7939/");
		}

		try {
			Class.forName("org.spigotmc.SpigotConfig");
			CommandAPI.getLog().info("Hooked into Spigot successfully for Chat/ChatComponents");
		} catch (ClassNotFoundException e) {
			CommandAPI.getLog().warning("Couldn't hook into Spigot for Chat/ChatComponents");
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
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.getLog().info("Unregistering command /" + commandName);
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
	 * @param args     set of ordered argument pairs which contain the prompt text
	 *                 and their argument types
	 * @param actualArgs 
	 * @param executor code to be ran when the command is executed
	 * @return a brigadier command which is registered internally
	 * @throws CommandSyntaxException if an error occurs when the command is ran
	 */
	static Command generateCommand(List<Argument> args, CustomCommandExecutor executor, boolean converted)
			throws CommandSyntaxException {

		// Generate our command from executor
		return (cmdCtx) -> {
			CommandSender sender = NMS.getSenderForCommand(cmdCtx, executor.isForceNative());
			Object[] arguments;
			if(converted) {
				String[] argsAndCmd = cmdCtx.getRange().get(cmdCtx.getInput()).split(" ");
				String[] result = new String[argsAndCmd.length - 1];
				System.arraycopy(argsAndCmd, 1, result, 0, argsAndCmd.length - 1);
				arguments = result;
			} else {
				arguments = argsToObjectArr(cmdCtx, args);
			}
			return executor.execute(sender, arguments);
		};
	}
	
	/**
	 * Converts the List&lt;Argument> into an Object[] for command execution
	 * @param cmdCtx the command context that will execute this command
	 * @param args the map of strings to arguments
	 * @return an Object[] which can be used in (sender, args) -> 
	 * @throws CommandSyntaxException
	 */
	private static Object[] argsToObjectArr(CommandContext cmdCtx, List<Argument> args) throws CommandSyntaxException {
		// Array for arguments for executor
		List<Object> argList = new ArrayList<>();

		// Populate array
		for (Argument argument : args) {
			Object result = parseArgument(cmdCtx, argument.getNodeName(), argument);
			if(result != null) {
				argList.add(result);
			}
		}
		
		return argList.toArray();
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
			String customresult;
			if(arg.isKeyed()) {
				customresult = getNMS().getKeyedAsString(cmdCtx, key);
			} else {
				customresult = (String) cmdCtx.getArgument(key, String.class);
			}
			
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
			return NMS.getEntityType(cmdCtx, key);
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
			LiteralArgument a = (LiteralArgument) value;
			return a.isMulti ? a.getLiteral() : null;
		case LOCATION:
			LocationType locationType = ((LocationArgument) value).getLocationType();
			return NMS.getLocation(cmdCtx, key, locationType);
		case LOCATION_2D:
			LocationType locationType2d = ((Location2DArgument) value).getLocationType();
			return NMS.getLocation2D(cmdCtx, key, locationType2d);
		case LOOT_TABLE:
			return NMS.getLootTable(cmdCtx, key);
		case MATH_OPERATION:
			return NMS.getMathOperation(cmdCtx, key);
		case NBT_COMPOUND:
			return NMS.getNBTCompound(cmdCtx, key);
		case OBJECTIVE:
			return NMS.getObjective(cmdCtx, key);
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
		case MULTI_LITERAL:
			//This case should NEVER occur!
			break;
		case SIMPLE_TYPE:
			return cmdCtx.getArgument(key, value.getPrimitiveType());
		case SOUND:
			return NMS.getSound(cmdCtx, key);
		case TEAM:
			return NMS.getTeam(cmdCtx, key);
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
	 * @param requirements 
	 */
	static Predicate generatePermissions(String commandName, CommandPermission permission, Predicate<CommandSender> requirements) {
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

		return (Object clw) -> permissionCheck(NMS.getCommandSenderForCLW(clw), finalPermission, requirements);
	}

	/**
	 * Checks if a sender has a given permission.
	 * 
	 * @param sender     the sender to check permissions of
	 * @param permission the CommandAPI CommandPermission permission to check
	 * @return true if the sender satisfies the provided permission
	 */
	static boolean permissionCheck(CommandSender sender, CommandPermission permission, Predicate<CommandSender> requirements) {
		boolean satisfiesPermissions = false;
		if (sender == null) {
			satisfiesPermissions = true;
		} else {
			if (permission.equals(CommandPermission.NONE)) {
				satisfiesPermissions = true;
			} else if (permission.equals(CommandPermission.OP)) {
				satisfiesPermissions = sender.isOp();
			} else {
				satisfiesPermissions = sender.hasPermission(permission.getPermission());
			}
		}
		
		return satisfiesPermissions && requirements.test(sender);
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
				CommandAPI.getLog().info("Linking permissions to commands:");
			}
			
			PERMISSIONS_TO_FIX.forEach((cmdName, perm) -> {
				
				if(CommandAPI.getConfiguration().hasVerboseOutput()) {
					CommandAPI.getLog().info(perm.toString() + " -> /" + cmdName);
				}
				
				String permNode = perm.equals(CommandPermission.NONE) ? "" : perm.getPermission();
				
				/*
				 * Sets the permission. If you have to be OP to run this command,
				 * we set the permission to null. Doing so means that Bukkit's
				 * "testPermission" will always return true, however since the
				 * command's permission check occurs internally via the CommandAPI,
				 * this isn't a problem.
				 * 
				 * If anyone dares tries to use testPermission() on this command,
				 * seriously, what are you doing and why?
				 */
				if (NMS.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
					knownCommands.get(cmdName).setPermission(permNode);
				}
				if (NMS.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
					knownCommands.get(cmdName).setPermission(permNode);
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
	static void register(String commandName, CommandPermission permissions, String[] aliases, Predicate<CommandSender> requirements,
			final List<Argument> args, CustomCommandExecutor executor, boolean converted) throws Exception {
		
		//"Expands" our MultiLiterals into Literals
		Predicate<Argument> isMultiLiteral = arg -> arg.getArgumentType() == CommandAPIArgumentType.MULTI_LITERAL;
		if(args.stream().filter(isMultiLiteral).count() > 0) {
		
			int index = 0;
			for(Argument argument : args) {
				
				//Find the first multiLiteral in the for loop
				if(isMultiLiteral.test(argument)) {
					MultiLiteralArgument superArg = (MultiLiteralArgument) argument;
					
					//Add all of its entries
					for(int i = 0; i < superArg.getLiterals().length; i++) {
						LiteralArgument litArg = new LiteralArgument(superArg.getLiterals()[i]);
						litArg.isMulti = superArg.isMulti;
						
						//Reconstruct the list of arguments and place in the new literals
						List<Argument> newArgs = new ArrayList<>();
						{
							int j = 0;
							for(Argument previousEntry : args) {
								if(j == index) {
									newArgs.add(litArg);
								} else {
									newArgs.add(previousEntry);
								}
								j++;
							}
						}
						register(commandName, permissions, aliases, requirements, newArgs, executor, converted);
					}
					return;
				}
				index++;
			}
		}
		
		if (CommandAPI.getConfiguration().hasVerboseOutput()) {
			// Create a list of argument names
			StringBuilder builder = new StringBuilder();
			args.forEach(arg -> builder.append(arg.getNodeName()).append("<").append(arg.getClass().getSimpleName()).append("> "));
			CommandAPI.getLog().info("Registering command /" + commandName + " " + builder.toString());
		}

		Command command = generateCommand(args, executor, converted);

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
					.requires(generatePermissions(commandName, permissions, requirements)).executes(command));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPI.getConfiguration().hasVerboseOutput()) {
					CommandAPI.getLog().info("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permissions, requirements)).executes(command));
			}
		} else {

			// List of keys for reverse iteration
			//ArrayList<String> keys = new ArrayList<>(args.keySet());

			// Link the last element to the executor
			ArgumentBuilder inner;
			// New scope used here to prevent innerArg accidentally being used below
			{
				Argument innerArg = args.get(args.size() - 1);

				// Handle Literal arguments
				if (innerArg instanceof LiteralArgument) {
					String str = ((LiteralArgument) innerArg).getLiteral();
					inner = getLiteralArgumentBuilderArgument(str, innerArg.getArgumentPermission(), innerArg.getRequirements()).executes(command);
				}

				// Handle arguments with built-in suggestion providers
				else if (innerArg instanceof ICustomProvidedArgument
						&& !innerArg.getOverriddenSuggestions().isPresent()) {
					inner = getRequiredArgumentBuilderWithProvider(innerArg,
							NMS.getSuggestionProvider(((ICustomProvidedArgument) innerArg).getSuggestionProvider()))
									.executes(command);
				}

				// Handle every other type of argument
				else {
					inner = getRequiredArgumentBuilderDynamic(args, innerArg).executes(command);
				}
			}

			// Link everything else up, except the first
			ArgumentBuilder outer = inner;
			for (int i = args.size() - 2; i >= 0; i--) {
				Argument outerArg = args.get(i);

				// Handle Literal arguments
				if (outerArg instanceof LiteralArgument) {
					String str = ((LiteralArgument) outerArg).getLiteral();
					outer = getLiteralArgumentBuilderArgument(str, outerArg.getArgumentPermission(), outerArg.getRequirements()).then(outer);
				}

				// Handle arguments with built-in suggestion providers
				else if (outerArg instanceof ICustomProvidedArgument && !outerArg.getOverriddenSuggestions().isPresent()) {
					outer = getRequiredArgumentBuilderWithProvider(outerArg,
							NMS.getSuggestionProvider(((ICustomProvidedArgument) outerArg).getSuggestionProvider()))
									.then(outer);
				}

				// Handle every other type of argument
				else {
					outer = getRequiredArgumentBuilderDynamic(args, outerArg).then(outer);
				}
			}

			// Link command name to first argument and register
			resultantNode = DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permissions, requirements)).then(outer));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPI.getConfiguration().hasVerboseOutput()) {
					CommandAPI.getLog().info("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				
				DISPATCHER.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permissions, requirements)).then(outer));
			}
		}

		// Produce the commandDispatch.json file for debug purposes
		if (CommandAPI.getConfiguration().willCreateDispatcherFile()) {
			File file = CommandAPI.getDispatcherFile();
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
	static CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, StringTooltip[] array) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (int i = 0; i < array.length; i++) {
			StringTooltip str = array[i];
			if (str.getSuggestion().toLowerCase(Locale.ROOT).startsWith(remaining)) {
				builder.suggest(str.getSuggestion(), str.getTooltip());
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
			CommandPermission permission, Predicate<CommandSender> requirements) {
		return LiteralArgumentBuilder.literal(commandName)
				.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), permission, requirements));
	}

	// Gets a RequiredArgumentBuilder for a DynamicSuggestedStringArgument
	static <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderDynamic(
			final List<Argument> args, Argument argument) {

		// If there are no changes to the default suggestions, return it as normal
		if (!argument.getOverriddenSuggestions().isPresent()) {
			return RequiredArgumentBuilder.argument(argument.getNodeName(), (ArgumentType<T>) argument.getRawType())
					.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), argument.getArgumentPermission(), argument.getRequirements()));
		}

		// Otherwise, we have to handle arguments of the form BiFunction<CommandSender,
		// Object[], String[]>
		else {
			return getRequiredArgumentBuilderWithProvider(argument, Brigadier.toSuggestions(argument.getNodeName(), args));
		}
	}

	// Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	static <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderWithProvider(Argument argument,
			SuggestionProvider provider) {
		return RequiredArgumentBuilder.argument(argument.getNodeName(), argument.getRawType())
				.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), argument.getArgumentPermission(),
						argument.getRequirements()))
				.suggests(provider);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Reflection //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Caches a field using reflection if it is not already cached, then return the
	 * field of a given class. This will also make the field accessible.
	 * 
	 * @param clazz the class where the field is declared
	 * @param name the name of the field
	 * @return a Field reference
	 */
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
	
	/**
	 * The Brigadier class is used to access some of the internals of the CommandAPI
	 * so you can use the CommandAPI alongside Mojang's com.mojang.brigadier package
	 */
	public static abstract class Brigadier {
		
		/**
		 * Returns the Brigadier CommandDispatcher tree that is used internally by the
		 * CommandAPI. Modifying this CommandDispatcher tree before the server finishes
		 * loading will still keep any changes made to it. For example, adding a new
		 * node to this tree will keep the node once the server has finished loading.
		 * 
		 * @return The CommandAPI's internal CommandDispatcher instance
		 */
		public static CommandDispatcher getCommandDispatcher() {
			return DISPATCHER;
		}

		/**
		 * Returns the root node of the current CommandDispatcher. This is the
		 * equivalent of running
		 * 
		 * <code>
		 * Brigadier.getCommandDispatcher().getRoot();
		 * </code>
		 * 
		 * @return The Brigadier CommandDispatcher's root node
		 */
		public static RootCommandNode getRootNode() {
			return DISPATCHER.getRoot();
		}
		
		/**
		 * Creates a new literal argument builder from a CommandAPI LiteralArgument
		 * 
		 * @param the LiteralArgument to convert from
		 * @return a LiteralArgumentBuilder that represents the literal
		 */
		public static LiteralArgumentBuilder fromLiteralArgument(LiteralArgument literalArgument) {
			return getLiteralArgumentBuilderArgument(literalArgument.getLiteral(), literalArgument.getArgumentPermission(), literalArgument.getRequirements());
		}
		
		/**
		 * Constructs a RedirectModifier from a predicate that uses a command sender and
		 * some arguments. RedirectModifiers can be used with Brigadier's
		 * <code>fork()</code> method to invoke other nodes in the CommandDispatcher
		 * tree. You would use this method as shown:
		 * 
		 * <pre>
		 * Brigadier.fromPredicate((sender, args) -> {
		 *     ...
		 * }, arguments);
		 * </pre>
		 * 
		 * @param predicate the predicate to test
		 * @param args      the arguments that the sender has filled in
		 * @return a RedirectModifier that encapsulates the provided predicate
		 */
		public static RedirectModifier fromPredicate(BiPredicate<CommandSender, Object[]> predicate, List<Argument> args) {
			return cmdCtx -> {
				if(predicate.test(NMS.getSenderForCommand(cmdCtx, false), argsToObjectArr(cmdCtx, args))) {
					return Collections.singleton(cmdCtx.getSource());
				} else {
					return Collections.emptyList();
				}
			};
		}

		/**
		 * Converts a CommandAPICommand into a Brigadier Command
		 * 
		 * @param command the command to convert
		 * @return a Brigadier Command object that represents the provided command
		 */
		public static Command fromCommand(CommandAPICommand command) {
			try {
				return generateCommand(command.args, command.executor, command.isConverted);
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}
			//This case should never happen, because the exception should be caught
			//"lower down" in execution
			return null;
		}
		
		/**
		 * Constructs a RequiredArgumentBuilder from a given argument within a command
		 * declaration. For example:
		 * 
		 * <pre>
		 * List<Argument> arguments = new ArrayList<>();
		 * arguments.add(new IntegerArgument("hello"));
		 * 
		 * RequiredArgumentBuilder argBuilder = Brigadier.fromArguments(arguments, "hello");
		 * </pre>
		 * 
		 * @param args  the List of arguments which you typically declare for
		 *              commands
		 * @param value the name of the argument you want to specify
		 * @return a RequiredArgumentBuilder that represents the provided argument
		 */
		public static RequiredArgumentBuilder fromArgument(List<Argument> args, String value) {
			return getRequiredArgumentBuilderDynamic(args, getArgument(args, value));
		}
		
		private static Argument getArgument(List<Argument> args, String nodeName) {
			return args.stream().filter(arg -> arg.getNodeName().equals(nodeName)).findFirst().get();
		}
		
		/**
		 * Constructs a RequiredArgumentBuilder from a given argument and prompt text.
		 * @param prompt the prompt to display when the user is typing the command
		 * @param argument the argument to create a RequiredArgumentBuilder of
		 * @return a RequiredArgumentBuilder that represents the provided argument
		 */
		public static RequiredArgumentBuilder fromArgument(Argument argument) {
			List<Argument> arguments = new ArrayList<>();
			arguments.add(argument);
			return getRequiredArgumentBuilderDynamic(arguments, argument);
		}
		
		/**
		 * Converts an argument name and a list of arguments to a Brigadier
		 * SuggestionProvider
		 * 
		 * @param argumentName the name (prompt) of the argument as declared by its key
		 *                     in the List
		 * @param args         the list of arguments
		 * @return a SuggestionProvider that suggests the overridden suggestions for the
		 *         argument declared in the List with key argumentName
		 */
		public static SuggestionProvider toSuggestions(String argumentName, List<Argument> args) {
			return (CommandContext context, SuggestionsBuilder builder) -> {
				// Populate Object[], which is our previously filled arguments
				List<Object> previousArguments = new ArrayList<>();

				for (Argument arg : args) {
					if (arg.getNodeName().equals(argumentName)) {
						break;
					}
					
					Object result;
					try {
						result = parseArgument(context, arg.getNodeName(), arg);
					} catch(IllegalArgumentException e) {
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
					previousArguments.add(result);
				}
				return getSuggestionsBuilder(builder, getArgument(args, argumentName).getOverriddenSuggestions().get()
						.apply(NMS.getCommandSenderForCLW(context.getSource()), previousArguments.toArray()));
			};
		}
	}
}
