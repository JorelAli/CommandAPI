package io.github.jorelali.commandapi.api;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.github.jorelali.commandapi.api.arguments.Argument;
import io.github.jorelali.commandapi.api.arguments.CustomArgument;
import io.github.jorelali.commandapi.api.arguments.CustomArgument.CustomArgumentException;
import io.github.jorelali.commandapi.api.arguments.CustomArgument.MessageBuilder;
import io.github.jorelali.commandapi.api.arguments.CustomProvidedArgument;
import io.github.jorelali.commandapi.api.arguments.EntitySelectorArgument;
import io.github.jorelali.commandapi.api.arguments.LiteralArgument;
import io.github.jorelali.commandapi.api.arguments.Location2DArgument;
import io.github.jorelali.commandapi.api.arguments.LocationArgument;
import io.github.jorelali.commandapi.api.arguments.LocationType;
import io.github.jorelali.commandapi.api.arguments.ScoreHolderArgument;
import io.github.jorelali.commandapi.api.nms.NMS;

/**
 * Class to access the main methods in NMS. The wrapper's implementations occur
 * here.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class CommandAPIHandler {
	private TreeMap<String, CommandPermission> permissionsToFix;

	// Cache maps
	private static Map<ClassCache, Field> fields;
	private static Map<ClassCache, Method> methods;

	// NMS variables
	private static String packageName = null;
	private CommandDispatcher dispatcher;

	private static NMS nms;
	private Object nmsServer;

	public static NMS getNMS() {
		return nms;
	}

	protected CommandAPIHandler() throws ClassNotFoundException {
		// Package checks

		try {
			Class.forName("com.mojang.brigadier.CommandDispatcher");
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Cannot hook into Brigadier (Are you running Minecraft 1.13 or above?)");
		}

		// Setup NMS
		try {
			this.nmsServer = Bukkit.getServer().getClass().getDeclaredMethod("getServer").invoke(Bukkit.getServer());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			CommandAPIMain.getLog().severe("Unable to hook into NMS properly!");
		}
		CommandAPIHandler.packageName = nmsServer.getClass().getPackage().getName();

		// Load higher order versioning
		String hoVersion = null;
		try {
			hoVersion = (String) Class.forName(packageName + ".MinecraftServer").getDeclaredMethod("getVersion")
					.invoke(nmsServer);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			CommandAPIMain.getLog().severe("Failed to load higher order versioning system!");
		}

		// Handle versioning
		Version version = new Version(packageName.split("\\Q.\\E")[3]);
		try {
			switch (hoVersion) {
			case "1.13":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_13").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.13.1":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_13_1").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.13.2":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_13_2").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.14":
			case "1.14.1":
			case "1.14.2":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_14").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.14.3":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_14_3").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.14.4":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_14_4").getDeclaredConstructor()
						.newInstance();
				break;
			case "1.15":
			case "1.15.1":
			case "1.15.2":
				nms = (NMS) Class.forName("io.github.jorelali.commandapi.api.nms.NMS_1_15").getDeclaredConstructor()
						.newInstance();
				break;
			default:
				throw new UnsupportedClassVersionError("This version of Minecraft is unsupported: " + version);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		// Log successful hooks
		if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
			String compatibleVersions = Arrays.toString(nms.compatibleVersions());
			compatibleVersions = compatibleVersions.substring(1, compatibleVersions.length() - 1);
			CommandAPIMain.getLog()
					.info("Hooked into NMS " + version + " (compatible with " + compatibleVersions + ")");
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
			CommandAPIMain.getLog().warning("Couldn't hook into Spigot for chat/chatcomponents");
		}

		// Everything from this line will use getNMSClass(), so we initialize our cache
		// here
		fields = new HashMap<>();
		methods = new HashMap<>();
		permissionsToFix = new TreeMap<>();

		this.dispatcher = nms.getBrigadierDispatcher(nmsServer);
	}

	// Unregister a command
	protected void unregister(String commandName, boolean force) {
		try {
			if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
				CommandAPIMain.getLog().info("Unregistering command /" + commandName);
			}

			// Get the child nodes from the loaded dispatcher class
			Field children = getField(CommandNode.class, "children");
			Map<String, CommandNode<?>> commandNodeChildren = (Map<String, CommandNode<?>>) children
					.get(dispatcher.getRoot());

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

	// Used in the register() method to generate the command to actually be
	// registered
	private Command generateCommand(LinkedHashMap<String, Argument> args, CustomCommandExecutor executor)
			throws CommandSyntaxException {

		// Generate our command from executor
		return (cmdCtx) -> {

			// Get the CommandSender via NMS
			CommandSender sender = nms.getSenderForCommand(cmdCtx);

			// Array for arguments for executor
			List<Object> argList = new ArrayList<>();

			// Populate array
			for (Entry<String, Argument> entry : args.entrySet()) {
				switch (entry.getValue().getArgumentType()) {
				case ADVANCEMENT:
					argList.add(nms.getAdvancement(cmdCtx, entry.getKey()));
					break;
				case CHATCOLOR:
					argList.add(nms.getChatColor(cmdCtx, entry.getKey()));
					break;
				case CHAT_COMPONENT:
					argList.add(nms.getChatComponent(cmdCtx, entry.getKey()));
					break;
				case CUSTOM:
					CustomArgument arg = (CustomArgument) entry.getValue();
					String customresult = (String) cmdCtx.getArgument(entry.getKey(), String.class);
					try {
						argList.add(arg.getParser().apply(customresult));
					} catch (CustomArgumentException e) {
						throw e.toCommandSyntax(customresult, cmdCtx);
					} catch (Exception e) {
						String errorMsg = new MessageBuilder("Error in executing command ").appendFullInput()
								.append(" - ").appendArgInput().appendHere().toString().replace("%input%", customresult)
								.replace("%finput%", cmdCtx.getInput());
						throw new SimpleCommandExceptionType(() -> {
							return errorMsg;
						}).create();
					}
					break;
				case ENCHANTMENT:
					argList.add(nms.getEnchantment(cmdCtx, entry.getKey()));
					break;
				case ENTITY_SELECTOR:
					EntitySelectorArgument argument = (EntitySelectorArgument) entry.getValue();
					argList.add(nms.getEntitySelector(cmdCtx, entry.getKey(), argument.getEntitySelector()));
					break;
				case ENTITY_TYPE:
					argList.add(nms.getEntityType(cmdCtx, entry.getKey(), sender));
					break;
				case FUNCTION:
					argList.add(nms.getFunction(cmdCtx, entry.getKey()));
					break;
				case ITEMSTACK:
					argList.add(nms.getItemStack(cmdCtx, entry.getKey()));
					break;
				case LITERAL:
					break;
				case LOCATION:
					LocationType locationType = ((LocationArgument) entry.getValue()).getLocationType();
					argList.add(nms.getLocation(cmdCtx, entry.getKey(), locationType, sender));
					break;
				case LOOT_TABLE:
					argList.add(nms.getLootTable(cmdCtx, entry.getKey()));
					break;
				case PARTICLE:
					argList.add(nms.getParticle(cmdCtx, entry.getKey()));
					break;
				case PLAYER:
					argList.add(nms.getPlayer(cmdCtx, entry.getKey()));
					break;
				case POTION_EFFECT:
					argList.add(nms.getPotionEffect(cmdCtx, entry.getKey()));
					break;
				case RECIPE:
					argList.add(nms.getRecipe(cmdCtx, entry.getKey()));
					break;
				case SIMPLE_TYPE:
					argList.add(cmdCtx.getArgument(entry.getKey(), entry.getValue().getPrimitiveType()));
					break;
				case SOUND:
					argList.add(nms.getSound(cmdCtx, entry.getKey()));
					break;
				case TIME:
					argList.add(nms.getTime(cmdCtx, entry.getKey()));
					break;
				case LOCATION_2D:
					LocationType locationType2d = ((Location2DArgument) entry.getValue()).getLocationType();
					argList.add(nms.getLocation2D(cmdCtx, entry.getKey(), locationType2d, sender));
					break;
				case INT_RANGE:
					argList.add(nms.getIntRange(cmdCtx, entry.getKey()));
					break;
				case FLOAT_RANGE:
					argList.add(nms.getFloatRange(cmdCtx, entry.getKey()));
					break;
				case ENVIRONMENT:
					argList.add(nms.getDimension(cmdCtx, entry.getKey()));
					break;
				case ROTATION:
					argList.add(nms.getRotation(cmdCtx, entry.getKey()));
					break;
				case AXIS:
					argList.add(nms.getAxis(cmdCtx, entry.getKey()));
					break;
				case SCOREBOARD_SLOT:
					argList.add(nms.getScoreboardSlot(cmdCtx, entry.getKey()));
					break;
				case TEAM:
					argList.add(nms.getTeam(cmdCtx, entry.getKey(), sender));
					break;
				case OBJECTIVE_CRITERIA:
					argList.add(nms.getObjectiveCriteria(cmdCtx, entry.getKey()));
					break;
				case OBJECTIVE:
					argList.add(nms.getObjective(cmdCtx, entry.getKey(), sender));
					break;
				case CHAT:
					argList.add(nms.getChat(cmdCtx, entry.getKey()));
					break;
				case SCORE_HOLDER:
					ScoreHolderArgument scoreHolderArgument = (ScoreHolderArgument) entry.getValue();
					argList.add(scoreHolderArgument.isSingle() ? nms.getScoreHolderSingle(cmdCtx, entry.getKey())
							: nms.getScoreHolderMultiple(cmdCtx, entry.getKey()));
					break;
				case NBT_COMPOUND:
					argList.add(nms.getNBTCompound(cmdCtx, entry.getKey()));
					break;
				case MATH_OPERATION:
					argList.add(nms.getMathOperation(cmdCtx, entry.getKey()));
					break;
				}
			}

			return executor.execute(sender, argList.toArray());
		};
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Permissions //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	private Predicate generatePermissions(String commandName, CommandPermission permission) {

		// If we've already registered a permission, set it to the "parent" permission.
		/*
		 * This permission generation setup ONLY works iff: - You register the parent
		 * permission node FIRST. - Example: /mycmd - permission node my.perm /mycmd
		 * <arg> - permission node my.perm.other
		 *
		 * the my.perm.other permission node is revoked for the COMMAND REGISTRATION,
		 * however: - The permission node IS REGISTERED. - The permission node, if used
		 * for an argument (as in this case), will be used for suggestions for said
		 * argument
		 */
		if (permissionsToFix.containsKey(commandName.toLowerCase())) {
			if (!permissionsToFix.get(commandName.toLowerCase()).equals(permission)) {
				permission = permissionsToFix.get(commandName.toLowerCase());
			}
		} else {
			// Add permission to a list to fix conflicts with minecraft:permissions
			permissionsToFix.put(commandName.toLowerCase(), permission);
		}

		final CommandPermission finalPermission = permission;

		// Register it to the Bukkit permissions registry
		if (finalPermission.getPermission() != null) {
			try {
				Bukkit.getPluginManager().addPermission(new Permission(finalPermission.getPermission()));
			} catch (IllegalArgumentException e) {
			}
		}

		return (clw) -> {
			return permissionCheck(nms.getCommandSenderForCLW(clw), finalPermission);
		};
	}

	// Checks if a CommandSender has permission permission from CommandPermission
	// permission
	private boolean permissionCheck(CommandSender sender, CommandPermission permission) {
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

	protected void fixPermissions() {
		/*
		 * Makes permission checks more "Bukkit" like and less "Vanilla Minecraft" like
		 */
		try {

			// Get the command map to find registered commands
			SimpleCommandMap map = nms.getSimpleCommandMap();
			Field f = getField(SimpleCommandMap.class, "knownCommands");
			Map<String, org.bukkit.command.Command> knownCommands = (Map<String, org.bukkit.command.Command>) f
					.get(map);

			CommandAPIMain.getLog().info("Linking permissions to commands:");
			permissionsToFix.forEach((cmdName, perm) -> {

				if (perm.equals(CommandPermission.NONE)) {
					if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
						CommandAPIMain.getLog().info("NONE -> /" + cmdName);
					}
					// Set the command permission to empty string (Minecraft standard for "no
					// permission required")
					if (nms.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
					if (nms.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
						knownCommands.get(cmdName).setPermission("");
					}
				} else {
					if (perm.getPermission() != null) {
						if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
							CommandAPIMain.getLog().info(perm.getPermission() + " -> /" + cmdName);
						}
						// Set the command permission to the (String) permission node
						if (nms.isVanillaCommandWrapper(knownCommands.get(cmdName))) {
							knownCommands.get(cmdName).setPermission(perm.getPermission());
						}
						if (nms.isVanillaCommandWrapper(knownCommands.get("minecraft:" + cmdName))) {
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
	protected void register(String commandName, CommandPermission permissions, String[] aliases,
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
			resultantNode = this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permissions)).executes(command));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
					CommandAPIMain.getLog().info("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				// TODO: What if we just do
				// getLiteralArgumentBuilder(alias).redirect(resultantNode)?
				this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(alias)
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
				if (innerArg instanceof LiteralArgument) {
					String str = ((LiteralArgument) innerArg).getLiteral();
					inner = getLiteralArgumentBuilderArgument(str, innerArg.getArgumentPermission()).executes(command);
				} else if (innerArg instanceof CustomProvidedArgument) {
					inner = getRequiredArgumentBuilderWithProvider(keys.get(keys.size() - 1), innerArg.getRawType(),
							innerArg.getArgumentPermission(),
							nms.getSuggestionProvider(((CustomProvidedArgument) innerArg).getSuggestionProvider()))
									.executes(command);
				} else {
					inner = getRequiredArgumentBuilderDynamic(keys.get(keys.size() - 1), innerArg,
							innerArg.getArgumentPermission()).executes(command);
				}
			}

			// Link everything else up, except the first
			ArgumentBuilder outer = inner;
			for (int i = keys.size() - 2; i >= 0; i--) {
				Argument outerArg = args.get(keys.get(i));
				if (outerArg instanceof LiteralArgument) {
					String str = ((LiteralArgument) outerArg).getLiteral();
					outer = getLiteralArgumentBuilderArgument(str, outerArg.getArgumentPermission()).then(outer);
				} else if (outerArg instanceof CustomProvidedArgument) {
					outer = getRequiredArgumentBuilderWithProvider(keys.get(i), outerArg.getRawType(),
							outerArg.getArgumentPermission(),
							nms.getSuggestionProvider(((CustomProvidedArgument) outerArg).getSuggestionProvider()))
									.then(outer);
				} else {
					outer = getRequiredArgumentBuilderDynamic(keys.get(i), outerArg, outerArg.getArgumentPermission())
							.then(outer);
				}
			}

			// Link command name to first argument and register
			resultantNode = this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(commandName)
					.requires(generatePermissions(commandName, permissions)).then(outer));

			// Register aliases
			for (String alias : aliases) {
				if (CommandAPIMain.getConfiguration().hasVerboseOutput()) {
					CommandAPIMain.getLog().info("Registering alias /" + alias + " -> " + resultantNode.getName());
				}
				this.dispatcher.register((LiteralArgumentBuilder) getLiteralArgumentBuilder(alias)
						.requires(generatePermissions(alias, permissions)).then(outer));
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

			nms.createDispatcherFile(nmsServer, file, dispatcher);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: SuggestionProviders //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// NMS ICompletionProvider.a()
	private CompletableFuture<Suggestions> getSuggestionsBuilder(SuggestionsBuilder builder, String[] array) {
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

	// Gets a LiteralArgumentBuilder for a command name
	private LiteralArgumentBuilder<?> getLiteralArgumentBuilder(String commandName) {
		return LiteralArgumentBuilder.literal(commandName);
	}

	private LiteralArgumentBuilder<?> getLiteralArgumentBuilderArgument(String commandName,
			CommandPermission permission) {
		return LiteralArgumentBuilder.literal(commandName).requires(clw -> {
			return permissionCheck(nms.getCommandSenderForCLW(clw), permission);
		});
	}

	// Gets a RequiredArgumentBuilder for a DynamicSuggestedStringArgument
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderDynamic(String argumentName, Argument type,
			CommandPermission permission) {
		return getRequiredArgumentBuilderWithProvider(argumentName, type.getRawType(), permission,
				(context, builder) -> getSuggestionsBuilder(builder,
						type.getOverriddenSuggestions().apply(nms.getCommandSenderForCLW(context.getSource()))));
	}

	// Gets a RequiredArgumentBuilder for an argument, given a SuggestionProvider
	private <T> RequiredArgumentBuilder<?, T> getRequiredArgumentBuilderWithProvider(String argumentName,
			ArgumentType<T> type, CommandPermission permission, SuggestionProvider provider) {
		return RequiredArgumentBuilder.argument(argumentName, type).requires(clw -> {
			return permissionCheck(nms.getCommandSenderForCLW(clw), permission);
		}).suggests(provider);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	// SECTION: Reflection //
	//////////////////////////////////////////////////////////////////////////////////////////////////////

	// Gets a field using reflection and caches it
	public static Field getField(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if (fields.containsKey(key)) {
			return fields.get(key);
		} else {
			Field result = null;
			try {
				result = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
			result.setAccessible(true);
			fields.put(key, result);
			return result;
		}
	}

	// Gets a field using reflection and caches it
	public static Method getMethod(Class<?> clazz, String name) {
		ClassCache key = new ClassCache(clazz, name);
		if (methods.containsKey(key)) {
			return methods.get(key);
		} else {
			Method result = null;
			try {
				result = clazz.getDeclaredMethod(name);
			} catch (SecurityException | NoSuchMethodException e) {
				e.printStackTrace();
			}
			result.setAccessible(true);
			methods.put(key, result);
			return result;
		}
	}

	private class Version {
		private int primaryVersion; // e.g. 14
		private int rev; // e.g. 1

		public Version(String version) {
			this.primaryVersion = Integer.parseInt(version.split("_")[1]);

			Matcher revMatcher = Pattern.compile("(?<=R).+").matcher(version);
			if (revMatcher.find()) {
				this.rev = Integer.parseInt(revMatcher.group());
			}
		}

		@Override
		public String toString() {
			return "Version " + primaryVersion + " rev " + rev;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			Version other = (Version) obj;

			if (primaryVersion != other.primaryVersion) {
				return false;
			}
			if (rev != other.rev) {
				return false;
			}
			return true;
		}
	}

	/**
	 * Class to store cached methods and fields
	 * <p>
	 * This is required because each key is made up of a class and a field or method
	 * name
	 */
	@SuppressWarnings("unused")
	private static class ClassCache {

		private final Class<?> clazz;
		private final String name;

		public ClassCache(Class<?> clazz, String name) {
			this.clazz = clazz;
			this.name = name;
		}
	}
}
