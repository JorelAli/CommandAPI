# Configuration for server owners

The CommandAPI has a few configuration options to change how it functions. These options can be set in the `plugins/CommandAPI/config.yml` file, which is generated automatically when the CommandAPI runs for the first time.

## Default `config.yml` file

The default `config.yml` is shown below:

<details>
  <summary><b>config.yml</b></summary>

<div class="multi-pre">

```yaml,Paper
{{#include ../../commandapi-platforms/commandapi-paper/commandapi-paper-plugin/src/main/resources/config.yml}}
```

````yaml,Spigot
{{#include ../../commandapi-platforms/commandapi-spigot/commandapi-spigot-plugin/src/main/resources/config.yml}}
````

</div>

</details>

## Configuration settings

-----

### `verbose-outputs`

If `true`, outputs command registration and unregistration logs in the console. This is primarily used for developers to identify issues with command registration.

**Default value**

```yml
verbose-outputs: false
```

**Example value**

```yml
verbose-outputs: true
```

-----

### `silent-logs`

If `true`, turns off all logging from the CommandAPI, except for errors.

**Default value**

```yml
silent-logs: false
```

**Example value**

```yml
silent-logs: true
```

-----

### `messages`

Controls messages that the CommandAPI displays to players. Available messages:

- `missing-executor-implementation` - the message to display to senders when a command has no executor. This message supports format parameters:
  - `%s` - the executor class (lowercase). For example "craftplayer"
  - `%S` - the executor class (normal case). For example "CraftPlayer"

**Default value**

```yml
messages:
  missing-executor-implementation: "This command has no implementations for %s"
```

-----

### `create-dispatcher-json`

Controls whether the CommandAPI should generate a `command_registration.json` file showing the mapping of registered commands.

This is primarily designed to be used by developers. Setting this to `false` will slightly improve command registration performance.

The `command_registration.json` JSON representation of commands is in the same format as Minecraft's [_Data Generators_ Commands report](https://wiki.vg/Data_Generators#Commands_report). The format is Brigadier's command graph - more information about the JSON format can be found [here](https://wiki.vg/Command_Data).

**Default value**

```yml
create-dispatcher-json: false
```

**Example value**

```yml
create-dispatcher-json: true
```

-----

### `use-latest-nms-version`

Controls whether the CommandAPI should use the latest NMS implementation for command registration and execution.

This setting can be used to run the CommandAPI on Minecraft versions higher than it can support. For example, if the CommandAPI supports Minecraft 1.18 and Minecraft 1.18.1 comes out, you can use this to enable support for 1.18.1 before an official CommandAPI release comes out that supports 1.18.1.

<div class="warning">

This feature is very experimental and should only be used if you know what you are doing. In almost every case, it is better to wait for an official CommandAPI release that supports the latest version of Minecraft. Using `use-latest-nms-version` is _not_ guaranteed to work and can cause unexpected side-effects!

</div>

**Default value**

```yml
use-latest-nms-version: false
```

**Example value**

```yml
use-latest-nms-version: true
```

-----

### `hook-paper-reload`

Controls whether the CommandAPI hooks into the Paper-exclusive `ServerResourcesReloadedEvent` when available.

When the CommandAPI detects it is running on a Paper-based server, its default behavior will be to hook into the `ServerResourcesReloadedEvent`, which triggers when `/minecraft:reload` is run. During this event, the CommandAPI runs a custom datapack reloading sequence that helps commands registered with the CommandAPI work within datapacks. See [Reloading datapacks](./internal.md#reloading-datapacks) for more information on this process.

By default, this value is set to `true` and the CommandAPI will hook into the `ServerResourcesReloadedEvent`. If you want, you can set this to `false`, and the CommandAPI will not hook into this event.

**Default value**

```yml
hook-paper-reload: true
```

**Example value**

```yml
hook-paper-reload: false
```

-----

### `skip-initial-datapack-reload`

Controls whether the CommandAPI should perform its initial datapack reload when the server has finished loading.

The CommandAPI automatically reloads all datapacks in a similar fashion to `/minecraft:reload` in order to propagate CommandAPI commands into datapack functions and tags. This operation may cause a slight delay to server startup and is not necessary if you are not using datapacks or functions that use CommandAPI commands. This operation can be skipped by setting this value to `true`.

Note that datapacks will still be reloaded if performed manually when `hook-paper-reload` is set to `true` and you run `/minecraft:reload`.

**Default value**

```yml
skip-initial-datapack-reload: false
```

**Example value**

```yml
skip-initial-datapack-reload: true
```

-----

### `plugins-to-convert`

Controls the list of plugins to process for command conversion. See [Command conversion](./conversionforowners.md) for more information.

**Default value**

```yml
plugins-to-convert: []
```

**Example values**

```yml
plugins-to-convert:
  - Essentials: ~
```

```yml
plugins-to-convert:
  - Essentials:
    - speed
    - hat
  - MyPlugin:
    - mycommand
  - MyOtherPlugin: ~
```

```yml
plugins-to-convert:
  - Essentials:
    - speed <speed>[0..10]
    - speed <target>[minecraft:game_profile]
    - speed (walk|fly) <speed>[0..10]
    - speed (walk|fly) <speed>[0..10] <target>[minecraft:game_profile]
```

-----

### `skip-sender-proxy`

Determines whether the proxy sender should be skipped when converting a command. See [Skipping proxy senders](./skippingproxysenders.md) for more information.

**Default value**

```yml
skip-sender-proxy: []
```

**Example value**

```yml
skip-sender-proxy:
  - SkinsRestorer
  - MyPlugin
```

-----

### `other-commands-to-convert`

A list of other commands to convert. This should be used for commands which are not declared in a `plugin.yml` file. See [Arbitrary command conversion](./conversionforownerssingle.md#arbitrary-command-conversion) for more information.

**Default value**

```yml
other-commands-to-convert: []
```

**Example value**

```yml
other-commands-to-convert:
  - /set
  - mycommand
```
