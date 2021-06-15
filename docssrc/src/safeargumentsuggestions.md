# Safely typed argument suggestions

So far, we've covered how to replace suggestions using the `replaceSuggestions()` method. The issue with using Strings for suggestion listings is that they are prone to errors - it is possible to suggest something which is not actually a valid argument, which makes that suggestion unusable. As a result, some arguments include the `replaceWithSafeSuggestions()`, which provides type-safety checks for argument suggestions, as well as automatic "Bukkit-to-suggestion" conversion.

The whole point of the safe argument suggestions method is that parameters entered in this method are **guaranteed** to work.

The use of the safe replace suggestions function is basically the same as `replaceSuggestions()` from the previous section, except instead of returning a `String[]`, you now return a `T[]`, where `T` is the class corresponding to the argument. This is described in more detail in the table below.

```java
Argument replaceWithSafeSuggestions(Function<SuggestionInfo, S[]> suggestions);
```

-----

## Supported arguments

Not all arguments support safe suggestions. This is mostly due to implementation constraints or inadequate support by the Bukkit API.

The list of supported arguments are displayed in the following table. The parameter `T` (shown in the method signatures above) are also provided for each argument. This parameter is the same as the cast argument described in [Argument Casting](./arguments.md#argument-casting), except for a few exceptions which are outlined in **bold**.

|                                                     Argument | Class (T)                                      |
| -----------------------------------------------------------: | :--------------------------------------------- |
|            [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`           |
|                               [`AxisArgument`](./axisarg.md) | `java.util.EnumSet<org.bukkit.Axis>`           |
|                        [`BiomeArgument`](./biomeargument.md) | `org.bukkit.block.Biome`                       |
| [`BooleanArgument`](./primitivearguments.md#boolean-arguments) | **`Boolean`**                                  |
| [`ChatColorArgument`](./chatarguments.md#chat-color-argument) | `org.bukkit.ChatColor`                         |
| [`DoubleArgument`](./primitivearguments.md#numerical-arguments) | **`Double`**                                   |
|            [`EnchantmentArgument`](./enchantmentargument.md) | `org.bukkit.enchantments.Enchantment`          |
| [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) | `org.bukkit.entity.EntityType`                 |
|                [`EnvironmentArgument`](./environmentargs.md) | `org.bukkit.World.Environment`                 |
| [`FloatArgument`](./primitivearguments.md#numerical-arguments) | **`Float`**                                    |
| [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`     |
|                   [`FunctionArgument`](./functionwrapper.md) | **`org.bukkit.NamespacedKey`**                 |
| [`GreedyStringArgument`](./stringarguments.md#greedy-string-argument) | `String`                                       |
| [`IntegerArgument`](./primitivearguments.md#numerical-arguments) | **`Integer`**                                  |
| [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`   |
|               [`ItemStackArgument`](./itemstackarguments.md) | `org.bukkit.inventory.ItemStack`               |
| [`Location2DArgument`](./locationargument.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`     |
| [`LocationArgument`](./locationargument.md#location-3d-space) | `org.bukkit.Location`                          |
| [`LongArgument`](./primitivearguments.md#numerical-arguments) | **`Long`**                                     |
|                [`LootTableArgument`](./loottableargument.md) | `org.bukkit.loot.LootTable`                    |
|       [`MathOperationArgument`](./mathoperationarguments.md) | `dev.jorel.commandapi.wrappers.MathOperation`  |
|                   [`NBTCompoundArgument`](./nbtarguments.md) | `de.tr7zw.nbtapi.NBTContainer`                 |
| [`ObjectiveArgument`](./objectivearguments.md#objective-argument) | **`org.bukkit.scoreboard.Objective`**          |
| [`OfflinePlayerArgument`](./entityarguments.md#player-argument) | `org.bukkit.OfflinePlayer`                     |
|                 [`ParticleArgument`](./particlearguments.md) | `org.bukkit.Particle`                          |
|     [`PlayerArgument`](./entityarguments.md#player-argument) | `org.bukkit.entity.Player`                     |
|               [`PotionEffectArgument`](./potionarguments.md) | `org.bukkit.potion.PotionEffectType`           |
|                      [`RecipeArgument`](./recipeargument.md) | `org.bukkit.inventory.Recipe`                  |
|                      [`RotationArgument`](./rotationargs.md) | `dev.jorel.commandapi.wrappers.Rotation`       |
| [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot` |
|                        [`SoundArgument`](./soundargument.md) | `org.bukkit.Sound`                             |
|                         [`TeamArgument`](./teamarguments.md) | **`org.bukkit.scoreboard.Team`**               |
|                              [`TimeArgument`](./timeargs.md) | **`dev.jorel.commandapi.wrappers.Time`**       |

-----

## Safe time arguments

While most of the arguments are fairly straight forward, I'd like to bring your attention to the `TimeArgument`'s safe suggestions function. This uses `dev.jorel.commandapi.wrappers.Time` as the class for `T` to ensure type-safety. The `Time` class has three static methods:

```java
Time ticks(int ticks);
Time days(int days);
Time seconds(int seconds);
```

These create representations of ticks (e.g. `40t`), days (e.g. `2d`) and seconds (e.g. `60s`) respectively.

-----

## Safe function arguments

Although all safe arguments are indeed "type-safe", the function argument uses a `NamespacedKey` which cannot be checked fully at compile time. As a result, this is argument should be used with caution - providing a `NamespacedKey` suggestion that does not exist when the server is running will cause that command to fail if that suggestion is used.

-----

## Safe scoreboard slot arguments

Scoreboard slots now include two new static methods so they can be used with safe arguments:

```java
ScoreboardSlot of(DisplaySlot slot);
ScoreboardSlot ofTeamColor(ChatColor color);
```

This allows you to create `ScoreboardSlot` instances which can be used with the safe replace suggestions method.

-----

## Examples

While this should be fairly straight forward, here's a few examples of how this can be used in practice:

<div class="example">

### Example - Safe recipe arguments

Say we have a plugin that registers custom items which can be crafted. In this example, we use an "emerald sword" with a custom crafting recipe. Now say that we want to have a command that gives the player the item from our declared recipes, which will have the following syntax:

```mccmd
/giverecipe <recipe>
```

To do this, we first register our custom items:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeRecipeArguments}}
```

Once we've done that, we can now include them in our command registration. To do this, we use `replaceWithSafeSuggestions(recipes)` and then register our command as normal:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeRecipeArguments_2}}
```

</div>

<div class="example">

### Example - Safe /spawnmob suggestions

Say we have a command to spawn mobs:

```mccmd
/spawnmob <mob>
```

Now say that we don't want non-op players to spawn bosses. To do this, we'll create a `List<EntityType>` which is the list of all mobs that non-ops are allowed to spawn:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeMobSpawnArguments}}
```

We then use our safe arguments to return an `EntityType[]` as the list of values that are suggested to the player. In this example, we use the `sender()` method to determine if the sender has permissions to view the suggestions:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeMobSpawnArguments_2}}
```

Now we register our command as normal:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafeMobSpawnArguments_3}}
```

</div>

<div class="example">

### Example - Removing a potion effect from a player

Say we wanted to remove a potion effect from a player. To do this, we'll use the following command syntax:

```mccmd
/removeeffect <player> <potioneffect>
```

Now, we don't want to remove a potion effect that already exists on a player, so instead we'll use the safe arguments to find a list of potion effects on the target player and then only suggest those potion effects. To do this, we'll use the `previousArguments()` method, as it allows us to access the previously defined `<player>` argument.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafePotionArguments}}
```

And then we can register our command as normal:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:SafePotionArguments_2}}
```

</div>

