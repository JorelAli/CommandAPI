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

* Commands do not need to be declared in the `plugin.yml` file
* Commands are not "linked" to a certain plugin. In other words, you cannot look up which commands are registered by which plugin.

-----

## How this documentation works

This documentation is split into the major sections that build up the CommandAPI. It's been designed in such a way that it should be easy to find exactly what you want to help you get started with the CommandAPI, and how to make effective use of it. Each step of the way, the documentation will include examples which showcase how to use the CommandAPI. 

You can use the side bar on the left to access the various sections of the documentation and can change the theme to your liking using the paintbrush icon in the top left corner. 

Using the search icon in the top left corner, you can search for anything in this entire documentation. For example, typing "Example" will show a list of examples which are included throughout the documentation.

-----

## Documentation changelog

Here's the list of changes to the documentation between each update. You can view the current documentation version at the top of this page.

### Documentation changes 3.4 \\(\rightarrow\\) 4.0:

- Arguments now include pictures that showcase how they work!
- Adds documentation for [6.2. Safe argument suggestions](./safeargumentsuggestions.md)
- Adds documentation for [6.10.3. BlockState arguments](./blockstatearguments.md)
- Adds documentation for new arguments:
  - `UUIDArgument`: [6.10.14. UUID arguments](./uuidargs.md)
  - `BlockPredicateArgument`: [6.11.1. Block predicate arguments](./blockpredicateargs.md)
  - `ItemStackPredicateArgument`: [6.11.2. ItemStack predicate arguments](./itemstackpredicateargs.md)
- Adds page [Incompatible version information](./incompatibleversions.md) outlining what parts of the CommandAPI are incompatible with what versions of Minecraft
- Adds `getCommands()` documentation to the [7.2. The FunctionWrapper class](./functionwrapper.md#getcommands) page
- Adds page [11. Brigadier + CommandAPI](./brigadier.md) which shows how the CommandAPI can be used with Brigadier side-by-side

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