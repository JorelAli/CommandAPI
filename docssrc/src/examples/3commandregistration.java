// Create our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("message", new GreedyStringArgument());

//Create our command
new CommandAPICommand("broadcastmsg")
	.withArguments(arguments)                     // The arguments
	.withAliases("broadcast", "broadcastmessage") // Command aliases
	.withPermission(CommandPermission.OP)         // Required permissions
	.executes((sender, args) -> {
		String message = (String) args[0];
		Bukkit.getServer().broadcastMessage(message);
	}).register();