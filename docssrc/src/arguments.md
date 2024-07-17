# Arguments

Arguments in the CommandAPI are registered by using an `Argument[]` or `List<Argument>` object. There are two things you need to keep in mind when creating arguments:

* The order which they will be used
* The type of each argument

By definition of a `List`, the order of the elements inserted into it are preserved, meaning the order you add arguments to the `List` will be the resulting order of which arguments are presented to the user when they run that command.

Adding arguments for registration is simple:

```java
// Create a List
List<Argument> arguments = new ArrayList<>();

// Add an argument with the node "target", which is a PlayerArgument
arguments.add(new PlayerArgument("target"));
```

The String value is the node that is registered into Minecraft's internal command graph. This is name is also used as a prompt that is shown to a player when they are entering the command.

-----

The CommandAPI is very flexible when it comes to registering arguments, and lets you use a number of different methods to suit your preference:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:arguments1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:arguments1}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:arguments1}}
```

</div>

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:arguments2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:arguments2}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:arguments2}}
```

</div>

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:arguments3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:arguments3}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:arguments3}}
```

</div>

-----

## Argument Casting

To access arguments, they have to be casted to the type that the argument represents. The order of the arguments in the [`CommandArguments args`](./commandarguments.md) is the same as the order in which the arguments were declared.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-documentation-code/src/main/java/dev/jorel/commandapi/examples/java/Examples.java:arguments4}}
```

```kotlin,Kotlin
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/Examples.kt:arguments4}}
```

```kotlin,Kotlin_DSL
{{#include ../../commandapi-documentation-code/src/main/kotlin/dev/jorel/commandapi/examples/kotlin/ExamplesKotlinDSL.kt:arguments4}}
```

</div>

The type to cast each argument (declared in the `dev.jorel.commandapi.arguments` package) is listed below:

|                                                                                     Argument class | Data type                                                                                                                                                                                                                                             |
|---------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|                                                             [`AngleArgument`](./argument_angle.md) | `float`                                                                                                                                                                                                                                               |
|                                                  [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`                                                                                                                                                                                                                  |
| [`AdventureChatComponentArgument`](./argument_chat_adventure.md#adventure-chat-component-argument) | `net.kyori.adventure.text.Component`                                                                                                                                                                                                                  |
|                                                               [`AxisArgument`](./argument_axis.md) | `java.util.EnumSet<org.bukkit.Axis>`                                                                                                                                                                                                                  |
|                                                             [`BiomeArgument`](./argument_biome.md) | `org.bukkit.block.Biome`                                                                                                                                                                                                                              |
|                                               [`BiomeArgument.NamespacedKey`](./argument_biome.md) | `org.bukkit.NamespacedKey`                                                                                                                                                                                                                            |
|                                           [`BlockPredicateArgument`](./argument_blockpredicate.md) | `java.util.function.Predicate`<br />&emsp;`<org.bukkit.block.Block>`                                                                                                                                                                                  |
|                                                   [`BlockStateArgument`](./argument_blockstate.md) | `org.bukkit.block.data.BlockData`                                                                                                                                                                                                                     |
|                                    [`BooleanArgument`](./argument_primitives.md#boolean-arguments) | `boolean`                                                                                                                                                                                                                                             |
|                                          [`ChatArgument`](./argument_chat_spigot.md#chat-argument) | The cast type changes depending on the version of the CommandAPI you use:<br/><ul><li>`commandapi-paper-XXX`:<br/>`net.kyori.adventure.text.Component`</li><br/><li>`commandapi-spigot-XXX`:<br/>`net.md_5.bungee.api.chat.BaseComponent[]`</li></ul> |
|                                     [`ChatColorArgument`](./argument_chats.md#chat-color-argument) | The cast type changes depending on the version of the CommandAPI you use:<br/><ul><li>`commandapi-paper-XXX`:<br/>`net.kyori.adventure.text.format.NamedTextFormat`</li><br/><li>`commandapi-spigot-XXX`:<br/>`org.bukkit.ChatColor`</li></ul>        |
|                       [`ChatComponentArgument`](./argument_chat_spigot.md#chat-component-argument) | The cast type changes depending on the version of the CommandAPI you use:<br/><ul><li>`commandapi-paper-XXX`:<br/>`net.kyori.adventure.text.Component`</li><br/><li>`commandapi-spigot-XXX`:<br/>`net.md_5.bungee.api.chat.BaseComponent[]`</li></ul> |
|                                                         [`CommandArgument`](./argument_command.md) | `dev.jorel.commandapi.wrappers.CommandResult`                                                                                                                                                                                                         |
|                                                     [`CustomArgument<T, B>`](./argument_custom.md) | `T`                                                                                                                                                                                                                                                   |
|                                   [`DoubleArgument`](./argument_primitives.md#numerical-arguments) | `double`                                                                                                                                                                                                                                              |
|                                                 [`EnchantmentArgument`](./argument_enchantment.md) | `org.bukkit.enchantments.Enchantment`                                                                                                                                                                                                                 |
|           [`EntitySelectorArgument.ManyEntities`](./argument_entities.md#entity-selector-argument) | `Collection<org.bukkit.entity.Entity>`                                                                                                                                                                                                                |
|            [`EntitySelectorArgument.ManyPlayers`](./argument_entities.md#entity-selector-argument) | `Collection<org.bukkit.entity.Player>`                                                                                                                                                                                                                |
|              [`EntitySelectorArgument.OneEntity`](./argument_entities.md#entity-selector-argument) | `org.bukkit.entity.Entity`                                                                                                                                                                                                                            |
|              [`EntitySelectorArgument.OnePlayer`](./argument_entities.md#entity-selector-argument) | `org.bukkit.entity.Player`                                                                                                                                                                                                                            |
|                                [`EntityTypeArgument`](./argument_entities.md#entity-type-argument) | `org.bukkit.entity.EntityType`                                                                                                                                                                                                                        |
|                                    [`FloatArgument`](./argument_primitives.md#numerical-arguments) | `float`                                                                                                                                                                                                                                               |
|                     [`FloatRangeArgument`](./argument_range.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`                                                                                                                                                                                                            |
|                                                         [`FunctionArgument`](./functionwrapper.md) | `dev.jorel.commandapi.wrappers.FunctionWrapper[]`                                                                                                                                                                                                     |
|                             [`GreedyStringArgument`](./argument_strings.md#greedy-string-argument) | `String`                                                                                                                                                                                                                                              |
|                                  [`IntegerArgument`](./argument_primitives.md#numerical-arguments) | `int`                                                                                                                                                                                                                                                 |
|                   [`IntegerRangeArgument`](./argument_range.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`                                                                                                                                                                                                          |
|                                                     [`ItemStackArgument`](./argument_itemstack.md) | `org.bukkit.inventory.ItemStack`                                                                                                                                                                                                                      |
|                                   [`ItemStackPredicateArgument`](./argument_itemstackpredicate.md) | `java.util.function.Predicate`<br />&emsp;`<org.bukkit.inventory.ItemStack>`                                                                                                                                                                          |
|                                                               [`ListArgument`](./argument_list.md) | `java.util.Collection<T>`                                                                                                                                                                                                                             |
|                                                         [`LiteralArgument`](./argument_literal.md) | N/A                                                                                                                                                                                                                                                   |
|                                  [`Location2DArgument`](./argument_locations.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`                                                                                                                                                                                                            |
|                                    [`LocationArgument`](./argument_locations.md#location-3d-space) | `org.bukkit.Location`                                                                                                                                                                                                                                 |
|                                     [`LongArgument`](./argument_primitives.md#numerical-arguments) | `long`                                                                                                                                                                                                                                                |
|                                                     [`LootTableArgument`](./argument_loottable.md) | `org.bukkit.loot.LootTable`                                                                                                                                                                                                                           |
|                                                                 [`MapArgument`](./argument_map.md) | `java.util.LinkedHashMap`                                                                                                                                                                                                                             |
|                                             [`MathOperationArgument`](./argument_mathoperation.md) | `dev.jorel.commandapi.wrappers.MathOperation`                                                                                                                                                                                                         |
|                                               [`MultiLiteralArgument`](./argument_multiliteral.md) | `String`                                                                                                                                                                                                                                              |
|                                             [`NamespacedKeyArgument`](./argument_namespacedkey.md) | `org.bukkit.NamespacedKey`                                                                                                                                                                                                                            |
|                                                      [`NBTCompoundArgument<T>`](./argument_nbt.md) | The cast type changes depending on whether you're shading the CommandAPI or using the CommandAPI as a plugin:<br /><ul><li>Shading:<br />`T` (implemented yourself)</li><br /><li>Plugin:<br />`dev.jorel.commandapi.nbtapi.NBTContainer`</li></ul>   |
|                                 [`ObjectiveArgument`](./argument_objectives.md#objective-argument) | `org.bukkit.scoreboard.Objective`                                                                                                                                                                                                                     |
|                [`ObjectiveCriteriaArgument`](./argument_objectives.md#objective-criteria-argument) | `String`                                                                                                                                                                                                                                              |
|                           [`OfflinePlayerArgument`](./argument_entities.md#offlineplayer-argument) | `org.bukkit.OfflinePlayer`                                                                                                                                                                                                                            |
|                                                      [`ParticleArgument`](./argument_particles.md) | `dev.jorel.commandapi.wrappers.ParticleData`                                                                                                                                                                                                          |
|                                         [`PlayerArgument`](./argument_entities.md#player-argument) | `org.bukkit.entity.Player`                                                                                                                                                                                                                            |
|                                                     [`PotionEffectArgument`](./argument_potion.md) | `org.bukkit.potion.PotionEffectType`                                                                                                                                                                                                                  |
|                                       [`PotionEffectArgument.NamespacedKey`](./argument_potion.md) | `org.bukkit.NamespacedKey`                                                                                                                                                                                                                            |
|                                                           [`RecipeArgument`](./argument_recipe.md) | The cast type changes depending on your Minecraft version:<br><ul><li>Version 1.14.4 and below:<br />`org.bukkit.inventory.Recipe`</li><br /><li>1.15 and above:<br />`org.bukkit.inventory.ComplexRecipe` </li></ul>                                 |
|                                                       [`RotationArgument`](./argument_rotation.md) | `dev.jorel.commandapi.wrappers.Rotation`                                                                                                                                                                                                              |
|                     [`ScoreboardSlotArgument`](./argument_scoreboards.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot`                                                                                                                                                                                                        |
|                    [`ScoreHolderArgument.Single`](./argument_scoreboards.md#score-holder-argument) | `String`                                                                                                                                                                                                                                              |
|                  [`ScoreHolderArgument.Multiple`](./argument_scoreboards.md#score-holder-argument) | `Collection<String>`                                                                                                                                                                                                                                  |
|                                                             [`SoundArgument`](./argument_sound.md) | `org.bukkit.Sound`                                                                                                                                                                                                                                    |
|                                               [`SoundArgument.NamespacedKey`](./argument_sound.md) | `org.bukkit.NamespacedKey`                                                                                                                                                                                                                            |
|                                          [`StringArgument`](./argument_strings.md#string-argument) | `String`                                                                                                                                                                                                                                              |
|                                                               [`TeamArgument`](./argument_team.md) | `org.bukkit.scoreboard.Team`                                                                                                                                                                                                                          |
|                                              [`TextArgument`](./argument_strings.md#text-argument) | `String`                                                                                                                                                                                                                                              |
|                                                               [`TimeArgument`](./argument_time.md) | `int`                                                                                                                                                                                                                                                 |
|                                                               [`UUIDArgument`](./argument_uuid.md) | `java.util.UUID`                                                                                                                                                                                                                                      |
|                                                             [`WorldArgument`](./argument_world.md) | `org.bukkit.World`                                                                                                                                                                                                                                    |
