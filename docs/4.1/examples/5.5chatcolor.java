LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("chatcolor", new ChatColorArgument());

new CommandAPICommand("namecolor")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
        ChatColor color = (ChatColor) args[0];
	    player.setDisplayName(color + player.getName());
    })
    .register();