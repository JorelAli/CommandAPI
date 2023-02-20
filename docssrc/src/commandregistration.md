# Command registration

To register commands with the CommandAPI, we use the `CommandAPICommand` class. It follows a simple builder pattern to improve readability.

I think the easiest way to explain it is with an example:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandRegistration1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandRegistration1}}
```

</div>

- First, we create a new `CommandAPICommand`, with the name of the command that the sender must enter to run it.

- When, we create an argument to add to the command using `withArguments`. This is described in more detail in [the section on arguments](./arguments.html).

- In this example, we add an alias, "broadcast", to the command. This allows the sender to use either `/broadcastmsg <message>` or `/broadcast <message>`.

- By using `withPermission`, we require the sender to be an OP in order to run the command.

- We control what the command does using `executes` (this is described in more detail in [the section on command executors](./commandexecutors.html)).

- Finally, we register the command to the CommandAPI using `register`.

That's it! This simple snippet of code fully registers the command to the server. You don't need to supply a plugin instance, you don't have to create a custom class and you don't have to mess with the `plugin.yml` file.

Throughout this documentation, we will use the various different methods for command registration to give you an idea of when and where certain methods are more suitable than others.

-----

## `CommandAPICommand` methods

The `CommandAPICommand` has various methods, which are outlined below:

#### Setting the command name

```java
new CommandAPICommand(String commandName)
```

This constructor creates a new instance of the `CommandAPICommand` object. This constructor requires the _name_ of the command.

#### Setting command properties

```java
CommandAPICommand withArguments(List<Argument> arguments)
CommandAPICommand withArguments(Argument... arguments)
```

The `withArguments` method is used to add arguments to your command. The `arguments` parameter is appended to the the list of arguments for the command.

```java
CommandAPICommand withPermission(CommandPermission)
CommandAPICommand withPermission(String)
```

The `withPermission` method is used to assign a permission that is required to execute the command. (See [the section on permissions](permissions.html) for more info).

```java
CommandAPICommand withRequirements(sender -> {})
```

The `withRequirements` method is used to assign additional constraints required to execute the command, similar to permissions. (See [the section on requirements](./requirements.md) for more info).

```java
CommandAPICommand withAliases(String... args)
```

The `withAliases` method is used to declare a list of aliases that can be used to run this command via. (See [the section on aliases](./aliases.md) for more info).

```java
CommandAPICommand withHelp(String shortDescription, fullDescription)
CommandAPICommand withShortDescription(String shortDescription)
CommandAPICommand withFullDescription(String fullDescription)
```

The `withHelp` method, along with its specific `withShortDescription` and `withFullDescription` methods are used to declare the help topic for this command which is displayed in the `/help` command. (See [the section on help](./help.md) for more info).

```java
CommandAPICommand withSubcommand(CommandAPICommand subcommand)
```

The `withSubcommand` method is used to declare a subcommand that leads on from the current command. (See [the section on subcommands](./subcommands.md) for more info).

#### Setting the command's executor

```java
CommandAPICommand executes((sender, args) -> {})
```

Executes a command using the `CommandSender` object.

```java
CommandAPICommand executesPlayer((player, args) -> {})
```

Executes a command only if the command sender is a `Player`.

```java
CommandAPICommand executesEntity((entity, args) -> {})
```

Executes a command only if the command sender is an `Entity`.

```java
CommandAPICommand executesCommandBlock((cmdblock, args) -> {})
```

Executes a command only if the command sender is a `BlockCommandSender`.

```java
CommandAPICommand executesConsole((console, args) -> {})
```

Executes a command only if the command sender is a `ConsoleCommandSender`.

```java
CommandAPICommand executesProxy((proxy, args) -> {})
```

Executes a command only if the command sender is a `ProxiedCommandSender`.

```java
CommandAPICommand executesNative((proxy, args) -> {})
```

Executes a command regardless of what the command sender is, using the `NativeProxyCommandSender`.  Read more about native proxied command senders [here](./native.md).

> **Developer's Note:**
>
> Sometimes, the Java compiler throws an error saying that a method is ambiguous for the type CommandAPICommand. This is due to a limitation in Java's type inference system and is not a fault of the CommandAPI. If we take the following code, used to spawn a pig:
>
> ```java
> new CommandAPICommand("spawnpigs")
>      .executesPlayer((player, args) -> {
>            for(int i = 0; i < 10; i++) {
>                player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
>            }
>      })
>      .register();
> ```
>
> The Java type inference system cannot determine what the type of the lambda `(player, args) -> ()` is, therefore it produces the following compilation error:
>
> ```txt
> The method executesPlayer(PlayerCommandExecutor) is ambiguous for the type CommandAPICommand
> ```
>
> This can easily be resolved by declaring the specific type of the command sender and the arguments. For example:
>
> ```java
> new CommandAPICommand("spawnpigs")
>      .executesPlayer((Player player, Object[] args) -> {
>            for(int i = 0; i < 10; i++) {
>                player.getWorld().spawnEntity(player.getLocation(), (EntityType) args[0]);
>            }
>      })
>      .register();
> ```

#### Registering the command

```java
void register()
```

Registers the command.

-----

## Command loading order

In order to register commands properly, **commands must be registered before the server finishes loading**. The CommandAPI will output a warning if you register a command after the server has loaded. This means that all command registration must occur during a plugin's `onLoad()` or `onEnable()` method. With the CommandAPI, depending on whether you use `onLoad()` or `onEnable()` to load your commands depends on whether your plugin is used with Minecraft's functions:

| When to load        | What to do                                                                                                     |
| ------------------- | -------------------------------------------------------------------------------------------------------------- |
| `onLoad()` method   | Register commands to be used in Minecraft functions ([see the Function section for more info](functions.html)) |
| `onEnable()` method | Register regular commands                                                                                      |

-----

## Command unregistration

The CommandAPI has support to unregister commands completely from Minecraft's command list. This includes Minecraft built in commands!

<div class="warning">

**Developer's Note:**

Command unregistration, although powerful, is not recommended! It is the CommandAPI's most "dangerous" feature as it can cause unexpected side effects, such as command blocks executing commands you wouldn't expect them to. In almost every case, I'd recommend just creating a new command instead of unregistering one to replace it.

For instance, instead of unregistering `/gamemode`, you could register a command `/gm` or `/changegamemode`.

</div>

| Method                                             | Result                                                       |
| -------------------------------------------------- | ------------------------------------------------------------ |
| `CommandAPI.unregister(String cmd)`                | Unregisters a command from the game                          |
| `CommandAPI.unregister(String cmd, boolean force)` | Attempts to unregister a command from the game by force. This includes `/minecraft:cmd`, `/bukkit:cmd` and `/spigot:cmd` commands as well. |

<div class="example">

### Example - Replacing Minecraft's `/gamemode` command

To replace a command, we can first unregister it and then register our implementation of that command.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandRegistration2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandRegistration2}}
```

</div>

</div>
