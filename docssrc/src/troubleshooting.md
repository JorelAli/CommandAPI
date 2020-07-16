# Troubleshooting

This section basically summarizes the list of things that _could_ go wrong with the CommandAPI and how to mitigate these circumstances.

## CommandAPI errors when reloading datapacks

If you get an error along the lines of:

```log
[14:47:25] [Server thread/INFO]: [CommandAPI] Reloading datapacks...
[14:47:25] [Server thread/WARN]: java.lang.NoSuchFieldException: modifiers
[14:47:25] [Server thread/WARN]:     at java.base/java.lang.Class.getDeclaredField(Class.java:2489)
[14:47:25] [Server thread/WARN]:     at dev.jorel.commandapi.nms.NMS_1_16_R1.reloadDataPacks(NMS_1_16_R1.java:154)
[14:47:25] [Server thread/WARN]:     at dev.jorel.commandapi.CommandAPI.cleanup(CommandAPI.java:43)
[14:47:25] [Server thread/WARN]:     at dev.jorel.commandapi.CommandAPIMain.lambda$onEnable$0(CommandAPIMain.java:52)
[14:47:25] [Server thread/WARN]:     at org.bukkit.craftbukkit.v1_16_R1.scheduler.CraftTask.run(CraftTask.java:81)
[14:47:25] [Server thread/WARN]:     at org.bukkit.craftbukkit.v1_16_R1.scheduler.CraftScheduler.mainThreadHeartbeat(CraftScheduler.java:400)
[14:47:25] [Server thread/WARN]:     at net.minecraft.server.v1_16_R1.MinecraftServer.b(MinecraftServer.java:1061)
[14:47:25] [Server thread/WARN]:     at net.minecraft.server.v1_16_R1.DedicatedServer.b(DedicatedServer.java:354)
[14:47:25] [Server thread/WARN]:     at net.minecraft.server.v1_16_R1.MinecraftServer.a(MinecraftServer.java:1009)
[14:47:25] [Server thread/WARN]:     at net.minecraft.server.v1_16_R1.MinecraftServer.v(MinecraftServer.java:848)
[14:47:25] [Server thread/WARN]:     at net.minecraft.server.v1_16_R1.MinecraftServer.lambda$0(MinecraftServer.java:164)
[14:47:25] [Server thread/WARN]:     at java.base/java.lang.Thread.run(Thread.java:832)
```

This is likely due to using an incompatible version of Java. The CommandAPI was designed to run on Java 8 and *should* be able to support Java 8, 9, 10, 11, 12, 13, 14 and 15 for OpenJDK. For an Oracle Java installation, it is recommended to use Java 8.

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

## Server/Plugin reloading

Due to the implementation of the CommandAPI, the CommandAPI does **not** support plugin reloading for plugins that use the CommandAPI. This includes, but is not limited to:

* The `/reload` command which reloads all plugins on the server
* Plugin reloading plugins, such as [PlugMan](https://dev.bukkit.org/projects/plugman)
* Any form of plugin enabling/disabling process for plugins which register commands via the CommandAPI

> **Developer's Note:**
>
> Plugin reloading gets very complicated with respect to the CommandAPI. Since the loading sequence of Minecraft commands is so picky, reloading the CommandAPI or a plugin which registers commands can cause commands to be re-registered. This can lead to very odd effects, such as command collisions _(commands just don't work)_, to duplicate commands being registered under different namespaces _(e.g. plugins are registered under Bukkit as well as Minecraft)_. These effects are not "100% guaranteed" and have only been seen during dodgy tests. In short, **do not enable or reload plugins**, and **absolutely do not reload the server with `/reload`**

## Players cannot connect/timeout when joining

If players cannot connect, this could be due to the size of the command data packet. To see the resultant packet being sent to players when they log in, enable the `create-dispatcher-json: true` setting and view the file size of the resultant file. If the file size is abnormally large _(Over 2MB is considered very large)_, consider reducing the number of `LiteralArguments` which your plugin uses.

## Command conversion throws a `NullPointerException`

This is likely caused by the fact that the plugin you want to convert hasn't been loaded yet. Ensure that it loads before your plugin by adding the following to the target plugin's `plugin.yml` file:

```yaml
loadbefore: [YourPlugin, CommandAPI]
```

## My issue isn't on here, what do I do?!

If you've found a bug that isn't solved here, submit a bug report on [the CommandAPI's issues page](https://github.com/JorelAli/1.13-Command-API/issues/new/choose) and I'll try my best to resolve the issue!
