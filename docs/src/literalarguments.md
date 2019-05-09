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

Unlike regular commands (as those implemented by Bukkit for example), these four options are "hardcoded" - they're not "suggestions".

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

