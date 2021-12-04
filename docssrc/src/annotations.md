# Annotations

This page outlines in detail the list of all annotations that the CommandAPI's annotation-based command system includes.

---

## Annotations that go on classes

### `@Command`("commandName")

The `@Command` annotation is used to declare a command. The parameter is the name of the command that will be registered.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_command}}
```

### `@Alias({...})`

The `@Alias` annotation is used to declare a list of aliases for a command. The parameter is a list of aliases which can be used for the command.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/TeleportCommand.java:teleport_command}}
```

### `@Permission("permissionNode")`

The `@Permission` annotation is used to add a permission node to a command. Users that want to run this command must have this permission. The parameter is the permission node required to run the command.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:teleport_command_perms}}
```

### `@NeedsOp`

The `@NeedsOp` annotation is used to indicate that a command needs to have operator privileges to run it. This annotation has no parameters.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:teleport_command_needsop}}
```

### `@Help("Full description")`

The `@Help` annotation is used to add a help topic to a command. This annotation can take two forms:

A simple form which just uses a string which is used as the full description for a command:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:teleport_help}}
```

A form with two parameters `value` and `shortDescription`, to provide the full description (`value`) and short description (`shortDescription`) content for a command:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:teleport_full_help}}
```

-----

## Annotations that go on methods

To use annotations on methods, **methods must be static**.

### `@Default`

The `@Default` annotation indicates that the method is _not_ a subcommand. This acts in a similar way to regular Bukkit commands. Commands with the `@Default` annotation can be used to run the main code when the command named with the `@Command` annotation is stated, such as the following:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_help}}
```

The `@Default` annotation does not mean that the command can't have arguments! Arguments can still be used and declared as shown:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_warp}}
```

### `@Subcommand`

The `@Subcommand` simply tells the CommandAPI that the declared method is a subcommand. This acts in a similar way to the regular CommandAPI's `.withSubcommand()` method. The subcommand annotation can take in a single string which is the name of the subcommand:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_create}}
```

Or, it can take in a list of strings which represent the _aliases_ that can also be used for the declared subcommand:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/TeleportCommand.java:teleport_subcommand}}
```

### `@Permission`

The `@Permission` annotation can also be used on methods to indicate that a permission is required to execute a command.

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_create}}
```

### `@NeedsOp`

The `@NeedsOp` annotation can also be used on methods to indicate that the user must be an operator to run the command.

-----

## Annotations that go on parameters

The annotations for arguments are really simple, there's just two things you need to know:

- To use an annotation argument, just add the letter `A` (for 'annotation') at the beginning of it! For example:

  \begin{align}
  \texttt{StringArgument}&\xrightarrow{A}\texttt{@AStringArgument}\\\\
  \texttt{PlayerArgument}&\xrightarrow{A}\texttt{@APlayerArgument}\\\\
  \texttt{AdvancementArgument}&\xrightarrow{A}\texttt{@AAdvancementArgument}\\\\
  &\hspace{0.75em}\vdots
  \end{align}

  For example, we use `@AStringArgument` to indicate that this command takes a `StringArgument` as its first parameter:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_warp}}
```

- **The name of the argument (referred to as "nodeName" in the normal CommandAPI system) is the name of the variable assigned to the parameter.** In the above code, this means that the name of the argument is `warpName`.

### Special argument annotations

Certain argument annotations have extra parameters that can be supplied to provide additional customization:

#### Numerical arguments

The following numerical arguments can take both a `min` and `max` value. Both of these are completely optional. This indicates the range of values (inclusive) that is valid for this argument. For example:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:number_arguments}}
```

#### Literal arguments

Both the `LiteralArgument` and `MultiLiteralArgument` can be used. When these are used, the name of the variable assigned to the parameter is *ignored* and not used as the argument's name.

For the `@ALiteralArgument` annotation, the parameter is the literal to be used for the command. For the `@AMultiLiteralArgument`, the parameter can be an array of multiple literals to use:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:literal_arguments}}
```

#### Other arguments

The `LocationArgument`, `Location2DArgument`, `EntitySelectorArgument` and `ScoreHolderArgument` can all take an extra parameter in their constructors. As a result, the annotation-equivalent of these arguments also allow you to provide the parameter in the annotation:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/Test2Command.java:other_arguments}}
```

