new CommandAPICommand("suicide")
    .executesPlayer((player, args) -> {
		player.setHealth(0);
    })
    .executesEntity((entity, args) -> {
        entity.getWorld().createExplosion(e.getLocation(), 4);
		entity.remove();
    }).register();