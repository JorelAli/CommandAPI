# Literal arguments

Literal arguments are used to represent "forced options" for a command. For instance, take Minecraft's `/gamemode` command. The syntax consists of the following:

```
/gamemode <mode> [player]
```

It consists of a gamemode, followed by an optional player argument. The list of gamemodes are as follows:

```
/gamemode survival 
/gamemode creative
/gamemode adventure
/gamemode spectator
```

Unlike regular commands (as those implemented by Bukkit for example), these four options are "hardcoded" - they're not "suggestions". The user can _only_ enter one of these four examples, no other values are allowed.

## Literal arguments vs regular arguments

Unlike regular arguments that are shown in this chapter, the literal argument is _technically_ not an argument. Due to this fact, the literal argument is **not** present in the `args[]` for the command declaration.

### Example - Literal arguments and regular arguments

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("literal", new LiteralArgument("hello"));
arguments.put("text", new TextArgument());

CommandAPI.getInstance().register("mycommand", arguments, (sender, args) -> {
	/* This gives the variable "text" the contents of the 
	 * TextArgument, and not the literal "hello" */
	String text = (String) args[0];
});
```

As you can see from this example, when you retrieve `args[0]`, it returns the value of the `TextArgument` instead of the `LiteralArgument`

## Example - Gamemode command using literal arguments

This is a demonstration of how you could create a command similar to Minecraft's `/gamemode` command by using literal arguments. To do this, we are effectively registering 4 separate commands, each called `/gamemode`, but with different literal arguments.

```java
//Create a map of gamemode names to their respective objects
HashMap<String, GameMode> gamemodes = new HashMap<>();
gamemodes.put("adventure", GameMode.ADVENTURE);
gamemodes.put("creative", GameMode.CREATIVE);
gamemodes.put("spectator", GameMode.SPECTATOR);
gamemodes.put("survival", GameMode.SURVIVAL);

//Iterate over the map
for(String key : gamemodes.keySet()) {
    
    //Create our arguments as usual, using the LiteralArgument for the name of the gamemode
	LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
	arguments.put(key, new LiteralArgument(key));
    
    //Register the command as usual
	CommandAPI.getInstance().register("gamemode", arguments, (sender, args) -> {
	    if(sender instanceof Player) {
	        Player player = (Player) sender;
            
            //Retrieve the object from the map via the key and NOT the args[]
	        player.setGameMode(gamemodes.get(key));
	    }
	});
}
```

## Literal argument warnings

Literal arguments require a string in the constructor. If the literal is an empty String or is null, the CommandAPI will throw a `BadLiteralException`.

Because literal arguments are _"hardcoded"_, each literal is effectively mapped to a single command. This is shown when using the configuration option `create-dispatcher-json: true` which shows the JSON result of registered commands. For instance, take the `/defaultgamemode` command:

```json
"defaultgamemode": {
    "type": "literal",
    "children": {
        "adventure": {
            "type": "literal",
            "executable": true
        },
        "creative": {
            "type": "literal",
            "executable": true
        },
        "spectator": {
            "type": "literal",
            "executable": true
        },
        "survival": {
            "type": "literal",
            "executable": true
        }
    }
},
```

Each option produces a new "command" in the tree of commands. This means that having exceptionally large lists of literals, or nested literals (e.g. `/command <literal1> <literal2>`) can cause very large trees which cannot be sent to the clients _(it can cause clients to crash)_.

> **Developer's Note:**
>
> Take care when using literal arguments. If your list of arguments is exceptionally large, or contains many nested arguments, the server may be unable to send the command information to the client. If many command argument choices are required, consider using a `StringArgument` and using `.overrideSuggestions()` to create your own list of required arguments.