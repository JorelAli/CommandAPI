# Scoreboard arguments

The CommandAPI uses two classes to provide information about a scoreboard:

- The `ScoreHolderArgument` class represents **score holder** - a player's name or an entity's UUID that has scores in an objective. This is described in more detail [on the Minecraft Wiki](https://minecraft.wiki/w/Scoreboard#Objectives).
- The `ScoreboardSlotArgument` class represents a **display slot** (sidebar, list or belowName) as well as the team color if the display is the sidebar. This is described in more detail [on the Minecraft Wiki](https://minecraft.wiki/w/Scoreboard#Display_slots).

-----

## Score holder argument

The score holder argument can accept either a single entity or a collection of multiple entities. In order to specify which one to use, you must use the `ScoreHolderArgument.Single` or `ScoreHolderArgument.Multiple` constructor respectively:

```java
new ScoreHolderArgument.Single(nodeName);
new ScoreHolderArgument.Multiple(nodeName);
```

Depending on which constructor is used, the cast type changes. If you use `ScoreHolderArgument.Single`, the argument must be casted to a `String`. Otherwise, if you use `ScoreHolderArgument.Multiple`, the argument must be casted to a `Collection<String>`.

<div class="example">

### Example - Rewarding players with scoreboard objectives

Say we want to reward all players that fit a certain criteria. We want a command with the following syntax:

```mccmd
/reward <players>
```

Since we could have multiple players that fit a certain criterion, we want to use `ScoreHolderArgument.Multiple` constructor.

To give this example a bit more context, let's say we want to reward all players that have died less than 10 times in the server. To do this, we will use the following command:

```mccmd
/reward @e[type=player,scores={deaths=..9}]
```

Note how we use `..9` to represent 9 or less deaths (since ranges are inclusive). Also note how we restrict our input to players via the command using `type=player`. We can now implement our command:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentScoreboards1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentScoreboards1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentScoreboards1}}
```

</div>

</div>

> **Developer's Note:**
>
> In the example above, we have our user use the `@e[type=player]` entity selector to restrict the `Collection<String>` so it only returns player names, which allows us to use `Bukkit.getPlayer(playerName)`. In practice, we cannot guarantee that such a selector will be used, so we could update the code to accept both entities and players. For example, we can differentiate between players and entities by using the `UUID.fromString(String)` method:
>
> ```java
> Collection<String> entitiesAndPlayers = (Collection<String>) args.get(0);
> for(String str : entitiesAndPlayers) {
>     try {
>         UUID uuid = UUID.fromString(str);
>         // Is a UUID, so it must by an entity
>         Bukkit.getEntity(uuid);
>     } catch(IllegalArgumentException exception) {
>         // Not a UUID, so it must be a player name
>         Bukkit.getPlayer(str); 
>     }
> }
> ```

-----

## Scoreboard slot argument

![A scoreboardslotargument showing a list of suggestions of valid Minecraft scoreboard slot positions](./images/arguments/scoreboardslot.png)

The `ScoreboardSlotArgument` represents where scoreboard information is displayed. Since the Bukkit scoreboard `DisplaySlot` is not able to represent the case where team colors are provided, the CommandAPI uses the `ScoreboardSlot` wrapper class as the representation of the `ScoreboardSlotArgument`.

### `ScoreboardSlot` wrapper

The `ScoreboardSlot` wrapper class has 3 main methods:

```java
enum ScoreboardSlot {

    PLAYER_LIST,
    SIDEBAR, // Unused, use the other SIDEBAR_TEAM_### values below
    BELOW_NAME,
    SIDEBAR_TEAM_BLACK,
    SIDEBAR_TEAM_DARK_BLUE,
    SIDEBAR_TEAM_DARK_GREEN,
    SIDEBAR_TEAM_DARK_AQUA,
    SIDEBAR_TEAM_DARK_RED,
    SIDEBAR_TEAM_DARK_PURPLE,
    SIDEBAR_TEAM_GOLD,
    SIDEBAR_TEAM_GRAY,
    SIDEBAR_TEAM_DARK_GRAY,
    SIDEBAR_TEAM_BLUE,
    SIDEBAR_TEAM_GREEN,
    SIDEBAR_TEAM_AQUA,
    SIDEBAR_TEAM_RED,
    SIDEBAR_TEAM_LIGHT_PURPLE,
    SIDEBAR_TEAM_YELLOW,
    SIDEBAR_TEAM_WHITE;

    public DisplaySlot getDisplaySlot();
    public ChatColor getTeamColor();
    public boolean hasTeamColor();

    public NamespacedKey getKey();
}
```

The `getDisplaySlot()` method returns the display slot that was chosen. Sidebar scoreboard colors can be accessed via `ScoreboardSlot.SIDEBAR_TEAM_###`. You can also retrieve the color using the `getTeamColor()` method.

<div class="example">

### Example - Clearing objectives in a scoreboard slot

Say we want to clear all objectives in a specific scoreboard slot. In this example, we will use the main server scoreboard, which is accessed using `Bukkit.getScoreboardManager.getMainScoreboard()`. We want a command with the following syntax:

```mccmd
/clearobjectives <slot>
```

We implement this simply by using the `ScoreboardSlotArgument` as our argument, and then we can clear the slot using the scoreboard `clearSlot(DisplaySlot)` method.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:argumentScoreboards2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:argumentScoreboards2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:argumentScoreboards2}}
```

</div>

</div>
