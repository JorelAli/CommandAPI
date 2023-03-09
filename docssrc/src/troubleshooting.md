# Troubleshooting

This section summarizes the list of things that _could_ go wrong with the CommandAPI and how to mitigate these circumstances.

-----

## Argument suggestions are empty or aren't updating

If you've registered a command with an argument and replaced/included additional suggestions, but the suggestions:

- Are empty (e.g. a list of worlds returns no suggestions)
- Don't update automatically (e.g. a list of players doesn't update when new players join the game)

This is most likely caused by using a constant array instead of using a lambda to dynamically update argument suggestions when the player requests them:

```java
new StringArgument("players")
    .replaceSuggestions(ArgumentSuggestions.strings( 
        Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)
    ));
```

$$\downarrow$$

```java
new StringArgument("players")
    .replaceSuggestions(ArgumentSuggestions.strings( 
        info -> Bukkit.getOnlinePlayers().stream().map(Player::getName).toArray(String[]::new)
    ));
```

-----

## Permissions don't work (shading the CommandAPI)

If you're shading the CommandAPI and any of the following occur:

- Players with no permissions cannot run CommandAPI commands
- `.withPermission(CommandPermission.NONE)` doesn't work, even if a player has no permissions

You've probably not initialized the CommandAPI correctly. To assign permissions to a command, you have to add `CommandAPI.onEnable()` to your plugin's `onEnable()` method.

-----

## I've registered my command but nothing happens

If you've registered a command, the command should be present in the console if verbose logging is enabled. If this does not appear in the console, check that you've:

- Used `.register()` at the end of your command declaration
- Added `CommandAPI.onLoad()` and `CommandAPI.onEnable()` to your `onLoad()` and `onEnable()` methods if you're shading the CommandAPI
- Not added the commands to your `plugin.yml` file if you're not shading the CommandAPI

-----

## The CommandAPI doesn't load, something about `UnsupportedClassVersionError` and "class file version 60.0"

If you're getting an error which looks something like this (key thing to look for is `class file version 60.0`), then you're running an old and unsupported version of Java:

```log
[06.09 16:48:26] [Server] [Server thread/ERROR]: Could not load 'plugins/CommandAPI.jar' in folder 'plugins'
org.bukkit.plugin.InvalidPluginExceptionjava.lang.UnsupportedClassVersionError: dev/jorel/commandapi/CommandAPIMain has been compiled by a more recent version of the Java Runtime (class file version 60.0), this version of the Java Runtime only recognizes class file versions up to 52.0
    at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:149) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at org.bukkit.plugin.SimplePluginManager.loadPlugin(SimplePluginManager.java:394) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at org.bukkit.plugin.SimplePluginManager.loadPlugins(SimplePluginManager.java:301) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at org.bukkit.craftbukkit.v1_16_R3.CraftServer.loadPlugins(CraftServer.java:381) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at net.minecraft.server.v1_16_R3.DedicatedServer.init(DedicatedServer.java:224) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at net.minecraft.server.v1_16_R3.MinecraftServer.w(MinecraftServer.java:928) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at net.minecraft.server.v1_16_R3.MinecraftServer.lambda$0(MinecraftServer.java:273) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at java.lang.Thread.run(Thread.java:748) [?:1.8.0_271]
Caused byjava.lang.UnsupportedClassVersionError: dev/jorel/commandapi/CommandAPIMain has been compiled by a more recent version of the Java Runtime (class file version 60.0), this version of the Java Runtime only recognizes class file versions up to 52.0
    at java.lang.ClassLoader.defineClass1(Native Method) ~[?:1.8.0_271]
    at java.lang.ClassLoader.defineClass(ClassLoader.java:756) ~[?:1.8.0_271]
    at java.security.SecureClassLoader.defineClass(SecureClassLoader.java:142) ~[?:1.8.0_271]
    at org.bukkit.plugin.java.PluginClassLoader.findClass(PluginClassLoader.java:186) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at java.lang.ClassLoader.loadClass(ClassLoader.java:418) ~[?:1.8.0_271]
    at org.bukkit.plugin.java.PluginClassLoader.loadClass0(PluginClassLoader.java:104) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at org.bukkit.plugin.java.PluginClassLoader.loadClass(PluginClassLoader.java:99) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at java.lang.ClassLoader.loadClass(ClassLoader.java:351) ~[?:1.8.0_271]
    at java.lang.Class.forName0(Native Method) ~[?:1.8.0_271]
    at java.lang.Class.forName(Class.java:348) ~[?:1.8.0_271]
    at org.bukkit.plugin.java.PluginClassLoader.<init>(PluginClassLoader.java:67) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    at org.bukkit.plugin.java.JavaPluginLoader.loadPlugin(JavaPluginLoader.java:145) ~[spigot-1.16.5.jar:3096-Spigot-9fb885e-296df56]
    ... more
```

As of CommandAPI 6.0.0 onwards, the CommandAPI is written in Java 16, so old versions such as Java 8 will not run the CommandAPI. Consider upgrading to Java 16.

For context, a list of Java versions and their class file version:

| Java version     | Class file version |   | Java version     | Class file version |
| ---------------: | :----------------- | - | ---------------: | :----------------- |
| **Java 17**      | 61.0               |   | **Java 12**      | 56.0               |
| **Java 16**      | 60.0               |   | **Java 11**      | 55.0               |
| **Java 15**      | 59.0               |   | **Java 10**      | 54.0               |
| **Java 14**      | 58.0               |   | **Java 9**       | 53.0               |
| **Java 13**      | 57.0               |   | **Java 8**       | 52.0               |

-----

## Server errors when loading datapacks in 1.16+

If you get an error at the very start of the server's startup sequence along the lines of:

```log
[15:57:29] [Worker-Main-5/ERROR]: Failed to load function mycustomnamespace:test
java.util.concurrent.CompletionException: java.lang.IllegalArgumentException: Whilst parsing command on line 2: Unknown or incomplete command, see below for error at position 0: <--[HERE]
    at java.util.concurrent.CompletableFuture.encodeThrowable(Unknown Source) ~[?:1.8.0_261]
    at java.util.concurrent.CompletableFuture.completeThrowable(Unknown Source) [?:1.8.0_261]
    at java.util.concurrent.CompletableFuture$AsyncSupply.run(Unknown Source) [?:1.8.0_261]
    at java.util.concurrent.CompletableFuture$AsyncSupply.exec(Unknown Source) [?:1.8.0_261]
    at java.util.concurrent.ForkJoinTask.doExec(Unknown Source) [?:1.8.0_261]
    at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(Unknown Source) [?:1.8.0_261]
    at java.util.concurrent.ForkJoinPool.runWorker(Unknown Source) [?:1.8.0_261]
    at java.util.concurrent.ForkJoinWorkerThread.run(Unknown Source) [?:1.8.0_261]
Caused by: java.lang.IllegalArgumentException: Whilst parsing command on line 2: Unknown or incomplete command, see below for error at position 0: <--[HERE]
    at net.minecraft.server.v1_16_R1.CustomFunction.a(SourceFile:62) ~[spigot-1.16.1.jar:git-Spigot-758abbe-8dc1da1]
    at net.minecraft.server.v1_16_R1.CustomFunctionManager.a(SourceFile:84) ~[spigot-1.16.1.jar:git-Spigot-758abbe-8dc1da1]
    ... 6 more
```

You can safely ignore it - the CommandAPI fixes this later. This is described in more detail [here](./functions.md#functions-in-116).

-----

## Running `/reload` doesn't work

See [Plugin reloading](./reloading.md)

-----

## Players cannot connect/timeout when joining

If players cannot connect, this could be due to the size of the command data packet. To see the resultant packet being sent to players when they log in, enable the `create-dispatcher-json: true` setting and view the file size of the resultant file. If the file size is abnormally large _(Over 2MB is considered very large)_, consider reducing the number of `LiteralArguments` which your plugin uses.

-----

## My issue isn't on here, what do I do?

If you've found a bug that isn't solved here, submit a bug report on [the CommandAPI's issues page](https://github.com/JorelAli/CommandAPI/issues/new/choose) and I'll try my best to resolve the issue!
