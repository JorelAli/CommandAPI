# Configuration for server owners

The CommandAPI has a few configuration options to change how it functions. These options can be configured in the `plugins/CommandAPI/config.yml` file, which is generated automatically when the CommandAPI runs for the first time.

**Configuration settings:**

- **`verbose-outputs`** - If `true`, outputs command registration and unregistration logs in the console


- **`silent-logs`** - If `true`, turns off all logging from the CommandAPI, except for errors


- **`missing-executor-implementation`** - Sets the text to display when a command is run by an executor which has not been implemented (for example, if a command can only be run by the console and a player tries to run the command).


- **`create-dispatcher-json`** - If `true`, the CommandAPI creates a `command_registration.json` file showing the mapping of registered commands. This is designed to be used by developers - setting this to `false` will improve command registration performance


- **`use-latest-nms-version`** - If `true`, the CommandAPI will always use the latest NMS implementation.

  > **Developer's Note:**
  >
  > This can be used to run the CommandAPI on versions higher than it can support. For example, if the CommandAPI supports Minecraft 1.18 and Minecraft 1.18.1 comes out, you can use this to enable support for 1.18.1 before an official CommandAPI release comes out that supports 1.18.1. This feature is not guaranteed to work in every case, so beware!


- **`plugins-to-convert`** - Controls the list of plugins to process for command conversion. See [Command conversion](./conversionforowners.md) for more information!


- **`skip-sender-proxy`** - Determines whether the proxy sender should be skipped when converting a command. See [Skipping proxy senders](./skippingproxysenders.md) for more information!


- **`other-commands-to-convert`** - A list of other commands to convert. This should be used for commands which are not declared in a `plugin.yml` file. See [Arbitrary command conversion](./conversionforownerssingle.md#arbitrary-command-conversion) for more information

## Default configuration file

The default `config.yml` is shown below:

```yaml
{{#include ../../CommandAPI/commandapi-plugin/src/main/resources/config.yml}}
```