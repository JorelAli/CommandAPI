# Plugin reloading

Formally, the CommandAPI **does not** support plugin reloading. This includes, but is not limited to:

* The `/reload` command which reloads all plugins on the server
* Plugin reloading plugins, such as [PlugMan](https://dev.bukkit.org/projects/plugman)
* Any form of plugin enabling/disabling process for plugins which register commands via the CommandAPI

In general, using the `/reload` command is _not advised_. Here's some useful resources from various Bukkit/Spigot/Paper developers:

* Maddy Miller (WorldEdit creator):
  * [Why you should never /reload on Spigot, Bukkit, and Paper](https://madelinemiller.dev/blog/problem-with-reload/)
* Bukkit Forums:
  * [Is /reload that bad](https://bukkit.org/threads/is-reload-that-bad.129514/)
  * [Petition to remove the /reload command](https://bukkit.org/threads/petition-to-remove-the-reload-command.43212/)
* Spigot Forums:
  * [What's with the /reload command?](https://www.spigotmc.org/threads/whats-with-the-reload-command.344458/)
  * [Let's kill /reload, or make it better.](https://www.spigotmc.org/threads/lets-kill-reload-or-make-it-better.35611/)

The CommandAPI is _not like normal Bukkit/Spigot/Paper plugins_. It directly accesses all of the nitty-gritty Vanilla Minecraft code to convert and expose Minecraft's internal command framework into a Bukkit-API friendly interface for you to use. As the CommandAPI hooks directly into Vanilla Minecraft code, and `/reload` is a Bukkit feature, using `/reload` can cause Vanilla Minecraft's internal system to become unstable. If you are having issues with `/reload`, seriously reconsider shutting your server down correctly and restarting it, instead of running `/reload`.

-----

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
