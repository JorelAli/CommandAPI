# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13

> # PSA: v3.0 release date
> As I'm sure most of you are aware, the CommandAPI v3.0 is under heavy development. An absurd amount of new content is being added, _(in particular, a tonne of new arguments and serious API refactoring)_ and it's unfortunately come to my attention that I'll not be able to meet my intended deadline of releasing this by October.
> 
> For now, **v3.0 is officially scheduled for August 2020**. This is mainly due to various events occuring right now (my higher education takes priority over this project), and working on v3.0 the project is a _lot_ more time consuming than I originally expected. **This date is just an estimate** and can change at any time.
>
> | Date                         | Project status                                                             |
> |------------------------------|----------------------------------------------------------------------------|
> | October 2019 - December 2019 | On Hold                                                                    |
> | December 2019 - January 2020 | Work on implementing new features from issues                              |
> | January 2020 - July 2020     | On Hold                                                                    |
> | July 2020 - August 2020      | Implementing other final features <br>3.0 documentation write up <br>3.0 release |
>
> However, hope is not lost. **I will continue to monitor the issues pages** and respond to them as normal _(within 24 hours)_, but do not expect any code-based progress until about Decemberish.
>
> If Minecraft are to release a new version (1.15) before the planned release, I will ensure that a version of the CommandAPI (based on version 2.3) will be released with full compatibility for Minecraft 1.15 as soon as possible.
>
> Thanks for all of your support and I hope you'll be understanding of the current situation.

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

* **No other dependencies** - You don't need to import Brigadier in your projects to use the CommandAPI

## Downloads & Documentation (includes usage for server owners)

| Version | Direct download                                                                                        | Documentation                                                                  |
| ------- | ------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------ |
| 3.0     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v3.0/CommandAPI.jar)   | [Version 3.0 documentation](https://jorelali.github.io/1.13-Command-API/)      |
| 2.3     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v2.3/CommandAPI.jar)   | [Version 2.1 documentation](https://jorelali.github.io/1.13-Command-API/)      |
| 2.2     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v2.2/CommandAPI.jar)   | [Version 2.1 documentation](https://jorelali.github.io/1.13-Command-API/)      |
| 2.1     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v2.1/CommandAPI.jar)   | [Version 2.1 documentation](https://jorelali.github.io/1.13-Command-API/)      |
| 2.0.1   | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v2.0.1/CommandAPI.jar) | [Version 2.0 documentation](https://jorelali.github.io/1.13-Command-API/)      |
| 2.0     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v2.0/CommandAPI.jar)   | [Version 2.0 documentation](https://jorelali.github.io/1.13-Command-API/)      |
| 1.8.2   | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.8.2/CommandAPI.jar) | [Version 1.8 - 1.8.2 documentation](./docssrc/olddocs/v1.8%20Documentation.md) |
| 1.7.2   | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.7.2/CommandAPI.jar) | [Version 1.7 - 1.7.2 documentation](./docssrc/olddocs/v1.7%20Documentation.md) |
| 1.6     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.6/CommandAPI.jar)   | [Version 1.6 documentation](./docssrc/olddocs/v1.6%20Documentation.md)         |
| 1.5     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.5/CommandAPI.jar)   | [Version 1.5 documentation](./docssrc/olddocs/v1.5%20Documentation.md)         |
| 1.4     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.4/CommandAPI.jar)   | [Version 1.4 documentation](./docssrc/olddocs/v1.4%20Documentation.md)         |
| 1.3     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.3/CommandAPI.jar)   | [Version 1.3 documentation](./docssrc/olddocs/v1.3%20Documentation.md)         |
| 1.2     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.2/CommandAPI.jar)   | [Version 1.2 documentation](./docssrc/olddocs/v1.2%20Documentation.md)         |
| 1.1     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.1/CommandAPI.jar)   | [Version 1.1 documentation](./docssrc/olddocs/v1.1%20Documentation.md)         |
| 1.0     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.0/CommandAPI.jar)   | [Version 1.0 documentation](./docssrc/olddocs/v1.0%20Documentation.md)         |

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

To build the CommandAPI, copies of the Spigot.jar servers are required for the following versions of Minecraft:

|                   |        |        |        |        |        |
| ----------------- | ------ | ------ | ------ | ------ | ------ |
| **1.13 versions** | 1.13   | 1.13.1 | 1.13.2 |        |        |
| **1.14 versions** | 1.14.2 | 1.14.3 | 1.14.4 |        |        |
| **1.15 versions** | 1.15.2 |    ?   |        |        |        |

These versions of Minecraft must be installed in your local machine's Maven repository (`~/.m2`). The easiest way to do this is to build them manually using Spigot's BuildTools, as it automatically adds it to the `.m2` local repository folder.


There are various methods of acquiring the required Spigot.jar server jar files:

#### Building them using _BuildTools_ + downloadSpigot file (Recommended)

* Download the `BuildTools.jar` file from [here](https://hub.spigotmc.org/jenkins/job/BuildTools/) and place it in a separate directory
* Download the `downloadSpigot` file from this repository:
  * If on Windows, use `downloadSpigot.bat`  [(from here)](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/downloadSpigot.bat)
  * If on Linux/MacOS, use `downloadSpigot.sh`  [(from here)](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/downloadSpigot.bat)
* If on Windows:
  * Download the `downloadSpigot.bat` file from this repository [(or just right click here, save as...)](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/downloadSpigot.bat)
  * Copy the `downloadSpigot.bat` file into the same directory as the `BuildTools.jar` file
  * Double click on the `downloadSpigot.bat` file to run it
* If on Linux/MacOS:
  * If on linux/mac, download the `downloadSpigot.sh` file from this repository [(or just right click here, save as...)](https://raw.githubusercontent.com/JorelAli/1.13-Command-API/master/downloadSpigot.sh)
  * Copy the `downloadSpigot.sh` file into the same directory as the `BuildTools.jar` file
  * Open up a terminal in your folder and make the `downloadSpigot.sh` file executable by using `chmod u+x ./downloadSpigot.sh`
  * Run the `downloadSpigot` file using `./downloadSpigot.sh`

> **Note:** Sometimes, the `downloadSpigot` file doesn't work because of incompatibilities with cloning certain files it needs. If for any reason, downloadSpigot stops working, delete the directories created (BuildData, Bukkit, CraftBukkit, Spigot and work) and re-run the downloadSpigot file.

#### Building them using _BuildTools_ + manual command line (Recommended)

- Download the `BuildTools.jar` file from [here](https://hub.spigotmc.org/jenkins/job/BuildTools/) and place it in a separate directory
- Use the command `java -jar BuildTools.jar --rev <VERSION>` to download the specific version of the Spigot.jar you need. For example, to download Spigot for 1.14.4, use `java -jar BuildTools.jar --rev 1.14.4`

-----

## Changelog

* Version 3.0
  * **Note: This version is incompatible with pre 3.0 versions CommandAPI plugins (See documentation for more information)**
  * Adds Time argument
  * Adds IntegerRange and FloatRange arguments
  * Adds 2D location arguments
  * Adds NBT arguments
  * Adds environment arguments
  * Adds rotation arguments
  * Adds item slot arguments
  * Adds axes (swizzle) argument
  * Adds scoreboard slot arguments
  * Adds scoreholder arguments
  * Adds team arguments
  * Adds objective and objective criteria arguments
  * Adds chat arguments
  * Removes defined custom arguments
  * Removes suggested string arguments
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
