# Command registration

To register commands with the CommandAPI, there are several methods which can be used, depending on whether you want your command to have aliases or permissions in order to run it.

| CommandRegistration method                                   | Outcome                                                   |
| ------------------------------------------------------------ | --------------------------------------------------------- |
| `CommandAPI.getInstance().register(String, LinkedHashMap, CommandExecutor)` | Basic command registration                                |
| `CommandAPI.getInstance().register(String, String[], LinkedHashMap, CommandExecutor)` | Register command with an array of aliases                 |
| `CommandAPI.getInstance().register(String, CommandPermission, LinkedHashMap, CommandExecutor)` | Register command which need certain permissions           |
| `CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, CommandExecutor)` | Register command with aliases and permission requirements |

The following fields are as follows:

* `String` - The command name

  The first argument represents the command name which will be registered. For instance, to register the command `/god`, you would use the following:

  ```java
  CommandAPI.getInstance().register("god", ...);
  ```
* `LinkedHashMap<String, Argument>` - The list of arguments 

  The CommandAPI requires a list of arguments which are used for the command. The argument map consists of a key which is the tooltip that is displayed as a prompt to users entering commands, and a value which is an instance of an argument (See the section on arguments). This list of arguments is interpreted in the _order that arguments are added to the LinkedHashMap_.

* `String[]` - An array of aliases that the command can be run via 
* `CommandPermission` - The required permission to execute a command. (See the section on permissions).

## Command loading order

In order to register commands properly, **commands must be registered before the server finishes loading**. The CommandAPI will prevent command registration after the server has loaded. This basically means that all command registration must occur during a plugin's `onLoad()` or `onEnable()` method. With the CommandAPI, depending on which of these functions you load your commands is crutial if your plugin is used with Minecraft's functions.

| When to load        | What to do                                                                               |
| ------------------- | ---------------------------------------------------------------------------------------- |
| `onLoad()` method   | Register commands to be used in Minecraft functions (see Function section for more info) |
| `onEnable()` method | Register regular commands                                                                |

## Command unregistration

The CommandAPI has support to unregister commands completely from Minecraft's command list. This includes Minecraft built in commands!

| Method                                                       | Result                                                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `CommandAPI.getInstance().unregister(String cmd)`            | Unregisters a command from the game                          |
| `CommandAPI.getInstance().unregister(String cmd, boolean force)` | Attempts to unregister a command from the game by force. This includes `/minecraft:cmd`, `/bukkit:cmd` and `/spigot:cmd` commands as well. |

### Example - Replacing Minecraft's `/gamemode` command

To replace a command, we can first unregister it and then register our implementation of that command.

```java
//Unregister the gamemode command from the server (by force)
CommandAPI.getInstance().unregister("gamemode", true);

LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

/* Arguments for the gamemode command. In this sample, I'm just 
 * using a simple literal argument which allows for /gamemode survival */
arguments.put("gamemode", new LiteralArgument("survival"));

CommandAPI.getInstance().register("gamemode", arguments, (sender, args) -> {
	//Implementation of our /gamemode command
});
```

> **Developer's Note:**
>
> Command unregistration, although powerful, is highly unrecommended. It is the CommandAPI's most "dangerous" feature as it can cause unexpected sideffects, such as command blocks executing commands you wouldn't expect them to. In almost every case, I'd recommend just creating a new command instead of unregistering one to replace it.
>
> For instance, instead of unregistering `/gamemode`, you could register a command `/gm` or `/customgamemode`.
