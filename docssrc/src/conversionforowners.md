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

## Extract plugin info

Drag a plugin here to view a list of available commands which can be registered for the CommandAPI.

<style>
    .drop_zone_parent {
        padding: 20px;
    }
    
    .drop_zone {
        border: 2px solid;
        border-radius: 20px;
        padding: 20px;
        border-style: dashed;
        height: 50px;
        display: flex;
        justify-content: center;
        align-items: center;
    }
    
    #drop_zone_output {
        margin-top: 20px;
    }

    .drop_zone_text {
        margin-left: 20px;
    }
</style>

<div class="drop_zone_parent">
    <div class="drop_zone" ondrop="pluginDropHandler(event);" ondragover="pluginDragHandler(event);">
    <!-- From https://tablericons.com/. Governed by the MIT license. -->
    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-upload" width="32" height="32" viewBox="0 0 24 24" stroke-width="1.5" stroke="#000000" fill="none" stroke-linecap="round" stroke-linejoin="round">
  <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
  <path d="M4 17v2a2 2 0 0 0 2 2h12a2 2 0 0 0 2 -2v-2" />
  <polyline points="7 9 12 4 17 9" />
  <line x1="12" y1="4" x2="12" y2="16" />
</svg><span ><h3 class="drop_zone_text" id="plugin_upload_text" >Drag and drop a plugin .jar file here</h3></span>
    </div>
</div>

<div id="plugin_upload_output"></div>

-----

## YAML configuration rules

To configure command conversion, the CommandAPI reads this information from the `config.yml` file. This file has a bit of a weird structure, so to put it simply, these are the following rules:

- **`config.yml` cannot have tab characters** - The `config.yml` file _must_ only consist of spaces!
- Indentation is important and should be _two spaces_

If you're uncertain if your configuration is valid (or you're getting weird errors in the console), you can check if your configuration is valid by dropping your `config.yml` file below:

<div class="drop_zone_parent">
    <div class="drop_zone" ondrop="configDropHandler(event);" ondragover="configDragHandler(event);">
    <!-- From https://tablericons.com/. Governed by the MIT license. -->
    <svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-upload" width="32" height="32" viewBox="0 0 24 24" stroke-width="1.5" stroke="#000000" fill="none" stroke-linecap="round" stroke-linejoin="round">
  <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
  <path d="M4 17v2a2 2 0 0 0 2 2h12a2 2 0 0 0 2 -2v-2" />
  <polyline points="7 9 12 4 17 9" />
  <line x1="12" y1="4" x2="12" y2="16" />
</svg><span ><h3 class="drop_zone_text" id="config_upload_text" >Drag and drop your config.yml here</h3></span>
    </div>
</div>

<div id="config_upload_output"></div>

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