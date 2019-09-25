# Normal command executors

Command executors are of the following format, where `sender` is a [`CommandSender`](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/command/CommandSender.html), and `args` is an `Object[]`, which represents arguments which are parsed by the CommandAPI.

```java
new CommandAPICommand("...").executes((sender, args) -> {
  //Code here  
});
```

With normal command executors, these do not need to return anything. By default, this will return a _success value_ of 1 if it runs successfully, and a _success value_ of 0 if it runs unsuccessfully, either by throwing an exception _(RuntimeException)_ or by forcing the command to fail (See the section on [handling command failures](./commandfailures.html).

## Example - Creating a message broadcasting system

```java
// Create our arguments
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("message", new GreedyStringArgument());

//Create our command
new CommandAPICommand("broadcastmsg")
    .withArguments(arguments)                     // The arguments
    .withAliases("broadcast", "broadcastmessage") // Command aliases
    .withPermission(CommandPermission.OP)         // Required permissions
    .executes((sender, args) -> {
        String message = (String) args[0];
        Bukkit.getServer().broadcastMessage(message);
    });
```

-----

## Restricting who can run your command

The `CommandAPICommand` has multiple different `executes...()` methods that can restrict the command sender to any of the following objects:

- `CommandSender` - No restriction, players, the console etc. can use this command. This is what Bukkit normally uses.
- `Player` - Only in-game players can run this command

- `Entity` - Only entities (therefore, players as well) can run this command
- `BlockCommandSender` - Only command blocks can run this command

This is done using the respective method:

| Restricted sender    | Method to use             |
| -------------------- | ------------------------- |
| `CommandSender`      | `.executes()`             |
| `Player`             | `.executesPlayer()`       |
| `Entity`             | `.executesEntity()`       |
| `BlockCommandSender` | `.executesCommandBlock()` |

### Example - A `/suicide` command

Say we wanted to create a command `/suicide`, which kills the player that executes it. Since this command isn't really "designed" for command senders that are not players, we can restrict it so only players can execute this command (meaning that the console and command blocks cannot run this command). Since it's a player, we can use the `.executesPlayer()` method:

```java
new CommandAPICommand("suicide")
	.executesPlayer((player, args) -> player.setHealth(0));
```