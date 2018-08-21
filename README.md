# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13

## Purpose
This project provides an API to help Bukkit/Spigot developers use the new Minecraft 1.13 command UI.

![commandUI](https://i.imgur.com/aTJa77G.gif "commandUI")

## Basic principles (and changes between this API compared to normal command registration)
- Commands are registered using the command registration system for default Minecraft commands. These are sent to the player in a [special packet](https://wiki.vg/Command_Data).
- Commands registered using this Command API do not need to be stated in the plugin.yml (neither do they need a registered CommandExecutor)
- This may be less compatible with plugins which lookup commands from other plugins
- Arguments can now be represented as [primitive data types](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) as opposed to Strings
- Arguments require a description to identify them

## Usage

Check out the [documentation for v1.1](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.1%20Documentation.md)