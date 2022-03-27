# Command trees

So far in this documentation, we've described many different ways to register commands. We've described writing commands by declaring a `CommandAPICommand` object, using a list of arguments and providing an executor for the command. We've also described another way of registering commands with multiple "paths" using the `withSubcommand` method to generate a tree-like structure. As of CommandAPI 7.0.0, another method for registering commands, _command trees_, has been introduced.

The Command Tree represents command structures in a tree-like fashion, in a very similar way that Brigadier's API lets you declare commands. Command tree commands typically revolve around two methods: `executes()` and `then()`. Because the underlying type hierarchy of command trees is relatively complex, we'll describe how command trees work using these two methods (`executes()` and `then()`) in practice.

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

\\[\downarrow\\]

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
/sayhi
/sayhi <target>
```

We can do this by adding a `PlayerArgument` to our command. As described above, to add this argument, we must use the `then()` method:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:CommandTree_sayhi1}}
```

In this example, we have our normal `/sayhi` command using the `.executes()` method. We then add a new argument (a new "branch" in our "tree"), the `PlayerArgument`, using the `then()` method. **We want to make this branch executable, so we also use the `executes()` method _on the argument itself_**. To register the full command tree (which includes both `/sayhi` and `/sayhi <target>`), we call `register()` on the `CommandTree` object.

</div>