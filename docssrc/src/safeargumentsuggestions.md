# Safe argument suggestions

So far, we've covered how to override suggestions using the `overrideSuggestions()` method. The issue with using Strings for suggestion listings is that they are prone to errors. As a result, some arguments include the `safeOverrideSuggestions()`, which provides type-safety checks for argument suggestions, as well as automatic "Bukkit-to-suggestion" conversion.

The whole point of the safe argument suggestions method is that parameters entered in this method are **guaranteed** to work.

The use of the safe override suggestions function is basically the same as `overrideSuggestions()` from the previous section, except instead of returning a `String[]`, you now return a `T[]`, where `T` is the class corresponding to the argument. This is described in more detail in the table below.

```java
Argument safeOverrideSuggestions(T... suggestions);
Argument safeOverrideSuggestions(Function<CommandSender, T[]> suggestions);
Argument safeOverrideSuggestions(BiFunction<CommandSender, Object[], T[]> suggestions);
```

## Supported arguments

Not all arguments support safe suggestions. This is mostly due to implementation constraints or inadequate support by the Bukkit API.

The list of supported arguments are displayed in the following table. The parameter `T` (shown in the method signatures above) are also provided for each argument. This parameter is the same as the cast argument described in [Argument Casting](./arguments.md#argument-casting), except for a few exceptions which are outlined in **bold**.

|                                                     Argument | Class (T)                                      |
| -----------------------------------------------------------: | :--------------------------------------------- |
|            [`AdvancementArgument`](./advancementargument.md) | `org.bukkit.advancement.Advancement`           |
|                               [`AxisArgument`](./axisarg.md) | `java.util.EnumSet<org.bukkit.Axis>`           |
|                        [`BiomeArgument`](./biomeargument.md) | `org.bukkit.block.Biome`                       |
|                                         `BlockStateArgument` | `org.bukkit.block.data.BlockData`              |
| [`BooleanArgument`](./primitivearguments.md#boolean-arguments) | **`Boolean`**                                  |
| [`ChatColorArgument`](./chatarguments.md#chat-color-argument) | `org.bukkit.ChatColor`                         |
| [`DoubleArgument`](./primitivearguments.md#numerical-arguments) | **`Double`**                                   |
|            [`EnchantmentArgument`](./enchantmentargument.md) | `org.bukkit.enchantments.Enchantment`          |
| [`EntityTypeArgument`](./entityarguments.md#entity-type-argument) | `org.bukkit.entity.EntityType`                 |
|                [`EnvironmentArgument`](./environmentargs.md) | `org.bukkit.World.Environment`                 |
| [`FloatArgument`](./primitivearguments.md#numerical-arguments) | **`Float`**                                    |
| [`FloatRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.FloatRange`     |
|                   [`FunctionArgument`](./functionwrapper.md) | **`org.bukkit.NamespacedKey`**                 |
| [`GreedyStringArgument`](./stringarguments.md#greedy-string-argument) | `String`                                       |
| [`IntegerArgument`](./primitivearguments.md#numerical-arguments) | **`Integer`**                                  |
| [`IntegerRangeArgument`](./rangedarguments.md#the-integerrange--floatrange-class) | `dev.jorel.commandapi.wrappers.IntegerRange`   |
|               [`ItemStackArgument`](./itemstackarguments.md) | `org.bukkit.inventory.ItemStack`               |
| [`Location2DArgument`](./locationargument.md#location-2d-space) | `dev.jorel.commandapi.wrappers.Location2D`     |
| [`LocationArgument`](./locationargument.md#location-3d-space) | `org.bukkit.Location`                          |
| [`LongArgument`](./primitivearguments.md#numerical-arguments) | **`Long`**                                     |
|                [`LootTableArgument`](./loottableargument.md) | `org.bukkit.loot.LootTable`                    |
|       [`MathOperationArgument`](./mathoperationarguments.md) | `dev.jorel.commandapi.wrappers.MathOperation`  |
|                   [`NBTCompoundArgument`](./nbtarguments.md) | `de.tr7zw.nbtapi.NBTContainer`                 |
| [`ObjectiveArgument`](./objectivearguments.md#objective-argument) | **`org.bukkit.scoreboard.Objective`**          |
|                 [`ParticleArgument`](./particlearguments.md) | `org.bukkit.Particle`                          |
|               [`PotionEffectArgument`](./potionarguments.md) | `org.bukkit.potion.PotionEffectType`           |
|                      [`RecipeArgument`](./recipeargument.md) | `org.bukkit.inventory.Recipe`                  |
|                      [`RotationArgument`](./rotationargs.md) | `dev.jorel.commandapi.wrappers.Rotation`       |
| [`ScoreboardSlotArgument`](./scoreboardarguments.md#scoreboard-slot-argument) | `dev.jorel.commandapi.wrappers.ScoreboardSlot` |
|                        [`SoundArgument`](./soundargument.md) | `org.bukkit.Sound`                             |
|                         [`TeamArgument`](./teamarguments.md) | **`org.bukkit.scoreboard.Team`**               |
|                              [`TimeArgument`](./timeargs.md) | **`dev.jorel.commandapi.wrappers.Time`**       |

-----

## Safe time arguments

While most of the arguments are fairly straight forward, I'd like to bring your attention to the `TimeArgument`'s safe suggestions function. This uses `dev.jorel.commandapi.wrappers.Time` as the class for `T`.