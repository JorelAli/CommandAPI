# Arguments

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

commandRegister.register("cmd", arguments, (sender, args) -> {
	String stringArg = (String) args[0];
	PotionEffectType potionArg = (PotionEffectType) args[1];
	Location locationArg = (Location) args[2];
});
```
