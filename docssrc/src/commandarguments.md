# Command arguments

![Command arguments](./images/commandargument.gif)

Command arguments allows users to provide an executable server command. The `CommandArgument` class lets you specify:

- Arbitrary commands - any command that the user has permissions to run can be provided.
- Restricted commands - only specific commands can be provided.

Using the `CommandArgument` will return a `CommandResult`, which contains a Bukkit `Command` instance representing the command to be executed, and a `String[]` of command arguments.

-----

## Command results

The `CommandResult` record contains the following methods:

```java
public record CommandResult {
    Command command();
    String[] args();
}
```

These methods can be used to retrieve information about the command that was provided by the user:

```java
Command command();
```

`command()` returns the Bukkit `Command` instance that the user provided. For example, if a player provided `/mycommand hello world`, then `command()` will represent the `/mycommand` command.

-----

```java
String[] args();
```

`args()` returns an array of string argument inputs that were provided to the command. For example, if a player provided `/mycommand hello world`, then `args()` will be the following:

```java
[ "hello", "world" ]
```

-----

## Arbitrary commands

Arbitrary commands let the user enter any command that they have permission to execute. To use arbitrary commands, you just need to use the `CommandArgument` normally.

<div class="example">

### Example - A /sudo command

We want to create a `/sudo` command which lets you execute a command as another online player.

![Sudo command example](./images/sudocommand.gif)

To do this, we want to use the following command syntax:

```mccmd
/sudo <target> <command>
```

In this example, we want to be able to run any arbitrary command, so we will simply use the `CommandArgument` on its own (without using suggestions). Using the `CommandArgument` generates a `CommandResult` and we can use the `.command()` and `.args()` methods above to access the command and arguments. We can make use of the `Command.execute()` method to execute our command and use the target player as the command sender.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:command_argument_sudo}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:command_argument_sudo}}
```

</div>

</div>

-----

## Restricted commands

Blah blah blah blah, something something replace suggestions and branching.
