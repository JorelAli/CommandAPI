# Configuration for server owners

The CommandAPI has a few configuration options to change how it functions. These options can be configured in the `plugins/CommandAPI/config.yml` file, which is generated automatically when the CommandAPI runs for the first time.

**Configuration settings:**

- **`verbose-outputs`** - If `true`, outputs command registration and unregistration logs in the console


- **`silent-logs`** - If `true`, turns off all logging from the CommandAPI, except for errors


- **`create-dispatcher-json`** - If `true`, the CommandAPI creates a `command_registration.json` file showing the mapping of registered commands. This is designed to be used by developers - setting this to `false` will improve command registration performance


- **`plugins-to-convert`** - Controls the list of plugins to process for command conversion. See [Command conversion](./conversionforowners.md) for more information!


- **`skip-sender-proxy`** - Determines whether the proxy sender should be skipped when converting a command. See [Skipping proxy senders](./skippingproxysenders.md) for more information!


- **`other-commands-to-convert`** - A list of other commands to convert. This should be used for commands which are not declared in a `plugin.yml` file. See [Arbitrary command conversion](./conversionforownerssingle.md#arbitrary-command-conversion) for more information

## Default configuration file

The default `config.yml` is shown below:

```yaml
{{#include ../../CommandAPI/commandapi-plugin/src/main/resources/config.yml}}
```