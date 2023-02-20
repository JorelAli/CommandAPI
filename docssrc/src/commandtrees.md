# Command trees

So far in this documentation, we've described many different ways to register commands. We've described writing commands by declaring a `CommandAPICommand` object, using a list of arguments and providing an executor for the command. We've also described another way of registering commands with multiple "paths" using the `withSubcommand` method to generate a tree-like structure. As of CommandAPI 7.0.0, another method for registering commands, _command trees_, has been introduced.

-----

## The `executes()` and `then()` methods

The Command Tree represents command structures in a tree-like fashion, in a very similar way that Brigadier's API lets you declare commands. Command tree commands effectively revolve around two methods:

```java
public T executes(CommandExecutor executor);

public CommandTree  then(ArgumentTree branch);
public ArgumentTree then(ArgumentTree branch);
```

The `executes()` method is the same `executes()` method that you have seen previously in this documentation for normal CommandAPI commands. This also includes all of the `executes...()` methods described in [Normal command executors](./normalexecutors.md#restricting-who-can-run-your-command), but for the sake of simplicity, we'll simply refer to all of these by `executes()`.

The `then()` method allows you to create new "branches" in your command "tree" data structure. If you are familiar with [Brigadier](https://github.com/Mojang/brigadier)'s `then()` method for argument nodes, then you should feel right at home. Otherwise, for all intents and purposes `then()` lets you specify additional paths that a command can take when a user is typing their command.

Because the underlying type hierarchy of command trees is fairly complex (`then()` having multiple return types and taking in `ArgumentTree` objects), instead of trying to describe how all of that works, we'll instead describe how to make command trees by using the methods `executes()` and `then()` in practice.

## Declaring a command tree

The basic syntax of a command tree is effectively identical to a normal `CommandAPICommand`, but instead you use the `CommandTree` object. For example, if we want to create a simple command which sends "Hi!" to a command sender, we declare the name of our command, make use of the `executes()` method, and then we use the `CommandTree` constructor instead of the `CommandAPICommand` constructor:

```mccmd
/sayhi
```

```java
new CommandAPICommand("sayhi")
    .executes((sender, args) -> {
        sender.sendMessage("Hi!");
    })
    .register();
```

$$\downarrow$$

```java
new CommandTree("sayhi")
    .executes((sender, args) -> {
        sender.sendMessage("Hi!");
    })
    .register();
```

## Adding arguments to a command tree

Unlike the `CommandAPICommand` class, the `CommandTree` class doesn't let you add arguments using the `withArguments()` method. Instead, it makes use of the `then()` method, which allows you to provide an argument to it. This is best described with an example.

<div class="example">

### Example - Declaring a command tree with a single argument

Say we want to take our `/sayhi` command from above and also have an argument which lets you specify a target player. In this example, we'll have the following command syntax:

```mccmd
/sayhi          - Says "Hi!" to the current sender
/sayhi <target> - Says "Hi!" to a target player
```

We can do this by adding a `PlayerArgument` to our command. As described above, to add this argument, we must use the `then()` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandTrees1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandTrees1}}
```

</div>

In this example, we have our normal `/sayhi` command using the `executes()` method. We then add a new argument (a new "branch" in our "tree"), the `PlayerArgument`, using the `then()` method. **We want to make this branch executable, so we also use the `executes()` method _on the argument itself_**. To register the full command tree (which includes both `/sayhi` and `/sayhi <target>`), we call `register()` on the `CommandTree` object.

</div>

-----

That's effectively all of the basics of command trees! We start by writing a normal command, use `executes()` to make it executable and use `then()` to add additional paths to our command. Finally, we finish up with `register()` to register our command. Below, I've included a few more examples showcasing how to design commands using command trees.

## Command tree examples

<div class="example">

### Example - Sign editing plugin

Say we wanted to create a plugin to let a user edit signs. We have a single command tree `/signedit`, with a number of branching paths `set`, `clear`, `copy` and `paste` which represent various operations that this command can be performed on a sign:

```mccmd
/signedit set <line_number> <text> - Sets the text for a line on a sign
/signedit clear <line_number>      - Clears a sign's text on a specific line
/signedit copy <line_number>       - Copies the current text from a line on a sign
/signedit paste <line_number>      - Pastes the copied text onto a line on a sign
```

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandTrees2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandTrees2}}
```

</div>

</div>
