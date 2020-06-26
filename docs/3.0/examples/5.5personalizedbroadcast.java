LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("message", new ChatArgument());

new CommandAPICommand("pbroadcast")
    .withArguments(arguments)
    .executes((sender, args) -> {
        BaseComponent[] message = (BaseComponent[]) args[0];
    
        //Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(message);
    })
    .register();