# Command conversion

Since the CommandAPI is used to register commands as a vanilla Minecraft command, you may want to use other plugins that are not written with the CommandAPI. For instance, if you want to include a command from a plugin which doesn't use the CommandAPI in a commandblock, _(such as the `/execute` command)_, you can use the CommandAPI's command conversion system.

> **Developer's Note:**
>
> The command conversion system is nowhere near perfect. It tries its best to connect Bukkit plugins to vanilla Minecraft commands, but is not guaranteed to run flawlessly. If possible, consider forking/requesting a plugin and writing it with compatibility for the CommandAPI.

-----

## Entire plugins

To register all commands that are declared by a plugin, the `Converter.convert(Plugin)` method can be used. This attempts to register all commands declared in a plugin's `plugin.yml` file, as well as any aliases or permissions stated in the `plugin.yml` file.

It is important to note that the plugin must be loaded before your plugin before attempting conversion. _(Use `loadbefore: [YourPlugin, CommandAPI]` to ensure it loads before your plugin)_

<div class="example">

### Example - Converting commands for a plugin

Say you have some `plugin.yml` file for a plugin that adds some basic functionality to a server. The plugin in this example is called "myPlugin":

```yaml
name: myPlugin
main: some.random.package.Main
version: 1.0
commands:
  gmc:
    aliases: gm1
  gms:
  i:
    permission: item.permission
```

As you can see, it declares 3 commands: `/gmc`, `/gms` and `/i`. **Since this plugin hasn't been told to load before the CommandAPI, we must first modify the `plugin.yml` file:**

```yaml
name: myPlugin
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

Now that the plugin has been loaded before the CommandAPI, we can now begin writing our own plugin that uses the CommandAPI converter. We will call this plugin "MyCoolPlugin":

```java
public class MyCoolPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        Converter.convert(Bukkit.getPluginManager().getPlugin("myPlugin"));
        //Other code goes here...
    }
    
}
```

To ensure that *MyCoolPlugin* loads _after_ _myPlugin_, we must ensure that the `plugin.yml` for _myPlugin_ states this. So, we update the `plugin.yml` for _myPlugin_ again:

```yaml
name: myPlugin
main: some.random.package.Main
loadbefore: [MyCoolPlugin, CommandAPI]
version: 1.0
commands:
  gmc:
    aliases: gm1
  gms:
  i:
    permission: item.permission
```

When this is run, the commands `/gmc`, `/gm1`, `/gms` and `/i` will all be registered by the CommandAPI.

-----

So to summarise, our plugin loading order is the following:

\\[ \texttt{myPlugin} \xrightarrow{then} \texttt{CommandAPI} \xrightarrow{then} \texttt{MyCoolPlugin} \\]

where:

- \\(\texttt{myPlugin}\\) is the plugin that registers commands normally via Bukkit
- \\(\texttt{CommandAPI}\\) is the CommandAPI
- \\(\texttt{MyCoolPlugin}\\) is your plugin that converts `myPlugin` commands using the CommandAPI

</div>

-----

## Only specific commands

In addition to converting the whole plugin, the CommandAPI allows you to convert single commands at a time using the `Converter.convert(Plugin, String)` method, where the `String` argument refers to the command name as declared in the plugin's `plugin.yml` file.