# Arguments

Arguments in the CommandAPI are registered by using a `LinkedHashMap<String, Argument>` object. There are two things you need to keep in mind when creating arguments:

* The order which they will be used
* The type of each argument

By definition of a LinkedHashMap, the order of the elements inserted into it are preserved, meaning the order you add arguments to the LinkedHashMap will be the resulting order of which arguments are presented to the user when they run that command.

Adding arguments for registration is simple:

```java
//Create LinkedHashMap
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//Add an argument called "target", which is a PlayerArgument
arguments.put("target", new PlayerArgument());
```

The String value is the tooltip that is shown to a player when they are entering the command.

## Argument Casting

To access arguments, they have to be casted to the type that the argument represents. The order of the arguments in the `args[]` is the same as the order in which the arguments were declared.

```java
{{#include examples/5argumentcasting.java}}
```

The type to cast each argument (declared in the `dev.jorel.commandapi.arguments` package) is listed below:

|                                               Argument class | Data type                                                    |
| -----------------------------------------------------------: | :----------------------------------------------------------- |
|                                        `AdvancementArgument` | `org.bukkit.advancement.Advancement`                         |
|                               [`AxisArgument`](./axisarg.md) | `java.util.EnumSet<org.bukkit.Axis>`                         |
| [`BooleanArgument`](./primitivearguments.md#boolean-arguments) | `boolean`                                                    |
|           [`ChatArgument`](./chatarguments.md#chat-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
| [`ChatColorArgument`](./chatarguments.md#chat-color-argument) | `org.bukkit.ChatColor`                                       |
| [`ChatComponentArgument`](./chatarguments.md#chat-component-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
|                                          `CustomArgument<T>` | `T`                                                          |
| [`DoubleArgument`](./primitivearguments.md#numerical-arguments) | `double`                                                     |
|                                       ` EnchantmentArgument` | `org.bukkit.enchantments.Enchantment`                        |
| [`EntitySelectorArgument`](./entityarguments.md#entity-selector-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`EntitySelector.MANY_ENTITIES` - `Collection<org.bukkit.entity.Entity>`</li><br /><li>`EntitySelector.MANY_PLAYERS` - `Collection<org.bukkit.entity.Player>`</li><br /><li>`EntitySelector.ONE_ENTITY` - `org.bukkit.entity.Entity`</li><br /><li>`EntitySelector.ONE_PLAYER` - `org.bukkit.entity.Player`</li></ul> |
| [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) | `org.bukkit.entity.EntityType`                               |
|                                        `EnvironmentArgument` | `org.bukkit.World.Environment`                               |
| [`FloatArgument`](./primitivearguments.md#numerical-arguments) | `float`                                                      |
| [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`                   |
|                                          ` FunctionArgument` | `dev.jorel.commandapi.wrappers.FunctionWrapper[]`            |
| [`GreedyStringArgument`](./stringarguments.md#greedy-string-argument) | `String`                                                     |
| [`IntegerArgument`](./primitivearguments.md#numerical-arguments) | `int`                                                        |
| [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`                 |
|                                         ` ItemStackArgument` | `org.bukkit.inventory.ItemStack`                             |
|                                           [`LiteralArgument`](./literalarguments.md) | N/A                                                          |
| [`Location2DArgument`](./locationargument.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`                   |
| [`LocationArgument`](./locationargument.md#location-3d-space) | `org.bukkit.Location`                                        |
| [`LongArgument`](./primitivearguments.md#numerical-arguments) | `long`                                                       |
|                                          `LootTableArgument` | `org.bukkit.loot.LootTable`                                  |
|                                      `MathOperationArgument` | `dev.jorel.commandapi.wrappers.MathOperation`                |
|                                        `NBTCompoundArgument` | `de.tr7zw.nbtapi.NBTContainer`                               |
| [`ObjectiveArgument`](./objectivearguments.md#objective-argument) | `String`                                                     |
| [`ObjectiveCriteriaArgument`](./objectivearguments.md#objective-criteria-argument) | `String`                                                     |
|                                          ` ParticleArgument` | `org.bukkit.Particle`                                        |
|     [`PlayerArgument`](./entityarguments.md#player-argument) | `org.bukkit.entity.Player`                                   |
|                                      ` PotionEffectArgument` | `org.bukkit.potion.PotionEffectType`                         |
|                                             `RecipeArgument` | `org.bukkit.inventory.Recipe`                                |
|                      [`RotationArgument`](./rotationargs.md) | `dev.jorel.commandapi.wrappers.Rotation`                     |
| [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot`               |
| [`ScoreHolderArgument`](./scoreboardarguments.md#score-holder-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`ScoreHolderType.SINGLE` - `String`</li><br /><li>`ScoreHolderType.MULTIPLE` - `Collection<String>`</li></ul> |
|                                              `SoundArgument` | `org.bukkit.Sound`                                           |
|     [`StringArgument`](./stringarguments.md#string-argument) | `String`                                                     |
|                         [`TeamArgument`](./teamarguments.md) | `String`                                                     |
|         [`TextArgument`](./stringarguments.md#text-argument) | `String`                                                     |
|                                               `TimeArgument` | `int`                                                        |

## Arguments with overrideable suggestions

Some arguments have a feature allowing you to override the list of suggestions they provide. This is achieved by using `.overrideSuggestions(String[])` on an instance of an argument, with the String array consisting of suggestions that will be shown to the user whilst they type their command. It's been designed such that this returns the same argument so it can be used inline.


### Example - Friend list by overriding suggestions

Say you have a plugin which has a "friend list" for players. If you want to teleport to a friend in that list, you could use a `PlayerArgument`, which has the list of suggestions overridden with the list of friends that that player has.

```java
String[] friends = //Some String array populated with friends

LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("friend", new PlayerArgument().overrideSuggestions(friends));

CommandAPI.getInstance().register("friendtp", arguments, (sender, args) -> {
	Player target = (Player) args[0];
	Player player = (Player) sender;
	player.teleport(target);
});
```
