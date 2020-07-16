// Register the /god command with the permission node "command.god"
new CommandAPICommand("god")
	.withPermission(CommandPermission.fromString("command.god"))
	.executesPlayer((player, args) -> {
		player.setInvulnerable(true);
	})
	.register();