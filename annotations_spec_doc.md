# Annotations API specification

This document outlines the new (post 9.0.0) annotations API specification. This describes what will be implemented, what features will be present, how the new annotations will look and other important stuff

## Motivation

The current annotations API was a proof-of-concept showcasing how compile-time command declaration is a feasible task that will work for the CommandAPI. Since the introduction of the annotations API, the CommandAPI has added more and more increasingly more complex features that have not been ported to the annotations API. As such, the annotations API is lacking in important features that make the CommandAPI what it is today.

-----

## An example of a simple command

```java
@Command("warp")
public class WarpCommand {
    // List of warp names and their locations
    static Map<String, Location> warps = new HashMap<>();

    @Default
    public static void warp(CommandSender sender) {
        sender.sendMessage("--- Warp help ---");
        sender.sendMessage("/warp - Show this help");
        sender.sendMessage("/warp <warp> - Teleport to <warp>");
        sender.sendMessage("/warp create <warpname> - Creates a warp at your current location");
    }

    @Default
    public static void warp(Player player, @AStringArgument String warpName) {
        player.teleport(warps.get(warpName));
    }

    @Subcommand("create")
    @Permission("warps.create")
    public static void createWarp(Player player, @AStringArgument String warpName) {
        warps.put(warpName, player.getLocation());
    }
}
```

-----

## Features

Features that have been implemented so far:

- Declaring a command in a file (one command per file)
- Declaring a 'default' implementation (e.g. `/warp`)
- Declaring subcommands, such as `/warp create <args>`
- Permissions with permission nodes
- Permissions for operators (via `@NeedsOp`)
- Help, via `@Help`

Features that exist in the CommandAPI and haven't been implemented so far:

- Requirements (more powerful permissions)
- Suggestions:
  - With tooltips
  - Asynchronous
- Custom arguments
- Other argument properties, including:
  - Argument listing
  - Optional arguments

-----

## Known changes from 9.0.0

- TeamArgument, ObjectiveArgument class changes

-----

## Issues

- https://github.com/JorelAli/CommandAPI/issues/318
- https://github.com/JorelAli/CommandAPI/issues/292
- https://github.com/JorelAli/CommandAPI/issues/263
- https://github.com/JorelAli/CommandAPI/issues/201
- https://github.com/JorelAli/CommandAPI/issues/163

## Issue draft requirements

### 292 - multiple command executors

Issue https://github.com/JorelAli/CommandAPI/issues/292 requests multiple command executors. The CommandAPI can support multiple command executors via the various `.executesPlayer`, `.executesConsole` etc. methods.

The annotation API could accommodate that using overloading using a different parameter type:

```patch
@Command("warp")
public class WarpCommand {
    // List of warp names and their locations
    static Map<String, Location> warps = new HashMap<>();

    @Default
    public static void warp(Player player, @AStringArgument String warpName) {
        player.teleport(warps.get(warpName));
    }

+   @Default
+   public static void warp(ConsoleCommandSender console, @AStringArgument String warpName) {
+       console.sendMessage("This command can't be run from the console!");
+   }
}
```

For multiple executors, it may be possible to use the `ExecutorType` like this:

```patch
@Command("warp")
public class WarpCommand {
    // List of warp names and their locations
    static Map<String, Location> warps = new HashMap<>();

    @Default
+   public static void warp(@Senders({ ExecutorType.PLAYER, ExecutorType.ENTITY }) CommandSender sender, @AStringArgument String warpName) {
+       ((Entity) sender).teleport(warps.get(warpName));
    }
}
```

-----

## Must have requirements

- Commands that don't need the `static` modifier on their methods

## Main spec

- An example of a new command using this system can be found [here](https://github.com/JorelAli/CommandAPI/blob/dev/annotations/commandapi-annotations/src/test/java/HordeCommand2.java).

### Commands

Commands consist of an (ideally) public class with the `@Command` annotation. The `@Command` annotation specifies the command's name. We'll call this the "command class".

### Command class "global" arguments

Inside the command class, class field members can be specified with argument annotations, for example:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    // ...

}
```

These arguments are arguments that are present for all commands in the class. Arguments are accessed in declaration order, so the following will only allow `/mycommand <name> <value>`, and _not_ `/mycommand <value> <name>`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @AIntegerArgument
    int value;

    // ...

}
```

Due to the nature of command class arguments, the declaration location does not matter when it comes to subcommands. The following two code blocks will yield the same command `/mycommand <name> <value> subcommand`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @AIntegerArgument
    int value;

    @Subcommand("subcommand")
    class Subcommand {

        // Executor

    }
}
```

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @Subcommand("subcommand")
    class Subcommand {

        // Executor

    }

    @AIntegerArgument
    int value;
}
```

### Subcommand executors

In the `dev/annotations` example, subcommand executors were implemented using `@Subcommand` on the method itself. I think it would be better to use a different annotation to avoid misleading syntax. For example, the following allows `/mycommand <name> subcommand`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @Subcommand("subcommand")
    class Subcommand {

        @Executes
        public void myMethod(Player player) {
            // ...
        }

    }
}
```

Per-method arguments are still valid, allowing `/mycommand <name> subcommand <value>`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @Subcommand("subcommand")
    class Subcommand {

        @Executes
        public void myMethod(Player player, @AIntegerArgument int value) {
            // ...
        }

    }
}
```

### Subcommand non-class subcommands

It may be desirable to add subcommands without creating a giant tree. This can be done by applying (a repeatable) `@Subcommand` annotation to the executable method. This example shows `/mycommand <name> subcommand subsubcommand <value>`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @Subcommand("subcommand")
    class Subcommand {

        @Subcommand("subsubcommand")
        @Executes
        public void myMethod(Player player, @AIntegerArgument int value) {
            // ...
        }

    }
}
```

This could also be implemented on the class-level:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @Subcommand("subcommand")
    @Subcommand("subsubcommand")
    class Subcommand {

        @Executes
        public void myMethod(Player player, @AIntegerArgument int value) {
            // ...
        }

    }
}
```

### Arguments with different names to their parameters

**Currently not planned unless demand requires this feature.**

### Suggestions

Since suggestions are implemented as functional interfaces, we can simply bind functional interface implementations to corresponding arguments via class references.

Arguments with suggestions requires `@Suggests`, and classes that implement suggestions requires `@Suggestion`. These "pairs" will be checked at compile time. Since we can observe all matching pairs (each `@Suggests` should have a corresponding `@Suggestion`), we can also provide compiler warnings for unused suggestions.

The compiler should error if `@Suggestion` does not implement the correct type.

This command represents `/mycommand <name>`, and the suggestions for `<name>` will be `Player1`, `Player2` and `Player3`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    @Suggests(ListOfNames.class)
    String name;

    @Executes
    public void myExecutor(Player player) {
        // ...
    }

    @Suggestion
    class ListOfNames implements Supplier<ArgumentSuggestions> {

        @Override
        public ArgumentSuggestions get() {
            return ArgumentSuggestions.strings("Player1", "Player2", "Player3");
        }

    }
}
```

### Subcommands from another class

See https://github.com/JorelAli/CommandAPI/issues/318#issuecomment-1205781476.

Subcommands need not be declared in the command class. They can be declared in their own class. To link these together, the CommandAPI can use a `@ExternalSubcommand` annotation (actual name TBD) which takes in the class reference of the subcommand. The external subcommand must have its corresponding `@Subcommand` declaration on the class level. The external subcommand class _must_ inherit the parent command in order to inherit global class variables. For example, `Subcommand` can access `name` because it extends `MyCommand`:

```java
@Command("mycommand")
public class MyCommand {

    @AStringArgument
    String name;

    @ExternalSubcommand(Subcommand.class)
    Subcommand subcommand;
}
```

```java
@Subcommand("subcommand")
public class Subcommand extends MyCommand {

    @Executes
    public void myMethod(Player player, @AIntegerArgument int value) {
        // ...
    }

}
```

### Aliases

Aliases should no longer be provided using the `@Alias` annotation - it gets too messy. Consider adding it as a parameter to the `@Command` annotation.
