
new CommandAPICommand("perm")
 	.withSubcommand(new CommandAPICommand("group")
		.withSubcommand(new CommandAPICommand("add")
			.withArguments(new StringArgument("permission"))
			.withArguments(new StringArgument("groupName"))
			.executes((sender, args) -> {
				//perm group add 
				Bukkit.broadcastMessage("perm group add " + Arrays.deepToString(args));
				
			})
		)
		.withSubcommand(new CommandAPICommand("remove")
			.withArguments(new StringArgument("permission"))
			.withArguments(new StringArgument("groupName"))
			.executes((sender, args) -> {
				//perm group remove 
				Bukkit.broadcastMessage("perm group remove " + Arrays.deepToString(args));
			})
		)
		.executes((s, a) -> {})
	)
 	.withSubcommand(new CommandAPICommand("user")
		.withSubcommand(new CommandAPICommand("add")
			.withArguments(new StringArgument("permission"))
			.withArguments(new StringArgument("userName"))
			.executes((sender, args) -> {
				//perm user add 
				Bukkit.broadcastMessage("perm user add " + Arrays.deepToString(args));
			})
		)
		.withSubcommand(new CommandAPICommand("remove")
			.withArguments(new StringArgument("permission"))
			.withArguments(new StringArgument("userName"))
			.executes((sender, args) -> {
				//perm user remove 
				Bukkit.broadcastMessage("perm user remove" + Arrays.deepToString(args));
			})
		)
	)
 	.register();