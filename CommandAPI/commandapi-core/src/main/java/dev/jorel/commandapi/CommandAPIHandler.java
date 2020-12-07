package dev.jorel.commandapi;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.permissions.Permission;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
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

import de.tr7zw.changeme.nbtapi.NBTContainer;
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
import dev.jorel.commandapi.arguments.SafeOverrideableArgument;
import dev.jorel.commandapi.arguments.SafeOverrideableArgument.PlaceholderArgument;
import dev.jorel.commandapi.arguments.ScoreHolderArgument;
import dev.jorel.commandapi.exceptions.ParseException;
import dev.jorel.commandapi.nms.NMS;
import dev.jorel.commandapi.wrappers.StringRangeWrapper;

/**
 * Handles the main backend of the CommandAPI. This constructs brigadier Command
 * objects, applies and generates arguments and handles suggestions. This also
 * handles permission registration for Bukkit, interactions for NMS and the
 * registration and unregistration of commands.
 */
public class CommandAPIHandler<CommandListenerWrapper> {
	
	private static CommandAPIHandler<?> instance;
	
	public static CommandAPIHandler<?> getInstance() {
		if(instance == null) {
			instance = new CommandAPIHandler<>();
		}
		return instance;
	}
	
	final Map<ClassCache, Field> FIELDS = new HashMap<>();
	final TreeMap<String, CommandPermission> PERMISSIONS_TO_FIX = new TreeMap<>();
	final NMS<CommandListenerWrapper> NMS;
	final CommandDispatcher<CommandListenerWrapper> DISPATCHER;
	final Map<String, List<String>> registeredCommands; //Keep track of what has been registered for type checking 
	
	private CommandAPIHandler() {
		String bukkit = Bukkit.getServer().toString();
		NMS = CommandAPIVersionHandler.getNMS(bukkit.substring(bukkit.indexOf("minecraftVersion") + 17, bukkit.length() - 1));
		DISPATCHER = NMS.getBrigadierDispatcher();
		registeredCommands = new HashMap<>();
	}
	
	void checkDependencies() {
		try {
			@SuppressWarnings("unused")
			Class<?> commandDispatcherClass = CommandDispatcher.class;
		} catch (NoClassDefFoundError e) {
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
			@SuppressWarnings("unused")
			Class<NBTContainer> container = NBTContainer.class;
			CommandAPI.getLog().info("Hooked into the NBTAPI successfully.");
		} catch(NoClassDefFoundError e) {
			if(CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.getLog().warning(
					"Couldn't hook into the NBTAPI for NBT support. Download it from https://www.spigotmc.org/resources/nbt-api.7939/");
			}
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
	public NMS<CommandListenerWrapper> getNMS() {
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
	void unregister(String commandName, boolean force) {
		try {
			if (CommandAPI.getConfiguration().hasVerboseOutput()) {
				CommandAPI.getLog().info("Unregistering command /" + commandName);
			}

			// Get the child nodes from the loaded dispatcher class
			Field children = getField(CommandNode.class, "children");
			@SuppressWarnings("unchecked")
			Map<String, CommandNode<?>> commandNodeChildren = (Map<String, CommandNode<?>>) children.get(DISPATCHER.getRoot());

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
	Command<CommandListenerWrapper> generateCommand(List<Argument> args, CustomCommandExecutor executor, boolean converted)
			throws CommandSyntaxException {

		// Generate our command from executor
		return (cmdCtx) -> {
			
//			if(NMS.g().isInstance(cmdCtx)) {
//				System.out.println("YAY!!!!");
//				System.out.println(cmdCtx.getClass().getCanonicalName());
//			}
//			
//			System.out.println(cmdCtx.getInput());
			
			CommandSender sender = NMS.getSenderForCommand(cmdCtx, executor.isForceNative());
			Object[] arguments;
			if(converted) {
				// Return a String[] of arguments for converted commands
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
	Object[] argsToObjectArr(CommandContext<CommandListenerWrapper> cmdCtx, List<Argument> args) throws CommandSyntaxException {
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
	Object parseArgument(CommandContext<CommandListenerWrapper> cmdCtx, String key, Argument value) throws CommandSyntaxException {
		if(!value.isListed()) {
			return null;
		}
		if(value instanceof PlaceholderArgument) {
			return ((SafeOverrideableArgument<?>) value).getDefaultValue();
		}
		switch (value.getArgumentType()) {
		case ANGLE:
			return NMS.getAngle(cmdCtx, key);
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
			CustomArgument<?> arg = (CustomArgument<?>) value;
			String customresult;
			if(arg.isKeyed()) {
				customresult = getNMS().getKeyedAsString(cmdCtx, key);
			} else {
				customresult = (String) cmdCtx.getArgument(key, String.class);
			}
			
			try {
				return arg.getParserOld().apply(customresult);
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
			return ((LiteralArgument) value).getLiteral();
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
		case PRIMITIVE_BOOLEAN:
		case PRIMITIVE_DOUBLE:
		case PRIMITIVE_FLOAT:
		case PRIMITIVE_INTEGER:
		case PRIMITIVE_LONG:
		case PRIMITIVE_STRING:
		case PRIMITIVE_GREEDY_STRING:
		case PRIMITIVE_TEXT:
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
	Predicate<CommandListenerWrapper> generatePermissions(String commandName, CommandPermission permission, Predicate<CommandSender> requirements) {
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

		return (CommandListenerWrapper clw) -> permissionCheck(NMS.getCommandSenderForCLW(clw), finalPermission, requirements);
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
				satisfiesPermissions = true;
			} else if (permission.equals(CommandPermission.OP)) {
				satisfiesPermissions = sender.isOp();
			} else {
				satisfiesPermissions = sender.hasPermission(permission.getPermission());
			}
		}
		if(permission.isNegated()) {
			satisfiesPermissions = !satisfiesPermissions;
		}
		return satisfiesPermissions && requirements.test(sender);
	}

	/*
	 * Makes permission checks more "Bukkit" like and less "Vanilla Minecraft" like
	 */
	void fixPermissions() {
		// Get the command map to find registered commands
		SimpleCommandMap map = NMS.getSimpleCommandMap();

		if(!PERMISSIONS_TO_FIX.isEmpty()) {
			CommandAPI.logInfo("Linking permissions to commands:");
		}
		
		for(Entry<String, CommandPermission> entry : PERMISSIONS_TO_FIX.entrySet()) {
			String cmdName = entry.getKey();
			CommandPermission perm = entry.getValue();
			CommandAPI.logInfo(perm.toString() + " -> /" + cmdName);
			
			String permNode = (perm.equals(CommandPermission.NONE) || perm.isNegated()) ? "" : perm.getPermission();
			
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
			if (NMS.isVanillaCommandWrapper(map.getCommand(cmdName))) {
				map.getCommand(cmdName).setPermission(permNode);
			}
			if (NMS.isVanillaCommandWrapper(map.getCommand("minecraft:" + cmdName))) {
				map.getCommand(cmdName).setPermission(permNode);
			}
		}
		CommandAPI.getLog().info("Linked " + PERMISSIONS_TO_FIX.size() + " Bukkit permissions to commands");
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Registration //
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// Builds our NMS command using the given arguments for this method, then
	// registers it
	void register(String commandName, CommandPermission permissions, String[] aliases, Predicate<CommandSender> requirements,
			final List<Argument> args, CustomCommandExecutor executor, boolean converted) throws Exception {
		
		// "Expands" our MultiLiterals into Literals
		Predicate<Argument> isMultiLiteral = arg -> arg.getArgumentType() == CommandAPIArgumentType.MULTI_LITERAL;
		if(args.stream().filter(isMultiLiteral).count() > 0) {
		
			int index = 0;
			for(Argument argument : args) {
				
				//Find the first multiLiteral in the for loop
				if(isMultiLiteral.test(argument)) {
					MultiLiteralArgument superArg = (MultiLiteralArgument) argument;
					
					//Add all of its entries
					for(int i = 0; i < superArg.getLiterals().length; i++) {
						LiteralArgument litArg = (LiteralArgument) new LiteralArgument(superArg.getLiterals()[i])
							.setListed(superArg.isListed())
							.withPermission(superArg.getArgumentPermission())
							.withRequirement(superArg.getRequirements());
						
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
		
		// Expand optional arguments
		Predicate<Argument> isOptional = arg -> {
			if(arg instanceof SafeOverrideableArgument) {
				return ((SafeOverrideableArgument<?>) arg).isOptional();
			} else {
				return false;
			}
		};
		if(args.stream().filter(isOptional).count() > 0) {
			for(int index = 0; index < args.size(); index++) {
				Argument argument = args.get(index);
				if(isOptional.test(argument)) {
					SafeOverrideableArgument<?> optionalArg = (SafeOverrideableArgument<?>) argument;
					optionalArg.setOptional(false);

					// Reconstruct the list of arguments, keeping it as it should be
					{
						List<Argument> newArgs = new ArrayList<>();
						int j = 0;
						for(Argument previousEntry : args) {
							if(j == index) {
								newArgs.add(optionalArg);
							} else {
								newArgs.add(previousEntry);
							}
							j++;
						}
						register(commandName, permissions, aliases, requirements, newArgs, executor, converted);
					}
					
					//Reconstruct the list of arguments and place in the "placeholder" (default) value
					{	
						List<Argument> newArgs = new ArrayList<>();
						int j = 0;
						for(Argument previousEntry : args) {
							if(j == index) {
								newArgs.add(optionalArg.asOptional());
							} else {
								newArgs.add(previousEntry);
							}
							j++;
						}
						register(commandName, permissions, aliases, requirements, newArgs, executor, converted);
					}
					return;
				}
			}
		}
		
		// Prevent nodes of the same name but with different types:
		// allow    /race invite<LiteralArgument> player<PlayerArgument>
		// disallow /race invite<LiteralArgument> player<EntitySelectorArgument>
		if(registeredCommands.containsKey(commandName)) {
			List<String[]> regArgs = registeredCommands.get(commandName).stream().map(s -> s.split(":")).collect(Collectors.toList());
			for(int i = 0; i < args.size(); i++) {
				// Avoid IAOOBEs
				if(regArgs.size() == i && regArgs.size() < args.size()) {
					break;
				}
				// We want to ensure all node names are the same
				if(!regArgs.get(i)[0].equals(args.get(i).getNodeName())) {
					break;
				}
				// This only applies to the last argument
				if(i == args.size() - 1) {
					if(!regArgs.get(i)[1].equals(args.get(i).getClass().getSimpleName())) { 
						
						// Ignore PlaceholderArguments because they dont' exist at command registration time
						if(args.get(i) instanceof PlaceholderArgument || regArgs.get(i)[1].equals(PlaceholderArgument.class.getSimpleName())) {
							continue;
						}
						
						//Command we're trying to register
						StringBuilder builder = new StringBuilder();
						args.forEach(arg -> builder.append(arg.getNodeName()).append("<").append(arg.getClass().getSimpleName()).append("> "));
						
						//Command it conflicts with
						StringBuilder builder2 = new StringBuilder();
						regArgs.forEach(arg -> builder2.append(arg[0]).append("<").append(arg[1]).append("> "));
						
						// Lovely high quality error message formatting (inspired by Elm)
						CommandAPI.getLog().severe("Failed to register command:");
						CommandAPI.getLog().severe("");
						CommandAPI.getLog().severe("  " + commandName + " " + builder.toString());
						CommandAPI.getLog().severe("");
						CommandAPI.getLog().severe("Because it conflicts with this previously registered command:");
						CommandAPI.getLog().severe("");
						CommandAPI.getLog().severe("  " + commandName + " " + builder2.toString());
						CommandAPI.getLog().severe("");
						return;
					}						
				}
			}
		}
		registeredCommands.put(commandName, args.stream().map(arg -> arg.getNodeName() + ":" + arg.getClass().getSimpleName()).collect(Collectors.toList()));
		
		if (CommandAPI.getConfiguration().hasVerboseOutput()) {
			// Create a list of argument names
			StringBuilder builder = new StringBuilder();
			args.forEach(arg -> builder.append(arg.getNodeName()).append("<").append(arg.getClass().getSimpleName()).append("> "));
			CommandAPI.logInfo("Registering command /" + commandName + " " + builder.toString());
//			System.out.println("Command:     " + commandName);
//			System.out.println("Permissions: " + permissions.toString());
//			System.out.println("NExecutors:  " + executor.getNormalExecutors().size());
//			System.out.println("RExecutors:  " + executor.getResultingExecutors().size());
//			System.out.println("Converted:   " + converted);
//			System.out.println("Arguments:   ");
//			for(Argument arg : args) {
//				System.out.println("- Name:        " + arg.getNodeName());
//				System.out.println("  Type:        " + arg.getClass().getSimpleName());
//				System.out.println("  Perms:       " + arg.getArgumentPermission().toString());
//				System.out.println("  OverrideSug: " + arg.getOverriddenSuggestions().isPresent());
//				System.out.println("  Listed:      " + arg.isListed());
//				System.out.println("  Primitive:   " + arg.getPrimitiveType().getSimpleName());
//				System.out.println("  Raw type:    " + arg.getRawType());
//			}
		}

		Command<CommandListenerWrapper> command = generateCommand(new ArrayList<Argument>(args), executor, converted);
		
		// At this point, remove all internally optional arguments
		ListIterator<Argument> argumentsIterator = args.listIterator();
		while(argumentsIterator.hasNext()) {
			Argument arg = argumentsIterator.next();
			if(arg instanceof PlaceholderArgument) {
				argumentsIterator.remove();
			}
		}

		/*
		 * The innermost argument needs to be connected to the executor. Then that
		 * argument needs to be connected to the previous argument etc. Then the first
		 * argument needs to be connected to the command name
		 *
		 * CommandName -> Args1 -> Args2 -> ... -> ArgsN -> Executor
		 */

		LiteralCommandNode<CommandListenerWrapper> resultantNode;
		if (args.isEmpty()) {
			// Link command name to the executor
			resultantNode = DISPATCHER.register(getLiteralArgumentBuilder(commandName).requires(generatePermissions(commandName, permissions, requirements)).executes(command));

			// Register aliases
			for (String alias : aliases) {
				CommandAPI.logInfo("Registering alias /" + alias + " -> " + resultantNode.getName());
				DISPATCHER.register(getLiteralArgumentBuilder(alias).requires(generatePermissions(alias, permissions, requirements)).executes(command));
			}
		} else {

			// List of keys for reverse iteration
			//ArrayList<String> keys = new ArrayList<>(args.keySet());

			// Link the last element to the executor
			ArgumentBuilder<CommandListenerWrapper, ?> inner;
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
			ArgumentBuilder<CommandListenerWrapper, ?> outer = inner;
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
			resultantNode = DISPATCHER.register(getLiteralArgumentBuilder(commandName).requires(generatePermissions(commandName, permissions, requirements)).then(outer));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPI.getConfiguration().hasVerboseOutput()) {
					CommandAPI.logInfo("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				
				DISPATCHER.register(getLiteralArgumentBuilder(alias).requires(generatePermissions(alias, permissions, requirements)).then(outer));
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
	CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, IStringTooltip[] array) {
		String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);
		for (int i = 0; i < array.length; i++) {
			IStringTooltip str = array[i];
			if (str.getSuggestion().toLowerCase(Locale.ROOT).startsWith(remaining)) {
				Message tooltipMsg = null;
				if(str.getTooltip() != null) {
					tooltipMsg = new LiteralMessage(str.getTooltip());
				}
				builder.suggest(str.getSuggestion(), tooltipMsg);
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
	LiteralArgumentBuilder<CommandListenerWrapper> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}

	/**
	 * Creates a literal for a given name that requires a specified permission.
	 * 
	 * @param commandName the name fo the literal to create
	 * @param permission  the permission required to use this literal
	 * @return a brigadier LiteralArgumentBuilder representing a literal
	 */
	LiteralArgumentBuilder<CommandListenerWrapper> getLiteralArgumentBuilderArgument(String commandName,
			CommandPermission permission, Predicate<CommandSender> requirements) {
		LiteralArgumentBuilder<CommandListenerWrapper> builder = LiteralArgumentBuilder.literal(commandName);
		return builder.requires((CommandListenerWrapper clw) -> permissionCheck(NMS.getCommandSenderForCLW(clw), permission, requirements));
	}

	// Gets a RequiredArgumentBuilder for a DynamicSuggestedStringArgument
	<T> RequiredArgumentBuilder<CommandListenerWrapper, T> getRequiredArgumentBuilderDynamic(
			final List<Argument> args, Argument argument) {

		// If there are no changes to the default suggestions, return it as normal
		if (!argument.getOverriddenSuggestions().isPresent()) {
			
			// Construct the builder
			@SuppressWarnings("unchecked")
			RequiredArgumentBuilder<CommandListenerWrapper, T> builder = RequiredArgumentBuilder
					.argument(argument.getNodeName(), (ArgumentType<T>) argument.getRawType());
			
			// Apply requirements
			builder.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw), argument.getArgumentPermission(), argument.getRequirements()));
			
			// Merge default suggestions with parser
			if(builder.getSuggestionsProvider() != null) {
				builder.suggests(mergeSuggestionsWithParser(getArgument(args, argument.getNodeName()), builder.getSuggestionsProvider()));
			}
			
			return builder;
		}

		// Otherwise, we have to handle arguments of the form BiFunction<CommandSender,
		// Object[], String[]>
		else {
			return getRequiredArgumentBuilderWithProvider(argument, toSuggestions(argument.getNodeName(), args));
		}
	}
	
	// Given an argument and a provider, merge the two together and apply the parser before the provider
	private SuggestionProvider<CommandListenerWrapper> mergeSuggestionsWithParser(Argument argument, SuggestionProvider<CommandListenerWrapper> provider) {
		// Merge default suggestions with parser
		return (CommandContext<CommandListenerWrapper> context, SuggestionsBuilder builder) -> {
			try {
				argument.getParser().parse(context.getInput(), new StringRangeWrapper(context.getRange().getStart(), context.getRange().getEnd()));
			} catch (ParseException e) {
				return builder.restart().suggest(e.getMessage()).buildFuture();
			}
			
			return provider.getSuggestions(context, builder);
		};
	}

	// Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	<T> RequiredArgumentBuilder<CommandListenerWrapper, T> getRequiredArgumentBuilderWithProvider(Argument argument,
			SuggestionProvider<CommandListenerWrapper> provider) {
		
		// Construct the builder
		@SuppressWarnings("unchecked")
		RequiredArgumentBuilder<CommandListenerWrapper, T> builder = RequiredArgumentBuilder
				.argument(argument.getNodeName(), (ArgumentType<T>) argument.getRawType());
		
		// Apply requirements and suggestions
		return builder.requires(clw -> permissionCheck(NMS.getCommandSenderForCLW(clw),
				argument.getArgumentPermission(), argument.getRequirements()))
				.suggests(mergeSuggestionsWithParser(argument, provider));
	}
	
	static Argument getArgument(List<Argument> args, String nodeName) {
		return args.stream().filter(arg -> arg.getNodeName().equals(nodeName)).findFirst().get();
	}
	
	SuggestionProvider<CommandListenerWrapper> toSuggestions(String nodeName, List<Argument> args) {
		return (CommandContext<CommandListenerWrapper> context, SuggestionsBuilder builder) -> {
			// Populate Object[], which is our previously filled arguments
			List<Object> previousArguments = new ArrayList<>();

			for (Argument arg : args) {
				if (arg.getNodeName().equals(nodeName)) {
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
				if(result != null) {
					previousArguments.add(result);
				}
			}
			
			return getSuggestionsBuilder(builder,
					getArgument(args, nodeName).getOverriddenSuggestions()
							.orElseGet(() -> (c, m) -> new IStringTooltip[0])
							.apply(NMS.getCommandSenderForCLW(context.getSource()), previousArguments.toArray()));
		};
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
	public Field getField(Class<?> clazz, String name) {
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
}
