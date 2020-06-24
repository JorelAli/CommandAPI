# Arguments

Arguments in the CommandAPI are registered by using a `LinkedHashMap<String, Argument>` object. There are two things you need to keep in mind when creating arguments:

* The order which they will be used
* The type of each argument

By definition of a `LinkedHashMap`, the order of the elements inserted into it are preserved, meaning the order you add arguments to the `LinkedHashMap` will be the resulting order of which arguments are presented to the user when they run that command.

Adding arguments for registration is simple:

```java
//Create LinkedHashMap
LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();

//Add an argument called "target", which is a PlayerArgument
arguments.put("target", new PlayerArgument());
```

The String value is the tooltip that is shown to a player when they are entering the command.

-----

## Argument Casting

To access arguments, they have to be casted to the type that the argument represents. The order of the arguments in the `args[]` is the same as the order in which the arguments were declared.

```java
{{#include examples/5argumentcasting.java}}
```

The type to cast each argument (declared in the `dev.jorel.commandapi.arguments` package) is listed below:

|                                               Argument class | Data type                                                    |
| -----------------------------------------------------------: | :----------------------------------------------------------- |
|                                        [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`                         |
|                               [`AxisArgument`](./axisarg.md) | `java.util.EnumSet<org.bukkit.Axis>`                         |
| [`BooleanArgument`](./primitivearguments.md#boolean-arguments) | `boolean`                                                    |
|           [`ChatArgument`](./chatarguments.md#chat-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
| [`ChatColorArgument`](./chatarguments.md#chat-color-argument) | `org.bukkit.ChatColor`                                       |
| [`ChatComponentArgument`](./chatarguments.md#chat-component-argument) | `net.md_5.bungee.api.chat.BaseComponent[]`                   |
|                                          `CustomArgument<T>` | `T`                                                          |
| [`DoubleArgument`](./primitivearguments.md#numerical-arguments) | `double`                                                     |
|                                       [`EnchantmentArgument`](./enchantmentargument.md) | `org.bukkit.enchantments.Enchantment`                        |
| [`EntitySelectorArgument`](./entityarguments.md#entity-selector-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`EntitySelector.MANY_ENTITIES` - `Collection<org.bukkit.entity.Entity>`</li><br /><li>`EntitySelector.MANY_PLAYERS` - `Collection<org.bukkit.entity.Player>`</li><br /><li>`EntitySelector.ONE_ENTITY` - `org.bukkit.entity.Entity`</li><br /><li>`EntitySelector.ONE_PLAYER` - `org.bukkit.entity.Player`</li></ul> |
| [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) | `org.bukkit.entity.EntityType`                               |
|                                        [`EnvironmentArgument`](./environmentargs.md) | `org.bukkit.World.Environment`                               |
| [`FloatArgument`](./primitivearguments.md#numerical-arguments) | `float`                                                      |
| [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`                   |
|                                          [`FunctionArgument`](./functionwrapper.md) | `dev.jorel.commandapi.wrappers.FunctionWrapper[]`            |
| [`GreedyStringArgument`](./stringarguments.md#greedy-string-argument) | `String`                                                     |
| [`IntegerArgument`](./primitivearguments.md#numerical-arguments) | `int`                                                        |
| [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`                 |
|                                         [`ItemStackArgument`](./itemstackarguments.md) | `org.bukkit.inventory.ItemStack`                             |
|                                           [`LiteralArgument`](./literalarguments.md) | N/A                                                          |
| [`Location2DArgument`](./locationargument.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`                   |
| [`LocationArgument`](./locationargument.md#location-3d-space) | `org.bukkit.Location`                                        |
| [`LongArgument`](./primitivearguments.md#numerical-arguments) | `long`                                                       |
|                                          `LootTableArgument` | `org.bukkit.loot.LootTable`                                  |
|                                      `MathOperationArgument` | `dev.jorel.commandapi.wrappers.MathOperation`                |
|                                        [`NBTCompoundArgument`](./nbtarguments.md) | `de.tr7zw.nbtapi.NBTContainer`                               |
| [`ObjectiveArgument`](./objectivearguments.md#objective-argument) | `String`                                                     |
| [`ObjectiveCriteriaArgument`](./objectivearguments.md#objective-criteria-argument) | `String`                                                     |
|                                          [`ParticleArgument`](./particlearguments.md) | `org.bukkit.Particle`                                        |
|     [`PlayerArgument`](./entityarguments.md#player-argument) | `org.bukkit.entity.Player`                                   |
|                                      [`PotionEffectArgument`](./potionarguments.md) | `org.bukkit.potion.PotionEffectType`                         |
|                                             [`RecipeArgument`](./recipeargument.md) | `org.bukkit.inventory.Recipe`                                |
|                      [`RotationArgument`](./rotationargs.md) | `dev.jorel.commandapi.wrappers.Rotation`                     |
| [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot`               |
| [`ScoreHolderArgument`](./scoreboardarguments.md#score-holder-argument) | The cast type changes depending on the input parameter:<br /><ul><li>`ScoreHolderType.SINGLE` - `String`</li><br /><li>`ScoreHolderType.MULTIPLE` - `Collection<String>`</li></ul> |
|                                              [`SoundArgument`](./soundargument.md) | `org.bukkit.Sound`                                           |
|     [`StringArgument`](./stringarguments.md#string-argument) | `String`                                                     |
|                         [`TeamArgument`](./teamarguments.md) | `String`                                                     |
|         [`TextArgument`](./stringarguments.md#text-argument) | `String`                                                     |
|                                               [`TimeArgument`](./timeargs.md) | `int`                                                        |

-----

## Overriding argument suggestions

Sometimes, you want to override the list of suggestions that are provided by an argument. To handle this, the CommandAPI arguments contain two methods to override suggestions:

```java
Argument overrideSuggestions(String... suggestions);
Argument overrideSuggestions(Function<CommandSender, String[]> suggestions);
```

The first method, `overrideSuggestions(String... suggestions)` allows you to *replace* the suggestions normally associated with that argument with an array of strings.

<div class="example">

### Example - Teleport to worlds by overriding suggestions

Say we're creating a plugin with the ability to teleport to different worlds on the server. If we were to retrieve a list of worlds, we would be able to override the suggestions of a typical `StringArgument` to teleport to that world. Let's create a command with the following structure:

```
/tpworld <world>
```

We then implement our world teleporting command using `overrideSuggestions()` on the `StringArgument` to provide a list of worlds to teleport to:

```java
//Populate a String[] with the names of worlds on the server
String[] worlds = Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new);

//Override the suggestions of the StringArgument with the aforementioned String[]
LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("world", new StringArgument().overrideSuggestions(worlds));

new CommandAPICommand("tpworld")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
       	String world = (String) args[0];
		player.teleport(Bukkit.getWorld(world).getSpawnLocation());
    })
    .register();
```

</div>

-----

The `overrideSuggestions(Function<CommandSender, String[]> suggestions)` allows you to replace the suggestions normally associated with that argument with an array of strings that are evaluated dynamically using information about the commandsender.

<div class="example">

### Example - Friend list by overriding suggestions

Say you have a plugin which has a "friend list" for players. If you want to teleport to a friend in that list, you could use a `PlayerArgument`, which has the list of suggestions overridden with the list of friends that that player has. Since the list of friends *depends on the sender*, we can use the function to determine what our suggestions should be. Let's use the following command to teleport to a friend from our friend list:

```
/friendtp <friend>
```

Let's say we have a simple class to get the friends of a command sender:

```java
public class Friends {
    public static String[] getFriends(CommandSender sender) {
        if(sender instanceof Player) {
            return //Look up friends in a database or file
        } else {
            return new String[0];
        }
    }
}
```

We can then use this to generate our suggested list of friends:

```java
LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("friend", new PlayerArgument().overrideSuggestions((sender) -> {
    Friends.getFriends(sender);
}));

new CommandAPICommand("friendtp")
    .withArguments(arguments)
    .executesPlayer((player, args) -> {
       	Player target = (Player) args[0];
		player.teleport(target);
    })
    .register();
```

> **Developer's Note:**
>
> The syntax of inlining the `.overrideSuggestions()` method has been designed to work well with Java's lambdas. For example, we could write the above code more consisely, such as:
>
> ```java
> LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
> arguments.put("friend", new PlayerArgument().overrideSuggestions(Friends::getFriends));
> ```
>
> 

</div>

