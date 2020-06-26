# Command registration

To register commands with the CommandAPI, we use the `CommandAPICommand` class. It follows a simple builder pattern to improve readability.

I think the easiest way to explain it is with an example:

```java
{{#include examples/3commandregistration.java}}
```

- First, we create our arguments for the command. This is described in more detail in [the section on arguments](./arguments.html). 
- We then create a new `CommandAPICommand`, with the name of the command that the sender must enter to run it. 
- We then add the arguments to the command with `withArguments`.
- In this example, we add an alias, "broadcast", to the command. This allows the sender to use either `/broadcastmsg <message>` or `/broadcast <message>`.
- By using `withPermission`, we require the sender to be an OP in order to run the command.
- We control what the command does using `executes` (this is described in more detail in [the section on command executors](./commandexecutors.html)).
- Finally, we register the command to the CommandAPI using `register`.

That's it! This simple snippet of code fully registers the command to the server. No hassling with a plugin instance, no messing with `plugin.yml` files.

-----

## `CommandAPICommand` methods

The `CommandAPICommand` has various methods, which are outlined below:

#### Setting the command name

- `new CommandAPICommand(String)` - This constructor is used to set the command's name. 

#### Setting command properties

- `withArguments(LinkedHashMap<String, Argument>)` - The list of arguments.

  The CommandAPI requires a list of arguments which are used for the command. The argument map consists of a key which is the tooltip that is displayed as a prompt to users entering commands, and a value which is an instance of an argument (See the section on arguments). This list of arguments is interpreted in the _order that arguments are added to the LinkedHashMap_.

- `withPermission(CommandPermission)` - The required permission to execute a command. (See [the section on permissions](permissions.html)).

- `withAliases(String... args)` - An array of aliases that the command can be run via.

#### Setting the command's executor

- `executes((sender, args) -> {})` - Executes a command using the `CommandSender` object.
- `executesPlayer((player, args) -> {})` - Executes a command using the `Player` object.
- `executesEntity((entity, args) -> {})` - Executes a command using the `Entity` object.
- `executesCommandBlock((cmdblock, args) -> {})` - Executes a command using the `BlockCommandSender` object.
- `executesConsole((console, args) -> {})` - Executes a command using the `ConsoleCommandSender` object.
- `executesProxy((proxy, args) -> {})` - Executes a command using the `ProxiedCommandSender` object.

> **Developer's Note:**
>
> Sometimes, the Java compiler throws an error saying that a method is ambiguous for the type CommandAPICommand. This is due to a limitation in Java's type inference system and is not a fault of the CommandAPI. If we take the following code, used to spawn a pig:
>
> ```java
> new CommandAPICommand("spawnpigs")
>  .executesPlayer((player, args) -> {
>      for(int i = 0; i < 10; i++) {
>          player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
>      }
>  })
>  .register();
> ```
>
> The Java type inference system cannot determine what the type of the lambda `(player, args) -> ()` is, therefore it produces the following compilation error:
>
> ```
> The method executesPlayer(PlayerCommandExecutor) is ambiguous for the type CommandAPICommand
> ```
>
> This can easily be resolved by declaring the specific type of the command sender and the arguments. For example:
>
> ```java
> new CommandAPICommand("spawnpigs")
>  .executesPlayer((Player player, Object[] args) -> {
>      for(int i = 0; i < 10; i++) {
>          player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
>      }
>  })
>  .register();
> ```

#### Registering the command

- `register()` - Registers the command.

-----


## Command loading order

In order to register commands properly, **commands must be registered before the server finishes loading**. The CommandAPI will prevent command registration after the server has loaded. This basically means that all command registration must occur during a plugin's `onLoad()` or `onEnable()` method. With the CommandAPI, depending on whether you use `onLoad()` or `onEnable()` to load your commands depends on whether your plugin is used with Minecraft's functions:

| When to load        | What to do                                                                                                     |
| ------------------- | -------------------------------------------------------------------------------------------------------------- |
| `onLoad()` method   | Register commands to be used in Minecraft functions ([see the Function section for more info](functions.html)) |
| `onEnable()` method | Register regular commands                                                                                      |

-----

## Command unregistration

The CommandAPI has support to unregister commands completely from Minecraft's command list. This includes Minecraft built in commands!

| Method                                             | Result                                                       |
| -------------------------------------------------- | ------------------------------------------------------------ |
| `CommandAPI.unregister(String cmd)`                | Unregisters a command from the game                          |
| `CommandAPI.unregister(String cmd, boolean force)` | Attempts to unregister a command from the game by force. This includes `/minecraft:cmd`, `/bukkit:cmd` and `/spigot:cmd` commands as well. |

<div class="example">

### Example - Replacing Minecraft's `/gamemode` command

To replace a command, we can first unregister it and then register our implementation of that command.

```java
{{#include examples/3replacegamemode.java}}
```

> **Developer's Note:**
>
> Command unregistration, although powerful, is highly unrecommended. It is the CommandAPI's most "dangerous" feature as it can cause unexpected sideffects, such as command blocks executing commands you wouldn't expect them to. In almost every case, I'd recommend just creating a new command instead of unregistering one to replace it.
>
> For instance, instead of unregistering `/gamemode`, you could register a command `/gm` or `/changegamemode`.

</div>