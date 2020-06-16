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

The type to cast each argument (declared in the `io.github.jorelali.commandapi.api.arguments` package) is listed below:

|          Argument class          |                          Data type                             |
| -------------------------------: | :------------------------------------------------------------- |
|      `AdvancementArgument`       |                        `org.bukkit.advancement.Advancement`    |
|      `AxisArgument`       |                        `java.util.EnumSet<org.bukkit.Axis>`                        |
|        `BooleanArgument`         |                          `boolean`                             |
|       `ChatArgument`        | `net.md_5.bungee.api.chat.BaseComponent[]`             |
|       `ChatColorArgument`        |                         `org.bukkit.ChatColor`        |
|     `ChatComponentArgument`      |        `net.md_5.bungee.api.chat.BaseComponent[]` |
| `CustomArgument<S>` | `S` |
|         `DoubleArgument`         |                           `double`                             |
|      ` EnchantmentArgument`      |               `org.bukkit.enchantments.Enchantment` |
|    ` EntitySelectorArgument`     | `Entity`, `Player`, `Collection<Entity>`, `Collection<Player>` |
|      ` EntityTypeArgument`       |                       `org.bukkit.entity.EntityType` |
| `EnvironmentArgument` | `org.bukkit.World.Environment` |
|         ` FloatArgument`         |                           `float`                              |
|`FloatRangeArgument`| `io.github.jorelali.commandapi.api.FloatRange` |
|       ` FunctionArgument`        | `io.github.jorelali.commandapi.api.wrappers.FunctionWrapper[]` |
|     ` GreedyStringArgument`      |                           `String`                             |
|        ` IntegerArgument`        |                            `int`                               |
|integerrange|??|
|       ` ItemStackArgument`       |                         `ItemStack`                            |
|        ` LiteralArgument`        |                             N/A                                |
|       ` Location2DArgument`        |                          `Location2D`                            |
|       ` LocationArgument`        |                          `Location`                            |
|longarg| `long`|
|       `LootTableArgument`        |                         `LootTable`                            |
|mathop| mathop|
|nbt|nbt|
|objective||
|objectivecriteria||
|       ` ParticleArgument`        |                          `Particle`                            |
|         `PlayerArgument`         |                           `Player`                             |
|     ` PotionEffectArgument`      |                      `PotionEffectType`                        |
|         `RecipeArgument`         |                           `Recipe`                             |
|rotation||
|scoreboardslot||
|`ScoreHolderArgument`|<ul><li>If `ScoreHolderType.SINGLE`: `String`</li><li>If `ScoreHolderType.MULTIPLE`: `Collection<String>`</li></ul>|
|         `SoundArgument`          |                           `Sound`                              |
|         `StringArgument`         |                           `String`                             |
|`TeamArgument`|`Team`|
|          `TextArgument`          |                           `String`                             |
|`TimeArgument`|`int`|

## Arguments with overrideable suggestions

Some arguments have a feature allowing you to override the list of suggestions they provide. This is achieved by using `.overrideSuggestions(String[])` on an instance of an argument, with the String array consisting of suggestions that will be shown to the user whilst they type their command. It's been designed such that this returns the same argument so it can be used inline (handy, eh?)


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
