# Subcommands

Subcommands is another method for registering commands that makes use of creating multiple different `CommandAPICommand` instances. Given a `CommandAPICommand`, we can add a subcommand by using the following method:

```java
CommandAPICommand withSubcommand(CommandAPICommand subcommand);
```

Using subcommands has no disadvantages to using regular commands with the `LiteralArgument` or `MultiLiteralArgument`, and should be slightly more intuitive to implement if you've used other command frameworks before.

<div class="example">

### Example - Permission system with subcommands

Say we wanted to write a permission management system. To do this, we'll use the following command structsyntaxure:

```mccmd
/perm group add <permission> <groupName>
/perm group remove <permission> <groupName>
/perm user add <permission> <userName>
/perm user remove <permission> <userName>
```

Let's start with the simplest example - the `/perm group ...` command. We have one command which is basically the following:

```mccmd
add <permission> <groupName>
```

We can implement this by creating a `CommandAPICommand` with the command name `add`:
```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:subcommandspart}}
```

Similarly, we have another part `remove <permission> <groupName>`. We can declare this similar to our `add` command. Once we've done that, we can now join everything up together. Here, we create a command `group` which adds the two other subcommands:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:subcommands}}
```

Finally, we can link everything up together to the `perm` command and register the whole thing together:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:subcommandsend}}
```

-----

Another, more intuitive method, is to shove everything in one go without creating lots of variables all over the place:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:subcommands1}}
```

</div>