# Technical arguments

As of version 2.1, the CommandAPI has additional support for _technical arguments_. These consist of Minecraft "Keyed" arguments _(something that takes the form of `String:String`, such as `minecraft:diamond`)_.

Based on the nature of technical arguments, **these are unlikely to survive Minecraft updates** and support for these will be implemented as soon as possible when new Minecraft updates are released. _(For example, advancement arguments are compatible with Minecraft 1.14, 1.14.1 and 1.14.2, but required a rewrite to enable support for 1.14.3+)_

> **Developer's Note:**
>
> As with any future bug, if you notice that a technical argument does not work with a specific version of Minecraft _(1.13.2+)_, I'd be grateful if you could submit a bug report on [the CommandAPI's issues page](https://github.com/JorelAli/1.13-Command-API/issues/new/choose) and I'll try to fix it as soon as I can!

Technical arguments are also compatible with data packs which allow you to add additional content to the game. In particular, Advancements, Loot Tables and Recipes are supported to allow extra interaction between Bukkit and data packs.