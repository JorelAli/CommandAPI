# Native commandsenders

In a similar way that the `ProxiedCommandSender` is used to store information about two command senders: a caller (the one that wrote the command) and a callee (the one that ends up executing the command), the CommandAPI also has a special `NativeProxyCommandSender` class which is a more powerful representation of the `ProxiedCommandSender` class. In addition to inheriting all of the methods from `ProxiedCommandSender`, this class also has the following two methods:

```java
public World getWorld();
public Location getLocation();
```

These methods contain additional information about the command executor's state, and are primarily designed to be used with Minecraft's `/execute` command.

-----

## Minecraft's `/execute` arguments

The following table represents how the different `/execute` arguments affect the `NativeProxyCommandSender` class:

| /execute argument     | How it changes NativeProxyCommandSender       |
| --------------------- | --------------------------------------------- |
| `/execute align`      | Changes `getLocation()` only                  |
| `/execute anchored`   | Changes nothing                               |
| `/execute as`         | Changes `getCallee()` only                    |
| `/execute at`         | Changes `getLocation()` and `getWorld()` only |
| `/execute facing`     | Changes `getLocation()` only                  |
| `/execute in`         | Changes `getWorld()` only                     |
| `/execute positioned` | Changes `getLocation()` only                  |
| `/execute rotated`    | Changes `getLocation()`only                   |

-----

## Using the native commandsender

As described in the section about [normal command executors](./normalexecutors.md), there are multiple methods to register a command executor. For the `NativeProxyCommandSender`, the `.executesNative()` method should be used.

> **Note:**
>
> The `.executesNative()` method has the highest priority over all over `.executesXXX()` methods - if you use the `.executesNative()` method, no other execution method will be run.

-----

<div class="example">

### Example - A really simple "break block" command

Say we wanted to make a command that simply sets the current block to air. For this example, we'll use the following command syntax:

```mccmd
/break
```

As you can see, this command takes no arguments. This is fine, since our "argument" will be the sender's location. We can access the sender's location using the `getLocation()` method from the `NativeProxyCommandSender` object, available from the `.executesNative()` method:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:native1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:native1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:native1}}
```

</div>

This can now be used via the following command examples:

```mccmd
/execute positioned 100 62 50 run break
```

```mccmd
/execute at @e[type=pig] run break
```

```mccmd
/execute in minecraft:overworld positioned 20 60 -20 run break
```

</div>
