# Normal command executors

Command executors are of the following format, where `sender` is a [`CommandSender`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/command/CommandSender.html), and `args` is an `Object[]`, which represents arguments which are parsed by the CommandAPI.

```java
new CommandAPICommand("...")
    .executes((sender, args) -> {
        //Code here  
    })
    .register();
```

With normal command executors, these do not need to return anything. By default, this will return a _success value_ of 1 if it runs successfully, and a _success value_ of 0 if it runs unsuccessfully, either by throwing an exception _(RuntimeException)_ or by forcing the command to fail (See the section on [handling command failures](./commandfailures.html).

In short, this is what values are returned when a command is executed from a normal command executor:

|                   | Command Works | Command Doesn't Work |
| :---------------: | :-----------: | :------------------: |
| **Success Value** |       1       |          0           |
| **Result Value**  |       1       |          0           |

<div class="example">

### Example - Creating a message broadcasting system

To illustrate this, let's take a look at a simple message broadcasting command. We declare our arguments (in this case, "message"), we provide some aliases and set a permission required to run the command. Then we declare our main command body by using the `.executes()` method, before finally registering the command:

```java
//Create our command
new CommandAPICommand("broadcastmsg")
    .withArguments(new GreedyStringArgument("message")) // The arguments
    .withAliases("broadcast", "broadcastmessage")       // Command aliases
    .withPermission(CommandPermission.OP)               // Required permissions
    .executes((sender, args) -> {
        String message = (String) args[0];
        Bukkit.getServer().broadcastMessage(message);
    }).register();
```

Note how when we finish up our implementation of `.executes()`, we don't return anything. This is unlike commands in the standard Bukkit API where the `onCommand` method returns a Boolean value:

```java
boolean onCommand(CommandSender, Command, String, String[])
```

The returning of this Boolean value is handled automatically by the CommandAPI on a much lower level.

</div>

-----

## Restricting who can run your command

The `CommandAPICommand` has multiple different `executes...()` methods that can restrict the command sender to any of the following objects:

- `CommandSender` - No restriction, players, the console etc. can use this command. This is what Bukkit normally uses.
- `Player` - Only in-game players can run this command
- `Entity` - Only entities (therefore, players as well) can run this command
- `BlockCommandSender` - Only command blocks can run this command
- `ConsoleCommandSender` - Only the console can run this command
- `ProxiedCommandSender` - Only proxied command senders (e.g. other entities via the `/execute as ...` command)
- `NativeProxyCommandSender` - This type has special rules governing it. See [Native commandsenders](./native.md) for more information

This is done using the respective method:

| Restricted sender          | Method to use             |
| -------------------------- | ------------------------- |
| `CommandSender`            | `.executes()`             |
| `Player`                   | `.executesPlayer()`       |
| `Entity`                   | `.executesEntity()`       |
| `BlockCommandSender`       | `.executesCommandBlock()` |
| `ConsoleCommandSender`     | `.executesConsole()`      |
| `ProxiedCommandSender`     | `.executesProxy()`        |
| `NativeProxyCommandSender` | `.executesNative()`       |

<div class="example">

### Example - A `/suicide` command

Say we wanted to create a command `/suicide`, which kills the player that executes it. Since this command isn't really "designed" for command senders that are not players, we can restrict it so only players can execute this command (meaning that the console and command blocks cannot run this command). Since it's a player, we can use the `.executesPlayer()` method:

```java
{{#include examples/4.1suicide.java}}
```

</div>

-----

## Multiple command executor implementations

The CommandAPI allows you to chain different implementations of the command depending on the type of `CommandSender`. This allows you to easily specify what types of `CommandSender`s are required to run a command.

Extending on the suicide example above, we could write another implementation for a different `CommandSender`. Here, we write an implementation to make entities (non-player) go out with a bang when they run the command (using `/execute as <entity> run <command>` command).

<div class="example">

### Example - A `/suicide` command with different implementations

```java
{{#include examples/4.1suicide2.java}}
```

This saves having to use `instanceof` multiple times to check the type of the `CommandSender`.

</div>

The different command sender priority is the following (from highest priority to lowest priority):

\begin{align}
&\quad\texttt{.executesNative()} && \texttt{(Always chosen if used)}\\\\
&\quad\texttt{.executesPlayer()} \\\\
&\quad\texttt{.executesEntity()} \\\\
&\quad\texttt{.executesConsole()} \\\\
&\quad\texttt{.executesCommandBlock()} \\\\
&\quad\texttt{.executesProxy()} \\\\
&\quad\texttt{.executes()}
\end{align}