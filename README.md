# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13. _(Despite the name, this API is compatible for Minecraft versions 1.13 - 1.16.1)_

## Downloads & Documentation (includes usage for server owners)

The documentation has moved! You can now get your hands on the latest and greatest documentation on the [CommandAPI website!](https://www.jorel.dev/1.13-Command-API/)

-----

## Purpose
This project provides an API to help Bukkit/Spigot developers use the new Minecraft 1.13 command UI, which includes:

* **Better commands** - Prevent players from running invalid commands, making it easier for developers

  ![better commands](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/images/printnumber.gif)

* **Better arguments** - Easily switch from Location arguments to raw JSON, fully supported with built-in error checking

  ![better arguments](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/images/explode.gif)

* **Support for proxied command senders** - Run your command as other entities using `/execute as ... run command`

  ![proxied senders](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/images/selfdestruct.gif)

* **Support for the `/execute` command** - Let your command to be executed by the built in `/execute` command

* **Support for Minecraft's functions** - Allow your command to be executed from Minecraft's functions and tags

* **No plugin.yml registration** - Commands don't need to be registered in the `plugin.yml` file anymore

* **You don't need Brigadier** - You don't need to import Brigadier in your projects to use the CommandAPI

* **No tracking** - The CommandAPI don't collect any stats about its plugin; what you see is what you get!

-----

## Building the CommandAPI

The CommandAPI can be built easily, but requires copies of the Spigot server jars to be present locally on your machine in order to be compatible with any Minecraft version. The CommandAPI is built using the Maven build tool - if you don't have it, you can download it [here](https://maven.apache.org/download.cgi).

* Clone the repository using your preferred method, or with the command below:

  ```
  git clone https://github.com/JorelAli/1.13-Command-API.git
  ```

* Go into the folder named `1.13CommandAPI` _(Not to be confused with the folder named `1.13-Command-API`, which is what is cloned)_. You want the folder which contains `pom.xml`.

* Ensure you have the required spigot server jars (see below)

* Run `mvn clean install`

### Spigot Libraries

To build the CommandAPI, the following versions of Spigot are required:

|                   |        |        |        |
| ----------------- | ------ | ------ | ------ |
| **1.13 versions** | 1.13   | 1.13.1 | 1.13.2 |
| **1.14 versions** | 1.14   | 1.14.3 | 1.14.4 |
| **1.15 versions** | 1.15   |        |        |
| **1.16 versions** | 1.16.1 |        |        |

These versions of Minecraft must be installed in your local machine's Maven repository (`~/.m2`). **The easiest way to do this is to build them manually using Spigot's BuildTools, as it automatically adds it to the `.m2` local repository folder.**

#### Building them using _BuildTools_ + downloadSpigot file (recommended)

* Download the `BuildTools.jar` file from [here](https://hub.spigotmc.org/jenkins/job/BuildTools/)
* Make sure you have maven installed on your machine. If not, it can be downloaded from [here](https://maven.apache.org/download.cgi)
* If on Windows:
  * Download the `downloadSpigot.bat` file [(right click this link, save as...)](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/downloadSpigot.bat) and place it in the same folder as the `BuildTools.jar`
  * Double click on the `downloadSpigot.bat` file to run it
* If on Linux/MacOS:
  * Download the `downloadSpigot.sh` file [(right click this link, save as...)](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/downloadSpigot.sh) and place it in the same folder as the `BuildTools.jar`
  * Open up a terminal in your folder and make the `downloadSpigot.sh` file executable by using `chmod u+x ./downloadSpigot.sh`
  * Run the `downloadSpigot` file using `./downloadSpigot.sh`

#### Building them using _BuildTools_ + manual command line

- Download the `BuildTools.jar` file from [here](https://hub.spigotmc.org/jenkins/job/BuildTools/) and place it in a separate directory
- Use the command `java -jar BuildTools.jar --rev <VERSION>` to download the specific version of the Spigot.jar you need. For example, to download Spigot for 1.14.4, use `java -jar BuildTools.jar --rev 1.14.4`

-----

## Changelog

- Version 3.4
  - Fix bug with custom recipes not registering in Minecraft 1.16+
  - Fix bug where command conversion didn't actually register commands
  - Adds command conversion as a built-in feature via the CommandAPI's `config.yml`
- Version 3.3
  - Fixes a bug where functions didn't work in Minecraft 1.16+
  - Fixes a bug where spigot produces a warning about api-versions
- Version 3.2
  - Fixes a bug with `.overrideSuggestions()` from version 3.1
- Version 3.1
  - Fixes bug where command senders didn't work properly, causing commands to not work properly
  - Adds the ability to override suggestions with the information of previously declared argument

* Version 3.0
  * **Note: This version is incompatible with pre 3.0 versions CommandAPI plugins (See documentation for more information)**
  * Complete code refactor to make command syntax slightly more intuitive and consistent
  * Removes lots of reflection to improve performance
  * Adds better documentation
  * Adds JavaDocs
  * Adds support for 1.16.1
  * Adds new command executors (These let you filter commands based on what type of command executor runs the command):
    * Player command executors
    * Command block command executors
    * Console command executors
    * Entity command executors
    * Proxied command executors
  * Adds new arguments:
    * Axis Argument
    * Biome Argument
    * ChatColor Argument
    * Chat Argument
    * FloatRange Argument
    * IntegerRange Argument
    * Location2D Argument
    * MathOperation Argument
    * NBT Argument (NBTAPI required)
    * Scoreboard arguments:
      * Objective Argument
      * ObjectiveCriteria Argument
      * ScoreboardSlot Argument
      * ScoreHolder Argument
      * Team Argument
    * Time Argument
    * Rotation Argument
    * Environment Argument
  * Removes old arguments:
    * SuggestedStringArgument
    * DynamicSuggestedStringArgument
    * DefinedCustomArguments
* Version 2.3a
  * Adds support for Minecraft 1.15, 1.15.1 and 1.15.2
* Version 2.3
  * Fixes bug where permissions didn't work
  * Fixes bug where functions weren't working on 1.14.3 and 1.14.4
* Version 2.2
  * Adds support for Minecraft 1.13 and 1.13.1 _(Funny isn't it? It's called the 1.13 CommandAPI but never supported Minecraft 1.13 until now)_
  * Improves support for different versions
  * Adds pointless witty comments into changelog notes
  * Adds [1.13-Command-API-SafeReflection library](https://github.com/JorelAli/1.13-Command-API-SafeReflection) to greatly improve reliability of reflection calls
* Version 2.1
  * Adds RecipeArgument
  * Adds SoundArgument
  * Adds AdvancementArgument
  * Adds LootTableArgument
  * Adds support for 1.14.3 and 1.14.4
  * Fixes bug where aliases weren't registering properly ([#43](https://github.com/JorelAli/1.13-Command-API/issues/43))
  * Fix documentation for tooltips
  * Improve documentation for dependencies and repositories
* Version 2.0.1
  * Fix a bug where Brigadier was required as a dependency to build plugins
* Version 2.0
  * Compatibility for 1.14
  * Major overhaul of the CommandAPI's internals - greatly improves performance
  * Deprecates SuggestedStringArgument, adding overrideSuggestions as an alternative for any argument type 
  * Adds CustomArguments, allowing you to create your own ... custom arguments
  * Excludes dependencies from final jar ([#40](https://github.com/JorelAli/1.13-Command-API/issues/40))
  * Adds DefinedCustomArguments - CustomArguments that have been created by yours truly
  * DynamicSuggestedArguments now have access to the CommandSender ([#41](https://github.com/JorelAli/1.13-Command-API/issues/41))
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
