# Chat arguments

The CommandAPI provides a number of ways to interact with chat formatting in Minecraft. These are the following:

- **ChatColor**: The color of text rendered in Minecraft
- **Chat**: Text which is said in chat. This also includes entity selectors such as `@a` and `@r`
- **ChatComponent**: Minecraft's [Raw JSON text format](https://minecraft.gamepedia.com/Raw_JSON_text_format)

The CommandAPI implements **Chat** and **ChatComponent** in two separate ways: [Spigot-compatible](./spigotchatarguments.md) and [Adventure-compatible](./adventurechatarguments.md). The differences between these and how to use them are described in their own relevant pages.

-----

## Chat color argument

![Chatcolor argument in-game, displaying a list of Minecraft chat colors](./images/arguments/chatcolor.png)

The `ChatColorArgument` class is used to represent a given chat color (e.g. red or green)

<div class="example">

### Example - Username color changing plugin

Say we want to create a plugin to change the color of a player's username. We want to create a command of the following form:

```mccmd
/namecolor <chatcolor>
```

We then use the `ChatColorArgument` to change the player's name color:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:chatcolorarguments}}
```

</div>