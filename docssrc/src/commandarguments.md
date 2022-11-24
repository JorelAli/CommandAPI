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

Blah blah blah, /sudo example here

## Restricted commands

Blah blah blah blah, something something replace suggestions and branching.
