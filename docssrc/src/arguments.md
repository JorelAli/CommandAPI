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

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:argumentsyntax1}}
```

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:argumentsyntax2}}
```

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:argumentsyntax3}}
```

-----

## Argument Casting

To access arguments, they have to be casted to the type that the argument represents. The order of the arguments in the `args[]` is the same as the order in which the arguments were declared.

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:argumentcasting}}
```

The type to cast each argument (declared in the `dev.jorel.commandapi.arguments` package) is listed below:

|                                               Argument class | Data type                                                    |
| -----------------------------------------------------------: | :----------------------------------------------------------- |
|                           [`AngleArgument`](./angleargument) | `float`                                                      |
|            [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`                         |
| [`AdventureChatArgument`](./adventurechatarguments.md#adventure-chat-argument) | `net.kyori.adventure.text.Component`                         |
| [`AdventureChatComponentArgument`](./adventurechatarguments.md#adventure-chat-component-argument) | `net.kyori.adventure.text.Component`                         |
|                               [`AxisArgument`](./axisarg.md) | `java.util.EnumSet<org.bukkit.Axis>`                         |
|                        [`BiomeArgument`](./biomeargument.md) | `org.bukkit.block.Biome`                                     |
|          [`BlockPredicateArgument`](./blockpredicateargs.md) | `java.util.function.Predicate<org.bukkit.block.Block>`       |
|             [`BlockStateArgument`](./blockstatearguments.md) | `org.bukkit.block.data.BlockData`                            |
| [`BooleanArgument`](./primitivearguments.md#boolean-arguments) | `boolean`                                                    |
|     [`ChatArgument`](./spigotchatarguments.md#chat-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
| [`ChatColorArgument`](./chatarguments.md#chat-color-argument) | `org.bukkit.ChatColor`                                       |
| [`ChatComponentArgument`](./spigotchatarguments.md#chat-component-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
|                  [`CustomArgument<T>`](./customarguments.md) | `T`                                                          |
| [`DoubleArgument`](./primitivearguments.md#numerical-arguments) | `double`                                                     |
|            [`EnchantmentArgument`](./enchantmentargument.md) | `org.bukkit.enchantments.Enchantment`                        |
| [`EntitySelectorArgument`](./entityarguments.md#entity-selector-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`EntitySelector.MANY_ENTITIES` - `Collection<org.bukkit.entity.Entity>`</li><br /><li>`EntitySelector.MANY_PLAYERS` - `Collection<org.bukkit.entity.Player>`</li><br /><li>`EntitySelector.ONE_ENTITY` - `org.bukkit.entity.Entity`</li><br /><li>`EntitySelector.ONE_PLAYER` - `org.bukkit.entity.Player`</li></ul> |
| [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) | `org.bukkit.entity.EntityType`                               |
|                [`EnvironmentArgument`](./environmentargs.md) | `org.bukkit.World.Environment`                               |
| [`FloatArgument`](./primitivearguments.md#numerical-arguments) | `float`                                                      |
| [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`                   |
|                   [`FunctionArgument`](./functionwrapper.md) | `dev.jorel.commandapi.wrappers.FunctionWrapper[]`            |
| [`GreedyStringArgument`](./stringarguments.md#greedy-string-argument) | `String`                                                     |
| [`IntegerArgument`](./primitivearguments.md#numerical-arguments) | `int`                                                        |
| [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`                 |
|               [`ItemStackArgument`](./itemstackarguments.md) | `org.bukkit.inventory.ItemStack`                             |
|  [`ItemStackPredicateArgument`](./itemstackpredicateargs.md) | `java.util.function.Predicate<org.bukkit.inventory.ItemStack>` |
|                   [`LiteralArgument`](./literalarguments.md) | N/A                                                          |
| [`Location2DArgument`](./locationargument.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`                   |
| [`LocationArgument`](./locationargument.md#location-3d-space) | `org.bukkit.Location`                                        |
| [`LongArgument`](./primitivearguments.md#numerical-arguments) | `long`                                                       |
|                [`LootTableArgument`](./loottableargument.md) | `org.bukkit.loot.LootTable`                                  |
|       [`MathOperationArgument`](./mathoperationarguments.md) | `dev.jorel.commandapi.wrappers.MathOperation`                |
|                  [`MultiLiteralArgument`](./multilitargs.md) | `String`                                                     |
|                   [`NBTCompoundArgument`](./nbtarguments.md) | `de.tr7zw.nbtapi.NBTContainer`                               |
| [`ObjectiveArgument`](./objectivearguments.md#objective-argument) | `String`                                                     |
| [`ObjectiveCriteriaArgument`](./objectivearguments.md#objective-criteria-argument) | `String`                                                     |
| [`OfflinePlayerArgument`](./entityarguments.md#offlineplayer-argument) | `org.bukkit.OfflinePlayer`                                   |
|                 [`ParticleArgument`](./particlearguments.md) | `org.bukkit.Particle`                                        |
|     [`PlayerArgument`](./entityarguments.md#player-argument) | `org.bukkit.entity.Player`                                   |
|               [`PotionEffectArgument`](./potionarguments.md) | `org.bukkit.potion.PotionEffectType`                         |
|                      [`RecipeArgument`](./recipeargument.md) | The cast type changes depending on your Minecraft version:<br><ul><li>Version 1.14.4 and below - `org.bukkit.inventory.Recipe`</li><br /><li>1.15 and above - `org.bukkit.inventory.ComplexRecipe` </li></ul> |
|                      [`RotationArgument`](./rotationargs.md) | `dev.jorel.commandapi.wrappers.Rotation`                     |
| [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot`               |
| [`ScoreHolderArgument`](./scoreboardarguments.md#score-holder-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`ScoreHolderType.SINGLE` - `String`</li><br /><li>`ScoreHolderType.MULTIPLE` - `Collection<String>`</li></ul> |
|                        [`SoundArgument`](./soundargument.md) | `org.bukkit.Sound`                                           |
|     [`StringArgument`](./stringarguments.md#string-argument) | `String`                                                     |
|                         [`TeamArgument`](./teamarguments.md) | `String`                                                     |
|         [`TextArgument`](./stringarguments.md#text-argument) | `String`                                                     |
|                              [`TimeArgument`](./timeargs.md) | `int`                                                        |
|                              [`UUIDArgument`](./uuidargs.md) | `java.util.UUID`                                             |

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

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:argumentkillcmd}}
```

Now we declare our command with arguments for our second command. Then, we can register our second command `/kill <target>` as usual:

```java
{{#include ../../CommandAPI/commandapi-core/src/test/java/Examples.java:argumentkillcmd2}}
```

This gives us the ability to run both `/kill` and `/kill <target>` with the same command name "kill", but have different results based on the arguments used.

In this example, we use the simpler, inline `.withArguments(Argument... arguments)` method to register our argument. There is no difference to using this method as opposed to explicitly declaring a list and using `.withArguments(List<Argument> arguments)`, so feel free to use whichever method you want!

</div>