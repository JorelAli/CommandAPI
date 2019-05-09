# Literal arguments

Literal arguments are used to represent "forced options" for a command. For instance, take Minecraft's `/gamemode` command. The syntax consists of the following:

```
/gamemode <mode> [player]
```

It consists of a gamemode, followed by an optional player argument. The list of gamemodes are as follows:

```
/gamemode survival 
/gamemode creative
/gamemode adventure
/gamemode spectator
```

Unlike regular commands (as those implemented by Bukkit for example), these four options are "hardcoded" - they're not "suggestions".
