# Configuration for server owners

The CommandAPI has a few configuration options to change how it functions. These options can be configured in the `plugins/CommandAPI/config.yml` file, which is generated automatically when the CommandAPI runs for the first time.

The default `config.yml` is shown below:

```yaml
verbose-outputs: true
create-dispatcher-json: false
plugins-to-convert: []
```

**Configuration settings:**

- **`verbose-outputs`** - If `true`, outputs command registration and unregistration logs in the console
- **`create-dispatcher-json`** - If `true`, creates a `command_registration.json` file showing the mapping of registered commands. This is not designed to by developers - setting this to `false` will improve command registration performance
- **`plugins-to-convert`** - Controls the list of plugins to process for command conversion. See below for more information

-----

## Command Conversion

The CommandAPI has the ability to convert plugin commands into "Vanilla compatible" commands automatically on startup. This allows you to use `/execute` and Minecraft functions/tags for plugins that do not use the CommandAPI. For example, if you want to use the `/hat` command from the plugin `Essentials`, you can use the CommandAPI's command conversion setting to do so.

To convert plugin commands, there are two things you need to do:

1. Update the plugin's `plugin.yml` file with `loadbefore: [CommandAPI]`
2. Populate the CommandAPI's `config.yml` file with the plugin (and any commands to be converted)

We explain these steps in detail below with examples, so don't worry!

### Updating `plugin.yml` files with `loadbefore`

For command conversion to work, the plugin must be loaded before the CommandAPI. To do this, you need to edit the plugin's `plugin.yml` file. The `plugin.yml` file can be found inside the plugin's `.jar` file, which can be opened using any archive tool such as 7-Zip or WinRAR.

![](./images/pluginyml1.png)

Next, you want to edit the `plugin.yml` file and add the following line:

```yaml
loadbefore: [CommandAPI]
```

The best place to put this line is before `commands` or `permissions` (underneath `version` would work fine if you're worried about where to put this line).

After you've done this, save the `plugin.yml` file, put the updated `plugin.yml` file back inside the `.jar` file and you're good to go!

<div class="example">

### Example - Editing `plugin.yml` for a plugin

Say we want to convert commands from the [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) plugin. We open the `.jar` file with an archive tool and locate it's `plugin.yml` file. We add the line `loadbefore: [CommandAPI]` and we get the following result:

```yaml
name: Essentials
main: com.earth2me.essentials.Essentials
version: 2.18.0.0
website: http://tiny.cc/EssentialsCommands
description: Provides an essential, core set of commands for Bukkit.
softdepend: [Vault, LuckPerms]
authors: [Zenexer, ementalo, Aelux, Brettflan, KimKandor, snowleo, ceulemans, Xeology, KHobbits, md_5, Iaccidentally, drtshock, vemacs, SupaHam, md678685]
api-version: "1.13"
loadbefore: [CommandAPI]
commands:
  afk:
    description: Marks you as away-from-keyboard.
    usage: /<command> [player/message...]
    aliases: [eafk,away,eaway]

# (other config options omitted)
```

</div>

-----

### Converting specific plugin commands

To use the command conversion, you need to first populate the `config.yml` with the list of plugins and commands to be processed. To illustrate this, we'll use an example:

<div class="example">

### Example - Converting commands

Say we're using [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) on our server and we want to be able to use `/afk` and `/hat` in command blocks. This would allow us to use (for example) the following commands in command blocks:

```
/execute as @p run afk
/execute as @p run hat
```

To do this, we need to add `Essentials` to our `config.yml` file, and include the commands `afk` and `hat` as the commands to be converted from the Essentials plugin. This would then make our `config.yml` file look like this:

```yaml
verbose-outputs: true
create-dispatcher-json: false
plugins-to-convert: 
  - Essentials: [hat, afk]
```

> **Developer's Note:**
>
> Note that the commands `hat` and `afk` are used, as opposed to an alias such as `head`. The CommandAPI is only able to convert plugin commands that are declared in a plugin's `plugin.yml` file. For example, if we take a look at the EssentialsX `plugin.yml` file, we can see the commands `afk` and `hat` have been declared and thus, are the commands which must be used in the CommandAPI's `config.yml` file:
>
> ```yaml
> name: Essentials
> main: com.earth2me.essentials.Essentials
> version: 2.18.0.0
> website: http://tiny.cc/EssentialsCommands
> description: Provides an essential, core set of commands for Bukkit.
> softdepend: [Vault, LuckPerms]
> authors: [Zenexer, ementalo, Aelux, Brettflan, KimKandor, snowleo, ceulemans, Xeology, KHobbits, md_5, Iaccidentally, drtshock, vemacs, SupaHam, md678685]
> api-version: "1.13"
> commands:
>   afk:
>     description: Marks you as away-from-keyboard.
>     usage: /<command> [player/message...]
>     aliases: [eafk,away,eaway]
>     
> # (other config options omitted)
> 
>   hat:
>     description: Get some cool new headgear.
>     usage: /<command> [remove]
>     aliases: [ehat,head,ehead]
>     
> # (other config options omitted)
> ```

</div>

-----

### Converting all plugin commands

If you want to convert all of the plugin commands that a plugin has, you can simply leave the list of commands to convert empty. **Make sure to keep the colon at the end!** For example, if you wanted to convert all commands that [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) has, you can use the following `config.yml`:

```yaml
verbose-outputs: true
create-dispatcher-json: false
plugins-to-convert: 
  - Essentials:
```



