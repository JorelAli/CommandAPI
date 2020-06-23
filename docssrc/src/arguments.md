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

|              Argument class | Data type                                                    |
| --------------------------: | :----------------------------------------------------------- |
|       `AdvancementArgument` | `org.bukkit.advancement.Advancement`                         |
|              `AxisArgument` | `java.util.EnumSet<org.bukkit.Axis>`                         |
|           `BooleanArgument` | `boolean`                                                    |
|              `ChatArgument` | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
|         `ChatColorArgument` | `org.bukkit.ChatColor`                                       |
|     `ChatComponentArgument` | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
|         `CustomArgument<S>` | `S`                                                          |
|            `DoubleArgument` | `double`                                                     |
|      ` EnchantmentArgument` | `org.bukkit.enchantments.Enchantment`                        |
|   ` EntitySelectorArgument` | The cast type changes depending on the input parameter:<br /><ul><li>`EntitySelector.MANY_ENTITIES` - `Collection<org.bukkit.entity.Entity>`</li><br /><li>`EntitySelector.MANY_PLAYERS` - `Collection<org.bukkit.entity.Player>`</li><br /><li>`EntitySelector.ONE_ENTITY` - `org.bukkit.entity.Entity`</li><br /><li>`EntitySelector.ONE_PLAYER` - `org.bukkit.entity.Player`</li></ul> |
|       ` EntityTypeArgument` | `org.bukkit.entity.EntityType`                               |
|       `EnvironmentArgument` | `org.bukkit.World.Environment`                               |
|            ` FloatArgument` | `float`                                                      |
|        `FloatRangeArgument` | `dev.jorel.commandapi.wrappers.FloatRange`                   |
|         ` FunctionArgument` | `dev.jorel.commandapi.wrappers.FunctionWrapper[]`            |
|     ` GreedyStringArgument` | `String`                                                     |
|          ` IntegerArgument` | `int`                                                        |
|      `IntegerRangeArgument` | `dev.jorel.commandapi.wrappers.IntegerRange`                 |
|        ` ItemStackArgument` | `org.bukkit.inventory.ItemStack`                             |
|          ` LiteralArgument` | N/A                                                          |
|       ` Location2DArgument` | `dev.jorel.commandapi.wrappers.Location2D`                   |
|         ` LocationArgument` | `org.bukkit.Location`                                        |
|              `LongArgument` | `long`                                                       |
|         `LootTableArgument` | `org.bukkit.loot.LootTable`                                  |
|     `MathOperationArgument` | `dev.jorel.commandapi.wrappers.MathOperation`                |
|       `NBTCompoundArgument` | `de.tr7zw.nbtapi.NBTContainer`                               |
|         `ObjectiveArgument` | `String`                                                     |
| `ObjectiveCriteriaArgument` | `String`                                                     |
|         ` ParticleArgument` | `org.bukkit.Particle`                                        |
|            `PlayerArgument` | `org.bukkit.entity.Player`                                   |
|     ` PotionEffectArgument` | `org.bukkit.potion.PotionEffectType`                         |
|            `RecipeArgument` | `org.bukkit.inventory.Recipe`                                |
|          `RotationArgument` | `dev.jorel.commandapi.wrappers.Rotation`                     |
|    `ScoreboardSlotArgument` | `dev.jorel.commandapi.wrappers.ScoreboardSlot`               |
|       `ScoreHolderArgument` | The cast type changes depending on the input parameter:<br /><ul><li>`ScoreHolderType.SINGLE` - `String`</li><br /><li>`ScoreHolderType.MULTIPLE` - `Collection<String>`</li></ul> |
|             `SoundArgument` | `org.bukkit.Sound`                                           |
|            `StringArgument` | `String`                                                     |
|              `TeamArgument` | `String`                                                     |
|              `TextArgument` | `String`                                                     |
|              `TimeArgument` | `int`                                                        |

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
