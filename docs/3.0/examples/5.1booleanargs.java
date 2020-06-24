// Load keys from config file
String[] configKeys = getConfig().getKeys(true).toArray(new String[0]);

// Create arguments with the config key and a boolean value to set it to
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("config-key", new TextArgument().overrideSuggestions(configKeys));
arguments.put("value", new BooleanArgument());

// Register our command
new CommandAPICommand("editconfig")
    .withArguments(arguments)
    .executes((sender, args) -> {
        // Update the config with the boolean argument
        getConfig().set((String) args[0], (boolean) args[1]);
    })
    .register();