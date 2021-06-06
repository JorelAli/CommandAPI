# Annotation-based commands

The CommandAPI also includes a very small lightweight annotation-based command framework. This works very differently compared to previous commands shown in this documentation and **it is less feature-rich than registering commands using the other methods.**

In short, the CommandAPI's annotation-based system:

- Has no runtime overhead compared to using the regular command registration system (unlike other annotation-based frameworks such as [ACF](https://github.com/aikar/commands)).
- Reduces code bloat (to an extent).
- Improves readability since commands are declared declaratively instead of imperatively.
- Is not as powerful as the regular command registration system.

> **Developer's Note:**
>
> Currently, the annotation framework is in its infancy, so any suggestions or improvements are heavily appreciated!

Before we go into too much detail, let's take a look at an example of what this annotation framework looks like, and compare this to the existing method.

-----

## Example: A warp command

_(So I would put this section in a big green box, but this example is REALLY big and that wouldn't look good)_

Let's say we're writing a plugin with the capability to create warps to places on the server. To do this, we'll make a simple command `/warp`, defined as follows:

```mccmd
/warp - Shows help
/warp <warp> - Teleports a player to <warp>
/warp create <name> - Creates a new warp <name> at the player's location
```

### Warp command (without annotations)

Using the regular CommandAPI, this is one way we can create this command. In the code below, we use StringArguments to represent the warp names. To teleport to a warp, we also populate it with suggestions (deferred so it updates), and also use a subcommand to represent `/warp create`:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:old_warps}}
```

Seems fairly straightforward, given everything else covered in this documentation. Now let's compare it to using annotations!

### Warp command (with annotations)

I think it's best to show the example and the explain it afterwards:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps}}
```

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_register}}
```

As we can see, the code certainly _looks_ very different to the normal registration method. Let's take it apart piece by piece to see what exactly is going on here.

#### Command declaration

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_command}}
```

Firstly, we declare our command `warp`. To do this, we use the `@Command` annotation and simply state the name of the command in the annotation. This annotation is attached to the class `WarpCommand`, which basically indicates that the whole class `WarpCommand` will be housing our command.

The annotation framework is designed in such a way that an entire command is represented by a single class. This provides a more modular approach to command declaration that allows you to easily contain the methods of a command in one location.

#### Default command

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_help}}
```

Here, declare the main command implementation using the `@Default` annotation. The `@Default` annotation basically informs the CommandAPI that the method it is attached to does not have any subcommands. This is basically the same as registering a regular command without using `.withSubcommand()`.

Here, we simply write what happens when no arguments are run (i.e. the user just runs `/warp` on its own). As such, we don't include any parameters to our method.

#### Default command (again!)

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_warp}}
```

We also have a second `@Default` annotated method, which handles our `/warp <warp>` command. Because this isn't a subcommand (the warp to teleport to is not a subcommand, it's an argument), we still using the `@Default` annotation. In this method, we include an argument with this command by using the `@AStringArgument` annotation. This argument uses the `StringArgument` class, and the name of this argument is "warpName", which is extracted from the name of the variable. Simply put, **the Annotation for an argument is A** followed by the name of the argument. This is synonymous with using the following:

```java
new StringArgument("warp")
```

It's also very important to note the parameters for this method. The first parameter is a `Player` object, which represents our command sender. The CommandAPI's annotation system uses the fact that the command sender is a `Player` object and automatically ensures that anyone using the command must be a `Player`. In other words, non-players (such as the console or command blocks), would be unable to execute this command.

The second argument is a `String` object, which represents the result of our argument "warp". The CommandAPI's annotation system can also infer the return type of the argument that is provided to it (in this case, a `StringArgument` will produce a `String`) and will automatically cast and provide the result to that parameter.

#### Subcommand

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_create}}
```

Lastly, we declare a subcommand to allow us to run `/warp create <name>`. To do this, we simply use the `@Subcommand` annotation. In this example, we also apply a permission node that is required to run the command by using the `@Permission` annotation. The rest is fairly straight forward - we declare an argument, in this case it's another `StringArgument` , so we use `@AStringArgument` and then declare everything else in a similar fashion to the default command executor.

#### Registering the command

Registering the command is fairly simple and is a one liner:

```java
{{#include ../../CommandAPI/commandapi-annotations/src/test/java/WarpCommand.java:warps_register}}
```

This line can be placed in your `onEnable()` or `onLoad()` method like you were registering a normal command.