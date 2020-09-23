// Declare our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("target", new PlayerArgument().withPermission(CommandPermission.OP));

// Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
new CommandAPICommand("kill")
	.withArguments(arguments)
	.executesPlayer((player, args) -> {
		((Player) args[0]).setHealth(0);
	})
	.register();