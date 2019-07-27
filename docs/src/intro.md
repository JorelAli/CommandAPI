# Introduction

Welcome to the documentation for the CommandAPI. The CommandAPI lets you create vanilla Minecraft commands which utilize the new command features which were implemented in Minecraft 1.13, including but not limited to:

* Having commands compatible with the vanilla `/execute` command
* Having commands which can be run using Minecraft functions
* Having better auto-completion and suggestions
* Having command type checks before execution (e.g. ensuring a number is within a certain range)

## How the CommandAPI works

> **Developer's Note:**
>
> This is a pretty important section, I would recommend reading before implementing the CommandAPI in your own projects. This section tells you about setup which is not stated anywhere else in the documentation. Think of it as the "knowledge you should know before using this API".

The CommandAPI does not follow the "standard" method of registering commands. In other words, commands which are registered with the CommandAPI will be registered as pure vanilla Minecraft commands as opposed to Bukkit or Spigot commands. This means that the following implications exist:

* Commands do not need to be declared in the `plugin.yml` file
* Commands are not "linked" to a certain plugin. In other words, you cannot look up which commands are registered by which plugin.

## How this documentation works

This documentation is split into the major sections that build up the CommandAPI. It's been designed in such a way that it should be easy to find exactly what you want to help you get started with the CommandAPI, how it's structured and how to make effective use of it. Each step of the way, the documentation will include examples which showcase how to use the CommandAPI. 

You can use the side bar on the left to access the various sections of the documentation and can change the theme to your liking using the paintbrush icon in the top left corner. 

Using the search icon in the top left corner, typing "Example" will show a list of examples which are included throughout the documentation.

## Documentation changelog

Whenever a new version of the CommandAPI comes out, the version number changes _(as you'd expect)_. In the same manner, if any changes to the documentation were made, the documentation version number changes. Ensure you keep up to date on the latest changes to the documentation (You can view the documentation version at the top of the page) when new versions of the CommandAPI are released. This changelog below gives a brief overview of the changes to pages that were made between each version of the documentation, as only the latest version of the documentation is hosted online.

- **2.0 → 2.1**
  - **[Click here](./commandregistration.html#command-registration)** - Include information about tooltips
  - **[Click here](./technicalargs.html)** - Adds information on technical arguments
  - **[Click here](./quickstart.html#using-maven-recommended)** - Improve documentation for adding dependencies and repositories to the `pom.xml` file
- **1.8 → 2.0**
  - **[Click here](./arguments.html#arguments-with-overrideable-suggestions)** - Deprecated `SuggestedStringArgument`, use `.overrideSuggestions()` instead
  - **[Click here](./customarguments.html)** - Adds a new argument, the `CustomArgument` 
  - **[Click here](./dynsugargs.html)** - The `DynamicSuggestedArgument` now has access to the `CommandSender` object
