// Register /kill command normally. Since no permissions are applied, anyone can run this command
new CommandAPICommand("kill")
	.executesPlayer((player, args) -> {
		player.setHealth(0);
	})
	.register();