# Chat arguments

The CommandAPI provides a number of ways to interact with chat formatting in Minecraft. These are the following:

- **ChatColor**: The color of text rendered in Minecraft
- **Chat**: Text which is said in chat. This also includes entity selectors such as `@a` and `@r`
- **ChatComponent**: Minecraft's [Raw JSON text format](https://minecraft.gamepedia.com/Raw_JSON_text_format)

The CommandAPI implements **Chat** and **ChatComponent** in two separate ways: [Spigot-compatible](./argument_chat_spigot.md) and [Adventure-compatible](./argument_chat_adventure.md). The differences between these and how to use them are described in their own relevant pages. To use Minecraft 1.19's chat preview feature, information on that can be found in [Chat preview](./chatpreview.md).

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

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentChats1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentChats1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentChats1}}
```

</div>

</div>
