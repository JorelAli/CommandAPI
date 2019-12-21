# Command registration

To register commands with the CommandAPI, the `CommandAPICommand` class is used. It follows a builder pattern to improve readability.

I think the easiest way to explain it is with an example:

```java
{{#include examples/3commandregistration.java}}
```

- First, we create our arguments. This is described in more detail in [the section on arguments](./arguments.html). 
- We then create a new `CommandAPICommand`, with the name of the command that the player must enter to run it. 
- We then add the arguments to the command with `withArguments`.
- We add an alias "broadcast". This allows the sender to use `/broadcastmsg <message>` or `/broadcast <message>`.
- We require the sender to be OP by using `withPermission`.
- We control what it does using `executes` (this is described in more detail in [the section on command executors](./commandexecutors.html)).
- Finally, we register the command to the CommandAPI using `register`.

## `CommandAPICommand` methods

The `CommandAPICommand` has various methods, which are outlined below:

#### Adding constraints to the command

- `withPermission(CommandPermission)` - The required permission to execute a command. (See [the section on permissions](permissions.html)).

- `withArguments(LinkedHashMap<String, Argument>)` - The list of arguments

  The CommandAPI requires a list of arguments which are used for the command. The argument map consists of a key which is the tooltip that is displayed as a prompt to users entering commands, and a value which is an instance of an argument (See the section on arguments). This list of arguments is interpreted in the _order that arguments are added to the LinkedHashMap_.

- `withAliases(String... args)` - An array of aliases that the command can be run via 

#### Setting the command's executor

- `executes((sender, args) -> {})` - Executes a command, using the `CommandSender` object
- `executesPlayer((player, args) -> {})` - Executes a command, using the `Player` object
- `executesEntity((entity, args) -> {})` - Executes a command, using the `Entity` object
- `executesCommandBlock((cmdblock, args) -> {})` - Executes a command, using the `BlockCommandSender` object
- `executesConsole((console, args) -> {})` - Executes a command, using the `ConsoleCommandSender` object

#### Registering the command

- `register()` - Registers the command


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
{{#include examples/3replacegamemode.java}}
```

> **Developer's Note:**
>
> Command unregistration, although powerful, is highly unrecommended. It is the CommandAPI's most "dangerous" feature as it can cause unexpected sideffects, such as command blocks executing commands you wouldn't expect them to. In almost every case, I'd recommend just creating a new command instead of unregistering one to replace it.
>
> For instance, instead of unregistering `/gamemode`, you could register a command `/gm` or `/customgamemode`.
