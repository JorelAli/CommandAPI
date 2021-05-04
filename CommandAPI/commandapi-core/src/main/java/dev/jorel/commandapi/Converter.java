package dev.jorel.commandapi;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;

/**
 * 'Simple' conversion of Plugin commands
 */
public abstract class Converter {
	
	private static final List<Argument> PLAIN_ARGUMENTS = Arrays.asList(new GreedyStringArgument("args"));
	private static final Set<String> CALLER_METHODS = new HashSet<>(Arrays.asList("isPermissionSet", "hasPermission",
			"addAttachment", "removeAttachment", "recalculatePermissions", "getEffectivePermissions", "isOp", "setOp"));

	/**
	 * Convert the provided command name into a CommandAPI-compatible command
	 * @param cmdName The name of the command (without the leading /). For commands such as //set in WorldEdit,
	 * 				  this parameter should be "/set"
	 */
	public static void convert(String cmdName) {
		convertCommand(cmdName, PLAIN_ARGUMENTS);
	}
	
	/**
	 * Convert the provided command name with its list of arguments into a CommandAPI-compatible command
	 * @param cmdName The name of the command (without the leading /). For commands such as //set in WorldEdit,
	 * 				  this parameter should be "/set"
	 * @param arguments The arguments that should be used to parse this command
	 */
	public static void convert(String cmdName, List<Argument> arguments) {
		convertCommand(cmdName, arguments);
	}
	
	private static void convertCommand(String commandName, List<Argument> arguments) {
		CommandAPI.logInfo("Converting command /" + commandName);
		 
		//No arguments
		new CommandAPICommand(commandName)
			.withPermission(CommandPermission.NONE)
			.executesNative((sender, args) -> { 
				CommandSender proxiedSender = sender.getCallee();
				Bukkit.dispatchCommand(proxiedSender, commandName);
			})
			.register();
		
		//Multiple arguments		
		CommandAPICommand multiArgs = new CommandAPICommand(commandName)
			.withPermission(CommandPermission.NONE)
			.withArguments(arguments)
			.executesNative((sender, args) -> { 
				// We know the args are a String[] because that's how converted things are handled in generateCommand()
				CommandSender proxiedSender = sender.getCallee();
				Bukkit.dispatchCommand(proxiedSender, commandName + " " +  String.join(" ", (String[]) args));
			});

		multiArgs.setConverted(true);
		multiArgs.register();
	}
	
	/**
	 * Convert all commands stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param plugin The plugin which commands are to be converted
	 */
	public static void convert(Plugin plugin) {
		CommandAPI.logInfo("Converting commands for " + plugin.getName() + ":");
		plugin.getDescription().getCommands().keySet().forEach(commandName -> convertPluginCommand((JavaPlugin) plugin, commandName, PLAIN_ARGUMENTS));
	}
	
	/**
	 * Convert a command stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param plugin The plugin where the command is registered
	 * @param cmdName The command to convert
	 */
	public static void convert(Plugin plugin, String cmdName) {
		convertPluginCommand((JavaPlugin) plugin, cmdName, PLAIN_ARGUMENTS);
	}
	
	/**
	 * Convert a command stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param plugin The plugin where the command is registered
	 * @param cmdName The command to convert
	 * @param arguments The arguments that should be used to parse this command
	 */
	public static void convert(Plugin plugin, String cmdName, List<Argument> arguments) {
		convertPluginCommand((JavaPlugin) plugin, cmdName, arguments);
	}
	
	/**
	 * Convert a command stated in Plugin's plugin.yml file into CommandAPI-compatible commands
	 * @param plugin The plugin where the command is registered
	 * @param cmdName The command to convert
	 * @param arguments The arguments that should be used to parse this command
	 */
	public static void convert(Plugin plugin, String cmdName, Argument... arguments) {
		convertPluginCommand((JavaPlugin) plugin, cmdName, Arrays.asList(arguments));
	}
	
	private static void convertPluginCommand(JavaPlugin plugin, String commandName, List<Argument> arguments) {
		CommandAPI.logInfo("Converting " + plugin.getName() + " command /" + commandName);
		/* Parse the commands */
		Map<String, Object> cmdData = plugin.getDescription().getCommands().get(commandName);
		
		if(cmdData == null) {
			CommandAPI.getLog().severe("Couldn't find /" + commandName + " in " + plugin.getName() + "'s plugin.yml. Are you sure you're not confusing it with an alias?");
			return;
		}

		//Convert stupid YAML aliases to a String[] for CommandAPI
		String[] aliases = null;
		Object aliasObj = cmdData.get("aliases");
		if(aliasObj == null) {
			aliases = new String[0];
		} else if(aliasObj instanceof String) {
			aliases = new String[] {(String) aliasObj};
		} else if(aliasObj instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) aliasObj;
			aliases = list.toArray(new String[0]);
		}
		if(aliases.length != 0) {
			CommandAPI.logInfo("Aliases for command /" + commandName + " found. Using aliases " + Arrays.deepToString(aliases));
		}
		 
		//Convert YAML to CommandPermission
		CommandPermission permissionNode = null;
		String permission = (String) cmdData.get("permission");
		if(permission == null) {
			permissionNode = CommandPermission.NONE;
		} else {
			CommandAPI.logInfo("Permission for command /" + commandName + " found. Using " + permission);
			permissionNode = CommandPermission.fromString(permission);
		}
		
		//No arguments
		new CommandAPICommand(commandName)
			.withPermission(permissionNode)
			.withAliases(aliases)
			.executesNative((sender, args) -> { 
				org.bukkit.command.Command command = plugin.getCommand(commandName);
				if(command == null) {
					command = CommandAPIHandler.getInstance().getNMS().getSimpleCommandMap().getCommand(commandName);
				}
				CommandSender proxiedSender = CommandAPI.getConfiguration().shouldSkipSenderProxy(plugin) ? sender.getCallee() : mergeProxySender(sender);
				command.execute(proxiedSender, commandName, new String[0]);
			})
			.register();
		
		//Multiple arguments		
		CommandAPICommand multiArgs = new CommandAPICommand(commandName)
			.withPermission(permissionNode)
			.withAliases(aliases)
			.withArguments(arguments)
			.executesNative((sender, args) -> { 
				// We know the args are a String[] because that's how converted things are handled in generateCommand()
				org.bukkit.command.Command command = plugin.getCommand(commandName);
				if(command == null) {
					command = CommandAPIHandler.getInstance().getNMS().getSimpleCommandMap().getCommand(commandName);
				}
				CommandSender proxiedSender = CommandAPI.getConfiguration().shouldSkipSenderProxy(plugin) ? sender.getCallee() : mergeProxySender(sender);
				command.execute(proxiedSender, commandName, (String[]) args);
			});
		// Good grief, what a hack~
		multiArgs.setConverted(true);
		multiArgs.register();
	}
	
	/*
	 * https://www.jorel.dev/blog/Simplifying-Bukkit-CommandSenders/
	 */
	private static CommandSender mergeProxySender(ProxiedCommandSender proxySender) {
		Class<?>[] calleeInterfaces = proxySender.getCallee().getClass().getInterfaces();
		Class<?>[] interfaces;
		if(proxySender.getCallee().getClass().isInterface()) {
			interfaces = new Class<?>[calleeInterfaces.length + 1];
			System.arraycopy(calleeInterfaces, 0, interfaces, 1, calleeInterfaces.length);
			interfaces[0] = proxySender.getCallee().getClass();
		} else {
			interfaces = calleeInterfaces;
		}
		return (CommandSender) Proxy.newProxyInstance(CommandSender.class.getClassLoader(), interfaces,
				(Object p, Method method, Object[] args) -> method.invoke(
						CALLER_METHODS.contains(method.getName()) ? proxySender.getCaller() : proxySender.getCallee(),
						args));
	}

}
