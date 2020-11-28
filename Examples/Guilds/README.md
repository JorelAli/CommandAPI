# Guilds

Guilds is a simple plugin that uses the CommandAPI to create a concept of guilds in Minecraft.

This example showcases the following features of the CommandAPI:

- Registering commands
- Using `.withArguments()` multiple times for command declaration
- Using custom requirements
- A maven project using the `commandapi-core` library

## Plugin description

Guilds simply connects players together in a group called a guild. If you are not in a guild, you are only able to create a guild. If you are in a guild, you are able to list information about your guild, add and remove members and leave your guild.

For this example plugin, all it does it allow you to create a group and set a tag (prefix) to your name in chat.

## Command description

If you are not in a guild, you can run these commands:

```
/guild - Displays help
/guild create <name> <tag> <tagColor> - Create a guild
```

Once you are in a guild, you are then able to run the following extra commands. If you are not in a guild, these commands will not work (the client doesn't recognize them as commands, so they won't be listed to the user either!):

```
/guild info - List current guild information
/guild add <player> - Adds a player to your guild
/guild kick <player> - Kick a player from your guild
/guild leave - Leave your current guild
```

## Compiling

To compile this plugin, run `mvn clean package` in the same folder as this `pom.xml` file. The plugin `commandapi-guilds-0.0.1-SNAPSHOT.jar` file will be built in the `target/` folder.