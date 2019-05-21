# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13

## Purpose
This project provides an API to help Bukkit/Spigot developers use the new Minecraft 1.13 command UI, which includes:

* **Better commands** - Prevent players from running invalid commands, making it easier for developers
* **Better arguments** - Easily switch from Location arguments to raw JSON, with error checks all the way
* **Support for the `/execute` command** - Let your command to be executed by the built in `/execute` command
* **Support for Minecraft's functions** - Allow your command to be executed from Minecraft's functions and tags
* **Support for proxied command senders** - Run your command as other entities using `/execute as ... run command`
* **No plugin.yml registration** - Commands don't need to be registered in the `plugin.yml` file anymore
* **No dependencies** - You don't need to import Brigadier in your projects to use the CommandAPI

![commandUI](https://i.imgur.com/aTJa77G.gif "commandUI")

## Downloads & Documentation (includes usage for server owners)

| Version | Direct download                                              | Documentation                                                |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 2.0 | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v2.0/CommandAPI.jar)  | [Version 2.0 documentation](https://jorelali.github.io/1.13-Command-API/) |
| 1.8.2 | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.8.2/CommandAPI.jar) | [Version 1.8 - 1.8.2 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.8%20Documentation.md) |
| 1.7.2     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.7.2/CommandAPI.jar) | [Version 1.7 - 1.7.2 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.7%20Documentation.md) |
| 1.6     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.6/CommandAPI.jar) | [Version 1.6 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.6%20Documentation.md) |
| 1.5     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.5/CommandAPI.jar) | [Version 1.5 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.5%20Documentation.md) |
| 1.4     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.4/CommandAPI.jar) | [Version 1.4 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.4%20Documentation.md) |
| 1.3     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.3/CommandAPI.jar) | [Version 1.3 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.3%20Documentation.md) |
| 1.2     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.2/CommandAPI.jar) | [Version 1.2 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.2%20Documentation.md) |
| 1.1     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.1/CommandAPI.jar) | [Version 1.1 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.1%20Documentation.md) |
| 1.0     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.0/CommandAPI.jar) | [Version 1.0 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.0%20Documentation.md) |

## Changelog

* Version 2.0
  * Compatibility for 1.14
  * Major overhaul of the CommandAPI's internals - greatly improves performance
  * Deprecates SuggestedStringArgument, adding overrideSuggestions as an alternative for any argument type 
  * Adds CustomArguments, allowing you to create your own ... custom arguments
  * Excludes dependencies from final jar (#40)
  * Adds DefinedCustomArguments - CustomArguments that have been created by yours truly
  * DynamicSuggestedArguments now have access to the CommandSender (#41)
  * Adds Loot Table support
* Version 1.8.2
  * Fix bug with PlayerArgument when player cannot be found
  * Adds LocationArgument options for block precision or exact precision
* Version 1.8.1
  * Fix permissions for argument from 1.8
  * Neaten up logging with verbose outputs
* Version 1.8
  * Fix bugs where DynamicSuggestedArguments don't work as the last argument
  * Fix support for latest spigot version
  * Adds permissions for arguments
  * Adds support to override suggestions for arguments
* Version 1.7.2
  * Fix a bug where default return value was 0 instead of 1, causing issues with commandblocks
* Version 1.7.1
  * Fix a bug with permission checks. Other than that, it's the same as 1.7 (in terms of documentation)
* Version 1.7
  * Adds DynamicSuggestedStringArguments for dynamically updating suggestions
  * Adds support for `success` and `result` values for `/execute store`
  * Overhaul permissions system so it works properly
  * **Note: This version is incompatible with pre-1.7 version CommandAPI plugins**
* Version 1.6
  * Adds FunctionArguments to handle Minecraft functions
  * Remove useless test code
  * Fix bug with ProxiedCommandSender callee and caller
  * Adds Converter for legacy plugin support
  * Improved performance by caching NMS better than in version 1.5
* Version 1.5
  * Adds ChatComponentArgument to handle raw JSON
  * Adds SuggestedStringArgument to suggest strings
  * Adds config file
  * Fix bug where command errors weren't being thrown
  * Improved performance by caching NMS
* Version 1.4
  * Fix critical bug where arguments weren't being handled properly
  * Adds GreedyStringArgument
  * Adds various Exception classes
* Version 1.3
  * Migrate to Maven
  * Remove unnecessary reflection
  * Adds EntitySelectorArgument
  * Adds LiteralArgument
  * Adds support for ProxiedCommandSender
* Version 1.2
  * Adds TextArgument
* Version 1.1
  * Adds PlayerArgument
  * Adds ParticleArgument
  * Adds ChatColorArgument
  * Adds EnchantmentArgument
  * Adds LocationArgument
  * Adds EntityTypeArgument
  * Adds permissions support
  * Adds alias support
* Version 1.0
  * Initial release
