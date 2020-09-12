//Unregister the gamemode command from the server (by force)
CommandAPI.unregister("gamemode", true);

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

/* Arguments for the gamemode command. In this sample, I'm just 
 * using a simple literal argument which allows for /gamemode survival */
arguments.put("gamemode", new LiteralArgument("survival"));

new CommandAPICommand("gamemode")
    .withArguments(arguments)
    .executes((sender, args) -> {
        //Implementation of our /gamemode command
    }).register();