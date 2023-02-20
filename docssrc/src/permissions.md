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
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions1}}
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
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions3}}
```

</div>

Now we declare our command with arguments. We use a `PlayerArgument` and apply the permission _to the argument_. After that, we register our command as normal:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions4}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions4}}
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

## Child-based permissions

Child-based permissions allow you to group permissions together.
We achieve this by laying out our permission groups in the `plugin.yml` file which Bukkit registers as valid permissions.
When the CommandAPI checks if our player has a permission, Bukkit considers if they have the child of a permission as well.
This not only keeps permissions easier to manage, it also makes your code cleaner and gives you a nice place to lay out all of your permissions,
detailing what they do and what other permissions inherit them.

### Example - /economy command with argument permissions

For example, say we're registering a command `/economy`:

```mccmd
/economy                         - shows your own balance                 | economy.self
/economy <target>                - shows you another players balance      | economy.other
/economy give  <target> <amount> - gives the target a set amount of money | economy.admin.give
/economy reset <target> <amount> - resets the targets balance             | economy.admin.reset
```

We first declare the command as normal. Nothing fancy is going on here:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:permissions5}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:permissions5}}
```

</div>

In our **plugin.yml** we can also set up our permissions for example...

```yml
permissions:
  economy.*:
    description: Gives the user full access to the economy commands
    children:
      economy.other: true
      economy.admin.*: true

  economy.self:
    description: Allows the user to view their own balance
  economy.other:
    description: Allows the user to another players balance
    children:
      economy.self: true

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

This setup of children allows us to give a player less permissions, but have them access more features.
Since `economy.*` inherits `economy.admin.*` which inherits `economy.admin.give`, a player with the permission `economy.*` will be able to execute `/economy give <target> <amount>` without them directly having the `economy.admin.give` permission node.
This also works with `economy.other`, if a player has `economy.other` they will **inherit** `economy`.
