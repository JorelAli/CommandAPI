# Annotation-based commands

The CommandAPI also includes a very small lightweight annotation-based command framework. This works very differently compared to previous commands shown in this documentation and **it is less feature-rich than registering commands using the other methods.**

In short, the CommandAPI's annotation-based system:

- Has no runtime overhead compared to using the regular command registration system (unlike other annotation-based frameworks such as ACF).
- Reduces code bloat (to an extent).
- Improves readability since commands are declared declaratively instead of imperatively.
- Is not as powerful as the regular command registration system.

Currently, the annotation framework is in its infancy, so any suggestions or improvements are heavily appreciated!

Before we go into too much detail, let's take a look at a few examples of what this annotation framework can do, and compare this to the existing method.

-----

## A warp command

Let's say we're writing a plugin with the capability to create warps to places on the server. To do this, we'll make a simple command `/warp`, defined as follows:

```
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

