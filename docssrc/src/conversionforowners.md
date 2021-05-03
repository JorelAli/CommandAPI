# Command Conversion (for server owners)

The CommandAPI has the ability to convert plugin commands into "Vanilla compatible" commands automatically on startup. This allows you to use `/execute` and Minecraft functions/tags for plugins that do not use the CommandAPI. For example, if you want to use the `/hat` command from the plugin `Essentials` in an `/execute` command or from a command block, you can use the CommandAPI's command conversion setting to do so.

The CommandAPI has 3 different conversion methods, each one more specific and powerful than the others. These are the following:

- [**Entire plugin conversion**](#converting-all-plugin-commands)

  Converts all commands from a plugin into Vanilla compatible commands

- [**Single command conversion**](./conversionforownerssingle.md) 

  Converts a single command into a Vanilla compatible command

- [**Single command conversion with custom arguments**](./conversionforownerssingleargs.md) 

  Converts a single command from a plugin into a Vanilla compatible command, whilst also declaring what the arguments to the command are

-----

## YAML configuration rules

To configure command conversion, the CommandAPI reads this information from the `config.yml` file. This file has a bit of a weird structure, so to put it simply, these are the following rules:

- **`config.yml` cannot have tab characters** - The `config.yml` file _must_ only consist of spaces!
- Indentation is important and should be _two spaces_

-----

### Converting all plugin commands

To convert all of the commands that a plugin has, add the name of the plugin, followed by a `~` character to the list of plugins to convert in the `config.yml` file.

<div class="example">

### Example - Converting all commands from EssentialsX

For example, if you wanted to convert all commands that [EssentialsX](https://www.spigotmc.org/resources/essentialsx.9089/) has, you can use the following `config.yml`:

```yaml
verbose-outputs: true
create-dispatcher-json: false
plugins-to-convert: 
  - Essentials: ~
```

</div>