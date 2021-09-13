# Plugin reloading

Formally, the CommandAPI **does not** support plugin reloading. This includes, but is not limited to:

* The `/reload` command which reloads all plugins on the server
* Plugin reloading plugins, such as [PlugMan](https://dev.bukkit.org/projects/plugman)
* Any form of plugin enabling/disabling process for plugins which register commands via the CommandAPI

Despite this, there is one way to get reloading to work using the `onDisable()` method in your plugin. If you register a command in your `onLoad()` or `onEnable()` method, by unregistering the command in your `onDisable()` method, this allows the CommandAPI to properly register the command again when the server reloads.

<div class="warning">

**Developer's Note:**

Despite the fact that you can do this, I cannot stress enough that **this is not recommended**, due to the fact that **functions/tags in datapacks do not work with `/reload`, even if you unregister the command**.

</div>

<div class="example">

### Example - Allowing a command to work with /reload

In this example, we add support for reloading the server via `/reload` by unregistering the command in the `onDisable()` method. Note that force-unregistering is not required for this:

```java
public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new CommandAPICommand("ping")
            .executes((sender, args) -> {
                sender.sendMessage("Pong!");
            })
            .register();
    }

    @Override
    public void onDisable() {
        CommandAPI.unregister("ping");
    }

}
```

</div>