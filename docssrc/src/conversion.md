# Command conversion

> **Developer's Note:**
>
> If you're a server owner, you're probably lost! This section is for developer command conversion. If you're looking for how to convert plugins with the `config.yml` file, you want [2. Configuration for server owners](./config.md#command-conversion).

The CommandAPI has the ability to convert plugin commands to vanilla Minecraft commands using its `config.yml`'s `plugins-to-convert` option. Nevertheless, the API for command conversion is not hidden and you're free to use it as you see fit!

-----

Before you continue, let's clear up a few naming conventions which is used in the following sections!

- **Target plugin** - This refers to a non-CommandAPI plugin which registers normal Bukkit commands. This typically uses the old `getCommand(...).setExecutor(...)` method
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

As you can see, it declares 3 commands: `/gmc`, `/gms` and `/i`. **Since this target plugin hasn't been told to load before the CommandAPI, we must first modify the `plugin.yml` file for the target plugin:**

```yaml
name: TargetPlugin
main: some.random.package.Main
loadbefore: [CommandAPI]
version: 1.0
commands:
  gmc:
    aliases: gm1
  gms:
  i:
    permission: item.permission
```

-----

Now that the target plugin has been loaded before the CommandAPI, we can now begin writing your plugin that uses the CommandAPI converter. We will call this plugin "YourPlugin":

```java
public class YourPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        Converter.convert(Bukkit.getPluginManager().getPlugin("TargetPlugin"));
        //Other code goes here...
    }
    
}
```

When this is run, the commands `/gmc`, `/gm1`, `/gms` and `/i` will all be registered by the CommandAPI.

-----

So to summarise, our plugin loading order is the following:

\\[ \texttt{TargetPlugin} \xrightarrow{then} \texttt{CommandAPI} \xrightarrow{then} \texttt{YourPlugin} \\]

This makes sure that the target plugin's commands are registered first, so they are identifiable by the CommandAPI. The CommandAPI then does its initial setup before your plugin loads the target plugin's commands via the CommandAPI.

</div>

-----

## Only specific commands

In addition to converting all commands from a target plugin, the CommandAPI allows you to convert single commands at a time using the `Converter.convert(Plugin, String)` method, where the `String` argument refers to the command name as declared in the target plugin's `plugin.yml` file.