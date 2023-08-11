# Command unregistration

The CommandAPI allows you to remove commands completely from Minecraft's command list. This includes Vanilla commands, Bukkit commands, and plugin commands.

There are three methods you might use when unregistering commands:

```java
CommandAPI.unregister(String commandName);
CommandAPI.unregister(String commandName, boolean unregisterNamespaces);
CommandAPIBukkit.unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit);
```

To understand when and how to use these methods, you need to know a little about how Bukkit loads and sets up commands. This is basically the order of events when a Bukkit server starts:

1. Vanilla commands are placed in the Vanilla CommandDispatcher
2. Bukkit commands are placed in the Bukkit CommandMap
   - Given the `bukkit` namespace (E.g. `bukkit:version`)
3. Plugins are loaded
   - `onLoad` is called
4. Plugins are enabled
   - Plugin commands are read from the [`plugin.yml` file](https://www.spigotmc.org/wiki/plugin-yml/#commands) and placed in the Bukkit CommandMap
   - Given the plugin's name as their namespace (E.g. `luckperms:lp`)
   - `onEnable` is called
   - Repeat for each plugin
5. Bukkit's help command is registered
6. Vanilla commands are added to the Bukkit CommandMap
   - Given the `minecraft` namespace (E.g. `minecraft:gamemode`)
7. The server is done loading

Unregistering a command only works if it happens after the command is created. Bukkit's command system is special and has two locations where commands can exist -- either the Vanilla CommandDispatcher or the Bukkit CommandMap -- so you also need to know where your command is registered. With that in mind, here is what each of the `unregister` methods do:

```java
CommandAPI.unregister(String commandName);
```

Unregisters a command from the Vanilla CommandDispatcher.

```java
CommandAPI.unregister(String commandName, boolean unregisterNamespaces);
```

Unregisters a command from the Vanilla CommandDispatcher. If `unregisterNamespaces` is `true`, then any namespaced version of the command is also unregistered.

```java
CommandAPIBukkit.unregister(String commandName, boolean unregisterNamespaces, boolean unregisterBukkit);
```

Unregisters a command from Bukkit. As before, if `unregisterNamespaces` is `true`, then any namespaced version of the command is also unregistered. If `unregisterBukkit` is `true`, then only Bukkit commands in the Bukkit CommandMap are unregistered. If `unregisterBukkit` is `false`, only commands from the Vanilla CommandDispatcher are unregistered.

To give a better idea of how and when to use these methods, the rest of this page documents how to unregister different types of commands.

## Unregistering a Bukkit command - `/version`

`/version` is a command provided by Bukkit. Looking at the sequence of events above, that means it is created during step 2, before plugins are loaded in step 3. Consequently, the command will exist when our plugin's `onLoad` method is called, so we'll unregister it there. The same code will work in `onEnable` too, since step 4 is also after step 2.

Since this command exists in the Bukkit CommandMap, we'll need to use `CommandAPIBukkit#unregister` with `unregisterBukkit` set to `true`. We'll also remove the namespaced version -- `/bukkit:version` -- so `unregisterNamespaces` will be `true`. All together, the code looks like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration1}}
```

</div>

With this plugin, executing `/version` or `/bukkit:version` will give the unknown command message. Note that aliases like `/ver` and its namespaced version `/bukkit:ver` will still work. To remove aliases as well, you need to unregister each as its own command. For, `/ver`, that would mean calling `CommandAPIBukkit.unregister("ver", true, true)`.

## Unregistering a Vanilla command - `/gamemode`

`/gamemode` is a command provided by Vanilla Minecraft. Like the [previous example](#unregistering-a-bukkit-command---version), Vanilla commands are created in step 1, before plugins are loaded in step 3. For variety, we'll unregister the command in our plugin's `onEnable` -- step 4 -- but the same code would also work in `onLoad`.

Since this command exists in the Vanilla CommandDispatcher, we can use `CommandAPI#unregister`. That works the same as `CommandAPIBukkit#unregister` with `unregisterBukkit` set to `false`. We don't care about the namespace, so `unregisterNamespaces` will be `false`. That means we can use the simplest method, `CommandAPI.unregister(String commandName)`, since it sets `unregisterNamespaces` to `false` by default. All together, the code looks like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration2}}
```

</div>

With this code, executing `/gamemode` will give the unknown command exception as expected. However, even though `unregisterNamespaces` was `false`, `/minecraft:gamemode` can also not be run. This happens because Vanilla commands are given their namespace in step 6, after our plugin has removed `/gamemode`.

When the server starts, `/gamemode` is created in step 2 inside the Vanilla CommandDispatcher. In step 4, our plugin is enabled and we remove the `/gamemode` command from that CommandDispatcher. After all the plugins enable, step 6 moves all commands in the Vanilla CommandDispatcher to the Bukkit CommandMap and gives them the `minecraft` namespace. Since `/gamemode` doesn't exist at this point, step 6 cannot create the `/minecraft:gamemode` command. So, even though `unregisterNamespaces` was `false`, `/minecraft:gamemode` doesn't exist anyway.

<div class="example">

### Example - Replacing Minecraft's `/gamemode` command

To replace a command, first unregister the original command, then register a new implementation for that command.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration3}}
```

</div>

Now, when `/gamemode` is executed, it will use the new implementation defined using the CommandAPI.

</div>

## Unregistering a Plugin command - `/luckperms:luckperms`

The `/luckperms` command is provided by the Bukkit [LuckPerms](https://luckperms.net/) plugin. Plugin commands are created during step 4, immediately before calling the `onEnable` method of the respective plugin. In this case, unregistering the command in our own plugin's `onLoad` would not work, since the command wouldn't exist yet. We also have to make sure that our `onEnable` method is called after LuckPerm's. The best way to make sure that happens is to add LuckPerms as a `depend` or `softdepend` in our plugin's plugin.yml. You can read more about the different between `depend` and `softdepend` in [Spigot's documentation](https://www.spigotmc.org/wiki/plugin-yml/#optional-attributes), but that will look something like this:

```yaml
name: MyPlugin
main: some.package.name.Main
version: 1.0
depend:
  - LuckPerms
```

Since plugin commands are stored in the Bukkit CommandMap, we need to use `CommandAPIBukkit#unregister` with `unregisterBukkit` set to `true`. For demonstration’s sake, we only want to unregister the namespaced version -- `/luckperms:luckperms` -- and leave `/luckperms` alone. To do this, give `"luckperms:luckperms"` as the `commandName`, and set `unregisterNamespaces` to `false`. All together, the code looks like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration4}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration4}}
```

</div>

Executing `/luckperms` will work as normal, but `/luckperms:luckperms` will give the unknown command message.

## Unregistering a CommandAPI command

Unregistering a command created by the CommandAPI is similar to both unregistering a Vanilla command and a plugin command. Like a Vanilla command, CommandAPI commands are stored in the Vanilla CommandDispatcher, so they should be unregistered with `unregisterBukkit` set to `false`. Like plugin commands, they may be created in `onEnable`, so you need to make sure your plugin is enabled after the plugin that adds the command.

Unlike plugin commands, CommandAPI commands may be created in `onLoad`, as discussed in [Command loading order](./commandregistration.md#command-loading-order). That just means you may also be able to unregister the command in you own plugin's `onLoad`. As always, simply make sure you unregister a command after it is created, and it will be removed properly.

For our example, let's say we want to unregister the `/break` command created by the [Bukkit Maven Example Project](https://github.com/JorelAli/CommandAPI/tree/master/examples/bukkit/maven) for the CommandAPI. If you look at that plugin's code, you can see that it registers the `/break` command in it's `onEnable` method. Therefore, we can unregister the command in our own plugin's `onEnable`, making sure that our plugin will enable second by adding ExamplePlugin as a [`depend` or `softdepend`](https://www.spigotmc.org/wiki/plugin-yml/#optional-attributes).

```yaml
name: MyPlugin
main: some.package.name.Main
version: 1.0
depend:
  - CommandAPI
  - ExamplePlugin
```

> **Developer's Note:**
>
> If you can't find the code where a CommandAPI command is registered or just don't have access to the code of a plugin, you can still figure out when a command is registered. If you set [`verbose-outputs`](./config.md#verbose-outputs) to `true` in the CommandAPI's configuration, it will log command registration.
>
> For the ExamplePlugin, setting `verbose-outputs` to `true` gives this:
>
> ```log
> [Server thread/INFO]: [ExamplePlugin] Enabling ExamplePlugin v0.0.1
> [Server thread/INFO]: [CommandAPI] Registering command /break block<LocationArgument>
> [Server thread/INFO]: [CommandAPI] Registering command /myeffect target<PlayerArgument> potion<PotionEffectArgument>
> [Server thread/INFO]: [CommandAPI] Registering command /nbt nbt<NBTCompoundArgument>
> ```
>
> You can see that the ExamplePlugin registers its commands when `onEnable` is called.

In summary, we will unregister the `/break` command in our plugin's `onEnable`. We added Example plugin to the `depend` list in our plugin.yml so that our `onEnable` method runs second. `unregisterNamespaces` and `unregisterBukkit` will be set to `false`, and those are the default values, so we can simply use `CommandAPI.unregister(String commandName)`. All together, the code looks like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration5}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration5}}
```

</div>

Now, when you try to execute `/break`, you will just get the unknown command message as if it never existed.

## Special case: Unregistering Bukkit's `/help`

If you look at the sequence of events at the top of this page, you might notice that Bukkit's `/help` command gets its own place in step 5. Unlike the other [Bukkit commands](#unregistering-a-bukkit-command---version), `/help` is special and gets registered after plugins are loaded and enabled (don't ask, I don't know why :P). That means unregistering `/help` in `onLoad` or `onEnable` does not work, since the command doesn't exist yet.

In order to run our unregister task after the server is enabled, we can use Bukkit's [Scheduler API](https://bukkit.fandom.com/wiki/Scheduler_Programming). There are many ways to set up and run a task, and this should work in whatever way you like. You can even give the task zero delay, since Bukkit only starts processing tasks after the server is enabled.

Since `/help` is in the Bukkit CommandMap, we need to use `CommandAPIBukkit#unregister` with `unregisterBukkit` set to `true`. We'll leave `/bukkit:help` alone, so `unregisterNamespaces` will be `false`. All together, we can unregister Bukkit's `/help` command with this code:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration6}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration6}}
```

</div>

Funnily, if you try to execute `/help`, the server will still tell you: `Unknown command. Type "/help" for help.`. Luckily, `unregisterNamespaces` was `false`, so you can still use `/bukkit:help` to figure out your problem.

## Special case: Unregistering only `/minecraft:gamemode`

In the earlier example for [Unregistering `/gamemode`](#unregistering-a-vanilla-command---gamemode), even though `unregisterNamespaces` was `false`, the `/minecraft:gamemode` command was also not executable. As explained up there, this happens because the namespaced version of commands in the Vanilla CommandDispatcher are not created until after plugins are loaded and enabled. Since we unregistered `/gamemode` in `onEnable`, when the time came for the server to transfer Vanilla commands into the Bukkit CommandMap, it didn't know to create the `minecraft:gamemode` command. Consequently, this means we cannot normally remove only the `/minecraft:gamemode` command without also unregistering `/gamemode`.

Of course, it is still possible to only unregister `/minecraft:gamemode` and the namespaced versions of other Vanilla commands. As always, in order to unregister a command, you have to unregister after the command is created. So, we just need to unregister `/minecraft:gamemode` after the server is enabled. Like the [previous special case](#special-case-unregistering-bukkits-help), we can use Bukkit's [Scheduler API](https://bukkit.fandom.com/wiki/Scheduler_Programming) to run our unregister task after the server is enabled.

While `/minecraft:gamemode` only exists in the Bukkit CommandMap, it is the namespaced version of the Vanilla `/gamemode` command, so it is considered a Vanilla command. That means `unregisterBukkit` should be `false`, which is what it defaults to when using `CommandAPI#unregister`. The CommandAPI understands that once the server is enabled Vanilla commands will have been copied to the CommandMap, so it will be able to find `/minecraft:gamemode`

Finally, `unregisterNamespaces` should be `false`, and since that's the default value we don't have to include it. All together, the code looks like this:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration7}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration7}}
```

</div>

With this code, `/gamemode` will execute as normal, but `/minecraft:gamemode` will give the unknown command message.

<div class="warning">

**Developer's Note:**

Doing the opposite action here -- only unregistering `/gamemode` but keeping `/minecraft:gamemode` -- is not recommended. That would be the following code, where `commandName` is `"gamemode"` (or any command in the Vanilla CommandDispatcher), and `unregisterNamespaces` is `false`:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration8}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration8}}
```

</div>

The expected outcome of this code is that `/minecraft:gamemode` would work as expected, and `/gamemode` would give the command not found message. However, that is only true for the player's commands. If you try to use `/minecraft:gamemode` in the console, it *will not work* properly. Specifically, while you can tab-complete the command's label, `minecraft:gamemode` the command's arguments will not have any suggestions. If you try to execute `/minecraft:gamemode` in the console, it will always tell you your command is unknown or incomplete.

The main point is that if you ever try to unregister a Vanilla command after the server is enabled, the namespaced version of that command will break for the console. To avoid this issue, always set `unregisterNamespaces` to `true` if `unregisterBukkit` is `false` when unregistering commands after the server is enabled.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:commandUnregistration9}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:commandUnregistration9}}
```

</div>

</div>
