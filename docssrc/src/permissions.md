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

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:permissions}}
```

As stated above, it is possible to assign a permission using a String instead of using `CommandPermission.fromString()`:
```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:permissions2}}
```

</div>

-----

## Adding permissions to arguments

For further fine-tuning of permission management, the CommandAPI allows you to add permissions to individual arguments. This prevents the user from executing a command *with a specific argument* if they do not have a specific permission.

This is done by using the `withPermission(CommandPermission)` method _at the end of an argument_.


If a player does not have the required permission:

* The argument hover text which suggests what the command is will not be shown
* The player will receive an error if they try to type something in for that argument
* Suggestions, such as a list of materials or players, will not be shown

<div class="example">

### Example - /kill command with argument permissions

For example, say we're registering a command `/kill`:

```mccmd
/kill          - Kills yourself
/kill <target> - Kills a target player
```

We first declare the command as normal. Nothing fancy is going on here:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:permissions3_1}}
```

Now we declare our command with arguments. We use a `PlayerArgument` and apply the permission _to the argument_. After that, we register our command as normal:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:permissions3_2}}
```

</div>

-----

> **Developer's Note:**
>
> As you can see, there are multiple ways of applying permissions to commands with arguments. In the `/god` command shown above, the permission was applied to the whole command. In the `/kill` command shown above, the permission was applied to the argument. 
>
> There's not really much difference between the two methods, but I personally would use _argument permissions_ as it has greater control over arguments.