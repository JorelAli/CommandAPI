# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13

## Purpose
This project provides an API to help Bukkit/Spigot developers use the new Minecraft 1.13 command UI. (Read about its creation in my [blog post](https://jorelali.github.io/blog/Creating-the-CommandAPI/)!)

![commandUI](https://i.imgur.com/aTJa77G.gif "commandUI")

## Basic principles (and changes between this API compared to normal command registration)
- Commands are registered using the command registration system for default Minecraft commands. These are sent to the player in a [special packet](https://wiki.vg/Command_Data).
- Commands registered using this Command API do not need to be stated in the plugin.yml (neither do they need a registered CommandExecutor)
- This may be less compatible with plugins which lookup commands from other plugins
  For example, say we make a plugin with this api, called _MyCustomPlugin_. Say another plugin, _PluginLookup_ tries to find the commands registered under _MyCustomPlugin_ (either by viewing the plugin.yml or commandMap via reflection), it would not return any results.
- Arguments are represented as their respective data types as opposed to Strings
  ```java
  //Instead of 
  onCommand(CommandSender sender, Command command, String label, String[] args) {
  	try {
		int i = Integer.parseInt(args[0]);
		//Do something with this number
	catch(NumberFormatException e) {
		//Do something with the fact this isn't a number...
	}
  }
  
  //You can now do
  CommandAPI.getInstance().register("mycommand", arguments, (sender, args) -> {
	int i = (int) args[0];
    //Do something with this number
  });
  ```
- Arguments require a description to identify them

## Downloads & Documentation (includes usage for server owners)

| Version | Direct download                                              | Documentation                                                |
| ------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1.0     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.0/CommandAPI.jar) | [Version 1.0 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.0%20Documentation.md) |
| 1.1     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.1/CommandAPI.jar) | [Version 1.1 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.1%20Documentation.md) |
| 1.2     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.2/CommandAPI.jar) | [Version 1.2 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.2%20Documentation.md) |
| 1.3     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.3/CommandAPI.jar) | [Version 1.3 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.3%20Documentation.md) |
| 1.4     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.4/CommandAPI.jar) | [Version 1.4 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.4%20Documentation.md) |
| 1.5     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.5/CommandAPI.jar) | [Version 1.5 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.5%20Documentation.md) |
| 1.6     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.6/CommandAPI.jar) | [Version 1.6 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.6%20Documentation.md) |
| 1.7.1     | [CommandAPI.jar](https://github.com/JorelAli/1.13-Command-API/releases/download/v1.7.1/CommandAPI.jar) | [Version 1.7 & 1.7.1 documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.7%20Documentation.md) |

## Changelog

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
