# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13

## Purpose
This project provides an API to help Bukkit/Spigot developers use the new Minecraft 1.13 command UI.

![commandUI](https://i.imgur.com/aTJa77G.gif "commandUI")

## Basic principles (and changes between this API compared to normal command registration)
- Commands are registered using the command registration system for default Minecraft commands. These are sent to the player in a [special packet](https://wiki.vg/Command_Data).
- Commands registered using this Command API do not need to be stated in the plugin.yml (neither do they need a registered CommandExecutor)
- This may be less compatible with plugins which lookup commands from other plugins
- Arguments can now be represented as [primitive data types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) as opposed to Strings
- Arguments require a description to identify them

## Super basic usage (Version 1.0 ONLY)

* Download [version 1.0](https://github.com/JorelAli/1.13-Command-API/releases/tag/v1.0) and add it to your build path
* Add the plugin as a dependent in the plugin.yml (`depend: [CommandAPI]`)
* (Make sure the CommandAPI.jar file is included in the plugins folder when running any servers)
* Create a new instance of the CommandAPI
  ```java
  CommandAPI commandRegister = new CommandAPI();
  ```
* Create a `LinkedHashMap<String, ArgumentType>` to store your arguments
  ```java
  LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
  //populate hashmap here
  ```
* Register the command via the command register
  ```java
  commandRegister.register("COMMAND_NAME", arguments, (sender, args) -> {/* Command execution goes here */});
  ```

For an example of this in action, view the [example class](https://github.com/JorelAli/1.13-Command-API/blob/v1.0/1.13CommandAPI/src/io/github/jorelali/commandapi/Example.java)!

## Examples (Version 1.0 ONLY)!

Multiple arguments with separate outcomes:
```java
// /walkspeed <speed>
// /walkspeed <speed> <target>

CommandAPI commandRegister = new CommandAPI();

LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("speed", ArgumentType.FLOAT);

commandRegister.register("walkspeed", arguments, (sender, args) -> {
  //Sender check 
  if(sender instanceof Player) {
    Player player = (Player) sender;
    player.setWalkSpeed((float) args[0]);
  } else {
    sender.sendMessage("You must be a player to use this command");
  }
});

//Adding a new argument, as an additional command to the previous
arguments.put("target", ArgumentType.STRING);

commandRegister.register("walkspeed", arguments, (sender, args) -> {
  //Get target
  Bukkit.getPlayer((String) args[1]).setWalkSpeed((float) args[0]);
});
```

## Known issues
- Permissions do not exist yet (Easily implemented, haven't gotten round to it yet)

## Future plans
- Argument updates
  - Ranged primitive types (only accept numbers between certain values)
  - Enumerated types (e.g. survival, creative, spectator, adventure)
  - Complex argument types (online players, itemstacks etc.)
- Alias support
- Permission support
