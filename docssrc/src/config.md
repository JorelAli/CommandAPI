# Configuration for server owners

The CommandAPI has a few configuration options to change how it functions. These options can be set in the `plugins/CommandAPI/config.yml` file, which is generated automatically when the CommandAPI runs for the first time.

## Default `config.yml` file

The default `config.yml` is shown below:

<details>
  <summary><b>config.yml</b></summary>

```yaml
# Verbose outputs (default: false)
# If "true", outputs command registration and unregistration logs in the console
verbose-outputs: false

# Silent logs (default: false)
# If "true", turns off all logging from the CommandAPI, except for errors.
silent-logs: false

# Messages
# Controls messages that the CommandAPI displays to players
messages:

    # Missing executor implementation (default: "This command has no implementations for %s")
    # The message to display to senders when a command has no executor. Available
    # parameters are:
    #   %s - the executor class (lowercase)
    #   %S - the executor class (normal case)
    missing-executor-implementation: This command has no implementations for %s

# Create dispatcher JSON (default: false)
# If "true", the CommandAPI creates a command_registration.json file showing the
# mapping of registered commands. This is designed to be used by developers -
# setting this to "false" will improve command registration performance.
create-dispatcher-json: false

# Use latest version (default: false)
# If "true", the CommandAPI will use the latest available NMS implementation
# when the CommandAPI is used. This avoids all checks to see if the latest NMS
# implementation is actually compatible with the current Minecraft version.
use-latest-nms-version: false

# Be lenient with version checks when loading for new minor Minecraft versions (default: false)
# If "true", the CommandAPI loads NMS implementations for potentially unsupported Minecraft versions.
# For example, this setting may allow updating from 1.21.1 to 1.21.2 as only the minor version is changing
# but will not allow an update from 1.21.2 to 1.22.
# Keep in mind that implementations may vary and actually updating the CommandAPI might be necessary.
be-lenient-for-minor-versions: false

# Hook into Paper's ServerResourcesReloadedEvent (default: true)
# If "true", and the CommandAPI detects it is running on a Paper server, it will
# hook into Paper's ServerResourcesReloadedEvent to detect when /minecraft:reload is run.
# This allows the CommandAPI to automatically call its custom datapack-reloading
# function which allows CommandAPI commands to be used in datapacks.
# If you set this to false, CommandAPI commands may not work inside datapacks after
# reloading datapacks.
hook-paper-reload: true

# Skips the initial datapack reload when the server loads (default: false)
# If "true", the CommandAPI will not reload datapacks when the server has finished
# loading. Datapacks will still be reloaded if performed manually when "hook-paper-reload"
# is set to "true" and /minecraft:reload is run.
skip-initial-datapack-reload: false

# Plugins to convert (default: [])
# Controls the list of plugins to process for command conversion.
plugins-to-convert: []

# Other commands to convert (default: [])
# A list of other commands to convert. This should be used for commands which
# are not declared in a plugin.yml file.
other-commands-to-convert: []

# Skip sender proxy (default: [])
# Determines whether the proxy sender should be skipped when converting a
# command. If you are having issues with plugin command conversion, add the
# plugin to this list.
skip-sender-proxy: []
```

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

### `be-lenient-for-minor-versions`

Controls whether the CommandAPI should be more lenient when updating to a new Minecraft version.

Similar to the [`use-latest-nms-version`](#use-latest-nms-version) setting, this can allow the CommandAPI to run on a version higher than it officially supports. As an example, this setting can allow updating to 1.21.2 from 1.21.1 but doesn't allow updating to 1.22 from 1.21.2.

<div class="warning">

Take the warning from the [`use-latest-nms-version`](#use-latest-nms-version) and apply it here too. This is _not_ guaranteed to work either and also may cause unexpected side-effects.

</div>

**Default value**

```yml
be-lenient-for-minor-versions: false
```

**Example value**

```yml
be-lenient-for-minor-versions: true
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
