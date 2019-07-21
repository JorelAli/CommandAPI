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
LinkedHashMap<String, ArgumentType> arguments = new LinkedHashMap<>();
arguments.put("arg0", new StringArgument());
arguments.put("arg1", new PotionEffectArgument());
arguments.put("arg2", new LocationArgument());

CommandAPI.getInstance().register("cmd", arguments, (sender, args) -> {
	String stringArg = (String) args[0];
	PotionEffectType potionArg = (PotionEffectType) args[1];
	Location locationArg = (Location) args[2];
});
```

The types of the arguments declared by the CommandAPI are all listed below:

## List of arguments

Arguments are found in the `io.github.jorelali.commandapi.api.arguments` package.

|          Argument class          |                          Data type                           |                      Description                       |
| :------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------: |
|      `AdvancementArgument`       |                        `Advancement`                         |                                                        |
|        `BooleanArgument`         |                          `boolean`                           |                                                        |
|       `ChatColorArgument`        |                         `ChatColor`                          |                                                        |
|     `ChatComponentArgument`      |                      `BaseComponent[]`                       |                 Formatted chat object                  |
|         `DoubleArgument`         |                           `double`                           |                                                        |
| `DynamicSuggestedStringArgument` |                           `String`                           |       Suggested string using a supplier function       |
|      ` EnchantmentArgument`      |                        `Enchantment`                         |                                                        |
|    ` EntitySelectorArgument`     | `Entity`, `Player`, `Collection<Entity>`, `Collection<Player>` |      Selects an entity (similar to `@a` or `@p`)       |
|      ` EntityTypeArgument`       |                         `EntityType`                         |          Selects a type of entity (e.g. Pig)           |
|         ` FloatArgument`         |                           `float`                            |                                                        |
|       ` FunctionArgument`        |                     `FunctionWrapper[]`                      |     A declared Minecraft function from a data pack     |
|     ` GreedyStringArgument`      |                           `String`                           |                 A string of any length                 |
|        ` IntegerArgument`        |                            `int`                             |                                                        |
|       ` ItemStackArgument`       |                         `ItemStack`                          |          Returns an `ItemStack` with amount 1          |
|        ` LiteralArgument`        |                             N/A                              |          A predefined hardcoded argument name          |
|       ` LocationArgument`        |                          `Location`                          |                                                        |
|       `LootTableArgument`        |                         `LootTable`                          |                                                        |
|       ` ParticleArgument`        |                          `Particle`                          |                                                        |
|         `PlayerArgument`         |                           `Player`                           | Similar to EntitySelector, but always returns 1 player |
|     ` PotionEffectArgument`      |                      `PotionEffectType`                      |                                                        |
|         `RecipeArgument`         |                           `Recipe`                           |                                                        |
|         `SoundArgument`          |                           `Sound`                            |                                                        |
|         `StringArgument`         |                           `String`                           |              String consisting of 1 word               |
|    `SuggestedStringArgument`     |                           `String`                           |          A list of suggested one word strings          |
|          `TextArgument`          |                           `String`                           |      String which can have spaces (used for text)      |

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
