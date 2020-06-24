LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("arg0", new StringArgument());
arguments.put("arg1", new PotionEffectArgument());
arguments.put("arg2", new LocationArgument());

new CommandAPICommand("cmd")
    .withArguments(arguments)
    .executes((sender, args) -> {
        String stringArg = (String) args[0];
        PotionEffectType potionArg = (PotionEffectType) args[1];
        Location locationArg = (Location) args[2];
    })
	.register();