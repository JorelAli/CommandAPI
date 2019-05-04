# Arguments

TODO: Write about declaration of functions (linkedhashmap etc.), the naming convention (it doesn't matter, but has to be unique)

## List of arguments

Arguments are found in the `io.github.jorelali.commandapi.api.arguments` package.

|          Argument class          |                          Data type                           |                      Description                       |
| :------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------: |
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
|       ` ParticleArgument`        |                          `Particle`                          |                                                        |
|         `PlayerArgument`         |                           `Player`                           | Similar to EntitySelector, but always returns 1 player |
|     ` PotionEffectArgument`      |                      `PotionEffectType`                      |                                                        |
|         `StringArgument`         |                           `String`                           |              String consisting of 1 word               |
|    `SuggestedStringArgument`     |                           `String`                           |          A list of suggested one word strings          |
|          `TextArgument`          |                           `String`                           |      String which can have spaces (used for text)      |

## Argument Casting

To access arguments, they are casted in the order of declaration.

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
