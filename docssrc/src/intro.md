# Introduction

Welcome to the documentation for the CommandAPI. The CommandAPI lets you create vanilla Minecraft commands which utilize the new command features which were implemented in Minecraft 1.13, including but not limited to:

* Having commands compatible with the vanilla `/execute` command
* Having commands which can be run using Minecraft functions
* Having better auto-completion and suggestions
* Having command type checks before execution (e.g. ensuring a number is within a certain range)

-----

## How the CommandAPI works

> **Developer's Note:**
>
> This is a pretty important section, I would recommend reading before implementing the CommandAPI in your own projects. This section tells you about setup which is not stated anywhere else in the documentation. Think of it as the "knowledge you should know before using this API".

The CommandAPI does not follow the "standard" method of registering commands. In other words, commands which are registered with the CommandAPI will be registered as pure vanilla Minecraft commands as opposed to Bukkit or Spigot commands. This means that the following implications exist:

* **Commands should not be declared in the `plugin.yml` file.**
* Commands are automatically registered under the `minecraft` namespace. For example, if you register a command `/hello`, you can also run it using `/minecraft:hello`.
* Commands are not "linked" to a certain plugin. In other words, you cannot look up which commands are registered by which plugin.

-----

## How this documentation works

This documentation is split into the major sections that build up the CommandAPI. It's been designed in such a way that it should be easy to find exactly what you want to help you get started with the CommandAPI, and how to make effective use of it. Each step of the way, the documentation will include examples which showcase how to use the CommandAPI. 

You can use the side bar on the left to access the various sections of the documentation and can change the theme to your liking using the paintbrush icon in the top left corner. 

Using the search icon in the top left corner, you can search for anything in this entire documentation. For example, typing "Example" will show a list of examples which are included throughout the documentation.

-----

## Documentation changelog

Here's the list of changes to the documentation between each update. You can view the current documentation version at the top of this page.

### Documentation changes 6.3.1 \\(\rightarrow\\) 6.4.0:

- Adds a section [Help](./help.md) for the new help feature.
- Update [Annotations](./annotations.md) section to include new `@Help` annotation.

### Documentation changes 6.3.0 \\(\rightarrow\\) 6.3.1:

- Adds Java 16 error to [Troubleshooting](./troubleshooting.md).
- Update [FAQ](faq.md).
- Adds some useful tools to [Command conversion](./conversionforowners.md) to get plugin info and check `config.yml` validity.
- Adds [Plugin reloading](./reloading.md) page which describes how to add minimal support for `/reload`.

### Documentation changes 6.2.0 \\(\rightarrow\\) 6.3.0:

- Update [Custom arguments](./customarguments.md) page with new custom argument constructor information
- Adds upgrade info to the [Upgrading guide](./upgrading.md#from-version-620-to-630) to help upgrade any existing custom arguments which you may have.

### Documentation changes 6.0.0 \\(\rightarrow\\) 6.2.0:

- Update [Configuration for server owners](./config.md) page with new config options `missing-executor-implementation` and `use-latest-nms-version`
- Update instructions for shading the CommandAPI with maven in [Shading the CommandAPI in your plugins](./shading.md#shading-with-maven) to support Java 16.
- Mention that commands registered with the CommandAPI appear in the `minecraft:` namespace (see above under "How the CommandAPI works")

### Documentation changes 5.12 \\(\rightarrow\\) 6.0.0:

- Adds entry for [Upgrading guide](./upgrading.md#from-version-5x-to-600) to help update from 5.12 to 6.0.0.
- Adds new `silent-logs` config entry to [Configuration for server owners](./config.md)
- Update syntax for `onLoad(CommandAPIConfig)` for [Shading the CommandAPI in your plugins](./shading.md)
- Update [Argument suggestions](./argumentsuggestions.md) including new `replaceSuggestions` method
- Adds documentation for [OfflinePlayerArgument](./entityarguments.md#offlineplayer-argument)
- Adds a new section **CommandAPI Contribution** which gives a bit of insight into the project structure of the CommandAPI (Still in progress, not complete yet)
- Fix old documentation typos
- Fix spacing issues in some existing code blocks
- Adds syntax highlighting for Minecraft commands in code blocks
- Fix old code examples which didn't work anymore

### Documentation changes 5.11 \\(\rightarrow\\) 5.12:

> **Developer's Note**
>
> The Maven/Gradle repository URL has changed! See [5. Setting up your development environment](./quickstart.md) for more information. For older versions of the CommandAPI (versions 5.11 and below), please consult the older documentation which can be found on the homepage [here](https://commandapi.jorel.dev/).

- Change the repository URL for the CommandAPI in [5. Setting up your development environment](./quickstart.md)
- Update the [Afterword](./afterword.md)

### Documentation changes 5.10 \\(\rightarrow\\) 5.11:

- Adds a section [Arbitrary command conversion](./conversionforownerssingle.md#arbitrary-command-conversion) on how to convert arbitrary commands
- Adds a section [3.3. Entity selectors](./conversionentityselectors.md) describing how to convert entity selector commands with the CommandAPI's command conversion system
- Updated the [list of all suppoprted argument types](./conversionforownerssingleargs.md#list-of-all-supported-argument-types) for command conversion

### Documentation changes 5.6 \\(\rightarrow\\) 5.10:

- Splits chat argument sections into two: [11.5.1. Spigot chat arguments](./spigotchatarguments.md) and [11.5.2. Adventure chat arguments](./adventurechatarguments.md)
- Adds a [FAQ page](./faq.md)
- Adds a warning about shading in [6. Shading the CommandAPI in your plugins](./shading.md)

### Documentation changes 5.3 \\(\rightarrow\\) 5.6:

- Adds a section [4. Skipping proxy senders](./skippingproxysenders.md) which describes how to use the `skip-sender-proxy` configuration option.

### Documentation changes 5.2 \\(\rightarrow\\) 5.3:

- Adds a section [6. Using the annotation system](./annotationsetup.md) on setting up your development environment to use the annotation system
- Adds a whole massive section on using annotations ([16. Annotation-based commands](./annotationsintro.md), [17. Annotations](./annotations.md), [18. Registering annotation-based commands](./registeringannotations.md))
- Adds a section on argument suggestion deferral in section [9.1. Argument suggestions](./argumentsuggestions.md#argument-suggestion-deferral)
- Improve warning for `LiteralArgument` - instead of it being "obsolete" compared to the `MultiLiteralArgument`, it is now "more complex" than `MultiLiteralArgument`s
- Fix issue in the section for custom arguments which should have been updated but wasn't

### Documentation changes 5.1 \\(\rightarrow\\) 5.2:

- Adds brief documentation for the CommandAPI's `reloadDatapacks` method in [16. Internal CommandAPI](./internal.md#reloading-datapacks)

### Documentation changes 5.0 \\(\rightarrow\\) 5.1:

- Adds a section [10.2. The SimpleFunctionWrapper class](./simplefunctionwrapper.md) which outlines the new `SimpleFunctionWrapper` class
- Updates the documentation for [10.3. The FunctionWrapper class](./functionwrapper.md)
- Update the name of the package from `dev.jorel.commandapi.CommandAPIHandler.Brigadier` to `dev.jorel.commandapi.Brigadier` in section [17. Brigadier + CommandAPI](./brigadier.md#brigadier-support-functions)
- Update the documentation for [11. Permissions](./permissions.md) stating that you can use `withPermission(String)` instead of `withPermission(CommandPermission)`

### Documentation changes 4.3 \\(\rightarrow\\) 5.0:

> **Developer's Note:**
>
> Lots and lots and lots of changes occurred in version 5.0! I highly recommend reading the [Upgrading guide](./upgrading.html) section which covers the changes in more detail and how to update your plugin for this version.

Basically every page has been rewritten in this update and checked for errors. In general, this is the list of new additions:

- New section [3. Command conversion](./conversionforowners.md) dedicated to command conversion via the `config.yml`
- New section [8.4. Listed arguments](./listed.md)
- New section [9.8.1. Angle arguments](./anglearguments.md)
- New section [14. Subcommands](./subcommands.md)
- New section [16. Internal CommandAPI](./internal) now lists all arguments and their respective Minecraft argument IDs
- Mentions listed arguments in section [9.11.1. Literal arguments](./literalarguments.md)
- Section [15. Command conversion](./conversion.md) has been rewritten
- Executes native is now present in the command registration page
- Section [8.3. Argument suggestions with tooltips](./tooltips.md) now mentions the `IStringTooltip` class

### Documentation changes 4.2 \\(\rightarrow\\) 4.3:

- Improve the documentation for [2. Configuration for server owners](./config.md) by using simple YAML lists (using the `-` symbol) and update the command conversion syntax for all commands using the `~` syntax
- Adds the command sender priority to [6.1. Normal command executors](./normalexecutors.md#multiple-command-executor-implementations)
- Adds new method and example for converting specific commands internally in [13. Command conversion](./conversion.md#only-specific-commands)
- Adds two sneaky little buttons in the main title toolbar at the top of the page

### Documentation changes 4.1 \\(\rightarrow\\) 4.2:

- Adds a warning about using redirected commands for suggestions that depend on previous arguments in [7.1. Argument suggestions](./argumentsuggestions.md#suggestions-depending-on-previous-arguments)
- Adds a new section [6.3. Native commandsenders](./native.md)
- Update documentation for [6.1. Normal command executors](normalexecutors.md#restricting-who-can-run-your-command) to include the `.executesNative()` method for native command senders

### Documentation changes 4.0 \\(\rightarrow\\) 4.1:

- Adds a new section [7.3. Argument suggestions with tooltips](./tooltips.md)
- Adds documentation for the `MultiLiteralArgument` in section [8.11.2. Multi literal arguments](./multilitargs.md)
- Adds a new section [4. Shading the CommandAPI into your plugins](./shading.md)
- Update documentation for [14. Brigadier + CommandAPI](./brigadier.md) with new (overloaded) function `argBuildOf`
- Update [Afterword](./afterword.md)

### Documentation changes 3.4 \\(\rightarrow\\) 4.0:

- Update the maven and gradle documentation to state that it is `provided` and `compileOnly`
- The project has been renamed from the "1.13 Command API" to simply the "CommandAPI". This has changed a few things, such as various links. See the section [Upgrading guide](./upgrading.md) to view the relevant changes with regards to maven.
- Updated [3. Setting up your development environment](./quickstart.md) to include new Maven repository links
- Fixed stronkage with Java versions - there's now no random warning boxes about incompatibility with Java 12!
- Arguments now include pictures that showcase how they work!
- Reorganised the sections - arguments is now split up into two sections: [6. Arguments (in general)](./arguments.md) and [7. Argument types](./argumenttypes.md)
- Adds documentation for [6.2. Safe argument suggestions](./safeargumentsuggestions.md)
- Adds documentation for [7.8.3. BlockState arguments](./blockstatearguments.md)
- Adds documentation for new arguments:
  - `UUIDArgument`: [7.8.14. UUID arguments](./uuidargs.md)
  - `BlockPredicateArgument`: [7.9.1. Block predicate arguments](./blockpredicateargs.md)
  - `ItemStackPredicateArgument`: [7.9.2. ItemStack predicate arguments](./itemstackpredicateargs.md)
- Adds page [Incompatible version information](./incompatibleversions.md) outlining what parts of the CommandAPI are incompatible with what versions of Minecraft
- Adds `getCommands()` documentation to the [8.2. The FunctionWrapper class](./functionwrapper.md#getcommands) page
- Adds page [13. Brigadier + CommandAPI](./brigadier.md) which shows how the CommandAPI can be used with Brigadier side-by-side
- Adds page [10. Requirements](./requirements.md) for the requirements feature to help restrict commands
- Adds page [14. Predicate tips](./predicatetips.md) with information on how to reduce the amount of repeated code when using requirements
- Update [Afterword](./afterword.md)

### Documentation changes 3.3 \\(\rightarrow\\) 3.4:

- Moves configuration for server owners to a new section [2. Configuration for server owners](./config.md). This has the side-effect of also re-numbering all of the sections on the left. Sorry!
- Adds server owner documentation for the CommandAPI's config command conversion system in section [2. Configuration for server owners](./config.md#command-conversion)
- Update the conversion page [10. Command conversion](./conversion.md) so it should be much easier to follow and understand
- Changed the list of Java versions that the CommandAPI is compatible with in the [Troubleshooting](./troubleshooting.md#commandapi-errors-when-reloading-datapacks) section

### Documentation changes 3.1 \\(\rightarrow\\) 3.3:

- Adds information on how functions are loaded in 1.16+ in section [6. Functions & Tags](./functions.md#functions-in-116)
- Added function error messages to section [Troubleshooting](./troubleshooting.md#server-errors-when-loading-datapacks-in-116)

### Documentation changes 3.0 \\(\rightarrow\\) 3.1:

- Adds new section [5.1 Argument suggestions](./argumentsuggestions.md) to cover how to override suggestions - Having it all in section _5. Arguments_ was a bit too content-heavy
- Adds documentation for the new `.overrideSuggestions()` method in section [5.1 Argument suggestions](./argumentsuggestions.md#suggestions-depending-on-previous-arguments)
- Simplified the description of the documentation updates
- Changed the artifact ID for the dependency of the CommandAPI. Instead of being `commandapi`, it is now `commandapi-core`. You can view the changes in section [2 Setting up your development environment](./quickstart.md)
- Changed the repository information for gradle in section [2 Setting up your development environment](./quickstart.md). You now have to include the NBTAPI repository because gradle can't automatically detect this for some reason. Kinda stupid tbh.
- Adds a section on using multiple or optional arguments in section [5 Arguments](./arguments.md#optionaldifferent-arguments)

### Documentation changes 2.1 \\(\rightarrow\\) 3.0:

> **Developer's Note:**
>
> Lots of changes occurred in version 3.0. I highly recommend reading the [Upgrading guide](./upgrading.md) section which covers the changes in more detail and how to update your plugin for this version.

- Sections on the left have been tidied up and should be more "approachable"
- Installation section ([1. Installation for server owners](./installation.md)) now includes information about additional dependencies
- Dependency section ([2. Setting up your development environment](./quickstart.md)) updated to use the new dependency Group ID
- Command registration section ([3. Command registration](./commandregistration.md)) updated to reflect new API changes
- Command execution section ([4. Command Executors](./commandexecutors.md)) updated to reflect new API changes
- Arguments section ([5. Arguments](./arguments.md)) completely rewritten to reflect new API changes. Adds more detailed examples for each argument
- Function arguments section ([6.3 Function Arguments](./functionarguments.md)) updated to reflect new API changes
- Permissions section ([7. Permissions](./permissions.md)) updated to reflect new API changes
- Aliases section ([8. Aliases](./aliases.md)) updated to reflect new API changes
- Command conversion section ([9. Command conversion](./conversion.md)) rewrite example to be more detailed