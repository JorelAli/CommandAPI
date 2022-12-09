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
{{#include ../../commandapi-core/src/test/java/Examples.java:argumentsyntax1}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:argumentsyntax1}}
```

</div>

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:argumentsyntax2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:argumentsyntax2}}
```

</div>

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:argumentsyntax3}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:argumentsyntax3}}
```

</div>

-----

## Argument Casting

To access arguments, they have to be casted to the type that the argument represents. The order of the arguments in the `args[]` is the same as the order in which the arguments were declared.

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:argumentcasting}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:argumentcasting}}
```

</div>

The type to cast each argument (declared in the `dev.jorel.commandapi.arguments` package) is listed below:

|                                                                                    Argument class | Data type                                                                                                                                                                                                                                                                                                                                                                                                 |
|--------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
|                                                                [`AngleArgument`](./angleargument) | `float`                                                                                                                                                                                                                                                                                                                                                                                                   |
|                                                 [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`                                                                                                                                                                                                                                                                                                                                                                      |
|                    [`AdventureChatArgument`](./adventurechatarguments.md#adventure-chat-argument) | `net.kyori.adventure.text.Component`                                                                                                                                                                                                                                                                                                                                                                      |
| [`AdventureChatComponentArgument`](./adventurechatarguments.md#adventure-chat-component-argument) | `net.kyori.adventure.text.Component`                                                                                                                                                                                                                                                                                                                                                                      |
|                                                                    [`AxisArgument`](./axisarg.md) | `java.util.EnumSet<org.bukkit.Axis>`                                                                                                                                                                                                                                                                                                                                                                      |
|                                                             [`BiomeArgument`](./biomeargument.md) | `org.bukkit.block.Biome`                                                                                                                                                                                                                                                                                                                                                                                  |
|                                               [`BlockPredicateArgument`](./blockpredicateargs.md) | `java.util.function.Predicate`<br />&emsp;`<org.bukkit.block.Block>`                                                                                                                                                                                                                                                                                                                                      |
|                                                  [`BlockStateArgument`](./blockstatearguments.md) | `org.bukkit.block.data.BlockData`                                                                                                                                                                                                                                                                                                                                                                         |
|                                    [`BooleanArgument`](./primitivearguments.md#boolean-arguments) | `boolean`                                                                                                                                                                                                                                                                                                                                                                                                 |
|                                          [`ChatArgument`](./spigotchatarguments.md#chat-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                                                                                                                                                                                                                                                                                                                                                                |
|                                     [`ChatColorArgument`](./chatarguments.md#chat-color-argument) | `org.bukkit.ChatColor`                                                                                                                                                                                                                                                                                                                                                                                    |
|                       [`ChatComponentArgument`](./spigotchatarguments.md#chat-component-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                                                                                                                                                                                                                                                                                                                                                                |
|                                                        [`CommandArgument`](./commandarguments.md) | `dev.jorel.commandapi.wrappers.CommandResult`                                                                                                                                                                                                                                                                                                                                                            |
|                                                    [`CustomArgument<T, B>`](./customarguments.md) | `T`                                                                                                                                                                                                                                                                                                                                                                                                       |
|                                   [`DoubleArgument`](./primitivearguments.md#numerical-arguments) | `double`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                                                 [`EnchantmentArgument`](./enchantmentargument.md) | `org.bukkit.enchantments.Enchantment`                                                                                                                                                                                                                                                                                                                                                                     |
|                         [`EntitySelectorArgument`](./entityarguments.md#entity-selector-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`EntitySelector.MANY_ENTITIES`:<br />`Collection<org.bukkit.entity.Entity>`</li><br /><li>`EntitySelector.MANY_PLAYERS`:<br />`Collection<org.bukkit.entity.Player>`</li><br /><li>`EntitySelector.ONE_ENTITY`:<br />`org.bukkit.entity.Entity`</li><br /><li>`EntitySelector.ONE_PLAYER`:<br />`org.bukkit.entity.Player`</li></ul> |
|                                 [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) | `org.bukkit.entity.EntityType`                                                                                                                                                                                                                                                                                                                                                                            |
|                                                     [`EnvironmentArgument`](./environmentargs.md) | `org.bukkit.World.Environment`                                                                                                                                                                                                                                                                                                                                                                            |
|                                    [`FloatArgument`](./primitivearguments.md#numerical-arguments) | `float`                                                                                                                                                                                                                                                                                                                                                                                                   |
|                   [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`                                                                                                                                                                                                                                                                                                                                                                |
|                                                        [`FunctionArgument`](./functionwrapper.md) | `dev.jorel.commandapi.wrappers.FunctionWrapper[]`                                                                                                                                                                                                                                                                                                                                                         |
|                             [`GreedyStringArgument`](./stringarguments.md#greedy-string-argument) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                                  [`IntegerArgument`](./primitivearguments.md#numerical-arguments) | `int`                                                                                                                                                                                                                                                                                                                                                                                                     |
|                 [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`                                                                                                                                                                                                                                                                                                                                                              |
|                                                    [`ItemStackArgument`](./itemstackarguments.md) | `org.bukkit.inventory.ItemStack`                                                                                                                                                                                                                                                                                                                                                                          |
|                                       [`ItemStackPredicateArgument`](./itemstackpredicateargs.md) | `java.util.function.Predicate`<br />&emsp;`<org.bukkit.inventory.ItemStack>`                                                                                                                                                                                                                                                                                                                              |
|                                                              [`ListArgument`](./listarguments.md) | `java.util.Collection<T>`                                                                                                                                                                                                                                                                                                                                                                                 |
|                                                        [`LiteralArgument`](./literalarguments.md) | N/A                                                                                                                                                                                                                                                                                                                                                                                                       |
|                                   [`Location2DArgument`](./locationargument.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`                                                                                                                                                                                                                                                                                                                                                                |
|                                     [`LocationArgument`](./locationargument.md#location-3d-space) | `org.bukkit.Location`                                                                                                                                                                                                                                                                                                                                                                                     |
|                                     [`LongArgument`](./primitivearguments.md#numerical-arguments) | `long`                                                                                                                                                                                                                                                                                                                                                                                                    |
|                                                     [`LootTableArgument`](./loottableargument.md) | `org.bukkit.loot.LootTable`                                                                                                                                                                                                                                                                                                                                                                               |
|                                            [`MathOperationArgument`](./mathoperationarguments.md) | `dev.jorel.commandapi.wrappers.MathOperation`                                                                                                                                                                                                                                                                                                                                                             |
|                                                       [`MultiLiteralArgument`](./multilitargs.md) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                                                  [`NamespacedKeyArgument`](./namespacedkeyarg.md) | `org.bukkit.NamespacedKey`                                                                                                                                                                                                                                                                                                                                                                                |
|                                                     [`NBTCompoundArgument<T>`](./nbtarguments.md) | The cast type changes depending on whether you're shading the CommandAPI or using the CommandAPI as a plugin:<br /><ul><li>Shading:<br />`T` (implemented yourself)</li><br /><li>Plugin:<br />`dev.jorel.commandapi.nbtapi.NBTContainer`</li></ul>                                                                                                                                                       |
|                                 [`ObjectiveArgument`](./objectivearguments.md#objective-argument) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                [`ObjectiveCriteriaArgument`](./objectivearguments.md#objective-criteria-argument) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                            [`OfflinePlayerArgument`](./entityarguments.md#offlineplayer-argument) | `org.bukkit.OfflinePlayer`                                                                                                                                                                                                                                                                                                                                                                                |
|                                                      [`ParticleArgument`](./particlearguments.md) | `dev.jorel.commandapi.wrappers.ParticleData`                                                                                                                                                                                                                                                                                                                                                              |
|                                          [`PlayerArgument`](./entityarguments.md#player-argument) | `org.bukkit.entity.Player`                                                                                                                                                                                                                                                                                                                                                                                |
|                                                    [`PotionEffectArgument`](./potionarguments.md) | `org.bukkit.potion.PotionEffectType`                                                                                                                                                                                                                                                                                                                                                                      |
|                                                           [`RecipeArgument`](./recipeargument.md) | The cast type changes depending on your Minecraft version:<br><ul><li>Version 1.14.4 and below:<br />`org.bukkit.inventory.Recipe`</li><br /><li>1.15 and above:<br />`org.bukkit.inventory.ComplexRecipe` </li></ul>                                                                                                                                                                                     |
|                                                           [`RotationArgument`](./rotationargs.md) | `dev.jorel.commandapi.wrappers.Rotation`                                                                                                                                                                                                                                                                                                                                                                  |
|                     [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot`                                                                                                                                                                                                                                                                                                                                                            |
|                           [`ScoreHolderArgument`](./scoreboardarguments.md#score-holder-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`ScoreHolderType.SINGLE`:<br />`String`</li><br /><li>`ScoreHolderType.MULTIPLE`:<br />`Collection<String>`</li></ul>                                                                                                                                                                                                                |
|                                                             [`SoundArgument`](./soundargument.md) | The cast type changes depending on the input parameter:<br /><ul><li>`SoundType.SOUND`:<br />`org.bukkit.Sound`</li><br /><li>`SoundType.NAMESPACED_KEY`:<br />`org.bukkit.NamespacedKey`</li></ul>                                                                                                                                                                                                       |
|                                          [`StringArgument`](./stringarguments.md#string-argument) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                                                              [`TeamArgument`](./teamarguments.md) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                                              [`TextArgument`](./stringarguments.md#text-argument) | `String`                                                                                                                                                                                                                                                                                                                                                                                                  |
|                                                                   [`TimeArgument`](./timeargs.md) | `int`                                                                                                                                                                                                                                                                                                                                                                                                     |
|                                                                   [`UUIDArgument`](./uuidargs.md) | `java.util.UUID`                                                                                                                                                                                                                                                                                                                                                                                          |
|                                                            [`WorldArgument`](./worldarguments.md) | `org.bukkit.World`                                                                                                                                                                                                                                                                                                                                                                                        |

-----

## Optional/Different Arguments

Sometimes, you want to register a command that has a different effect whether arguments are included or not. For example, take the `/kill` command. If you run `/kill` on its own, it will kill the command sender. If however you run `/kill <target>`, it will kill the target. In other words, we have the following command command syntax:

```mccmd
/kill          - Kills yourself
/kill <target> - Kills a target player
```

As shown by the command syntax, we need to register _two commands_.

<div class="example">

### Example - /kill command with two separate arguments

For example, say we're registering a command `/kill`:

```mccmd
/kill          - Kills yourself
/kill <target> - Kills a target player
```

We first register the first `/kill` command as normal:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:argumentkillcmd}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:argumentkillcmd}}
```

</div>

Now we declare our command with arguments for our second command. Then, we can register our second command `/kill <target>` as usual:

<div class="multi-pre">

```java,Java
{{#include ../../commandapi-core/src/test/java/Examples.java:argumentkillcmd2}}
```

```kotlin,Kotlin
{{#include ../../commandapi-core/src/test/kotlin/Examples.kt:argumentkillcmd2}}
```

</div>

This gives us the ability to run both `/kill` and `/kill <target>` with the same command name "kill", but have different results based on the arguments used.

In this example, we use the simpler, inline `.withArguments(Argument... arguments)` method to register our argument. There is no difference to using this method as opposed to explicitly declaring a list and using `.withArguments(List<Argument> arguments)`, so feel free to use whichever method you want!

</div>
