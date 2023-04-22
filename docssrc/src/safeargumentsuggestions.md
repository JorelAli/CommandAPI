# Safely typed argument suggestions

So far, we've covered how to replace suggestions using the `replaceSuggestions()` method. The issue with using strings for suggestion listings is that they are prone to errors - it is possible to suggest something which is not actually a valid argument, which makes that suggestion unusable. As a result, some arguments include the `replaceSafeSuggestions()`, which provides type-safety checks for argument suggestions, as well as automatic "Bukkit-to-suggestion" conversion.

The whole point of the safe argument suggestions method is that parameters entered in this method are **guaranteed** to work.

The use of the safe replace suggestions function is the same as `replaceSuggestions()` from the previous section, except instead of returning a `String[]`, you now return a `T[]`, where `T` is the class corresponding to the argument. This is described in more detail in the table below.

```java
Argument replaceSafeSuggestions(SafeSuggestions<T> suggestions);
Argument includeSafeSuggestions(SafeSuggestions<T> suggestions);
```

-----

## The `SafeSuggestions` interface

Similar to the [`ArgumentSuggestions` interface](./argumentsuggestions.md#the-argumentsuggestions-interface), safe suggestions use the `SafeSuggestions` interface which is a functional interface that takes in a mapping function from an Object to a String and returns some `ArgumentSuggestions` which represent the argument's suggestions. Again, this is typically implemented for anyone that wants to use a more powerful suggestion system.

As with `ArgumentSuggestions`, the CommandAPI provides some methods to generate safe suggestions:

```java
SafeSuggestions<T> suggest(T... suggestions);
SafeSuggestions<T> suggest(Function<SuggestionInfo, T[]> suggestions);
SafeSuggestions<T> suggestAsync(Function<SuggestionInfo, CompletableFuture<T[]>> suggestions);

SafeSuggestions<T> tooltips(Tooltip<T>... suggestions);
SafeSuggestions<T> tooltips(Function<SuggestionInfo, Tooltip<T>[]> suggestions);
SafeSuggestions<T> tooltipsAsync(Function<SuggestionInfo, CompletableFuture<Tooltip<T>[]>> suggestions);
```

-----

## Supported arguments

Not all arguments support safe suggestions. This is mostly due to implementation constraints or inadequate support by the Bukkit API.

The list of supported arguments are displayed in the following table. The parameter `T` (shown in the method signatures above) are also provided for each argument. This parameter is the same as the cast argument described in [Argument Casting](./arguments.md#argument-casting), except for a few exceptions which are outlined in **bold**.

|                                                                         Argument | Class (T)                                      |
|---------------------------------------------------------------------------------:|:-----------------------------------------------|
|                                [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`           |
|                                             [`AxisArgument`](./argument_axis.md) | `java.util.EnumSet<org.bukkit.Axis>`           |
|                                           [`BiomeArgument`](./argument_biome.md) | `org.bukkit.block.Biome`                       |
|                  [`BooleanArgument`](./argument_primitives.md#boolean-arguments) | **`Boolean`**                                  |
|                   [`ChatColorArgument`](./argument_chats.md#chat-color-argument) | `org.bukkit.ChatColor`                         |
|                 [`DoubleArgument`](./argument_primitives.md#numerical-arguments) | **`Double`**                                   |
|                               [`EnchantmentArgument`](./argument_enchantment.md) | `org.bukkit.enchantments.Enchantment`          |
|              [`EntityTypeArgument`](./argument_entities.md#entity-type-argument) | `org.bukkit.entity.EntityType`                 |
|                  [`FloatArgument`](./argument_primitives.md#numerical-arguments) | **`Float`**                                    |
|   [`FloatRangeArgument`](./argument_range.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`     |
|                                       [`FunctionArgument`](./functionwrapper.md) | **`org.bukkit.NamespacedKey`**                 |
|           [`GreedyStringArgument`](./argument_strings.md#greedy-string-argument) | `String`                                       |
|                [`IntegerArgument`](./argument_primitives.md#numerical-arguments) | **`Integer`**                                  |
| [`IntegerRangeArgument`](./argument_range.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`   |
|                                   [`ItemStackArgument`](./argument_itemstack.md) | `org.bukkit.inventory.ItemStack`               |
|                [`Location2DArgument`](./argument_locations.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`     |
|                  [`LocationArgument`](./argument_locations.md#location-3d-space) | `org.bukkit.Location`                          |
|                   [`LongArgument`](./argument_primitives.md#numerical-arguments) | **`Long`**                                     |
|                                   [`LootTableArgument`](./argument_loottable.md) | `org.bukkit.loot.LootTable`                    |
|                           [`MathOperationArgument`](./argument_mathoperation.md) | `dev.jorel.commandapi.wrappers.MathOperation`  |
|                                       [`NBTCompoundArgument`](./argument_nbt.md) | `de.tr7zw.nbtapi.NBTContainer`                 |
|               [`ObjectiveArgument`](./argument_objectives.md#objective-argument) | **`org.bukkit.scoreboard.Objective`**          |
|                [`OfflinePlayerArgument`](./argument_entities.md#player-argument) | `org.bukkit.OfflinePlayer`                     |
|                                     [`ParticleArgument`](./argument_particle.md) | `org.bukkit.Particle`                          |
|                       [`PlayerArgument`](./argument_entities.md#player-argument) | `org.bukkit.entity.Player`                     |
|                                   [`PotionEffectArgument`](./argument_potion.md) | `org.bukkit.potion.PotionEffectType`           |
|                                         [`RecipeArgument`](./argument_recipe.md) | `org.bukkit.inventory.Recipe`                  |
|                                     [`RotationArgument`](./argument_rotation.md) | `dev.jorel.commandapi.wrappers.Rotation`       |
|   [`ScoreboardSlotArgument`](./argument_scoreboards.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot` |
|                                           [`SoundArgument`](./argument_sound.md) | `org.bukkit.Sound`                             |
|                                             [`TeamArgument`](./argument_team.md) | **`org.bukkit.scoreboard.Team`**               |
|                                             [`TimeArgument`](./argument_time.md) | **`dev.jorel.commandapi.wrappers.Time`**       |

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

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions1}}
```

</div>

Once we've done that, we can now include them in our command registration. To do this, we use `replaceSafeSuggestions(recipes)` and then register our command as normal:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions2}}
```

</div>

</div>

<div class="example">

### Example - Safe /spawnmob suggestions

Say we have a command to spawn mobs:

```mccmd
/spawnmob <mob>
```

Now say that we don't want non-op players to spawn bosses. To do this, we'll create a `List<EntityType>` which is the list of all mobs that non-ops are allowed to spawn:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions3}}
```

</div>

We then use our safe arguments to return an `EntityType[]` as the list of values that are suggested to the player. In this example, we use the `sender()` method to determine if the sender has permissions to view the suggestions:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions4}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions4}}
```

</div>

Now we register our command as normal:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions5}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions5}}
```

</div>

</div>

<div class="example">

### Example - Removing a potion effect from a player

Say we wanted to remove a potion effect from a player. To do this, we'll use the following command syntax:

```mccmd
/removeeffect <player> <potioneffect>
```

Now, we don't want to remove a potion effect that already exists on a player, so instead we'll use the safe arguments to find a list of potion effects on the target player and then only suggest those potion effects. To do this, we'll use the `previousArguments()` method, as it allows us to access the previously defined `<player>` argument.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions6}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions6}}
```

</div>

And then we can register our command as normal:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:safeArgumentSuggestions7}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:safeArgumentSuggestions7}}
```

</div>

</div>
