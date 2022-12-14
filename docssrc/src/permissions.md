# Permissions

Permissions let you control which players are allowed to execute which commands. This is handled using the `CommandPermission` class, which has the following uses:

| Permission                                      | What it does                                               |
| ----------------------------------------------- | ---------------------------------------------------------- |
| `CommandPermission.OP`                          | Requires OP to execute the command                         |
| `CommandPermission.NONE`                        | Anyone can execute the command                             |
| `CommandPermission.fromString("my.permission")` | Requires a specific permission node to execute the command |

In addition to the `CommandPermission` class, there are two different ways to assign permissions (compared to the simple `CommandSender.hasPermission()` method that is provided by Bukkit), by using the `withPermission` method for arguments or for commands.

The `withPermission` method can take two values:

- A `CommandPermission`, which represents a permission such as `OP` or `NONE`
- A `String`, which will be converted automatically to a `CommandPermission` using `CommandPermission.fromString()`

-----

## Adding permissions to commands

To add a permission to a command, you can use the `withPermission(CommandPermission)` or `withPermission(String)` method _when declaring a command_.

<div class="example">

### Example - /god command with permissions

Say we created a command `/god` that sets a player as being invulnerable. Since this is a pretty non-survival command, we want to restrict who can run this command. As such, we want our player to have the permission `command.god` in order to run this command. To do this, we simply use the `withPermission(CommandPermission)` method from our command builder:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions}}
```

</div>

As stated above, it is possible to assign a permission using a String instead of using `CommandPermission.fromString()`:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions2}}
```

</div>

</div>

-----

## Adding permissions to arguments

For further fine-tuning of permission management, the CommandAPI allows you to add permissions to individual arguments. This prevents the user from executing a command _with a specific argument_ if they do not have a specific permission.

This is done by using the `withPermission(CommandPermission)` method _at the end of an argument_.

If a player does not have the required permission:

- The argument hover text which suggests what the command is will not be shown
- The player will receive an error if they try to type something in for that argument
- Suggestions, such as a list of materials or players, will not be shown

<div class="example">

### Example - /kill command with argument permissions

For example, say we're registering a command `/kill`:

```mccmd
/kill          - Kills yourself
/kill <target> - Kills a target player
```

We first declare the command as normal. Nothing fancy is going on here:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions3_1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions3_1}}
```

</div>

Now we declare our command with arguments. We use a `PlayerArgument` and apply the permission _to the argument_. After that, we register our command as normal:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions3_2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions3_2}}
```

</div>

</div>

-----

> **Developer's Note:**
>
> As you can see, there are multiple ways of applying permissions to commands with arguments. In the `/god` command shown above, the permission was applied to the whole command. In the `/kill` command shown above, the permission was applied to the argument.
>
> There's not really much difference between the two methods, but I personally would use _argument permissions_ as it has greater control over arguments.

-----

## Child Base Permissions
### Example - /economy command with argument permissions
Child Based Permissions allow you to group permissions together in groups, we acheive this by laying out our permission groups in the `plugin.yml` which Bukkit registers as valid permissions. When we then check if our player has a permission bukkit all considers if they have the child of a permission aswell. This not only keeps things easier to manage, it cleans up your code in the CommandAPI and gives you a nice place to layout out all of your permissions, what they do and what other permissions inheirt them.

For example, say we're registering a command `/economy`:

```mccmd
/economy                         - shows your own balance                 | economy.self
/economy <target>                - shows you another players balance      | economy.other
/economy give  <target> <amount> - gives the target a set amount of money | economy.admin.give
/economy reset <target> <amount> - resets the targets balance             | economy.admin.reset
```

We first declare the command as normal. Nothing fancy is going on here:
```java
// /economy
new CommandAPICommand("economy")
    .withPermission("economy.self") // The important part of this example
    .executesPlayer((player, args) -> { 
      player.sendMessage(getBalance(player))
    })
    .register();

// /economy <target>
new CommandAPICommand("economy")
    .withPermission("economy.other") // The important part of this example
    .withArguments(new PlayerArgument("target"))
    .executesPlayer((player, args) -> { 
        player.sendMessage(getBalance((Player) objects[0]))
    })
    .register();

// /economy give <target> <amount>
new CommandAPICommand("economy")
    .withPermission("economy.admin.give") // The important part of this example
    .withArguments(new PlayerArgument("target"))
    .withArguments(new DoubleArgument("amount"))
    .executesPlayer((player, args) -> { 
        updatePlayerBalance((Player) args[0], (Double) args[1])
    })
    .register();

// /economy reset <target>
new CommandAPICommand("economy")
    .withPermission("economy.admin.reset") // The important part of this example
    .withArguments(new PlayerArgument("target"))
    .executesPlayer((player, args) -> { 
        resetPlayerBalance((Player) args[0])
    })
    .register();
}
``In our **plugin.yml** we can also setup our permissions for example...

```yml
permissions:
  economy.*:
    description: Gives the user full access to the economy commands
    children:
      economy.other: true
      economy.admin.*: true

  economy:
    description: Allows the user to view their own balance
  economy.other:
    description: Allows the user to another players balance
    children:
      economy: true

  economy.admin.*:
    description: Gives the user access to all of the admin commands
    children:
      economy.admin.give: true
      economy.admin.reset: true
  economy.admin.give:
    description: Gives the user access to /economy give <target> <amount>
  economy.admin.reset:
    description: Gives the user access to /economy reset <target>
```

This setup of childeren allows us to give a player less permissions, but have them access more features. 
Since `economy.*` inherits `economy.admin.*` which inherits `economy.admin.give`, a player with the permission `economy.*` will be able to execute `/economy give <target> <amount>` without them directly having the `economy.admin.give` permission node

this also works with `economy.other`, if a player has `economy.other` they will **inherit** `economy`.

> **Developer's Note:**
>
> An example of what this command may look like without the usage of the plugin.yml is:
> The compexity of the example below can massively increase with more and more permissions. 
```java
// /economy
new CommandAPICommand("economy")
    .withRequirement(sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.other") ||
        sender.hasPermission("economy")
    )
    .executesPlayer((player, objects) -> { 
        player.sendMessage(getBalance(player))
    })
    .register();

// /economy <target>
new CommandAPICommand("economy")
    .withRequirement(sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.other") ||
    )
    .withArguments(new PlayerArgument("target"))
    .executesPlayer((player, objects) -> { 
        player.sendMessage(getBalance((Player) objects[0]))
    })
    .register();

// /economy give <target> <amount>
new CommandAPICommand("economy")
    .withRequirement(sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.admin.*") ||
        sender.hasPermission("economy.admin.give")
    )
    .withArguments(new PlayerArgument("target"))
    .withArguments(new DoubleArgument("amount"))
    .executesPlayer((player, objects) -> { 
        updatePlayerBalance((Player) objects[0], (Double) objects[1])
    })
    .register();

// /economy reset <target>
new CommandAPICommand("economy")
    .withRequirement(sender ->
      sender.hasPermission("economy.*") ||
      sender.hasPermission("economy.admin.*") ||
      sender.hasPermission("economy.admin.reset")
    )
    .withArguments(new PlayerArgument("target"))
    .executesPlayer((player, objects) -> { 
        resetPlayerBalance((Player) objects[0])
    })
    .register();
}
```
