# 1.13-Command-API
An API to use the new command UI introduced in Minecraft 1.13

## Purpose
This project provides an API to help Bukkit/Spigot developers use the new Minecraft 1.13 command UI.

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

## Usage
Click the link below for the relevant documentation:
* CommandAPI Version 1.0 [documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.0%20Documentation.md)
* CommandAPI Version 1.1 [documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.1%20Documentation.md)
* CommandAPI Version 1.2 [documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.2%20Documentation.md)
* CommandAPI Version 1.3 [documentation](https://github.com/JorelAli/1.13-Command-API/blob/master/v1.3%20Documentation.md)
