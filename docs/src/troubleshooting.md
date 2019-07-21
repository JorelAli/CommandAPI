# Troubleshooting

This section basically summarizes the list of things that _could_ go wrong with the CommandAPI and how to mitigate these circumstances.

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

## The server just hangs/slows down on my PaperSpigot server

Officially, the CommandAPI does _not_ really support PaperSpigot. As PaperSpigot is a fork of Spigot, it just assumes that it'll work on PaperSpigot if it works on Spigot.

> **Developer's Note:**
>
> There's a few things I personally don't like about PaperSpigot:
>
> * [Their developer documentation is non-existant](https://paper.readthedocs.io/en/stable/about/faq.html#what-can-i-do-with-paper)
> * [Their JavaDocs](https://papermc.io/javadocs/paper/1.13/overview-summary.html) are very... lacking of documentation
> * I personally think it's harder to keep track of new changes between Minecraft upgrades for PaperSpigot compared to Spigot's BuildTools

## Command conversion throws a `NullPointerException`

This is likely caused by the fact that the plugin you want to convert hasn't been loaded yet. Ensure that it loads before your plugin by adding the following to the target plugin's `plugin.yml` file:

```yaml
loadbefore: [YourPlugin, CommandAPI]
```

## Aliases don't work properly (It says unknown command)

This is a persistent bug which has been resolved in version 2.1+ of the CommandAPI.

## My issue isn't on here, what do I do?!

If you've found a bug that isn't solvable here, submit a bug report on [the CommandAPI's issues page](https://github.com/JorelAli/1.13-Command-API/issues/new/choose) and I'll try my best to resolve the issue!
