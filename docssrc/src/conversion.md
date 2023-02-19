# Command conversion

> **Developer's Note:**
>
> If you're a server owner, you're probably lost! This section is for developer command conversion. If you're looking for how to convert plugins with the `config.yml` file, you want [2. Configuration for server owners](./config.md#command-conversion).

The CommandAPI has the ability to convert plugin commands to vanilla Minecraft commands using its `config.yml`'s `plugins-to-convert` option. Nevertheless, the API for command conversion is not hidden and you're free to use it as you see fit!

-----

Before you continue, let's clear up a few naming conventions which is used in the following sections!

- **Target plugin** - This refers to a non-CommandAPI plugin which registers normal Bukkit commands. This typically uses the old `boolean onCommand(CommandSender ... )` method
- **Your plugin** - This refers to your plugin, the one that uses the CommandAPI and wants to add compatibility to a target plugin

-----

## Entire plugins

To register all commands that are declared by a target plugin, the `Converter.convert(Plugin)` method can be used. This attempts to register all commands declared in a target plugin's `plugin.yml` file, as well as any aliases or permissions stated in the `plugin.yml` file.

<div class="example">

### Example - Converting commands for a target plugin

Say you have some `plugin.yml` file for a target plugin that adds some basic functionality to a server. The target plugin in this example is called "TargetPlugin":

```yaml
name: TargetPlugin
main: some.random.package.Main
version: 1.0
commands:
  gmc:
    aliases: gm1
  gms:
  i:
    permission: item.permission
```

As you can see, it declares 3 commands: `/gmc`, `/gms` and `/i`. We can now begin writing your plugin that uses the CommandAPI converter. We will call this plugin "YourPlugin":

<div class="multi-pre">

```java,Java
public {{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:conversion1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:conversion1}}
```

</div>

When this is run, the commands `/gmc`, `/gm1`, `/gms` and `/i` will all be registered by the CommandAPI.

</div>

-----

## Only specific commands

In addition to converting all commands from a target plugin, the CommandAPI allows you to convert single commands at a time using the following methods from the `Converter` class:

```java
public static convert(Plugin plugin, String cmdName);
public static convert(Plugin plugin, String cmdName, List<Argument> arguments);
public static convert(Plugin plugin, String cmdName, Argument... arguments);
```

In these commands, the `plugin` refers to the plugin which has the command you want to convert and `cmdName` is the name of the command declared in the target plugin's `plugin.yml` file (just the main command, not the aliases!).

The `List<Argument>` or `Argument...` can be used to provide argument checks that lets you apply the command UI to a bukkit command.

<div class="example">

### Example - Converting EssentialsX's speed command

Say we want to convert EssentialsX's `/speed` command using the CommandAPI. The `plugin.yml` entry for the `/speed` command is the following:

```yaml
  speed:
    description: Change your speed limits.
    usage: /<command> [type] <speed> [player]
    aliases: [flyspeed,eflyspeed,fspeed,efspeed,espeed,walkspeed,ewalkspeed,wspeed,ewspeed]
```

From this, we can determine that there are the following commands, where "walk" and "fly" are the different types that the command can take:

```mccmd
/speed <speed>
/speed <speed> <target>
/speed <walk/fly> <speed>
/speed <walk/fly> <speed> <target>
```

With the EssentialsX plugin, the `<speed>` value can only take numbers between 0 and 10. As such, we'll ensure to apply these limits using the `IntegerArgument`. In addition, since the speed type can only be "walk" or "fly", we'll add that to our converter as well using a `MultiLiteralArgument`:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:conversion2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:conversion2}}
```

</div>

![An image showing /execute run for EssentialsX's /speed command](./images/speed.gif)

</div>
