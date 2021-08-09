# Upgrading guide

## From version 6.2.0 to 6.3.0

### Custom Arguments

The CustomArgument constructor syntax has changed, to use a much more powerful record type instead of the constructor which accepts the input and the other constructor which accepts the input and a command sender. The current input can be obtained using the `input()` method from the provided info, and the command sender can be obtained using the `sender()` method. This is described in more detail [here](./customarguments.md#the-customargumentinfoparser-class).

```java
CustomArgument<Player> myCustomArgument = new CustomArgument("player", input -> {
    return Bukkit.getPlayer(input);
});
```

\\[\downarrow\\]

```java
CustomArgument<Player> myCustomArgument = new CustomArgument("player", info -> {
    return Bukkit.getPlayer(info.input());
});
```

## From version 5.x to 6.0.0

> **Developer's Note**
>
> In 6.0.0, I've deprecated a number of methods which are no longer recommended for use. **These methods will be removed in 7.0.0**. The primary reason for this change is that these methods were not designed to be extensible and ended up causing much more issues than they solved.

### Suggestions

The `overrideSuggestions` and `overrideSuggestionsT` methods are now deprecated. Instead, this has been replaced with the much more powerful `replaceSuggestions` and `replaceSuggestionsT` methods. Instead of using `sender` or `(sender, args)` to get the sender and the previously declared arguments, you should use the `SuggestionsInfo` class with the `sender()` or `previousArgs()` methods:

```java
new CommandAPICommand("mycommand")
    .withArguments(new StringArgument("myargument").overrideSuggestions(sender -> {
        return new String[] { "hello", "world", sender.getName() };
    }))
    .executes((sender, args) -> {
        // etc.
    })
    .register();
```

\\[\downarrow\\]

```java
new CommandAPICommand("mycommand")
    .withArguments(new StringArgument("myargument").replaceSuggestions(info -> {
        return new String[] { "hello", "world", info.sender().getName() };
    }))
    .executes((sender, args) -> {
        // etc.
    })
    .register();
```

Safe suggestions have also been renamed from `safeOverrideSuggestions` and `safeOverrideSuggestionsT` to `replaceWithSafeSuggestions` and `replaceWithSafeSuggestionsT`

### Loading the CommandAPI (With shading)

Before, to initialize the CommandAPI when shading it into your plugin, you would use `CommandAPI.onLoad(boolean verbose)`, which is now deprecated. Instead, you should use the `CommandAPIConfig` variant, with a suitable CommandAPIConfig instance:

```java
CommandAPI.onLoad(true);
```

\\[\downarrow\\]

```java
CommandAPI.onLoad(new CommandAPIConfig().verboseOutput(true));
```



-----

## From version 4.x to 5.0

### Argument registration

`LinkedHashMap` is no longer used for argument registration. Instead, use a `List`, and put the argument's "prompt" as the first parameter in the argument's constructor. For example:

```java
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
arguments.put("target", new PlayerArgument())
arguments.put("location", new LocationArgument(LocationType.BLOCK_POSITION));

new CommandAPICommand("teleport")
    .withArguments(arguments)
    .executes((sender, args) -> {
        //Teleport <target> to <location>
    })
    .register();
```

\\[\downarrow\\]

```java
List<Argument> arguments = new ArrayList<>();
arguments.add(new PlayerArgument("target"));
arguments.add(new LocationArgument("location", LocationType.BLOCK_POSITION));

new CommandAPICommand("teleport")
    .withArguments(arguments)
    .executes((sender, args) -> {
        //Teleport <target> to <location>
    })
    .register();
```

Alternatively, you can declare them directly in the command's declaration so you don't have to construct a list:

```java
new CommandAPICommand("teleport")
    .withArguments(new PlayerArgument("target"))
    .withArguments(new LocationArgument("location", LocationType.BLOCK_POSITION))
    .executes((sender, args) -> {
        //Teleport <target> to <location>
    })
    .register();
```

Alternatively, you can declare it in one line:

```java
new CommandAPICommand("teleport")
    .withArguments(new PlayerArgument("target"), new LocationArgument("location", LocationType.BLOCK_POSITION))
    .executes((sender, args) -> {
        //Teleport <target> to <location>
    })
    .register();
```

### Method changes

Some of the `Brigadier` methods were changed:

```java
LiteralCommandNode registerNewLiteral(String name);
RequiredArgumentBuilder argBuildOf(LinkedHashMap<String, Argument> args, String value);
RequiredArgumentBuilder argBuildOf(String prompt, Argument argument);
```

\\[\downarrow\\]

```java
LiteralArgumentBuilder fromLiteralArgument(LiteralArgument literalArgument);
RequiredArgumentBuilder fromArgument(List<Argument> args, String nodeName);
RequiredArgumentBuilder fromArgument(Argument argument);
```

In particular, the `fromLiteralArgument` now takes in a `LiteralArgument` and returns a `LiteralArgumentBuilder`. To convert from a `LiteralArgumentBuilder` to the `LiteralCommandNode`, you can run the `.build()` method.

-----

## From version 3.x to 4.0

The maven repository url has changed:

Instead of being:

```
https://raw.githubusercontent.com/JorelAli/1.13-Command-API/mvn-repo/1.13CommandAPI/
```

You must now use:

```xml
https://raw.githubusercontent.com/JorelAli/CommandAPI/mvn-repo/
```

This information can be viewed in section [3. Setting up your development environment](./quickstart.md). _(Don't worry if you forget, it **should** work as normal nonetheless!)_

-----

## From version 2.3 to 3.0

The CommandAPI's upgrade from version 2.3 to 3.0 is very intense and various refactoring operations took place, which means that plugins that implement the CommandAPI version 2.3 or will not to work with the CommandAPI version 3.0. This page outlines the few major changes and points you to the various pages in the documentation that covers how to use version 3.0.

-----

### Imports & Renaming

The default package name has been changed. Instead of being registered under the `io.github.jorelali` package, the CommandAPI has been moved to the `dev.jorel` package:

\\[\texttt{io.github.jorelali.commandapi.api}\rightarrow\texttt{dev.jorel.commandapi}\\]

To organise classes with other classes of similar functions, new packages have been introduced. These can be fully explored using the [new JavaDocs](https://www.jorel.dev/CommandAPI/javadocs/html/annotated.html)

-----

### Removed classes & Alternatives

To reduce redundancies, the CommandAPI removed a few classes:

| Removed class                           | Alternative                                                  |
| --------------------------------------- | ------------------------------------------------------------ |
| `SuggestedStringArgument`               | Use `.overrideSuggestions(String[])` for the relevant argument, as described [here](./arguments.html#overriding-argument-suggestions) |
| `DefinedCustomArguments` for Objectives | Use [`ObjectiveArgument`](./objectivearguments.md)           |
| `DefinedCustomArguments` for Teams      | Use [`TeamArgument`](./teamarguments.md)                     |

-----

### Command registration

The way that commands are registered has been completely changed. It is highly recommended to switch to the new system, which is described [**here**](./commandregistration.html).

The following methods have been removed:

```java
CommandAPI.getInstance().register(String, LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, String[], LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, LinkedHashMap, CommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, CommandExecutor);

CommandAPI.getInstance().register(String, LinkedHashMap, ResultingCommandExecutor);
CommandAPI.getInstance().register(String, String[], LinkedHashMap, ResultingCommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, LinkedHashMap, ResultingCommandExecutor);
CommandAPI.getInstance().register(String, CommandPermission, String[], LinkedHashMap, ResultingCommandExecutor);
```

Additionally, the CommandAPI is no longer accessed by using `CommandAPI.getInstance()`. This has been replaced with static methods that can be accessed without an instance of the CommandAPI, so you can use the following:

```java
CommandAPI.fail(String command);
CommandAPI.canRegister();
CommandAPI.unregister(String command);
CommandAPI.unregister(String command, boolean force);
```

