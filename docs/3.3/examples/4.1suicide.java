new CommandAPICommand("suicide")
    .executesPlayer((player, args) -> {
		player.setHealth(0);
    }).register();