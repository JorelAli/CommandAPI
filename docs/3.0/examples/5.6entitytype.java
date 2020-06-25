LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

arguments.put("entity", new EntityTypeArgument());
arguments.put("amount", new IntegerArgument(1, 100)); //Prevent spawning too many entities

new CommandAPICommand("spawnmob")
    .withArguments(arguments)
    .executesPlayer((Player player, Object[] args) -> {
        for(int i = 0; i < (int) args[1]; i++) {
            player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
        }
    })
    .register();